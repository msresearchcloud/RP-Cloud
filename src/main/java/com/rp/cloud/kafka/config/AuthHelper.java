// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.rp.cloud.kafka.config;

import com.microsoft.aad.msal4j.*;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.rp.cloud.KafkaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Helpers for acquiring authorization codes and tokens from AAD
 */
@Component
public class AuthHelper {

    static final String PRINCIPAL_SESSION_NAME = "principal";
    static final String TOKEN_CACHE_SESSION_ATTRIBUTE = "token_cache";

    private String clientId;
    private String clientSecret;
    private String authority;
    private String redirectUriSignIn;
    private String kafkaRestEndpoint;
    private String authToken;
    private final static Set<String> SCOPE = Collections.singleton("https://hib.azurehdinsight.net/.default");

    @Autowired
    KafkaConfiguration configuration;

    @PostConstruct
    public void init() {
        clientId = configuration.getClientId();
        authority = configuration.getAuthority();
        clientSecret = configuration.getSecretKey();
        redirectUriSignIn = configuration.getRedirectUriSignin();
        kafkaRestEndpoint = configuration.getKafkaRestEndpoint();
    }

    void processAuthenticationCodeRedirect(HttpServletRequest httpRequest, String currentUri, String fullUrl)
            throws Throwable {

        Map<String, List<String>> params = new HashMap<>();
        for (String key : httpRequest.getParameterMap().keySet()) {
            params.put(key, Collections.singletonList(httpRequest.getParameterMap().get(key)[0]));
        }
        // validate that state in response equals to state in request
        StateData stateData = SessionManagementHelper.validateState(httpRequest.getSession(), params.get(SessionManagementHelper.STATE).get(0));

        AuthenticationResponse authResponse = AuthenticationResponseParser.parse(new URI(fullUrl), params);
        if (AuthHelper.isAuthenticationSuccessful(authResponse)) {
            AuthenticationSuccessResponse oidcResponse = (AuthenticationSuccessResponse) authResponse;
            // validate that OIDC Auth Response matches Code Flow (contains only requested artifacts)
            validateAuthRespMatchesAuthCodeFlow(oidcResponse);

            IAuthenticationResult result = getAuthResultByAuthCode(
                    httpRequest,
                    oidcResponse.getAuthorizationCode(),
                    currentUri);

            // validate nonce to prevent reply attacks (code maybe substituted to one with broader access)
            validateNonce(stateData, getNonceClaimValueFromIdToken(result.idToken()));

            SessionManagementHelper.setSessionPrincipal(httpRequest, result);
        } else {
            AuthenticationErrorResponse oidcResponse = (AuthenticationErrorResponse) authResponse;
            throw new Exception(String.format("Request for auth code failed: %s - %s",
                    oidcResponse.getErrorObject().getCode(),
                    oidcResponse.getErrorObject().getDescription()));
        }
    }

    IAuthenticationResult getAuthResultBySilentFlow(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Throwable {

        IAuthenticationResult result = null;
        try {
            result =  SessionManagementHelper.getAuthSessionObject(httpRequest);
        }catch (Exception ex){

        }


        IConfidentialClientApplication app = createClientApplication();

        Object tokenCache = httpRequest.getSession().getAttribute("token_cache");
        if (tokenCache != null) {
            app.tokenCache().deserialize(tokenCache.toString());
        }

        CompletableFuture<IAuthenticationResult> future = null;
        if(result == null ){
            ClientCredentialParameters parameters =
                    ClientCredentialParameters
                            .builder(SCOPE)
                            .build();
            future = app.acquireToken(parameters);
        } else {
            SilentParameters parameters = SilentParameters.builder(
                    Collections.singleton("User.Read"),
                    result.account()).build();
            future = app.acquireTokenSilently(parameters);
        }

        //CompletableFuture<IAuthenticationResult> future = app.acquireTokenSilently(parameters);
        IAuthenticationResult updatedResult = future.get();

        //update session with latest token cache
        SessionManagementHelper.storeTokenCacheInSession(httpRequest, app.tokenCache().serialize());

        return updatedResult;
    }

    private void validateNonce(StateData stateData, String nonce) throws Exception {
        if (StringUtils.isEmpty(nonce) || !nonce.equals(stateData.getNonce())) {
            throw new Exception(SessionManagementHelper.FAILED_TO_VALIDATE_MESSAGE + "could not validate nonce");
        }
    }

    private String getNonceClaimValueFromIdToken(String idToken) throws ParseException {
        return (String) JWTParser.parse(idToken).getJWTClaimsSet().getClaim("nonce");
    }

    private void validateAuthRespMatchesAuthCodeFlow(AuthenticationSuccessResponse oidcResponse) throws Exception {
        if (oidcResponse.getIDToken() != null || oidcResponse.getAccessToken() != null ||
                oidcResponse.getAuthorizationCode() == null) {
            throw new Exception(SessionManagementHelper.FAILED_TO_VALIDATE_MESSAGE + "unexpected set of artifacts received");
        }
    }

    void sendAuthRedirect(HttpServletRequest httpRequest, HttpServletResponse httpResponse, String scope, String redirectURL)
            throws IOException {

        // state parameter to validate response from Authorization server and nonce parameter to validate idToken
        String state = UUID.randomUUID().toString();
        String nonce = UUID.randomUUID().toString();

        SessionManagementHelper.storeStateAndNonceInSession(httpRequest.getSession(), state, nonce);

        httpResponse.setStatus(302);
        String authorizationCodeUrl = getAuthorizationCodeUrl(httpRequest.getParameter("claims"), scope, redirectURL, state, nonce);
        httpResponse.sendRedirect(authorizationCodeUrl);
    }

    String getAuthorizationCodeUrl(String claims, String scope, String registeredRedirectURL, String state, String nonce)
            throws UnsupportedEncodingException {

        String urlEncodedScopes = scope == null ? "" : URLEncoder.encode(scope, "UTF-8");

        PublicClientApplication pca = PublicClientApplication.builder(clientId).build();

        AuthorizationRequestUrlParameters parameters =
                AuthorizationRequestUrlParameters
                        .builder(registeredRedirectURL,
                                Collections.singleton(urlEncodedScopes))
                        .responseMode(ResponseMode.QUERY)
                        .prompt(Prompt.SELECT_ACCOUNT)
                        .state(state)
                        .nonce(nonce)
                        .claimsChallenge(claims)
                        .build();

        return pca.getAuthorizationRequestUrl(parameters).toString();
    }

    private IAuthenticationResult getAuthResultByAuthCode(
            HttpServletRequest httpServletRequest,
            AuthorizationCode authorizationCode,
            String currentUri) throws Throwable {

        IAuthenticationResult result;
        ConfidentialClientApplication app;
        try {
            app = createClientApplication();

            String authCode = authorizationCode.getValue();
            AuthorizationCodeParameters parameters = AuthorizationCodeParameters.builder(
                    authCode,
                    new URI(currentUri)).
                    build();

            Future<IAuthenticationResult> future = app.acquireToken(parameters);

            result = future.get();
        } catch (ExecutionException e) {
            throw e.getCause();
        }

        if (result == null) {
            throw new ServiceUnavailableException("authentication result was null");
        }

        SessionManagementHelper.storeTokenCacheInSession(httpServletRequest, app.tokenCache().serialize());

        return result;
    }

    private ConfidentialClientApplication createClientApplication() throws MalformedURLException {
        return ConfidentialClientApplication.builder(clientId, ClientCredentialFactory.createFromSecret(clientSecret)).
                authority(authority).
                build();
    }

    private static boolean isAuthenticationSuccessful(AuthenticationResponse authResponse) {
        return authResponse instanceof AuthenticationSuccessResponse;
    }

    public String getAccessToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws Throwable {
        IAuthenticationResult authResult = getAuthResultBySilentFlow(httpRequest, httpResponse);
        String authToken = authResult.accessToken();
        setAuthToken(authToken);
        return authToken;
    }

    String getRedirectUriSignIn() {
        return redirectUriSignIn;
    }

    public String getKafkaRestEndpoint() {
        return kafkaRestEndpoint;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

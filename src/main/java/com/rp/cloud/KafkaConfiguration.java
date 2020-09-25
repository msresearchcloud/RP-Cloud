// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.rp.cloud;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Object containing configuration data for the application. Spring will automatically wire the
 * values by grabbing them from application.properties file
 */
@Component
@ConfigurationProperties("aad")
public
class KafkaConfiguration {

    private String clientId;
    private String authority;
    private String redirectUriSignin;
    private String secretKey;
    private String kafkaRestEndpoint;

    public String getAuthority(){
        if (!authority.endsWith("/")) {
            authority += "/";
        }
        return authority;
    }

    public String getClientId() {
        return clientId;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRedirectUriSignin() {
        return redirectUriSignin;
    }

    public void setRedirectUriSignin(String redirectUriSignin) {
        this.redirectUriSignin = redirectUriSignin;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getKafkaRestEndpoint() {
        return kafkaRestEndpoint;
    }

    public void setKafkaRestEndpoint(String kafkaRestEndpoint) {
        this.kafkaRestEndpoint = kafkaRestEndpoint;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
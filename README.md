# RP-Cloud

###################### Maven ###########
 * Maven build
 mvn clean install
 
##################### Docker #####################
# How to enable docker on you project
 * add Dockerfile  on your project with all required commands to build docker image for your application 


# Check Docker version on system?
  * command $: docker --version
  * OUTPUT : Docker version 19.03.5, build 633a0ea

# Create docker image
 * command $: docker build -f Dockerfile -t researchportal/rp-cloud:v2 .

# check docker images on your system
 * docker images
 
# Run docker image and map port internal and external 
* command $: docker run  -p 80:80  --name rp-cloud researchportal/rp-cloud:v2
* docker is running on your system localhost:8080

# Remove images
* docker rmi -f IMAGE_ID

# checking docker containers
 * docker container ls

# kill container
* docker container kill INSTANCE_ID
* docker rm /rp-cloud

#Clean all docker unused 
docker system prune

#Pushing image to docker hub
docker push researchportal/rp-cloud:v2

##################### AKS #####################

#Sync az account login to AZ CLI
az login

# Merge the credentials of your cluster into current Kube config
az aks get-credentials -g msresearch-aks-rg -n msresearch-aks

####Create/Update a resource
* Namespace
- kubectl apply -f ./ns.yaml

* Deployment
- kubectl apply -f ./feed.deployment-blue.yaml
- kubectl apply -f ./feed.deployment-green.yaml

* Service API
- kubectl apply -f ./feed.service.yaml
- kubectl apply -f ./feed.servicestage.yaml

* Ingress Controller
- kubectl apply -f ./ingresscontrol.yaml
- kubectl apply -f ./ingresscontrolstage.yaml

* Horizontal Pod Autoscaling
- kubectl apply -f ./feed-hpa.deployment.yaml

#### Monitor a resource
* Get all resources under a namespace
- kubectl get all --namespace <ns>

* Get all services under a namespace
- kubectl get svc --namespace <ns>

* Get all deployments under a namespace
- kubectl get deployment --namespace <ns>

* Get all replicasets under a namespace
- kubectl get rs --namespace <ns>

* Get all pods under a namespace
- kubectl get pods --namespace <ns>

* Get all nodes under a namespace
- kubectl get nodes --namespace <ns>

* Get all hpa under a namespace
- kubectl get hpa --namespace <ns>

#### Delete a resource
* Delete a services under a namespace
- kubectl delete svc <service-name> -n <ns>

* Delete a deployment under a namespace
- kubectl delete deployment <deployment-name> -n <ns>

* Delete a replicasets under a namespace
- kubectl delete rs <replica-name> -n <ns>

* Delete all pod under a deployment & namespace
- kubectl delete pod <deployment-name> -n <ns>

* Delete all nodes under a deployment & namespace
- kubectl delete nodes <deployment-name> -n <ns>

* Delete all hpa under a deployment & namespace
- kubectl delete hpa <deployment-name> -n <ns>
# RP-Cloud


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



#Clean all docker unused 
docker system prune



docker push researchportal/rp-cloud:v2
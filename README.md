# microservices_springboot

### Docker commands

Create docker image manually:

* docker build . -t ppagote/cards

List docker image:

* docker images

Run docker Image:

* docker run -p 8080:8080 ppagote/cards (1st is internal port & 2nd is port on which application is running)
* docker run -d -p 8080:8080 ppagote/cards (without console logs)

List all running container

* docker ps
* docker ps -a (to check all old containers)

Logs of docker container

* docker logs <container ID>
* docker logs -f <container ID>

Stop container

* docker stop <container ID>

Start container

* docker container start [<container ID>]

Pause container

* docker container pause <container ID>

UnPause container

* docker container unpause <container ID>

Kill container

* docker kill <container ID>

Docker stats

* docker stats

Remove container

* docker rm <container ID>

Push local image to remote hub If image name do not start with account name docker tag <image Name> <account Name>
/<image name>
where image name should be 1 word

docker push <account Name>/<image name>

Run all images at a time

* docker-compose up

Stop all containers at time:

* docker-compose stop

Remove image

* docker image rm <Image ID>
FROM alpine:latest

RUN apk add --no-cache bash

WORKDIR /sprint1

CMD ./gradlew run

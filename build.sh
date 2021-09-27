#!/bin/bash
set -e

ARTIFACT_NAME="spring-boot-http2"
IMAGE_USER="turgaycan.dev"
IMAGE_NAME="spring-boot-http2"
VERSION=`cat VERSION`

# Final Jar Name is artifact name plus version number
FINAL_JAR="./build/libs/${ARTIFACT_NAME}-${VERSION}.jar"
FINAL_IMAGE="${IMAGE_USER}/${IMAGE_NAME}:latest"

echo "VERSION     = ${VERSION}"
echo "FINAL JAR   = ${FINAL_JAR}"
echo "FINAL IMAGE = ${FINAL_IMAGE}"

# Build project
./gradlew clean build

docker build \
  --build-arg JAR_FILE="${FINAL_JAR}" \
  -t "${FINAL_IMAGE}" .

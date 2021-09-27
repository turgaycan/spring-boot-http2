#!/bin/bash
set -ex

# SET THE FOLLOWING VARIABLES
ARTIFACT_NAME="spring-boot-http2"
IMAGE_USER="turgaycan.dev"
IMAGE_NAME="spring-boot-http2"
REPOSITORY="localhost:5000"
REPOSITORY_USER=""
REPOSITORY_PASS=""
GIT_BRANCH="master"

if [[ "${EXT_REPOSITORY_URL}" != "" ]]; then
  REPOSITORY="${EXT_REPOSITORY_URL}"
fi

if [[ "${EXT_REPOSITORY_USER}" != "" ]]; then
  REPOSITORY_USER="${EXT_REPOSITORY_USER}"
fi

if [[ "${EXT_REPOSITORY_PASS}" != "" ]]; then
  REPOSITORY_PASS="${EXT_REPOSITORY_PASS}"
fi

if [[ "${EXT_GIT_BRANCH}" != "" ]]; then
  GIT_BRANCH="${EXT_GIT_BRANCH}"
fi

# Validate release type parameter
RELEASE_TYPE=""
if [[ "${1}" = "major" ]] || [[ "${1}" = "minor" ]] || [[ "${1}" = "patch" ]]; then
  RELEASE_TYPE="${1}"
elif [[ ${1} = "" ]]; then
  RELEASE_TYPE="patch"
else
  echo "Illegal argument: ${1}"
  echo "Usage: ./release.sh [patch|minor|major]"
  exit 1
fi

echo "${RELEASE_TYPE}"

forceNewVersion="false"

until [[ "$#" == "0" ]]; do
    case "$1" in
        --force-new-version )
            forceNewVersion="true"
            ;;
    esac

    shift
done

echo "forceNewVersion=${forceNewVersion}"

# ensure we're up to date
git checkout -B "${GIT_BRANCH}" "origin/${GIT_BRANCH}"
git pull

# Check if the current commit is already versioned
previousVersion=$(cat VERSION)
alreadyVersioned=$(git tag -l --points-at HEAD "${previousVersion}")
if [[ "${alreadyVersioned}" != "" ]]; then
  echo "Current HEAD already contains version tag ${alreadyVersioned}"
fi

newVersion="false"
if [[ "${alreadyVersioned}" = "" ]] || [[ "${forceNewVersion}" = "true" ]]; then
  newVersion="true"

  # bump (increment) version
  docker run --rm -v "$PWD":/app treeder/bump "${RELEASE_TYPE}"
  while [[ "$(git tag -l "$(cat VERSION)")" != "" ]]
  do
    echo "Tag `cat VERSION` already exists, patching"
    docker run --rm -v "$PWD":/app treeder/bump "patch"
  done
fi

VERSION=`cat VERSION`

echo "Version: ${VERSION}"

# Run build
./build.sh

if [[ "${newVersion}" = "true" ]]; then
  # tag it
  git add -A
  git commit -m "Release version ${VERSION}"
  git tag -a "${VERSION}" -m "version ${VERSION}"
  git push
  git push --tags
fi

docker login -u "${REPOSITORY_USER}" -p "${REPOSITORY_PASS}" "${REPOSITORY}"

# tag the image in local registry
docker tag "${IMAGE_USER}/${IMAGE_NAME}:latest" "${REPOSITORY}/${IMAGE_USER}/${IMAGE_NAME}:latest"
docker tag "${IMAGE_USER}/${IMAGE_NAME}:latest" "${REPOSITORY}/${IMAGE_USER}/${IMAGE_NAME}:${VERSION}"

# push the image to the registry
docker push "${REPOSITORY}/${IMAGE_USER}/${IMAGE_NAME}:latest"
docker push "${REPOSITORY}/${IMAGE_USER}/${IMAGE_NAME}:${VERSION}"

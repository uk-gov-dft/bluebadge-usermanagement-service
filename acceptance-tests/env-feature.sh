#!/usr/bin/env bash

REPOS=(
    'authorisation-service::AZ_VERSION'
    'la-webapp::LA_VERSION'
    'usermanagement-service::UM_VERSION'
    'message-service::MG_VERSION'
    'referencedata-service::RD_VERSION'
    'badgemanagement-service::BB_VERSION'
    'applications-service::AP_VERSION'
)

echo "Fetching versions..."
for index in "${REPOS[@]}" ; do
    KEY="${index%%::*}"
    VALUE="${index##*::}"
    URL="https://github.com/uk-gov-dft/$KEY.git"
    version=$(git ls-remote --tags --sort="v:refname" $URL | tail -n1 | sed 's/v//; s/.*\///; s/\^{}//')
    export ${VALUE}=$version-SNAPSHOT
    #echo "$KEY, $VALUE - $version-SNAPSHOT"
done

# Override the version with the branch name
export UM_VERSION=$(cat VERSION-computed)

echo 'Setting version stack to:'
for index in "${REPOS[@]}" ; do
    envVar="${index##*::}"
    echo "${envVar} = ${!envVar}"
done


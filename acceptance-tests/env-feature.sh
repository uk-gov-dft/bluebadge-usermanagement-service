#!/usr/bin/env bash

# Override the version with the branch name
export UM_VERSION=$(cat VERSION-computed)
export AZ_VERSION=0.8.0-feature_BBB-52-lock-user-account

#!/bin/bash

set -euxo pipefail
mvn -f ../concordium-sdk/ versions:set -DnewVersion=$1


# version must be in one of the following two formats: x.y.z, x.y.z-SNAPSHOT

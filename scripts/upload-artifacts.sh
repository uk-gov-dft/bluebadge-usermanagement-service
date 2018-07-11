VERSION_SUFFIX=$(git rev-parse --abbrev-ref HEAD | \
    sed -E 's/develop/-SNAPSHOT/g; s/master|release.*//g; s/.*([a-zA-Z]{3}-[0-9]+).*/-\1/g; s/(.*)/-\1/g' | \
    tr -s '-')
NAME="$(cat VERSION)$VERSION_SUFFIX".tar.gz
tar -zcvf $NAME database-schema
/var/lib/jenkins/jfrog rt u $NAME \
    gradle-dev-local/uk/gov/dft/bluebadge/usermanagement-service/database-schema/ \
    --url=https://artifactory.does.not.exist/artifactory --recursive=false

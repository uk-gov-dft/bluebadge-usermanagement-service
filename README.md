# DFT BLUE BADGE BETA - USERMANAGEMENT-SERVICE

## Getting Started in few minutes
From command line:
```
git clone git@github.com:uk-gov-dft/usermanagement-service.git
cd usermanagement-service
gradle wrapper
gradle build
gradle bootRun
```

## INSTALLATION
From command line:
```
brew install postgres
```
[Download and install pgadmin](https://www.pgadmin.org/download/pgadmin-4-macos/)

To Start postgresql:
```
pg_ctl start -D /usr/local/var/postgres
```

Create User:
```
createuser -W developer -P
createdb bb_dev
psql bb_dev -U developer
```


## DEPLOYING ARTIFACTS TO LOCAL MAVEN REPOSITORY
```
cd model
gradle install
cd ..
cd client
gradle install
```

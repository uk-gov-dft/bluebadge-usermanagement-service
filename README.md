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

You may need to create the database using the (database-schemas project)[https://github.com/uk-gov-dft/database-schemas], please read the 
[database-schemes Readme](https://github.com/uk-gov-dft/database-schemas/blob/develop/migrations/README.md)

Some useful commands to test things in posgresql
```
psql bb_dev -U developer
\l to show all database
use database_name --- to change database
\dt to list tables 
```


## DEPLOYING ARTIFACTS TO LOCAL MAVEN REPOSITORY
```
cd model
gradle install
cd ..
cd client
gradle install
```

## PLAY WITH THE API
* [Retrieve users from local authority 1 (GET)](http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/authorities/1/users)

* Create user for local authority 1 with valid input:
```
curl --header "Content-Type: application/json" \
     --request POST \
     --data '{"id": 3, "name": "my user", "emailAddress": "myEmailAddress@email.com", "localAuthorityId": 1}' \
     http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/authorities/1/users
```
You may get a response like this
```
{"apiVersion":null,"context":null,"id":null,"method":null,"errors":null,"data":null}
```  

* Create user for local authority 1 with invalid input (wrong email format):
```
curl --header "Content-Type: application/json" \
     --request POST \
     --data '{"id": 3, "name": "my user", "emailAddress": "myEmailAddress", "localAuthorityId": 1}' \
     http://localhost:8180/uk-gov-dft/service-template-api/1.0.0/authorities/1/users
```
You may get a response like this
```
{
	"timestamp": "2018-05-17T15:04:12.437Z",
	"status": 400,
	"error": "Bad Request",
	"errors": [{
		"codes": [
			"Email.user.emailAddress",
			"Email.emailAddress",
			"Email.java.lang.String",
			"Email"
		],
		"arguments": [{
				"codes": [
					"user.emailAddress",
					"emailAddress"
				],
				"arguments": null,
				"defaultMessage": "emailAddress",
				"code": "emailAddress"
			},
			[],
			{
				"arguments": null,
				"defaultMessage": ".*",
				"codes": [".*"]
			}
		],
		"defaultMessage": "Wrong email",
		"objectName": "user",
		"field": "emailAddress",
		"rejectedValue": "myEmailAddress",
		"bindingFailure": false,
		"code": "Email"
	}],
	"message": "Validation failed for object='user'. Error count: 1",
	"path": "/uk-gov-dft/service-template-api/1.0.0/authorities/1/users"
}
```  

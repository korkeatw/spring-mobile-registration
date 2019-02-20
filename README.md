# Mobile Registration

The sample REST service using Spring

## Required software/lib

- Java 1.8+
- Maven
- PostgreSQL
- (Optional) Docker to run PostgreSQL

## How to run the server

1. Start PostgreSQL. Simply use Docker

```bash
docker run --name postgres -rm -d -p15432:5432 postgres:latest
```

2. Start REST service
```bash
cd spring-mobile-registration
./mvnw spring-boot:run
```

The server will run on `localhost:8080`


## Testing the API

First you need to login to get the access token

```bash
curl -XPOST \
    -H 'Content-Type: application/json' \
    -d '{"username":"kor@abc.com","password": "1234"}' \
    http://localhost:8080/auth/login
```

Use the token to access other endpoints. For example:

```bash
# Add new mobile user
curl -XPOST \
    -H 'Content-Type: application/json' \
    -H 'Authorization: Bearer <ACCESS_TOKEN>' \
    -d '{"phone_number":"081-222-3333","salary":15000}' \
    http://localhost:8080/mobile-users
    
# Get user by id
curl -XGET \
    -H 'Authorization: Bearer <ACCESS_TOKEN>' \
    http://localhost:8080/mobile-users/id/1
    
# Get user by refCode
curl -XGET \
    -H 'Authorization: Bearer <ACCESS_TOKEN>' \
    http://localhost:8080/mobile-users/ref-code/201902173333
```


## Running the test

Run all tests

```bash
mvn test
```

Specify the pattern of test file

```bash
mvn -Dtest=*UnitTest test

# or

mvn -Dtest=*IntegrationTest test
```

**Note: For integration test, it requires external resource such as database. You have to start PostgreSQL before run the test**
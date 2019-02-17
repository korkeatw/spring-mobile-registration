# Mobile Registration

The sample REST service using Spring

## Requirement software/lib

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


## Running the unit test

Run all tests

```bash
mvn test
```

Specify the pattern of test file

```bash
mvn -D*UnitTest test

# or

mvn -D*IntegrationTest test
```
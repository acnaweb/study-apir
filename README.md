# Study APIR

API de estudo de Spring Boot.

## Instruções de uso

```
mvn spring-boot:run
```

```
docker build -t study-api:1.0 .
docker run study-api:1.0
```


```
docker run \
  -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=3306 \
  -e DB_SCHEMA=db_api \
  -e DB_USER=root \
  -e DB_PWD=root_pwd \
  -e SPRING_PROFILES_ACTIVE=prd \ 
  study-api:1.0
```


$env:DB_SERVER_URL="localhost"
$env:DB_SERVER_PORT="3306"
$env:DB_SCHEMA="dbprd"
$env:DB_USER="root"
$env:DB_PWD="root_pwd"

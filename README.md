# Study APIR

API Java para estudo de Spring Boot

## Criando .jar

```
mvn clean package
```

- Localizar o .jar em /target

```
java -Dspring.profiles.active=dev -jar target/app.jar
```

## Variáveis de ambiente

```
export DB_USER=root
export DB_PASSWORD=root_pwd
export DB_SERVER=localhost
export DB_PORT=3306
export DB_DATABASE=api
```

- Usando arquivo .env e carregando em ambiente bash (Linux/Mac)

```
export $(cat .env | xargs)
env | grep DB_
```

## MER

![](assets/images/mer.png)

## Instalação

- Limpar e criar a pasta _/target_

```
mvn clean package
```

- Configuração do Swagger

  - https://springdoc.org/properties.html

* application.properties

```
springdoc.swagger-ui.path=/
springdoc.swagger-ui.disable-swagger-default-url=true
```

## Navegação

### Executar a API

- _Executando_ **Maven**

```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Documentação da API (Swagger)

- http://localhost:8080/swagger-ui.html

## Referencias

- https://springdoc.org/

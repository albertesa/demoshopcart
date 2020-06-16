# Simple Java Spring project featuring a Shopping Cart functionality 

## GitHub repository link
**[demoshopcart on GitHub](https://github.com/albertesa/demoshopcart)**

## Security and support for SSL/TLS
To prevent **XSS** attacks [OWASP](https://owasp.org/) [java-html-sanitizer](https://github.com/OWASP/java-html-sanitizer) and **Angular** built in feature of interpolating HTML tags are used to sanitize input and output values on client.

To prevent the **CSRF (XSRF)** attacks, the application uses built in **Angular** and **Spring Boot** features to support exchange of the **XSRF-TOKEN** cookie and **CORS** requests.

No authentication is implemented yet, as a consequence shopping carts are supported only anonymously.

HTTPS protocol (with SSL/TLS) is not supported yet.

## How to run the Angular application in development

NodeJs and Angular CLI must be installed.

    cd demo-shopping-cart-prj
    npm install
    ng serve
    ng build --prod

## Environment Setup
    export MONGO_USER=<user_name>
    export MONGO_PWD=<user_password>
    export MONGO_DB=<db_name>
    export MONGO_HOST=<db_host>

## Useful commands
    cd jsbexample 
    mvn clean install -Pdev
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
    mvn clean package -Pprod
    java -Dspring.profiles.active=prod -jar target/sample.project-0.0.1-SNAPSHOT.jar
    mvn javadoc:javadoc - to generate Java API documentation
    
## Cart and cart items are immutable objects
The **Cart** and **CartItem** objects are immutable and can be created/updated only by the **CartService** instance.

## Response Exceptions Handler Component

**albertesa.sample.prj.controllers.RestResponseEntityExceptionHandler**

## **FLEX Layout** is used in UI to support responsive UIs
**FLEX Layout** is used in UI to support responsive UIs.

### List of some useful **REST** API Endpoints

##### Swagger UI

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

##### Application info
    curl --location --request GET 'http://localhost:8080/actuator/info'

##### List all carts
    curl --location --request GET 'http://localhost:8080/cart/list' --data-raw ''

##### Get a cart by **id**
    curl --location --request GET 'http://localhost:8080/cart/222222'

##### Delete a cart by **id**
    curl --location --request POST 'http://localhost:8080/cart/222222/deletecart' --data-raw ''

##### Add a cart item for a product
    curl --location --request POST 'http://localhost:8080/cart/222222/additem' \
      --header 'Content-Type: application/json' \
      --data-raw '{
        "productId": "prod2",
        "productName": "kettle",
        "productImg": "kettle.jpg",
        "numOfItems": 5
    }'

##### NEW CART - Add a cart item for a product (cart ID must be "new")
The new cart ID will be returned in the response

    curl --location --request POST 'http://localhost:8080/cart/new/additem' \
      --header 'Content-Type: application/json' \
      --data-raw '{
        "productId": "prod2",
        "productName": "kettle",
        "productImg": "kettle.jpg",
        "numOfItems": 5
    }'


##### Remove a cart item from a cart (just send the whole cart item as is)
    curl --location --request POST 'http://localhost:8080/cart/222222/removeitem' \
      --header 'Content-Type: application/json' \
      --data-raw '{
        "productId": "prod2",
        "productName": "kettle",
        "productImg": "kettle.jpg",
        "numOfItems": 4
    }'

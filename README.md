# test_dev_roberto_do_rosario

This project respond to the second phase of The Agile Monkeys recruitment process.
The project use the Grails Framework, based on Spring, Hibernate, SpringSecurity, using Groovy as backend 
language, and H2Db as in memory Database.

The main structure and files for this project are:

## Setup

### grails-5.1.9
The folder with the binaries of the grails framework, this folder must be installed
as a JDK with enviroment variables, but in this case it will be used as a portable
binary.

We need to edit the enviroment variables an add the following ones:


Copy the path to the bin directory inside the grails folder you have downloaded, for example,

    C:/path_to_grails/bin

Go to Environment Variables, you can typically search or run the command below, the type env and then Enter

    Start + R

Edit the Path variable on User Variables / System Variables depending on your choice.

    Paste the copied path in the Path Variable.
    for this project exaple: <Donwload Path>\test_dev_roberto_do_rosario\grails-5.1.9\grails-5.1.9\bin

If Grails is working correctly you should now be able to type **grails -version** in the terminal window and see output similar to this:

    Grails version: 5.2.2

Next we need to build our project, for this we move into our project folder "test_dev_roberto_do_rosario"
and execute in a cmd the next command

    gradlew build

We wait until the dependencies are downloaded and the code compiled.



## App Structure and most important folders/files

### grails-app/domain 
In this folder we locate all the entities clases we persist on the database.

### grails-app/controllers
we found the controllers who receive our request and process it with the service.

### grails-app/services
Here is the back stage of the api, here we found all the services we us to process our request, save and update data, etc.

### grails-app/conf/application.groovy
This is our first configuration file, where we configure the rest parameters, and 
the sprinSecurity to define what access we grant to a specific role or if 
the user must be authenticated.

### grails-app/conf/application.yml
This file, give us the ability to make different configuration by
environment, for this project we will use the "development" and "production"
configuration

## App Configuration

### grails-app/conf/application.groovy
It's important in this file to define the rules of **All the new controllers we make** and what kind of access or
who can access to this controller, in this example we add in

    grails.plugin.springsecurity.controllerAnnotations.staticRules = []
the following rules

    //This means only a "ROLE_ADMIN" user can access to the "User" controller
    [pattern: '/user/**',   access: ['ROLE_ADMIN']],
    
    //This means only a "ROLE_ADMIN" and "ROLE_WORKER user can access to the "User" controller
    [pattern: '/customer/**',   access: ['ROLE_ADMIN', 'ROLE_WORKER']],
    
    //In this case, the "Api" controller is the rest api login from SpringSecurity, in this case we permit the access to all the incoming request
    [pattern: '/api/**',   access: ['permitAll']]  

The rest of parameters in **grails.plugin.springsecurity.rest** are the SpringSecurity rest api plugin configuration to read more about it, here is the [official documentation](https://grails-plugins.github.io/grails-spring-security-rest/latest/docs/index.html).


### grails-app/conf/application.yml
Here we configure two important thinks, the **S3 Bucket** and the **Database**

#### S3 Bucket
To a quick-setup we have this [guide](https://github.com/agorapulse/grails-aws-sdk/tree/master/grails-aws-sdk-s3) from @agorapulse, 
installed the s3 pluging and configured the s3 bucket with **public access** and with **only owner permision** we write in our application.yml belows of the grails tag, as we see below  

    grails:
        plugin:
            awssdk:
                region: eu-west-1
                accessKey: #AccessKey
                secretKey: #SecretKey
                s3:
                    bucket: testdevroberto

#### Database
This is a easy configuration, we only needs to fill the parameters, in this case we have 2 examples, 
the **development** config for a h2-database (in memory database) and 
the **production** config for a mysql database

    environments:
        development:
            dataSource:
                dbCreate: create-drop
                url: jdbc:h2:mem:devDb;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE
        production:
            dataSource:
                driverClassName: 'com.mysql.jdbc.Driver'
                dialect: org.hibernate.dialect.MySQL5InnoDBDialect
                username: 'admin'
                password: 'admin'
                dbCreate: 'update'
                schema: 'test_dev_roberto_do_rosario'
                url: jdbc:mysql://127.0.0.1:3306/test_dev_roberto_do_rosario
                properties:
                    jmxEnabled: true
                    initialSize: 5
                    maxActive: 50
                    minIdle: 5
                    maxIdle: 25
                    maxWait: 10000
                    maxAge: 600000
                    timeBetweenEvictionRunsMillis: 5000
                    minEvictableIdleTimeMillis: 60000
                    validationQuery: SELECT 1
                    validationQueryTimeout: 3
                    validationInterval: 15000
                    testOnBorrow: true
                    testWhileIdle: true
                    testOnReturn: false
                    jdbcInterceptors: ConnectionState
                    defaultTransactionIsolation: 2 # TRANSACTION_READ_COMMITTED


## Run the Application

In a cmd in the project folder we will execute this grails command: 

    grails run-app -Dgrails.env=development

When the console said **EXECUTING** we can access to our Database

## H2-Database
URL To access:

    http://localhost:8080/h2-console/

Web interface parameters:

    Driver class: org.h2.Driver
    JDBC URL: jdbc:h2:mem:devDb
    User name: sa
    Password:

# Rest API

## Api (login)

#### Login

```http
  POST /api/login
```
 with a JSON request

| Parameter | Type     | Description                |
| :-------- | :------- |:---------------------------|
| `username` | `string` | the user **username**.     |
| `password` | `string` | the user **password**. |

#### Logout

```http
  POST /api/logout
```

## User

#### Create a user with his basic information

```http
  POST /user
```

| Parameter | Type     | Description                                                                                        |
| :-------- |:---------|:---------------------------------------------------------------------------------------------------|
| `firstname` | `string` | the user **firstname**.                                                                            |
| `lastname` | `string` | the user **lastname**.                                                                             |
| `roleAuthority` | `string` | the **user roles**, the roles must be writen with "," between them e.g **ROLE_WORKER,ROLE_ADMIN**. |
| `username` | `string` | the user **username**. **REQUIRED**                                                                |
| `password` | `string` | the user **password**. **REQUIRED**                                                                            |

#### Update the basic information for a user

```http
  PUT /user/${id}
```

| Parameter | Type     | Description                                                                                        |
| :-------- |:---------|:---------------------------------------------------------------------------------------------------|
| `firstname` | `string` | the user **firstname**.                                                                            |
| `lastname` | `string` | the user **lastname**.                                                                             |
| `roleAuthority` | `string` | the **user roles**, the roles must be writen with "," between them e.g **ROLE_WORKER,ROLE_ADMIN**. |
| `username` | `string` | the user **username**.                                                                             |

#### Show User list filtered with some input parameters, the String filters works with SQL "like", so you can find with part of the text 

```http
  GET /user/
```

| Parameter | Type     | Description                                                                                        |
| :-------- |:---------|:---------------------------------------------------------------------------------------------------|
| `firstname` | `string` | the user **%firstname%**. can use part of the string to search                                     |
| `lastname` | `string` | the user **%lastname%**.  can use part of the string to search                                                                         |
| `roleAuthority` | `string` | the **user roles**, the roles must be writen with "," between them e.g **ROLE_WORKER,ROLE_ADMIN**. |
| `password` | `string` | the user **password**.                                                                             |

#### Show User, find a specific user by his id

```http
  GET /user/${id}
```

#### Delete User, Disable or do a logical delete with these method (the information will be hidden for queries but it can be found in the DB).

```http
  DELETE /user/${id}
```

## Customer

#### Create a customer with his basic information

```http
  POST /customer
```

| Parameter | Type     | Description       |
| :-------- |:---------|:-------------------------|
| `firstname` | `string` | the customer **firstname**.|
| `lastname` | `string` | the customer **lastname**.|
| `imageFile` | `File`   | the **customer profile image** |


#### Update a customer's basic information

```http
  POST /customer/${id}
```

| Parameter | Type     | Description       |
| :-------- |:---------|:-------------------------|
| `firstname` | `string` | the customer **firstname**.|
| `lastname` | `string` | the customer **lastname**.|
| `imageFile` | `File`   | the **customer profile image** |

#### Show Customer list filtered with some input parameters, the String filters works with SQL "like", so you can find with part of the text

```http
  GET /customer/
```

| Parameter | Type     | Description                                                                                        |
| :-------- |:---------|:---------------------------------------------------------------------------------------------------|
| `firstname` | `string` | the customer **%firstname%**. can use part of the string to search|
| `lastname` | `string` | the customer **%lastname%**.  can use part of the string to search|        

#### Show User, find a specific user by his id

```http
  GET /customer/${id}
```

#### Delete customer, Disable or do a logical delete with these method (the information will be hidden for queries but it can be found in the DB).

```http
  DELETE /customer/${id}
```

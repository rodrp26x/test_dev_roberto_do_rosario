# test_dev_roberto_do_rosario
test_dev_roberto_do_rosario

This project respond to the second phase of The Agile Monkeys recruitment process.
The project use the Grails Framework, based on Spring, Hibernate, SpringSecurity, using Groovy as backend 
language, and H2Db as in memory Database.

The main structure and files for this project are:

# grails-5.1.9
The folder with the binaries of the grails framework, this folder must be installed
as a JDK with enviroment variables, but in this case it will be used as a portable
binary. 

# grails-app/domain 
In this folder we locate all the entities clases we persist on the database.

# grails-app/controllers
we found the controllers who receive our request and process it with the service.

# grails-app/services
Here is the back stage of the api, here we found all the services we us to process our request, save and update data, etc.

# grails-app/conf/application.groovy
This is our first configuration file, where we configure the rest parameters, and 
the sprinSecurity to define what access we grant to a specific role or if 
the user must be authenticated.

# grails-app/conf/application.yml
This file, give us the ability to make different configuration by
environment, for this project we will use the "development" and "production"
configuration

# Rest API

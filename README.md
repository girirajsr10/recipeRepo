# recipeRepo
Recipe Restful API Java standalone app
The Application cretes Rest endpoints to creae update delete or Search Recipes.

The Application is a Rest based application. The Api's are created with help of Spring boot Spring MVC and Spring JPA libraries. With Java 8 as programming language.

The backend database is selected as Mysql cause of its general availability as well as ease of use.
The database primarily has 4 tables 
Users Table -> To store loging information
Recipe Table -> To store user created recipe data.
Ingredients Table -> To store all Ingredients as separate which are mapped to Recipe via Many to Many relationship. This is described using recipes_ingredients table
RecipeSteps Table -> To store the steps related to recipe.

The ER schema is attached.

![image](https://user-images.githubusercontent.com/7194144/182073375-ed30e3b2-1dba-457e-a1fe-336f1982e0bf.png)


Features of application
1) JWT Authentication mechanism. This was prefferd over basic auth to keep up with new standards. 
2) Restful API with Spring Data JPA to connect to DB. This reduces the migration effort if we need to change the underlying db from Mysql to something else.
3) Excecption Handling mechanism added using Sprign's controller advice. To reduce boiler plate code.
4) Added Spring AOP aspect for logging before and after any method of controller is called. This is done to create ease of debuging in case of issues.
5) Added Input Validations (Though very basic) To accept only sanitized inputs.

How to use the app -->
Step 1) Download the source code run mvn build command.
Step 2) Either provide MYSQL_HOST location in env variable or localhost will be taken as default. If the default DB port needs to be changed please update it in application.properties file.
Step 3) Provide a JWT_SECRET value in env variable. This value is used to create hash on which jwt token is generated.
Step 4) Execute the generated Jar file using cmd or run 

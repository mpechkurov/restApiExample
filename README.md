# Home Challenge

 
## Tools

* git
* JDK8
* Maven3
* Spring-boot

## How to build and run tests 
### Option 1 - command line 
1.  Clone repository
```
https://github.com/mpechkurov/mykhailo-pechkurov.git
```
2. Open directory 
```
cd <dir_name> 
```
3. Run test 
```
mvn test
```
4. Start server on localhost
```
mvn spring-boot:run
```

### Option 2 - intelij IDEA
1. Clone repo via Intelij IDEA

2. Open project in IDE 

3. Run tests 
 - open class TransactionControllerTest
 - click on green triangle
 
4. Start server on localhost
- open class Application 
- click on green triangle 


## Rest 

| type  | url | body example | response example |
| ------------- | ------------- |------------- |------------- |
| PUT  | /transactionservice/transaction/{transaction_id} |{"amount": 500, "type": "cars"}                 | { "status": "ok" }        |
| PUT  | /transactionservice/transaction/{transaction_id} |{"amount": 500, "type": "cars", "parent_id": 10}| { "status": "ok" }        |
| GET  | /transactionservice/transaction/{transaction_id} | -                                              | {"amount": 500, "type": "cars", "parent_id": 10} |
| GET  | /transactionservice/types/{type}                 | -                                              | [1,2]                     |
| GET  | /transactionservice/sum/{type}                   | -                                              | {"sum": 100 }             |

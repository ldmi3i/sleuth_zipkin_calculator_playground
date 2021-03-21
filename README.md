## Project main purpose is to tryout cloud sleuth and zipkin (Just for fun)
### Common description
Project designed as distributed calculator - each service perform different arithmetic operation  
After sending expression to main calculator service you can obtain traces using zipkin on http://localhost:9411/zipkin/  
Traces depend on expression and there can have many variations  
Also to all services added expression as tag and annotation of starting and ending events of "calculations"  
they can be observed with zipkin

Currently, supported only simple expressions without parentheses  
For example
```
 5.0 ^ 2 + 7.0 - 8.0 * 1 * 2 + 15 - 15 / 5 * 6
```

Supported operations: ```+ - * / ^```  
Multiple consecutive signs currently not supported ( '5 * -6' ).

### Endpoints
'calculate' endpoint: POST  
```
 http://localhost:8019/calculate
```
body
```
{
    "expression":"5.0 ^ 2 + 7.0 - 8.0 * 1 * 2 + 15 - 15 / 5 * 6"
}
```

### Build and start
Required: docker, gradle and +- 3 gb of free memory.
Build and start application with gradle and docker-compose:  
```gradle clean build```  
```docker-compose up -d --build```

### Notes
docker-compose file use the latest standard without version statement
if you do not have the latest version of docker - add line to file like ```version '3.1'```

Exponentiation operator can take only integer numbers because it converts it to expression and 
reuses base calculation endpoint. (if number is double - it will be cast to int)
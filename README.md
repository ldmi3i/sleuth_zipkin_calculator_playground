## Project main purpose is to tryout cloud sleuth and zipkin
### Common description
Project designed as distributed calculator - each service perform different arithmetic operation  
After sending expression to main calculator service you can obtain traces using zipkin on http://localhost:9411/zipkin/  
Traces depend on expression and there can be many variations  
Also to all services added expression as tag and annotation of starting and ending events of "calculations"  
they can be observed with zipkin

Currently, supported only simple expressions without parentheses  
For example
```
 5.0 ^ 2 + 7.0 - 8.0 * 1 * 2 + 15 - 15 / 5 * 6
```

Supported operations: ```+ - * / ^```  
Multiple consecutive signs currently not supported.

### Endpoints
calculate endpoint: POST  
```
 http://localhost:8019/calculate
```
body
```
{
    "expression":"5.0 ^ 2 + 7.0 - 8.0 * 1 * 2 + 15 - 15 / 5 * 6"
}
```

### Notes
Exponentiation operator can take only integer numbers because it converts it to expression and reuses base calculation endpoint
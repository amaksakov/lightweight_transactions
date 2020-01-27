# Lightweight Transaction Processing Engine - LTPE

LTPE or Lightsaber - is the  REST API and its implementation to provide 
lightweight and high-performant money transfer engine with transactions support

## API and usage

API is documented in 
[swagger.json] (api/swagger.json)

Project is build using maven 

### Running the endpoint

*mvn install* builds an executable jar which will run Undertow web container which binds to port 8080

Main class: com.amaksakov.lightsaber.TransferAPIApp

## Transaction model

Current implementation support in-memory storage 
and one operation at a time

## Next steps

0. Add swagger UI
1. Row-based locking
2. Asynchronous http response model
3. Using log4j for logging
4. Making API instrumented - i.e. adding dropwizard or other metrics
5. Load test to see the transaction speed and limits for the number of simultaneous requests
6. Specially designed test to verify for the deadlock situation
7. Use HTTPS only, no serving of HTTP






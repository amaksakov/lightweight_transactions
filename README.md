# Lightweight Transaction Processing Engine - LTPE

LTPE or Lightsaber - is the  REST API and its implementation to provide 
lightweight and high-performant money transfer engine with transactions support

## API and usage

<swagger json or UI here>

Project is build using maven 

### Running the endpoint

*mvn install* builds an executable jar which will run web container which binds to port 8747

## Transaction model

Current implementation support in-memory storage 
and one operation at a time

## Next steps

1. Row-based locking
2. Asynchronous http response model
3. Making API instrumented - i.e. adding dropwizard or other metrics
4. Load test to see the transaction speed and limits for the number of simultaneous requests
5. Specially designed test to verify for the deadlock situation






README
======


# Listing Report Application

##### Listings and their references are taken from API, which then get validated and saved into local database. Violations are written in csv and a report in a local json file with overall as well as monthly data regarding ebay and amazon marketplaces. The report gets uploaded to FTP and removed locally afterwards.

## To run the application:
Environment variables for the local database and FTP can be found in _.envVariables_ file. They probably have to be changed but each one has a default value given to it in application.properties.

To avoid making files in unwanted locations the application has to be run with 3 parameters
```
importLogCsvPath localReportJsonPath ftpReportJsonPath
```


## Technologies and traits of project
- Java SpringBoot
- Javax and custom validations
  - NotNull, min, max, email, size, UUID, decimal
  - Date deserialization using multiple formats
- RestTemplate API handling
- PostgreSQL
- Flyway
- API key, ebay and amazon names are obtained from application.properties
- H2 database for testing
- Tests for 13 significant classes with 97% line and 95% pitest mutation coverage
```
gradlew pitest
```
Html generated in /build/reports/pitest/timestamp


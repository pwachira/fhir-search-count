# fhir-search-count
A Java Commandline application that allows searching/counting for a patient from fhir resources
## Build
- Run postgres locally on port 5432 wiht user postgres and password postgres.
- create 2 databases:
  - oneuphealth
  - For convenience, consider running a docker postgres image with 
    - ```docker run --name postgres  --env POSTGRES_PASSWORD=postgres -itd --rm -p5432:5432  postgres```
    - ```CREATE DATABASE oneuphealth```
    

- Checkout the code
  - ```git clone https://github.com/pwachira/fhir-search-count.git ```
- Make sure you have  java jdk (project built on version 11)  
- Install gradle (using version 7.2 at time of creating project)
  - example ```brew install gradle```
- You might need to initialize gradle wrapper ```gradle wrapper```
- run ```./gradlew build -x test``` . Test results html page will then be at build/reports/tests/test/classes/com.uphealth.fhirsearch.FhirTests.html
- test the setup ```./gradlew test```
  - There is a test to verify loading of files to a table
  - There is a test to verify searching for a patient that not in the top level resource but in a referenced resource
  
## Run
- To load files to the db, run
    - ``` java -jar -Dspring.profiles.active=load  build/libs/fhir-search-0.0.1-SNAPSHOT.jar``` 
- To search for a patient using a resource id, run
  - ```java -jar -Dspring.profiles.active=search  build/libs/fhir-search-0.0.1-SNAPSHOT.jar c4768f2a-f932-4ab6-a4a5-6e8ae0f9da8d``` 
     
## Implementation Notes
The application loads the fhir resources as text type into postgresql with the following schema;
On reading and deserializing the resource body, hapi fhir resources objects are created
```                                   Table "public.fhir_record"
   Column    |          Type          | Collation | Nullable |             Default              
-------------+------------------------+-----------+----------+----------------------------------
 id          | bigint                 |           | not null | generated by default as identity
 body        | text                   |           |          | 
 resource_id | character varying(255) |           |          | 
 type        | character varying(255) |           |          | 
 ```

Different hapi fhir resources have different ways references are declared. To standardize the search an adapter layer 
is created
Each resource adapter has a reference to its fhir resource object

To allow searching for a patient in a referenced resource, a breadth first search is used with a specified
depth parameter. The default value for depth is 1, meaning 1 level removed from the resource being searched.

- TODO:
 - complete creating all adapters. Currently only searching 8 resource types.
 - Add a middle step to save only needed 3 fields (resource ids, subject, referenxes) in a table to bypass expensive deserialization during the actual search. 

# About this project

* Built on Spring Boot 2.0.3
* Java 8 Eclipse Jee Photon
* There is only one RESTful GET API defined
For example: /api/workers/{userId}/jobs?orderBy=distance,!job.billRate,job.jobId&limit=5
where:
    * url: /api/workers/{userId}/jobs
    * query1: orderBy
        * An array of field names based on the definition of "get jobs" API's response. Each field has a "job" prefix
        * A distance between the worker and job
        * An exclamation mark (!) indicates the field is in descending order. Default is in descending order
        * e.g. orderBy=distance,!job.billRate,job.jobId will sort the list using the specified fields from left to right
    * query2: limit
        * The number of the jobs returned. Default is 3
* It returns an JSON array, of each object contains a distance in km, plus a copy of the job
* It loads / reloads workers and jobs every 60s from the API provided - data are kept in local memory
* There is a test file: QuickTest.java that simulates a number of API requests being sent asynchronously
* logback.xml can be specified in -Dlogging.config=config/logback.xml

# Issues & ideas

* The client is built on google HttpRequest without error handling such as offline / timeout and HTTPS transfer
* The deserialization from JSON to java object is vulnable if JSON's structure is changed
* Many configurations are hard-coded but should be configured via properties file.
* ElasticSearch or solr can be used for bigger data with more complex matching criteria

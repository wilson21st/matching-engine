# About this project

* Built on Spring Boot 2.0.3
* Java 8 Eclipse Jee Photon with Maven
* There is only one RESTful GET API defined
For example: **/api/workers/{userId}/jobs?orderBy=distance,!job.billRate,job.jobId&limit=5**
where:
    * url: /api/workers/{userId}/jobs
    * query1: orderBy
        * An array of field names based on the definition of "get jobs" API's response. Each field has a "job" prefix
        * A distance between the worker and job
        * An exclamation mark (!) indicates the field is in descending order. Default is in ascending order
        * e.g. orderBy=distance,!job.billRate,job.jobId will sort the list using the specified fields from left to right
        * If not specified, default is **!job.billRate,distance,job.startDate**
    * query2: limit
        * The maximum number of the jobs returned. Default is 3
* It returns an JSON array, of each object contains a distance in km, plus a copy of the job
* It loads / reloads workers and jobs every 60s from the API provided - data are kept in local memory
* There is a test file: QuickTest.java that simulates a number of API requests being sent asynchronously
* logback.xml can be specified in -Dlogging.config=config/logback.xml

# Issues & ideas

* The client is built on google HttpRequest without error handling such as offline, timeout or HTTPS issues
* The deserialization from JSON to java object is vulnerable if JSON's structure is changed
* Many configuration params are hard-coded. Better if they are configured from property file
* ElasticSearch or solr can be used for bigger data with more complex matching criteria

# Response

Url: **http://localhost:8080/api/workers/0/jobs**

```
[
  {
    "job": {
      "requiredCertificates": [
        "The Asker of Good Questions"
      ],
      "driverLicenseRequired": false,
      "location": {
        "longitude": "14.013835",
        "latitude": "49.994037"
      },
      "billRate": "$19.79",
      "workersRequired": 2,
      "startDate": "2015-11-10T04:11:26.675Z",
      "about": "Eiusmod velit ad et aliquip sint incididunt non excepteur ut consequat ullamco occaecat. Excepteur ullamco tempor ut est. Labore do voluptate dolore elit. Ea dolor voluptate cupidatat cupidatat non ad cillum pariatur in. Id aliqua laborum ut voluptate laboris elit. Commodo mollit proident proident voluptate. Tempor consectetur minim reprehenderit aute ea quis tempor minim adipisicing proident exercitation magna tempor.",
      "jobTitle": "Ambassador of buzz",
      "company": "Zytrac",
      "guid": "562f66aad42092ef776f7ccb",
      "jobId": 26
    },
    "distance": 23
  },
  {
    "job": {
      "requiredCertificates": [
        "Outstanding Innovator"
      ],
      "driverLicenseRequired": false,
      "location": {
        "longitude": "13.716166",
        "latitude": "49.93936"
      },
      "billRate": "$10.24",
      "workersRequired": 2,
      "startDate": "2015-11-12T21:46:01.562Z",
      "about": "Sit consectetur sunt labore exercitation minim aliqua tempor fugiat tempor sint eu non consequat in. Aliquip dolore id exercitation nostrud aliquip magna eu amet ea esse fugiat. Tempor anim aute est nulla ea laboris ut cupidatat. Excepteur in commodo minim esse reprehenderit elit dolor elit cillum labore adipisicing Lorem. Aliquip commodo labore dolore ullamco cupidatat id excepteur aliquip.",
      "jobTitle": "Sous chef",
      "company": "Multron",
      "guid": "562f66aaa33cc26cdf48198f",
      "jobId": 35
    },
    "distance": 25
  }
]
```


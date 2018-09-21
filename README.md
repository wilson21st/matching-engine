# About this project

* Built on Spring Boot 2.0.3
* Java 8 Eclipse Jee Photon with Maven
* RESTful GET API defined:

**/api/workers/{userId}/jobs?limit={limit}**

where:

    * path: /api/workers/{userId}/jobs
    * query: 
        * limit: Optional. The maximum number of jobs returned. Default is 3

* It returns JSON array of each a Job object
* It loads / reloads workers and jobs every 60s from the API provided - data are kept in local memory

# Response

Url: **http://localhost:8080/api/workers/0/jobs**

```
[
  {
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
  {
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
  }
]
```


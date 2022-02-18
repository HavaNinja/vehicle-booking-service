<h1 align="center">Vehicle Hire Service </h1> <br>

## Table of Contents

- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [API](#API)



## Features

* List all vehicles the hire company has
* List available vehicles for hire – all vehicles that are available to be hired on a given day
* Calculate cost of hiring a specific vehicle for a provided date range
* Make a reservation/booking for a customer for a specific vehicle for a provided date range


## Requirements

* [Java 11 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org/download.cgi)


* [Docker](https://www.docker.com/get-docker)


### Run Local

```bash
$ mvn spring-boot:run
```
Application will run by default on port `8080`

## API
Available REST API
* List all vehicles which service has `GET localhost:8080/api/vehicle/all `
* List available vehicles for hire – all vehicles that are available to be hired on a given day `GET localhost:8080/api/vehicle/available?startDate=2025-02-17&endDate=2022-07-25`
*  Calculate cost of hiring a specific vehicle for a provided date range `POST localhost:8080/api/vehicle/cost`  `request body example {
   "startDate":"2022-02-21",
   "endDate":"2022-02-21",
   "registrationNumbers":["ZED 852"]
   }`
* Make a reservation/booking for a customer for a specific vehicle for a provided date range `POST localhost:8080/api/vehicle/reservation` `request body example {
  "startDate":"2022-02-21",
  "endDate":"2022-02-21",
  "registrationNumbers":["ZED 852"],
  "userId":"515abc5d-88f3-4ea0-9c2a-aa1b78c4eaf6"
  }` 
 



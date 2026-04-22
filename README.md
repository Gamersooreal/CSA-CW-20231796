# Smart Campus Sensor & Room Management API

## Overview

This project is a RESTful API developed for the 5COSC022W Client-Server Architectures coursework. The system models a Smart Campus environment where rooms contain sensors, and sensors maintain a history of readings. The API is built with JAX-RS and uses in-memory data structures instead of a database, in line with the coursework requirements.

The three main resources are:

- `Room`
- `Sensor`
- `SensorReading`

The API also includes:

- a discovery endpoint
- filtered sensor retrieval
- nested reading history
- custom exception mapping
- request and response logging

## API Design

Base path:

```text
/api/v1
```

Main endpoints:

- `GET /api/v1`
- `GET /api/v1/rooms`
- `POST /api/v1/rooms`
- `GET /api/v1/rooms/{roomId}`
- `DELETE /api/v1/rooms/{roomId}`
- `GET /api/v1/sensors`
- `POST /api/v1/sensors`
- `GET /api/v1/sensors?type=CO2`
- `GET /api/v1/sensors/{sensorId}/readings`
- `POST /api/v1/sensors/{sensorId}/readings`
- `GET /api/v1/sensors/{sensorId}`
- `DELETE /api/v1/sensors/{sensorId}`

Design choices:

- `Room` stores basic room information and a list of linked sensor IDs.
- `Sensor` stores the current state of a device and the room it belongs to.
- `SensorReading` stores historical measurement data for a sensor.
- `MockDatabase` keeps all data in memory using lists and maps.
- Exception mappers return structured JSON responses instead of raw server errors.
- A logging filter records request and response information centrally.

## Project Structure

```text
CW/
  CSA_20231796/
    CW_20231796/
      pom.xml
      src/main/java/com/mycompany/cw_20231796/
        RestApplication.java
        dao/
        exception/
        filter/
        model/
        resource/
```

## Recommended Method (Using NetBeans + Tomcat Integration)

1. One-Time Setup
2. Open NetBeans.
3. Go to Services → Servers.
4. Add Apache Tomcat Server.

### Select your Tomcat installation directory:

```
D:\apache-tomcat-9.0.100\apache-tomcat-9.0.100
```

5. Enter your Tomcat username and password
(configured in conf/tomcat-users.xml)

### Configure Project
1. Right-click the project → Properties
2. Go to Run
3. Set:
Server: Apache Tomcat
Context Path: /CW_20231796-1.0

### Run the Application
Click Run (F6)

NetBeans will:
- Build the project
- Deploy it to Tomcat
- Start the server
- Open the application in your browser


## Alternative Method (Manual WAR Deployment)

Use this method if NetBeans integration is not available.

### Build in NetBeans
1. Open the project in NetBeans.
2. Right-click the project.
3. Select Clean and Build.
4. This generates the .war file in:
```
D:\CSA_20231796\CW_20231796\target
```
### Deploy to Tomcat

Tomcat path:
```
D:\apache-tomcat-9.0.100\apache-tomcat-9.0.100
```

### Steps:

1. Copy the generated .war file into:

```
apache-tomcat-9.0.100\webapps
```

2. Open PowerShell.

3. Navigate to:
```
apache-tomcat-9.0.100\bin
```
4. Run:
```
startup.bat
```

To stop Tomcat:

```powershell
Ctrl + C
```


## Sample curl Commands

### 1. Discovery endpoint

curl http://localhost:8080/CW_20231796-1.0/api/v1

### 2. Create a Room

curl -i -X POST http://localhost:8080/CW_20231796-1.0/api/v1/rooms `
  -H "Content-Type: application/json" `
  -d "{\"id\":\"LIB-301\",\"name\":\"Library Quiet Study\",\"capacity\":30}"

### 3. Create a Sensor (with Room Validation)

curl -i -X POST http://localhost:8080/CW_20231796-1.0/api/v1/sensors `
  -H "Content-Type: application/json" `
  -d "{\"id\":\"TEMP-001\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":0.0,\"roomId\":\"LIB-301\"}"

### 4. Add a Sensor Reading

curl -i -X POST http://localhost:8080/CW_20231796-1.0/api/v1/sensors/TEMP-001/readings `
  -H "Content-Type: application/json" `
  -d "{\"id\":\"R1\",\"timestamp\":1713200000,\"value\":22.5}"

### 5. Get Reading History

curl http://localhost:8080/CW_20231796-1.0/api/v1/sensors/TEMP-001/readings
  
## Report Answers

### Part 1

#### 1. Project and Application Configuration

The API uses a JAX-RS `Application` subclass with `@ApplicationPath("/api/v1")` so that all resources are grouped under a clear versioned entry point. In this design, the resource classes are treated as request-handling classes rather than places to keep shared application state. Shared data is stored in in-memory collections outside the resource classes, because multiple requests may access the same rooms, sensors, and readings. To avoid data corruption or race conditions, the shared state is kept in central collections and the resource classes only perform operations on that shared data.

#### 2. Discovery Endpoint

The discovery endpoint returns basic API metadata such as version, contact information, and the main resource paths. This supports a more RESTful design because clients can learn how to navigate the API from the response itself instead of depending only on external documentation. Hypermedia improves usability by making the API easier to explore and reducing the amount of hardcoded knowledge needed on the client side.

### Part 2

#### 1. Room Resource Implementation

Returning full room objects gives the client immediate access to all useful room data such as name, capacity, and linked sensors. This is convenient and reduces extra requests. Returning only room IDs would reduce response size, but the client would then need additional calls to retrieve details for each room. The trade-off is therefore between smaller payloads and fewer client requests.

#### 2. Room Deletion and Safety Logic

The `DELETE` operation is idempotent because repeating the same delete request does not keep changing the system state after the room is already gone. In this implementation, if a room still has sensors linked to it, deletion is blocked and a conflict response is returned. If the room is empty, the first delete removes it. Sending the same delete again does not create a new effect on the system, because the room is already no longer available.

### Part 3

#### 1. Sensor Resource and Integrity

The `@Consumes(MediaType.APPLICATION_JSON)` annotation tells JAX-RS that the endpoint expects JSON input. If a client sends a different content type such as `text/plain` or `application/xml`, JAX-RS rejects the request before normal processing and typically returns `415 Unsupported Media Type`. This protects the API from invalid input formats and keeps request handling predictable.

#### 2. Filtered Retrieval and Search

Using `@QueryParam` for filtering is more suitable because the sensor collection remains the same resource, and the query parameter simply narrows the results. A path such as `/sensors/type/CO2` makes the filter look like a separate resource path rather than an optional search condition. Query parameters are therefore better for flexible filtering and searching over a collection.

### Part 4

#### 1. Sub-Resource Locator Pattern

The sub-resource locator pattern helps separate nested functionality into focused classes. Instead of placing all sensor and sensor-reading logic inside one large resource, the reading history is delegated to a dedicated `SensorReadingResource`. This improves readability, reduces complexity, and makes the code easier to maintain as the API grows.

#### 2. Historical Data Management

The reading history endpoint allows each sensor to keep its own collection of readings. When a new reading is posted, the system not only stores the reading in the history list but also updates the parent sensor's `currentValue`. This keeps the current sensor state consistent with its latest recorded measurement and avoids mismatches between historical and current data.

### Part 5

#### 1. Resource Conflict (409)

When a client tries to delete a room that still has sensors assigned to it, the request is rejected with `409 Conflict`. This is appropriate because the request itself is valid, but it conflicts with the current state of the system. The custom exception and mapper make sure the API returns a structured JSON explanation rather than a generic server error.

#### 2. Dependency Validation (422 / 400)

When a client tries to create a sensor with a `roomId` that does not exist, the request body may still be valid JSON, but the relationship inside it is invalid. That is why `422 Unprocessable Entity` is often more accurate than `404 Not Found`. A `404` usually means the requested endpoint or target resource cannot be found directly, while `422` better describes a valid request whose referenced data fails business validation.

#### 3. State Constraint (403)

A sensor in `MAINTENANCE` state cannot accept new readings because its current operational state forbids that action. Returning `403 Forbidden` makes sense because the server understands the request but refuses to perform it due to the sensor's status. This reflects the business rule clearly to the client.

#### 4. Global Safety Net (500)

A catch-all `ExceptionMapper<Throwable>` is important because it prevents raw stack traces or default HTML error pages from being exposed. From a security perspective, stack traces can reveal internal class names, package structure, implementation details, and other technical information about the application. That information could help an attacker understand how the system is built and where it may be weak.

#### 5. API Request and Response Logging Filters

Using JAX-RS filters for logging is better than placing logging statements inside every resource method because logging is a cross-cutting concern. A filter keeps this logic centralized, consistent, and reusable across the whole API. This reduces repetition and makes the resource classes easier to read because they can focus on business logic rather than infrastructure concerns.

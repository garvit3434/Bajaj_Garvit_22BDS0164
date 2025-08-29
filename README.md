# Webhook SQL Solution App

A Spring Boot application that automatically:
1. Sends a POST request on startup to generate a webhook
2. Solves a SQL problem based on the response
3. Submits the solution to the returned webhook URL using JWT authentication

## Overview

This application is designed to:
- On startup, send a POST request to generate a webhook
- Based on the response, solve a SQL problem 
- Send the solution (a final SQL query) to the returned webhook URL using a JWT token

## SQL Problem Solution

The SQL problem requires calculating the number of employees who are younger than each employee, grouped by their respective departments. For each employee, we need to return the count of employees in the same department whose age is less than theirs.

The solution uses a self-join:

```sql
SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, 
       COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT 
FROM EMPLOYEE e1 
JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID 
LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e1.DOB > e2.DOB 
GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME 
ORDER BY e1.EMP_ID DESC
```

## Technical Details

- Built using Spring Boot 2.7.0
- Uses RestTemplate for the initial webhook generation request
- Uses WebClient for the JWT authenticated solution submission
- Uses ApplicationRunner to trigger the flow on application startup

## How to Build

```bash
mvn clean package
```

## How to Run

```bash
java -jar target/webhook-app-0.0.1-SNAPSHOT.jar
```

## Project Structure

- `WebhookAppApplication.java`: Main Spring Boot application class
- `WebhookRunner.java`: ApplicationRunner implementation that triggers the webhook process on startup
- `WebhookService.java`: Service that handles the webhook process
- `SqlSolutionService.java`: Service that provides the SQL solution
- Model classes:
  - `WebhookRequest.java`: Request model for webhook generation
  - `WebhookResponse.java`: Response model for webhook generation
  - `SolutionRequest.java`: Request model for solution submission

## API Flow

1. POST request to `https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
2. Solve the SQL problem based on the last two digits of the registration number
3. Submit the solution to the webhook URL provided in the response

## Technologies Used

- Java 8
- Spring Boot
- Spring WebFlux (WebClient)
- Spring Web (RestTemplate)
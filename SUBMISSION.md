# Submission Details

## Repository Contents

This repository contains a Spring Boot application that:
1. Sends a POST request on startup to generate a webhook
2. Solves a SQL problem based on the response
3. Submits the solution to the returned webhook URL using JWT authentication

## SQL Solution

The SQL problem requires calculating the number of employees who are younger than each employee in their respective departments:

```sql
SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, 
       COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT 
FROM EMPLOYEE e1 
JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID 
LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e1.DOB > e2.DOB 
GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME 
ORDER BY e1.EMP_ID DESC
```

## Application Execution

When executed, the application:
1. Sends a POST request to the specified URL
2. Receives a webhook URL and access token
3. Determines the SQL problem based on the registration number
4. Generates the SQL solution
5. Submits the solution to the webhook URL with JWT authentication
6. Successfully receives a confirmation response

## Files in Repository

- `src/`: Contains all the source code
- `target/webhook-app-0.0.1-SNAPSHOT.jar`: The executable JAR file
- `pom.xml`: Maven project configuration
- `README.md`: Project documentation
- `.gitignore`: Git ignore file

## JAR File

The executable JAR file is located at:
```
target/webhook-app-0.0.1-SNAPSHOT.jar
```

To run the application:
```bash
java -jar target/webhook-app-0.0.1-SNAPSHOT.jar
```

## GitHub Repository

The public GitHub repository URL is: [REPOSITORY_URL]

## Raw JAR File Download Link

The direct download link to the JAR file is: [JAR_DOWNLOAD_URL]
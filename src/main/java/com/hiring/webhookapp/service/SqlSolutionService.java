package com.hiring.webhookapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SqlSolutionService {

    private static final Logger logger = LoggerFactory.getLogger(SqlSolutionService.class);

    public String getSqlSolution() {
        // Based on the problem shown in the images, we need to calculate the number of
        // employees who are younger than each employee in their respective departments

        // The SQL query for the problem:
        String sqlQuery = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, " +
                "COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT " +
                "FROM EMPLOYEE e1 " +
                "JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID " +
                "LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e1.DOB > e2.DOB " +
                "GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME " +
                "ORDER BY e1.EMP_ID DESC";

        logger.info("Generated SQL solution: {}", sqlQuery);
        return sqlQuery;
    }
}
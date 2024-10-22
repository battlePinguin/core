INSERT INTO department (name) VALUES ('HR');
INSERT INTO department (name) VALUES ('IT');
INSERT INTO department (name) VALUES ('Sales');
INSERT INTO department (name) VALUES ('Marketing');
INSERT INTO department (name) VALUES ('Finance');


INSERT INTO employee (first_name, last_name, position, salary, department_id)
SELECT
    'FirstName' || ROUND(RAND() * 1000),
    'LastName' || ROUND(RAND() * 1000),
    CASE
        WHEN MOD(ROUND(RAND() * 100), 4) = 0 THEN 'Manager'
        WHEN MOD(ROUND(RAND() * 100), 4) = 1 THEN 'Developer'
        WHEN MOD(ROUND(RAND() * 100), 4) = 2 THEN 'Salesperson'
        ELSE 'Analyst'
        END,
    ROUND(RAND() * 100000 + 30000, 2),
    FLOOR(RAND() * 5 + 1)
FROM SYSTEM_RANGE(1, 1000);

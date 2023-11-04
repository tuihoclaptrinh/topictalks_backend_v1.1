CREATE PROCEDURE DELETE_UNVERIFIED_USERS_PROCEDURE()
BEGIN
    DECLARE current_date_var DATETIME;
    DECLARE create_date_var DATETIME;

    SET current_date_var = NOW();

SELECT created_at INTO create_date_var
FROM user
WHERE is_verify = 0; -- Assuming there is a 'verified' column that indicates if the account is verified

IF DATE_ADD(create_date_var, INTERVAL 3 DAY) <= current_date_var THEN
DELETE FROM user WHERE is_verify = 0 AND created_at <= DATE_SUB(current_date_var, INTERVAL 3 DAY); -- Delete unverified accounts created 3 days ago or earlier
END IF;
END
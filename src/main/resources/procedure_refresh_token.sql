CREATE PROCEDURE DELETE_REFRESH_TOKEN_PROCEDURE()
BEGIN
    DECLARE current_date_var DATETIME;
    DECLARE create_date_var DATETIME;

    SET current_date_var = NOW();

SELECT created_at INTO create_date_var
FROM refresh_token;

IF DATE_ADD(create_date_var, INTERVAL 1 DAY) <= current_date_var THEN
DELETE FROM refresh_token WHERE created_at <= DATE_SUB(current_date_var, INTERVAL 1 DAY); -- Delete refresh token accounts created 1 day ago or earlier
END IF;
END
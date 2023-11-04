CREATE PROCEDURE UPDATE_IS_BAN_USER_PROCEDURE(IN username_value VARCHAR(255))
BEGIN
    DECLARE banned_date_var DATETIME(6);
    DECLARE num_date_ban_var INT;
    DECLARE current_date_var DATETIME(6);

SELECT banned_date, num_date_ban INTO banned_date_var, num_date_ban_var
FROM user
WHERE username = username_value;

SET current_date_var = NOW(6);

    IF DATE_ADD(banned_date_var, INTERVAL num_date_ban_var DAY) >= current_date_var THEN
UPDATE user SET is_banned = 1 WHERE username = username_value;
ELSE
UPDATE user SET is_banned = 0, num_date_ban = 0, banned_date = NULL WHERE username = username_value;
END IF;
END
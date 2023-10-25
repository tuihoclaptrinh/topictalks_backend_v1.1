CREATE TRIGGER update_is_banned
    BEFORE UPDATE ON user
    FOR EACH ROW
BEGIN
    IF NEW.is_banned = 0 THEN
        SET NEW.banned_date = null;
    END IF;
END
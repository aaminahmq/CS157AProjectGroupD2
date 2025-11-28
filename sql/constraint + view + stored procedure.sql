-- ======================================================
-- EXTRA CONSTRAINT + VIEW + STORED PROCEDURE
-- 1) EXTRA CONSTRAINTS
-- Donor: contact_info should look like an email
-- and (name, contact_info) must be unique
ALTER TABLE Donor
  ADD CONSTRAINT chk_donor_email
    CHECK (contact_info LIKE '%@%'),
  ADD CONSTRAINT uq_donor_name_email
    UNIQUE (name, contact_info);

-- Recipient: contact_info should also look like an email
ALTER TABLE Recipient
  ADD CONSTRAINT chk_recipient_email
    CHECK (contact_info LIKE '%@%');


-- 2) VIEW FOR REPORTING
-- Shows each ACTIVE recipient and total distribution value they received
CREATE OR REPLACE VIEW vw_active_recipient_summary AS
SELECT
    r.recipient_id,
    r.name,
    r.family_size,
    r.status,
    COALESCE(SUM(d.itemValue), 0) AS total_distributed_value
FROM Recipient r
LEFT JOIN Distribution d
    ON r.recipient_id = d.recipient_id
WHERE r.status = 'active'
GROUP BY
    r.recipient_id,
    r.name,
    r.family_size,
    r.status;


-- 3) STORED PROCEDURE
-- Safely insert a distribution ONLY if the recipient is ACTIVE.
-- If recipient does not exist or is paused, raise an error.
DELIMITER $$

CREATE PROCEDURE sp_add_active_distribution(
    IN p_recipient_id INT,
    IN p_itemValue DECIMAL(10,2),
    IN p_city VARCHAR(80)
)
BEGIN
    DECLARE v_status VARCHAR(10);

    -- Look up recipient status
    SELECT status INTO v_status
    FROM Recipient
    WHERE recipient_id = p_recipient_id;

    -- If no such recipient
    IF v_status IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Recipient does not exist';
    -- If not active
    ELSEIF v_status <> 'active' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Recipient is not active';
    -- OK: insert distribution
    ELSE
        INSERT INTO Distribution(recipient_id, itemValue, city_location)
        VALUES (p_recipient_id, p_itemValue, p_city);
    END IF;
END$$

DELIMITER ;

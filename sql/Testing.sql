/* ============================================================
   TESTING CONSTRAINTS, VIEW, AND STORED PROCEDURE

/* ------------------------------------------------------------
   TEST 1 — CHECK CONSTRAINT ON DONOR EMAIL
   Purpose:
      - Demonstrate that contact_info must contain '@'
      - This insert should FAIL with a CHECK constraint error
   What to say in video:
      “This checks our CHECK constraint on Donor. 
       The email is invalid so MySQL should reject it.”
------------------------------------------------------------- */
INSERT INTO Donor(name, contact_info, type)
VALUES ('Bad Donor', 'not-an-email', 'individual');



/* ------------------------------------------------------------
   TEST 2 — UNIQUE CONSTRAINT ON DONOR (name + contact_info)
   Purpose:
      - Demonstrate UNIQUE(name, contact_info)
      - Alice Chen already exists in seed data
      - This insert should FAIL with duplicate key error
   What to say in video:
      “This tests our UNIQUE constraint. 
       Duplicate name + email is not allowed, so this should fail.”
------------------------------------------------------------- */
INSERT INTO Donor(name, contact_info, type)
VALUES ('Alice Chen', 'alice.chen@example.com', 'individual');



/* ============================================================
   PART B — TESTING THE VIEW
   ============================================================ */

/* ------------------------------------------------------------
   TEST 3 — VIEW OUTPUT
   Purpose:
      - Show the view vw_active_recipient_summary
      - Displays all ACTIVE recipients and their total distribution value
   What to say in video:
      “This is our reporting view. 
       It summarizes total distribution value for each ACTIVE recipient.”
------------------------------------------------------------- */
SELECT * FROM vw_active_recipient_summary;



/* ------------------------------------------------------------
   INSERT ROW TO DEMONSTRATE VIEW AUTO-UPDATE
   Purpose:
      - Insert a new distribution for recipient 1
      - Show that the view updates its SUM() value automatically
   What to say in video:
      “Now I insert a new distribution for recipient 1. 
       After refreshing the view you can see the total value increase.”
------------------------------------------------------------- */
INSERT INTO Distribution(recipient_id, itemValue, city_location)
VALUES (1, 30.00, 'San Jose');



/* ------------------------------------------------------------
   REFRESH VIEW AFTER UPDATE
   Purpose:
      - Verify the view reflects the updated aggregated value
   What to say:
      “The view now shows the updated total distribution amount.”
------------------------------------------------------------- */
SELECT * FROM vw_active_recipient_summary;



/* ============================================================
   PART C — TESTING STORED PROCEDURE
   ============================================================ */

/* ------------------------------------------------------------
   TEST 4 — STORED PROCEDURE SUCCESS (recipient is ACTIVE)
   Purpose:
      - Demonstrate a successful stored procedure call
      - Recipient 1 is ACTIVE, so it should insert a row
   What to say:
      “Recipient 1 is active, so the stored procedure should insert successfully.”
------------------------------------------------------------- */
CALL sp_add_active_distribution(1, 50.00, 'San Jose');



/* ------------------------------------------------------------
   SHOW NEW DISTRIBUTION INSERTED BY STORED PROCEDURE
   Purpose:
      - Verify successful insertion by viewing last 3 records
   What to say:
      “Here we see the new row created by the stored procedure.”
------------------------------------------------------------- */
SELECT * FROM Distribution ORDER BY distribution_id DESC LIMIT 3;



/* ------------------------------------------------------------
   TEST 5 — STORED PROCEDURE FAILURE (recipient is PAUSED)
   Purpose:
      - Recipient 3 has status = 'paused' from seed data
      - Procedure should throw error “Recipient is not active”
   What to say:
      “Recipient 3 is paused, so the stored procedure should block the insert.”
------------------------------------------------------------- */
CALL sp_add_active_distribution(3, 50.00, 'San Jose');



/* ------------------------------------------------------------
   TEST 6 — STORED PROCEDURE FAILURE (recipient does NOT exist)
   Purpose:
      - No recipient with ID 99
      - Procedure should throw error “Recipient does not exist”
   What to say:
      “This tests the error handling for a missing recipient. 
       The procedure correctly rejects it.”
------------------------------------------------------------- */
CALL sp_add_active_distribution(99, 50.00, 'San Jose');


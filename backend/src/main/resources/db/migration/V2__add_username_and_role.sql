-- V2: Add username and role to the users table

-- Step 1: Add the 'role' column. We can give it a NOT NULL constraint
-- immediately by providing a default value for any existing rows.
ALTER TABLE users
ADD COLUMN role TEXT NOT NULL DEFAULT 'ROLE_USER';

-- Step 2: Add the 'username' column, allowing it to be null temporarily
-- so the command doesn't fail on tables with existing data.
ALTER TABLE users
ADD COLUMN username TEXT;

-- Step 3: Populate the new 'username' column for existing records.
-- A common and safe strategy is to use the email as the initial username.
UPDATE users SET username = email WHERE username IS NULL;

-- Step 4: Now that all rows have a value, add the NOT NULL constraint.
ALTER TABLE users
ALTER COLUMN username SET NOT NULL;

-- Step 5: Finally, add the UNIQUE constraint to enforce that all usernames are unique.
ALTER TABLE users
ADD CONSTRAINT users_username_unique UNIQUE (username);
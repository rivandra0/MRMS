-- DROP TABLE MATERIAL_REQUEST_ITEMS;
-- DROP TABLE MATERIAL_REQUESTS;
-- DROP TABLE users;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users (
    user_id VARCHAR(60) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    pwd VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL,
    name VARCHAR(60) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE material_requests (
    request_id SERIAL PRIMARY KEY,
    status VARCHAR(30) NOT NULL,
    submit_by VARCHAR(60) REFERENCES users(user_id) ON DELETE SET NULL,
    submit_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_by VARCHAR(60) REFERENCES users(user_id) ON DELETE SET NULL,
    approved_date TIMESTAMP,
    rejected_by VARCHAR(60) REFERENCES users(user_id) ON DELETE SET NULL,
    rejected_date TIMESTAMP
);

CREATE TABLE material_request_items (
    item_id SERIAL PRIMARY KEY,
    request_id INT REFERENCES material_requests(request_id) ON DELETE CASCADE,
    material_name VARCHAR(255) NOT NULL,
    requested_quantity INT NOT NULL,
    usage_description TEXT
);

-- Insert sample users into users table
INSERT INTO users (user_id, email, pwd, role, name)
VALUES
    ('prd1','production@company.com', crypt('production123',gen_salt('bf', 8)), 'WAREHOUSE_ADMIN', 'Warehouse Admin 1'),
    ('wh1', 'warehouse@company.com', crypt('warehouse123',gen_salt('bf', 8)), 'PRODUCTION_USER', 'Production User 1');

-- Insert sample material requests
INSERT INTO material_requests (status, submit_by, submit_date, approved_by, approved_date, rejected_by, rejected_date)
VALUES
    ('PENDING_APPROVAL', 'prd1', CURRENT_TIMESTAMP, NULL, NULL, NULL, NULL),  -- Request 1: Submitted by John, Pending Approval
    ('APPROVED', 'prd1', CURRENT_TIMESTAMP, 'wh1', CURRENT_TIMESTAMP, NULL, NULL),  -- Request 2: Submitted by Jane, Approved by John
    ('REJECTED', 'prd1', CURRENT_TIMESTAMP, NULL, NULL, 'wh1', CURRENT_TIMESTAMP);  -- Request 3: Submitted by Robert, Rejected by Jane

INSERT INTO material_request_items (request_id, material_name, requested_quantity, usage_description)
VALUES
    (1, 'Steel Plate', 10, 'For building frames'),  -- Item for Request 1
    (1, 'Steel Rod', 5, 'For foundation work'),      -- Item for Request 1
    (2, 'Lamp', 5, 'For lights'),      -- Item for Request 1
    (3, 'Nails', 100, 'For assembly work');        -- Item for Request 3


select * from users;
select * from material_requests;
select * from material_request_items;
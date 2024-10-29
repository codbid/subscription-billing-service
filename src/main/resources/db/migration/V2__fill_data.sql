INSERT INTO invoices(subscription_id, description, amount, status, created_date) VALUES (1, 'Test', 998, 'AWAITING_PAYMENT', '2024-10-27');

INSERT INTO payments(invoice_id, payment_method, status, created_at) VALUES (1, 'SBP', 'FAILED', '2024-10-27T23:59:59.425Z');

INSERT INTO subscriptions(owner_id, plan_id, status, start_date, end_date) VALUES (1, 1, 'ACTIVE', '2024-09-27', '2024-10-27');

INSERT INTO plans(name, description, price, billing_cycle, max_users) VALUES ('Test 1 for 1m', 'Test sub', 998, 'MONTHLY', 10);

INSERT INTO users(subscription_id, login, name, email, password) VALUES (1, 'admin', 'admin', 'admin@gmail.com', 'admin');
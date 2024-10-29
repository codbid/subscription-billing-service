INSERT INTO invoices(subscription_id, description, amount, status, created_date) VALUES (1, 'Test', 998, 'AWAITING_PAYMENT', '2024-10-27');

INSERT INTO payments(payment_id, invoice_id, payment_method, status, created_at, idempotence_key) VALUES ('2eb2ef08-000f-5000-b000-1e1b56d1529a', 1, 'YOO_MONEY', 'PENDING', '2024-10-27T23:59:59.425Z', 'e92aba65-9678-4389-a484-68bd3deddf57');

INSERT INTO subscriptions(owner_id, plan_id, status) VALUES (1, 1, 'INACTIVE');

INSERT INTO plans(name, description, price, billing_cycle, max_users) VALUES ('Test 1 for 1m', 'Test sub', 998, 'MONTHLY', 10);

INSERT INTO users(subscription_id, login, name, email, password) VALUES (1, 'admin', 'admin', 'admin@gmail.com', 'admin');
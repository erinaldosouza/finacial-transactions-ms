--Create customer, account and bind them
INSERT INTO customers (document_number, external_id) VALUES ('1234568910', '53574399-6671-46eb-8f08-ac15e4ec989a');
INSERT INTO accounts (number, external_id) VALUES('12345678910', '53574399-6671-46eb-8f08-ac15e4ec989b');
UPDATE customers SET account_id = (SELECT id FROM accounts WHERE external_id = '53574399-6671-46eb-8f08-ac15e4ec989b') WHERE external_id = '53574399-6671-46eb-8f08-ac15e4ec989a';

-- Creates a transaction
INSERT INTO public.transactions(
    amount, date_time, account_id, operation_type_id, external_id)
VALUES (100, now(), (SELECT id FROM accounts WHERE external_id = '53574399-6671-46eb-8f08-ac15e4ec989b'), 1, '53574399-6671-46eb-8f08-ac15e4ec989c');


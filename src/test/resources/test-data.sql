-- TODO temporarily added schema for unit tests.

DROP SCHEMA IF EXISTS usermanagement_unittest;
CREATE SCHEMA usermanagement_unittest;

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

CREATE TABLE usermanagement_unittest.email_link (
    user_id integer NOT NULL,
    uuid character varying(40) NOT NULL,
    created_on timestamp without time zone DEFAULT now(),
    is_active boolean DEFAULT true
);

CREATE TABLE usermanagement_unittest.roles (
    id integer NOT NULL,
    description character varying(20) NOT NULL
);


CREATE SEQUENCE usermanagement_unittest.roles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE usermanagement_unittest.users (
    id integer NOT NULL,
    email_address character varying(200) NOT NULL,
    role_id integer,
    name character varying(200) NOT NULL,
    password character varying(200),
    is_active boolean DEFAULT false,
    locked_timestamp timestamp without time zone,
    login_fail_count integer DEFAULT 0,
    user_uuid uuid NOT NULL,
    local_authority_short_code character varying(10) NOT NULL
);

CREATE SEQUENCE usermanagement_unittest.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY usermanagement_unittest.roles ALTER COLUMN id SET DEFAULT nextval('usermanagement_unittest.roles_id_seq'::regclass);

ALTER TABLE ONLY usermanagement_unittest.users ALTER COLUMN id SET DEFAULT nextval('usermanagement_unittest.users_id_seq'::regclass);

INSERT INTO  usermanagement_unittest.roles (id, description) values
(1,	'DfT Admin'),
(2,	'LA Admin'),
(3,	'LA User'),
(4,	'LA Read Only'),
(5,	'Read Only')
;

ALTER TABLE ONLY usermanagement_unittest.email_link
    ADD CONSTRAINT email_link_pkey PRIMARY KEY (uuid);

ALTER TABLE ONLY usermanagement_unittest.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);

ALTER TABLE ONLY usermanagement_unittest.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

CREATE INDEX email_link_user_id_ix ON usermanagement_unittest.email_link USING btree (user_id);

CREATE UNIQUE INDEX user_email_address_ux ON usermanagement_unittest.users USING btree (email_address);

CREATE INDEX users_la_code_ix ON usermanagement_unittest.users USING btree (local_authority_short_code);

CREATE UNIQUE INDEX users_uuid_ux ON usermanagement_unittest.users USING btree (user_uuid);

ALTER TABLE ONLY usermanagement_unittest.email_link
    ADD CONSTRAINT email_link_user_id_fk FOREIGN KEY (user_id) REFERENCES usermanagement_unittest.users(id) ON DELETE CASCADE;

ALTER TABLE ONLY usermanagement_unittest.users
    ADD CONSTRAINT users_role_id_fk FOREIGN KEY (role_id) REFERENCES usermanagement_unittest.roles(id);


SET search_path TO usermanagement_unittest;


DELETE FROM users WHERE email_address = 'createuservalid@dft.gov.uk';
DELETE FROM users WHERE id < 0;

INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-1, 'Sampath', 'abc@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'0093daf9-2782-47f8-93dc-bdf073204d6c');
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-2, 'Sampath', 'def@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'd327a64c-4876-4975-98f7-183447c9cca1');
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-3, 'nobody', 'abcnobody@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'af46bb29-6322-42c8-9c29-9fe80a0a38bf');
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-4, 'update test', 'updateme@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'c6187d21-93a5-4ceb-bfbe-9e9f9b872023');
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-5, 'delete test', 'deleteme@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'04b630ba-2809-45c7-8f5e-3a0f56788a3d');

INSERT INTO email_link (user_id, uuid, created_on, is_active)
VALUES (-1, '7d75652a-4e84-41e2-bd82-e5b5933b81da', '2018-07-10 09:37:07.119029', true);

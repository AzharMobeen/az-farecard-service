-- Create Tables
DROP TABLE if exists card CASCADE;
DROP TABLE if exists card_usage CASCADE;


CREATE TABLE card (card_id bigint not null, balance numeric(19,2), primary key (card_id));
CREATE TABLE card_usage (usage_id bigint not null, created_date_time timestamp, fare numeric(19,2), station bigint, modified_date_time timestamp, swipe integer, type integer, card_id bigint, primary key (usage_id));


-- Alter table for required constraints
ALTER TABLE card_usage add constraint FKforCardUsages foreign key (card_id) references card;

-- Sequence
DROP SEQUENCE if exists hibernate_sequence;
CREATE SEQUENCE hibernate_sequence start with 5 increment by 1;
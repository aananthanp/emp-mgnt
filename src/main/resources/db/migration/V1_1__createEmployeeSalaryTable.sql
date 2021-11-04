--transaction sequence id
create sequence public.transaction_id_sequence start with 1 increment by 1;

--Create employee salary
create table if not exists public.employee_salary
(
transaction_id Integer default nextval('public.transaction_id_sequence') not null constraint trans_id_sequence_trans_id_pk primary key,
name varchar(100) not null  not null constraint name_uk unique,
salary numeric(20,10) not null,
created_by varchar(20) not null,
created_time timestamp not null,
updated_by varchar(20),
updated_time timestamp
);
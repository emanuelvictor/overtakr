create table product_aud (id uuid not null, rev bigint not null, revtype smallint, name varchar(255), name_mod boolean, quantity_available integer, quantity_available_mod boolean, primary key (rev, id));
create table revision (id bigint not null, timestamp bigint not null, username varchar(255), primary key (id));
create sequence revision_seq start with 1 increment by 50;
alter table if exists product_aud add constraint fk_product_aud_revision foreign key (rev) references revision;
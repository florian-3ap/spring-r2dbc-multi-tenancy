CREATE TABLE tenant_member
(
    id         bigserial                NOT NULL PRIMARY KEY,
    first_name text,
    last_name  text,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL
);

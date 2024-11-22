INSERT INTO roles (name)
VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO users (username, password, email)
VALUES ('user', '$2a$12$HMERlUcLl6FVYFA8Z7PyFuYIN07LdHsaE0bXhRvcE6dO710TBzOZC', 'user@gmail.com'),
       ('admin', '$2a$12$HMERlUcLl6FVYFA8Z7PyFuYIN07LdHsaE0bXhRvcE6dO710TBzOZC', 'admin@gmail.com');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1), (2, 2);
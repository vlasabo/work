CREATE TABLE IF NOT EXISTS USERS
(
    chat_id               BIGINT PRIMARY KEY,
    first_name            VARCHAR(100),
    last_name             VARCHAR(100),
    user_name             VARCHAR(100),
    registered_at         TIMESTAMP,
    registration_attempts INTEGER,
    separated_shedule     BOOLEAN
);
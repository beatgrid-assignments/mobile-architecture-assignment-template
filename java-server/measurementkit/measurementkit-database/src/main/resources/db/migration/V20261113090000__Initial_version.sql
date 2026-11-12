-- Initial schema. Compatible with MySQL 8 (production) and H2 in MySQL mode (local development / tests).
-- Never modify a migration once it has been applied; add a new V<yyyyMMddHHmmss>__<description>.sql instead.

CREATE TABLE users
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    version       BIGINT      NOT NULL,
    created_at    DATETIME(6) NOT NULL,
    updated_at    DATETIME(6) NOT NULL,
    phone_number  VARCHAR(20) NOT NULL,
    country_code  VARCHAR(2),
    status        VARCHAR(16) NOT NULL,
    last_login_at DATETIME(6),
    CONSTRAINT users_phone_number_uk UNIQUE (phone_number)
);

CREATE TABLE devices
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    version         BIGINT      NOT NULL,
    created_at      DATETIME(6) NOT NULL,
    updated_at      DATETIME(6) NOT NULL,
    installation_id VARCHAR(64) NOT NULL,
    platform        VARCHAR(16) NOT NULL,
    app_version     VARCHAR(32),
    os_version      VARCHAR(32),
    user_id         BIGINT,
    last_seen_at    DATETIME(6),
    CONSTRAINT devices_installation_id_uk UNIQUE (installation_id),
    CONSTRAINT devices_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE user_sessions
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    version      BIGINT      NOT NULL,
    created_at   DATETIME(6) NOT NULL,
    updated_at   DATETIME(6) NOT NULL,
    user_id      BIGINT      NOT NULL,
    device_id    BIGINT,
    last_used_at DATETIME(6),
    expires_at   DATETIME(6) NOT NULL,
    revoked_at   DATETIME(6),
    CONSTRAINT user_sessions_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT user_sessions_device_id_fk FOREIGN KEY (device_id) REFERENCES devices (id)
);

CREATE INDEX user_sessions_user_id_idx ON user_sessions (user_id);

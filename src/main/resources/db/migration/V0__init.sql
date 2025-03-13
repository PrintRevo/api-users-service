CREATE TABLE system_users
(
    id                       VARCHAR(36)  NOT NULL,
    phone_number             VARCHAR(255),
    phone_number_verified    BOOLEAN DEFAULT FALSE,
    phone_number_verified_at TIMESTAMP with time zone,
    email                    VARCHAR(255),
    email_verified           BOOLEAN DEFAULT FALSE,
    email_verified_at        TIMESTAMP with time zone,
    first_name               VARCHAR(255),
    middle_name              VARCHAR(255),
    last_name                VARCHAR(255),
    country                  VARCHAR(255),
    user_status              VARCHAR(255) NOT NULL,
    is_active                BOOLEAN DEFAULT TRUE,
    created_at               TIMESTAMP with time zone,
    created_by               VARCHAR(36),
    updated_at               TIMESTAMP with time zone,
    updated_by               VARCHAR(36),
    auth_server_id           VARCHAR(36),
    pin_status               VARCHAR(255),
    CONSTRAINT pk_system_users PRIMARY KEY (id)
);


CREATE TABLE verification_requests
(
    id                 VARCHAR(36) NOT NULL,
    is_active          BOOLEAN DEFAULT TRUE,
    created_at         TIMESTAMP with time zone,
    created_by         VARCHAR(36),
    updated_at         TIMESTAMP with time zone,
    updated_by         VARCHAR(36),
    verification_type  VARCHAR(255),
    originating_source VARCHAR(255),
    identity_value     VARCHAR(255),
    correlation_id     VARCHAR(255),
    verified           BOOLEAN,
    verified_at        TIMESTAMP with time zone,
    expires_at         TIMESTAMP with time zone,
    user_id            VARCHAR(36),
    details            TEXT,
    CONSTRAINT pk_verification_requests PRIMARY KEY (id)
);
ALTER TABLE verification_requests
    ADD CONSTRAINT FK_VERIFICATION_REQUESTS_ON_USER FOREIGN KEY (user_id) REFERENCES system_users (id);


CREATE TABLE phone_number_verification_requests
(
    id           VARCHAR(36) NOT NULL,
    is_active    BOOLEAN DEFAULT TRUE,
    created_at   TIMESTAMP with time zone,
    created_by   VARCHAR(36),
    updated_at   TIMESTAMP with time zone,
    updated_by   VARCHAR(36),
    phone_number VARCHAR(255),
    time_to_live INTEGER,
    otp          VARCHAR(255),
    is_verified  BOOLEAN,
    sent_date    date,
    CONSTRAINT pk_phone_number_verification_requests PRIMARY KEY (id)
);

CREATE TABLE products
(
    id           VARCHAR(36) NOT NULL,
    is_active    BOOLEAN DEFAULT TRUE,
    created_at   TIMESTAMP with time zone,
    created_by   VARCHAR(255),
    updated_at   TIMESTAMP with time zone,
    updated_by   VARCHAR(255),
    product_name VARCHAR(255),
    description  VARCHAR(255),
    auth_group   VARCHAR(255),
    base_url     VARCHAR(255),
    is_public    BOOLEAN,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE TABLE email_verification_requests
(
    id               VARCHAR(36) NOT NULL,
    is_active        BOOLEAN DEFAULT TRUE,
    created_at       TIMESTAMP with time zone,
    created_by       VARCHAR(36),
    updated_at       TIMESTAMP with time zone,
    updated_by       VARCHAR(36),
    email_address    VARCHAR(255),
    time_to_live     INTEGER,
    token_service_id VARCHAR(255),
    user_id          VARCHAR(36),
    is_verified      BOOLEAN,
    sent_date        date,
    CONSTRAINT pk_email_verification_requests PRIMARY KEY (id)
);

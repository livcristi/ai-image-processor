-- Create table for TagData
CREATE TABLE tag_data
(
    tag_id  BINARY(16)   NOT NULL PRIMARY KEY,
    `key`   VARCHAR(255) NOT NULL,
    `value` VARCHAR(255) NOT NULL
);

-- Create table for Interaction
CREATE TABLE interaction
(
    interaction_id BINARY(16)                                        NOT NULL PRIMARY KEY,
    user_id        VARCHAR(255)                                      NOT NULL,
    container      VARCHAR(255),
    status         ENUM ('CREATED', 'COMPLETED', 'DELETED', 'ERROR') NOT NULL,
    operation_type ENUM ('OCR', 'BGR')                               NOT NULL,
    created_time   BIGINT                                            NOT NULL,
    updated_time   BIGINT                                            NOT NULL
);

-- Create table for Interaction tags (many-to-many relationship)
CREATE TABLE interaction_tags
(
    interaction_id BINARY(16) NOT NULL,
    tag_id         BINARY(16) NOT NULL,
    PRIMARY KEY (interaction_id, tag_id),
    CONSTRAINT fk_interaction FOREIGN KEY (interaction_id) REFERENCES interaction (interaction_id) ON DELETE CASCADE,
    CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tag_data (tag_id) ON DELETE CASCADE
);

-- Create table for ObjectInfo
CREATE TABLE object_info
(
    object_id    BINARY(16)                                        NOT NULL PRIMARY KEY,
    user_id      VARCHAR(255)                                      NOT NULL,
    name         VARCHAR(255)                                      NOT NULL,
    container    VARCHAR(255)                                      NOT NULL,
    status       ENUM ('CREATED', 'COMPLETED', 'DELETED', 'ERROR') NOT NULL,
    type         VARCHAR(50)                                       NOT NULL,
    created_time BIGINT                                            NOT NULL,
    updated_time BIGINT                                            NOT NULL
);

-- Create table for Object tags (many-to-many relationship)
CREATE TABLE object_tags
(
    object_id BINARY(16) NOT NULL,
    tag_id    BINARY(16) NOT NULL,
    PRIMARY KEY (object_id, tag_id),
    CONSTRAINT fk_object FOREIGN KEY (object_id) REFERENCES object_info (object_id) ON DELETE CASCADE,
    CONSTRAINT fk_tag_object FOREIGN KEY (tag_id) REFERENCES tag_data (tag_id) ON DELETE CASCADE
);

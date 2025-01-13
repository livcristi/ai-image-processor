-- Create table for TagData
CREATE TABLE tag_data
(
    tag_id    BINARY(16)   NOT NULL PRIMARY KEY,
    tag_key   VARCHAR(255) NOT NULL,
    tag_value VARCHAR(255) NOT NULL
);

-- Create table for Interaction
CREATE TABLE interaction
(
    interaction_id BINARY(16)   NOT NULL,
    user_id        VARCHAR(255) NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    operation_type VARCHAR(20)  NOT NULL,
    created_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (interaction_id)
);

-- Create table for Interaction tags (many-to-many relationship)
CREATE TABLE interaction_tags
(
    interaction_id BINARY(16) NOT NULL,
    tag_id         BINARY(16) NOT NULL,
    PRIMARY KEY (interaction_id, tag_id),
    CONSTRAINT fk_interaction_tag FOREIGN KEY (interaction_id) REFERENCES interaction (interaction_id) ON DELETE CASCADE,
    CONSTRAINT fk_tag_interaction FOREIGN KEY (tag_id) REFERENCES tag_data (tag_id) ON DELETE CASCADE
);

-- Create table for ObjectInfo
CREATE TABLE object_info
(
    object_id      BINARY(16)   NOT NULL,
    user_id        VARCHAR(255) NOT NULL,
    name           VARCHAR(255) NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    type           VARCHAR(20)  NOT NULL,
    interaction_id BINARY(16)   NOT NULL,
    created_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (object_id),
    CONSTRAINT fk_interaction FOREIGN KEY (interaction_id) REFERENCES interaction (interaction_id) ON DELETE CASCADE
);

-- Create table for Object tags (many-to-many relationship)
CREATE TABLE object_tags
(
    object_id BINARY(16) NOT NULL,
    tag_id    BINARY(16) NOT NULL,
    PRIMARY KEY (object_id, tag_id),
    CONSTRAINT fk_object_tag FOREIGN KEY (object_id) REFERENCES object_info (object_id) ON DELETE CASCADE,
    CONSTRAINT fk_tag_object FOREIGN KEY (tag_id) REFERENCES tag_data (tag_id) ON DELETE CASCADE
);

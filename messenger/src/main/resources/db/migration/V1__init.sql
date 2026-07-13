CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(30) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE chats(
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    name VARCHAR(255),
    avatar_url VARCHAR(500),
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_chat_creator
        FOREIGN KEY (created_by)
            REFERENCES users(id)
);

CREATE TABLE chat_participants(
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    joined_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_participant_chat
        FOREIGN KEY (chat_id)
            REFERENCES chats(id),

    CONSTRAINT fk_participant_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
);

CREATE TABLE messages(
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT,
    message_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    edited_at TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    client_message_id UUID,

    CONSTRAINT fk_message_chat
        FOREIGN KEY (chat_id)
            REFERENCES chats(id),

    CONSTRAINT fk_message_sender
        FOREIGN KEY (sender_id)
            REFERENCES users(id)
);

CREATE TABLE attachments(
    id BIGSERIAL PRIMARY KEY,
    message_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT,

    CONSTRAINT fk_attachment_message
        FOREIGN KEY (message_id)
            REFERENCES messages(id)
);

CREATE INDEX idx_message_chat
    ON messages(chat_id);

CREATE INDEX idx_message_sender
    ON messages(sender_id);

CREATE INDEX idx_participant_chat
    ON chat_participants(chat_id);

CREATE INDEX idx_participant_user
    ON chat_participants(user_id);

CREATE INDEX idx_message_content
    ON messages(content);
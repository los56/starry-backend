CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR NOT NULL,
    nickname VARCHAR(64) UNIQUE NOT NULL,
    email VARCHAR UNIQUE NOT NULL,
    user_role VARCHAR NOT NULL,
    create_date TIMESTAMP NOT NULL,
    password_change_date TIMESTAMP NOT NULL,
    profile_image_url VARCHAR
);

CREATE TABLE channels (
    id uuid PRIMARY KEY NOT NULL,
    description VARCHAR,
    verified BOOLEAN NOT NULL,
    banner_url VARCHAR,
    stream_key VARCHAR NOT NULL,
    stream_title VARCHAR,
    stream_category VARCHAR,
    last_open_time TIMESTAMP,
    last_close_time TIMESTAMP,
    last_stream_id VARCHAR,
    FOREIGN KEY(id) REFERENCES "users"(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE community_posts (
    index SERIAL PRIMARY KEY,
    writer uuid NOT NULL,
    title VARCHAR NOT NULL,
    content VARCHAR NOT NULL,
    write_date TIMESTAMP NOT NULL,
    modify_date TIMESTAMP,
    FOREIGN KEY(writer) REFERENCES "users"(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE community_replies (
    index SERIAL PRIMARY KEY,
    writer uuid,
    post INTEGER NOT NULL,
    content VARCHAR NOT NULL,
    write_date TIMESTAMP NOT NULL,
    FOREIGN KEY(writer) REFERENCES "users"(id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY(post) REFERENCES "community_posts"(index) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE follows (
    index SERIAL PRIMARY KEY,
    from_user uuid NOT NULL,
    to_user uuid NOT NULL,
    follow_date TIMESTAMP NOT NULL,
    FOREIGN KEY(from_user) REFERENCES "users"(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(to_user) REFERENCES "users"(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE static_files (
    index SERIAL PRIMARY KEY,
    uploader uuid,
    file_name VARCHAR NOT NULL,
    original_file_name VARCHAR NOT NULL,
    upload_date TIMESTAMP NOT NULL,
    uploader_ip VARCHAR NOT NULL,
    FOREIGN KEY(uploader) REFERENCES "users"(id) ON DELETE SET NULL ON UPDATE CASCADE
);
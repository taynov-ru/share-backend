CREATE TABLE IF NOT EXISTS publications
(
    id UUID PRIMARY KEY,
    publish_date TIMESTAMP WITH TIME ZONE,
    expiration_date TIMESTAMP WITH TIME ZONE,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS files
(
    file_uuid UUID PRIMARY KEY,
    file_name VARCHAR(255),
    size BIGINT,
    publication_id UUID REFERENCES publications(id)
);

CREATE TABLE IF NOT EXISTS file_details
(
    file_id UUID PRIMARY KEY REFERENCES files(file_uuid),
    downloads_limit INTEGER DEFAULT 0,
    downloads_count INTEGER DEFAULT 0,
    expiration_time BIGINT NOT NULL,
    password VARCHAR(255),
    link VARCHAR(255),
    deleted BOOLEAN DEFAULT FALSE
);
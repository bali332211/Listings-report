CREATE TABLE locations (
  id                         VARCHAR(255) PRIMARY KEY,
  manager_name               VARCHAR(255) NOT NULL,
  phone                      VARCHAR(255) NOT NULL,
  address_primary            VARCHAR(255) NOT NULL,
  address_secondary          VARCHAR(255) NOT NULL,
  country                    VARCHAR(255) NOT NULL,
  town                       VARCHAR(255) NOT NULL,
  postal_code                VARCHAR(255) NOT NULL
);

CREATE TABLE listing_statuses (
  id                         BIGINT PRIMARY KEY,
  status_name                VARCHAR(255)
);

CREATE TABLE marketplaces (
  id                         BIGINT PRIMARY KEY,
  marketplace_name           VARCHAR(255)
);

CREATE TABLE listings (
  id                         VARCHAR(255) PRIMARY KEY,
  title                      VARCHAR(255),
  description                VARCHAR(255),
  listing_price              BIGINT,
  currency                   VARCHAR(255),
  quantity                   BIGINT,
  upload_time                DATE,
  owner_email_address        VARCHAR(255),
  location_id VARCHAR(255),
  FOREIGN KEY (location_id) REFERENCES locations (id),
  listing_status             BIGINT,
  FOREIGN KEY (listing_status) REFERENCES listing_statuses (id),
  marketplace                BIGINT,
  FOREIGN KEY (marketplace) REFERENCES marketplaces (id)
);

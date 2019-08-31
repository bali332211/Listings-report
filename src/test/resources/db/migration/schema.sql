DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS listing_statuses;
DROP TABLE IF EXISTS marketplaces;
DROP TABLE IF EXISTS listings;

CREATE TABLE locations (
  id                         VARCHAR(255) PRIMARY KEY,
  manager_name               VARCHAR(255),
  phone                      VARCHAR(255),
  address_primary            VARCHAR(255),
  address_secondary          VARCHAR(255),
  country                    VARCHAR(255),
  town                       VARCHAR(255),
  postal_code                VARCHAR(255)
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
  title                      VARCHAR(255) NOT NULL,
  description                VARCHAR(255) NOT NULL,
  listing_price              BIGINT NOT NULL,
  currency                   VARCHAR(255) NOT NULL,
  quantity                   BIGINT NOT NULL,
  upload_time                DATE,
  owner_email_address        VARCHAR(255) NOT NULL,
  location_id                VARCHAR(255) NOT NULL,
  FOREIGN KEY (location_id) REFERENCES locations (id),
  listing_status             BIGINT,
  FOREIGN KEY (listing_status) REFERENCES listing_statuses (id),
  marketplace                BIGINT,
  FOREIGN KEY (marketplace) REFERENCES marketplaces (id)
);

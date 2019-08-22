CREATE TABLE listings (
  id                         VARCHAR(255) PRIMARY KEY,
  title                      VARCHAR(255),
  description                timestamp,
  FOREIGN KEY (inventory_item_location_id) REFERENCES locations (id),
  listing_price              BIGINT,
  currency                   VARCHAR(255),
  quantity                   BIGINT,
  FOREIGN KEY (listing_status) REFERENCES listing_statuses (id),
  FOREIGN KEY (marketplace) REFERENCES marketplaces (id),
  upload_time                timestamp,
  owner_email_address        VARCHAR(255)
);

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
  id                         SERIAL PRIMARY KEY,
  status_name                VARCHAR(255)
);

CREATE TABLE marketplaces (
  id                         SERIAL PRIMARY KEY,
  marketplace_name           VARCHAR(255)
);

CREATE TABLE listings (
  id                         SERIAL PRIMARY KEY,
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
  id                         SERIAL PRIMARY KEY,
  manager_name               VARCHAR(255),
  phone                      VARCHAR(255),
  address_primary            VARCHAR(255),
  address_secondary          VARCHAR(255),
  country                    VARCHAR(255),
  town                       VARCHAR(255),
  postal_code                VARCHAR(255)
);

--id UUID Location identifier
--● manager_name string Manager name
--● phone string Phone number
--● address_primary string Primary address
--● address_secondary string Secondary address
--● country string Country
--● town string Town
--● postal_code string Postal code

INSERT INTO notes
(content, timestamp, longest_palindrome_size) VALUES
('abrakadabra', '2018-10-09 00:12:12+0100', 3);

--● id UUID Listing identifier
--● title                         text Listing title
--● description                   text Listing description
--● inventory_item_location_id    UUID Location id
--● listing_price                 number Listing price
--● currency                      text Currency code
--● quantity                      number Item quantity
--● listing_status                number Listing status id
--● marketplace                   number Marketplace id
--● upload_time                   date Upload time
--● owner_email_address           text Owner email address
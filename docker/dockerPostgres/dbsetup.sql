
GRANT ALL PRIVILEGES ON DATABASE :db TO :user;

-- Creation of tables
CREATE TABLE customer
(
  cust_no integer NOT NULL,
  cust_name character varying(30),
  street character varying(30),
  city character varying(30),
  state character varying(30),
  zipcode integer,
  phone_no bigint,
  email character varying(40),
  birth_date date,
  ssn character varying(50),
  CONSTRAINT customer_pkey PRIMARY KEY (cust_no)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE customer OWNER TO :user;

CREATE TABLE public.cust_credentials
(
  cust_username character varying(30) NOT NULL,
  password character varying(30),
  cust_no integer,
  CONSTRAINT cust_credentials_pkey PRIMARY KEY (cust_username),
  CONSTRAINT "Cust_no_Fkey" FOREIGN KEY (cust_no)
      REFERENCES public.customer (cust_no) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.cust_credentials OWNER TO :user;

CREATE INDEX "fki_Cust_no_Fkey" ON public.cust_credentials USING btree(cust_no);

CREATE TABLE public.account
(
  account_no integer NOT NULL,
  cust_no integer,
  account_balance numeric(10,2),
  CONSTRAINT account_pkey PRIMARY KEY (account_no),
  CONSTRAINT account_cust_no_fkey FOREIGN KEY (cust_no)
      REFERENCES public.customer (cust_no) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

ALTER TABLE public.account OWNER TO :user;

CREATE SEQUENCE public.seq_transferid
  INCREMENT 1
  MINVALUE 10
  MAXVALUE 9223372036854775807
  START 76
  CACHE 1;
ALTER TABLE public.seq_transferid OWNER TO :user;

CREATE TABLE public.transfer_funds
(
  transferid bigint NOT NULL DEFAULT nextval('seq_transferid'::regclass),
  from_account_no integer,
  from_cust_no integer,
  to_account_no integer,
  to_cust_no integer,
  transfer_amount numeric(10,2),
  from_beforeamount numeric(10,2),
  from_afteramount numeric(10,2),
  to_beforeamount numeric(10,2),
  to_afteramount numeric(10,2),
  transfer_details character varying(30),
  transfer_date timestamp with time zone DEFAULT now(),
  CONSTRAINT "TransferID_PK" PRIMARY KEY (transferid),
  CONSTRAINT "from_cust_no_FK" FOREIGN KEY (from_cust_no)
      REFERENCES public.customer (cust_no) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT to_cust_no FOREIGN KEY (to_cust_no)
      REFERENCES public.customer (cust_no) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT transfer_funds_from_account_no_fkey FOREIGN KEY (from_account_no)
      REFERENCES public.account (account_no) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT transfer_funds_to_account_no_fkey FOREIGN KEY (to_account_no)
      REFERENCES public.account (account_no) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.transfer_funds OWNER TO :user;

-- Inserting queries into respective tables
-- While adding new users, all values here are arbitary except the following:
  -- Zip code should be 5 numeric values
  -- Phone number should be atleast 10 numeric values
  -- SSN should be Base64 encoded, for convinience we have a list of SSN's dockerPostgres/addOns/SSNEncodedList.txt

INSERT INTO customer VALUES (1, 'Cigital1', 'abc', 'Bloomington', 'Indiana', 47403, 9876545678, 'cigital1@cigital.com', '1980-01-10', 'WFtaWFta');
INSERT INTO customer VALUES (2, 'Cigital2', 'XYZ', 'Bloomington', 'Indiana', 47403, 9876545645, 'cigital2@cigital.com', '1990-01-10', 'WVpbWVpb');
INSERT INTO customer VALUES (3, 'Cigital3', 'E 5th street', 'Bloomington', 'Indiana', 47408, 5432167819, 'cigital3@cigital.com', '2001-01-03', 'U1BRU1BR');
INSERT INTO customer VALUES (4, 'Cigital4', 'Manhattan', 'New York City', 'New York', 12345, 1234567892, 'cigital4@gmail.com', '1974-10-5', 'WFNRUlBQ');
INSERT INTO customer VALUES (5, 'Cigital5', 'SF Road', 'San Francisco', 'CA', 11111, 1234544256, 'cigital5@yahoo.com', '1994-10-15', 'UlFQUlFQ');
INSERT INTO customer VALUES (6, 'Cigital6', 'King county', 'Seattle', 'New York', 13235, 1234523246, 'cigital6@in.com', '1976-1-12', 'VlRWVlpU');
INSERT INTO customer VALUES (7, 'Cigital7', 'Gainesville Rd', 'Gainesville', 'Florida', 56556, 2443451234, 'cigital7@gmail.com', '1924-04-05', 'WVpbWVpb');
INSERT INTO customer VALUES (8, 'Cigital8', 'Austin Road', 'Austin', 'Texas', 24335, 1345689860, 'cigital8@hotmail.com', '1984-03-07', 'UFBQVVdV');
INSERT INTO customer VALUES (9, 'Test', 'No where', 'Imaginary City', 'Funky State', 91332, 6666666666, 'test@iu.edu', '2016-03-08', 'UFFRUFFU');
INSERT INTO customer VALUES (10, 'Cigital Class', 'Manhattan', 'New York City', 'New York', 14655, 1234567891, 'cigital@gmail.com', '1994-10-15', 'UFNSUFNS');


INSERT INTO cust_credentials VALUES ('cigital1', 'cigital1', 1);
INSERT INTO cust_credentials VALUES ('cigital2', 'cigital2', 2);
INSERT INTO cust_credentials VALUES ('cigital3', 'cigital3', 3);
INSERT INTO cust_credentials VALUES ('cigital4', 'cigital4', 4);
INSERT INTO cust_credentials VALUES ('cigital5', 'cigital5', 5);
INSERT INTO cust_credentials VALUES ('cigital6', 'cigital6', 6);
INSERT INTO cust_credentials VALUES ('cigital7', 'cigital7', 7);
INSERT INTO cust_credentials VALUES ('cigital8', 'cigital8', 8);
INSERT INTO cust_credentials VALUES ('testUser', '12345', 9);
INSERT INTO cust_credentials VALUES ('cigital', 'cigital', 10);

INSERT INTO account VALUES (2001, 1, 1080.37);
INSERT INTO account VALUES (2002, 2, 1700.96);
INSERT INTO account VALUES (2003, 3, 1845.50);
INSERT INTO account VALUES (2004, 4, 2975.00);
INSERT INTO account VALUES (2005, 5, 2565.32);
INSERT INTO account VALUES (2006, 6, 1656.95);
INSERT INTO account VALUES (2007, 7, 7654.19);
INSERT INTO account VALUES (2008, 8, 2434.10);
INSERT INTO account VALUES (2009, 9, 9876.56);
INSERT INTO account VALUES (2010, 10, 8763.22);


INSERT INTO transfer_funds VALUES (4, 2009, 9, 2010, 10, 100.00, 9976.56, 9876.56, 8663.22, 8763.22, 'Bill clearance', '2016-03-09 10:42:06.325524-04');
INSERT INTO transfer_funds VALUES (5, 2001, 1, 2002, 2, -4.23, 1076.14, 1080.37, 1705.19, 1700.96, 'Thank you so much!', '2016-03-09 10:42:06.325524-04');
INSERT INTO transfer_funds VALUES (6, 2003, 3, 2005, 5, 5.00, 1850.50, 1845.50, 2535.32, 2540.32, 'test', '2016-03-14 22:34:46.902308-04');
INSERT INTO transfer_funds VALUES (7, 2004, 4, 2005, 5, 25.00, 3000.00, 2975.00, 2540.32, 2565.32, 'test', '2016-03-14 23:36:06.933278-04');
INSERT INTO transfer_funds VALUES (8, 2006, 6, 2007, 7, 1.00, 1657.95, 1656.95, 7653.19, 7654.19, 'tip', '2016-03-14 23:37:08.933278-04');
INSERT INTO transfer_funds VALUES (9, 2008, 8, 2009, 9, 10.00, 2444.10, 2434.10, 9866.56, 9876.56, 'thanks', '2016-03-14 23:38:08.933278-04');


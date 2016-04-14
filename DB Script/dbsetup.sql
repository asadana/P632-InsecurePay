create database insecurepaydatabase;
create user insecurepay;
GRANT ALL PRIVILEGES ON DATABASE insecurepaydatabase TO insecurepay;


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
ALTER TABLE customer OWNER TO insecurepay;

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
ALTER TABLE public.cust_credentials OWNER TO insecurepay;

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

ALTER TABLE public.account OWNER TO insecurepay;

CREATE SEQUENCE public.seq_transferid
  INCREMENT 1
  MINVALUE 10
  MAXVALUE 9223372036854775807
  START 76
  CACHE 1;
ALTER TABLE public.seq_transferid OWNER TO insecurepay;

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
ALTER TABLE public.transfer_funds OWNER TO insecurepay;

INSERT INTO customer VALUES (1, 'Cigital1', 'abc', 'Bloomington', 'Indiana', 47403, 9876545678, 'cigital1@cigital.com', '1980-01-10', 'WFtaWFta');
INSERT INTO customer VALUES (2, 'Cigital2', 'XYZ', 'Bloomington', 'Indiana', 47403, 9876545645, 'cigital2@cigital.com', '1980-01-10', 'WVpbWVpb');
INSERT INTO customer VALUES (3, 'Cigital3', 'E 5th street', 'Bloomington', 'Indiana', 47408, 54321679, 'cigital3@cigital.com', '2001-01-03', 'U1BRU1BR');
INSERT INTO customer VALUES (4, 'Test', 'No where', 'Imaginary City', 'Funky State', 9133, 6666666666, 'test@iu.edu', '2016-03-08', 'UFFRUFFU');
INSERT INTO customer VALUES (5, 'Foo Class', 'Manhattan', 'New York City', 'New York', 1234, 123456, 'foofan@gmail.com', '1994-10-15', 'UFBQVVdV');

INSERT INTO cust_credentials VALUES ('cigital1', 'cigital1', 1);
INSERT INTO cust_credentials VALUES ('cigital2', 'cigital2', 2);
INSERT INTO cust_credentials VALUES ('cigital3', 'cigital3', 3);
INSERT INTO cust_credentials VALUES ('testUser', '12345', 4);
INSERT INTO cust_credentials VALUES ('foo', 'abcde', 5);

INSERT INTO account VALUES (2001, 1, 2000.50);
INSERT INTO account VALUES (2002, 2, 1700.96);
INSERT INTO account VALUES (2003, 3, 3400.03);
INSERT INTO account VALUES (2004, 4, 450.89);
INSERT INTO account VALUES (2005, 5, 1234.59);



  

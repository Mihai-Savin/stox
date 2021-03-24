# stox
Stocks Monitoring Webapp

install postgresql and create a database named stox, no schema, no tables, just the db

after running the app for the first time localhost:8080/start

change the hibernate.hbm2ddl.auto property in hibernate.cfg.xml to update instead of create to keep the db

add this record into the appconfig table 

INSERT INTO public.appconfig (
id, apikey, pollinginterval, name, startengine, sendemails) VALUES (
'1'::bigint, 'demo'::character varying, '60'::integer, 'default'::character varying, true::boolean, false::boolean)
returning id;

maybe there will be some issues with the postgresql settings:
in case check https://stackoverflow.com/a/64336274

default user is mihai with an empty string password
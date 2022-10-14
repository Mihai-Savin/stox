# stox
Stocks Monitoring Webapp

install postgresql and create a database named stox, no schema, no tables, just the db

after running the app for the first time localhost:8080/start

change the hibernate.hbm2ddl.auto property in hibernate.cfg.xml to update instead of create to keep the db
and/ or comment out the
javax.persistence.schema-generation.database.action property 


then add this record into the appconfig table 

INSERT INTO public.appconfig (
id, apikey, pollinginterval, name, startengine, sendemails) VALUES (
'1'::bigint, 'demo'::character varying, '60'::integer, 'default'::character varying, true::boolean, false::boolean)
returning id;
commit;


maybe there will be some issues with the postgresql settings:
in case check https://stackoverflow.com/a/64336274

default user is mihai with an empty string password

set the apikey in the StockDAO instead of demo
To obtain a key go to https://www.alphavantage.co/support/#api-key

Due to the fact that the BATCH_STOCK_QUTES function was removed from the api,
the app will only allow setting an alarm for a predefined shortlist of symbols( for the time being MICROSOFT only)
begin

for rec in (select * from user_tables where logging='NO')
loop

begin

execute immediate 'ALTER TABLE '||rec.table_name||' LOGGING';

exception
when others then
NULL;

end;

end loop;

end;

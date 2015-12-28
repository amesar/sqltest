# sqltest

Data-driven SQL testing framework.

## Sample 

### Create table - [create.ddl](src/test/resources/create.ddl)
```
create table if not exists city (
  id int,
  name text
) 
```

### Insert rows - [insert.sql](src/test/resources/insert.sql)
```
insert into city (id, name) values (1,'santiago') ;
insert into city (id, name) values (2,'prague') ;
insert into city (id, name) values (3,'dakar') ;
```

### sqltest file - [success.sqt](src/test/resources/success.sqt)

```
#query

select * from city order by id 

#result

1|santiago
2|prague
3|dakar
```



# sqltest

Data-driven SQL testing mini-framework.

You create a file with a query and expected result set in PSV (pipe-separated) format. 
Then you validate the query against the results.

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

### Sample - [EmbeddedTest.scala](src/test/scala/org/amm/sqltest/EmbeddedTest.scala)

Create and populate the table.
```
  val dir = "src/test/resources"

  val ddlDrop = Source.fromFile(dir+"/drop.ddl").mkString
  stmt.executeUpdate(ddlDrop)

  val ddlCreate = Source.fromFile(dir+"/create.ddl").mkString
  stmt.executeUpdate(ddlCreate)

  for (line <- Source.fromFile(dir+"/insert.sql").getLines()) stmt.execute(line)
```

Execute your business logic and ensure the result are as expected.
```
  val isValid = SqlTestUtils.validate(conn,dir+"/success.sqt")
```

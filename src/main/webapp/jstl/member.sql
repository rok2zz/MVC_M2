

drop table member; 

create table member (
    id varchar2(100) not null primary key, 
    pass varchar2(100), 
    name varchar2(100)
    )
    
insert into member Values('1', '1234', 'È«±æµ¿'); 
insert into member values('2', '1235', '±è¶Ê¶Ê'); 

commit

select * from member; 


drop table member; 

create table member (
    id varchar2(100) not null primary key, 
    pass varchar2(100), 
    name varchar2(100)
    )
    
insert into member Values('1', '1234', 'ȫ�浿'); 
insert into member values('2', '1235', '��ʶ�'); 

commit

select * from member; 
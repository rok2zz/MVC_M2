

create table mvcboard (
    idx number primary key, 
    name varchar2(50) not null,
    title varchar2(200) not null, 
    content varchar2(2000) not null, 
    postdate date default sysdate not null, 
    ofile varchar2(200), 
    sfile varchar2(50) , 
    downcount number(5) default 0 not null, 
    pass varchar2 (50) not null, 
    visitcount number default 0 not null
    ); 
    
create sequence seq_board_num 
  increment by 1
  start with 1
  nocache
  
-- 데미 데이터 입력 (5개 정도 값입력) 
insert into mvcboard (idx, name, title,content,pass)
values (seq_board_num.nextval, '김유신', '자료실 제목1 입니다', '내용1','1234');
insert into mvcboard (idx, name, title,content,pass)
values (seq_board_num.nextval, '장보고', '자료실 제목2 입니다', '내용2','1234');
insert into mvcboard (idx, name, title,content,pass)
values (seq_board_num.nextval, '이순신', '자료실 제목3 입니다', '내용3','1234');
insert into mvcboard (idx, name, title,content,pass)
values (seq_board_num.nextval, '강감찬', '자료실 제목4 입니다', '내용4','1234');
insert into mvcboard (idx, name, title,content,pass)
values (seq_board_num.nextval, '대조영', '자료실 제목5 입니다', '내용5','1234');
commit; 

select * from mvcboard;







drop TABLE chat;

create table Chat (
    ip        Varchar2(20),
    id        Varchar2(10) CONSTRAINT chat_id PRIMARY KEY,
    pw        Varchar2(10),
    nick      Varchar2(20),
    ban       VArchar2(10),
    author    Varchar2(10)
 );

drop table Bword;

create table Bword(
    word Varchar2(20) CONSTRAINT bw_word UNIQUE
    );

drop table CHATROOM;

Create table  CHATROOM (

  title varchar2(20),
  pw varchar2(20),  
  limit varchar2(20), 
  nick varchar2(20) CONSTRAINT chat_nick PRIMARY KEY, 
  admin varchar2(20)
  );

drop table banword;

Create table BanWord(
   nick varchar2(20) CONSTRAINT bw_nick PRIMARY KEY, 
   word varchar2(20)
 );
 
Create table chatroomban(
  nick     varchar2(20),
  bantitle varchar2(20)
 );
 
 Create table userban(
  nick     varchar2(20),
  banuser varchar2(20)
 );

commit;
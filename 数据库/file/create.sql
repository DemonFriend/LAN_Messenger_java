create table T_user(
	id char(5) primary key,
	name varchar2(20),
	password varchar2(16) default '123456',
	sex varchar2(3) ,
	age number(3),
	address varchar2(60),
	regdate date default sysdate,
	isonline varchar2(4) default '����'
);
comment on table T_user is '�û���Ϣ';
comment on column T_user.id is '��ţ�QQ�ţ�';
comment on column T_user.name is '��ʵ����';
comment on column T_user.password is '����';
comment on column T_user.sex is '�Ա�';
comment on column T_user.age is '����';
comment on column T_user.address is '��ַ';
comment on column T_user.regdate is 'ע��ʱ��';
comment on column T_user.isonline is '����״̬';


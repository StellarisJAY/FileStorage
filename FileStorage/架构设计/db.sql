use FileStorage;

create table tb_user(
	id smallint primary key,               #id
    username varchar(50) not null unique,  #用户名
    password varchar(50) not null,
    max_space int,                         #最大可用容量
    used_space int                         #已使用空间
);

create table tb_file(
	id int primary key,      #id
    filename varchar(50) not null,     #文件名
    type varchar(50) not null,         #类型
    size int not null,                 #大小
    upload_date varchar(20) not null,  #上传日期
    url varchar(50) not null           #文件服务器地址
);

create table tb_user_file(
	user_id smallint,
    file_id int,
    foreign key(user_id) references tb_user(id),
    foreign key(file_id) references tb_file(id)
);

create table tb_group(
	id smallint primary key,
    name varchar(50) not null unique,
    user_count smallint,
	auth_code varchar(20)
);

create table tb_group_file(
	group_id smallint,
    file_id int,
    foreign key(group_id) references tb_group(id),
    foreign key(file_id) references tb_file(id)
);
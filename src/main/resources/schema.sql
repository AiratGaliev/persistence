drop table if exists officers;
create table officers
(
    id         int         not null auto_increment,
    rank       varchar(20) not null,
    first_name varchar(20) not null,
    last_name  varchar(20) not null,
    primary key (id)
)
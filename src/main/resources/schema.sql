create table if not exists Links (
    id int not null auto_increment,
    urlOld varchar(50) not null,
    urlNew varchar(30) not null,
    primary key (id)
);


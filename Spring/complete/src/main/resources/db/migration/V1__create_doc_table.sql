create table documents (
    id_doc int AUTO_INCREMENT,
    name_doc varchar(100) not null,
    status_doc int not null,
    createdate_doc int not null,
    updatedate_doc int not null,
    description_doc text not null
);

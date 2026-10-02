DROP DATABASE IF EXISTS magazzinoresistenza;
create database if not exists magazzinoresistenza;
use magazzinoresistenza;
create table `articoli` (
`idArticolo` int not null auto_increment,
`descrizione` varchar(100) not null, 
`numPezziMax` int not null,
`barcode` varchar(45) not null,
primary key(`idArticolo`)
);
create table `locazioni` (
`idLocazione` int not null auto_increment,
`corsia` char(2) not null,
`colonna` char(2) not null,
`ripiano` char(2) not null,
primary key(`idLocazione`)  
);

create table `movimenti` (
`idMovimento` int not null auto_increment,
`DataOraE` datetime DEFAULT NULL,
`ConfermatoE` bit(1) DEFAULT NULL,
`DataOraU` datetime DEFAULT NULL,
`ConfermatoU` bit(1) DEFAULT NULL,
`idArticolo` int not null,
`idLocazione` int not null,
`codiceInterno` int not null,
primary key (`idMovimento`),
foreign key (`idArticolo`) REFERENCES `articoli`(`idArticolo`),
foreign key (`idLocazione`) REFERENCES `locazioni`(`idLocazione`)
);


 create table `utenti` (
 `idUtente` int not null auto_increment,
 `chiaveRicerca` varchar (90) not null,
 `cognome` varchar (30) not null,
 `nome` varchar (30) not null,
 `username` varchar (20) unique not null,
 `password` varbinary (20) not null,
 primary key (`idUtente`)
 ); 
 
 
 CREATE
    ALGORITHM = UNDEFINED
    SQL SECURITY DEFINER
VIEW `magazzinoresistenza`.`vw_articoli_presenti` AS
    SELECT
        `magazzinoresistenza`.`movimenti`.`IdLocazione` AS `idlocazione`,
        `magazzinoresistenza`.`movimenti`.`IdArticolo` AS `idarticolo`,
        SUM(1) AS `NumeroPezzi`
    FROM
        `magazzinoresistenza`.`movimenti`
    WHERE
        (`magazzinoresistenza`.`movimenti`.`DataOraU` IS NULL)
    GROUP BY `magazzinoresistenza`.`movimenti`.`IdLocazione` , `magazzinoresistenza`.`movimenti`.`IdArticolo`;

   
        CREATE
    ALGORITHM = UNDEFINED
    SQL SECURITY DEFINER
VIEW `magazzinoresistenza`.`vw_occupazione_locazioni` AS
    SELECT
        `lo`.`IdLocazione` AS `IdLocazione`,
        `lo`.`Corsia` AS `Corsia`,
        `lo`.`Colonna` AS `Colonna`,
        `lo`.`Ripiano` AS `Ripiano`,
        `magazzinoresistenza`.`vw`.`NumeroPezzi` AS `NumeroPezzi`,
        `magazzinoresistenza`.`vw`.`idarticolo` AS `IdArticolo`
    FROM
        (`magazzinoresistenza`.`locazioni` `lo`
        LEFT JOIN `magazzinoresistenza`.`vw_articoli_presenti` `vw` ON ((`lo`.`IdLocazione` = `magazzinoresistenza`.`vw`.`idlocazione`)));
        
        
        
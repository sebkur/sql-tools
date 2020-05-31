CREATE TABLE `tbrick` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `identifier` varchar(255) DEFAULT NULL,
  `beschreibung` longtext,
  `flag_linkuse` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `identifier` (`identifier`),
  KEY `ID` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1171 DEFAULT CHARSET=latin1;

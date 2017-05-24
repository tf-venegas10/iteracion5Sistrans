DROP TABLE LOCALIDADES CASCADE CONSTRAINTS;
DROP TABLE COMPANIASTEATRO CASCADE CONSTRAINTS;
DROP TABLE ESPECTACULOS CASCADE CONSTRAINTS;
DROP TABLE FUNCIONES CASCADE CONSTRAINTS;
DROP TABLE DURACION CASCADE CONSTRAINTS;
DROP TABLE BOLETAS CASCADE CONSTRAINTS;
DROP TABLE REQUERIMIENTOSTECNICOS CASCADE CONSTRAINTS;
DROP TABLE SILLETERIA CASCADE CONSTRAINTS;
DROP TABLE SITIOS CASCADE CONSTRAINTS;
DROP TABLE APTOS CASCADE CONSTRAINTS;
DROP TABLE ROLES CASCADE CONSTRAINTS;
DROP TABLE CATEGORIAS CASCADE CONSTRAINTS;
DROP TABLE USUARIOS CASCADE CONSTRAINTS;
DROP TABLE PRESENTAN CASCADE CONSTRAINTS;                     
DROP TABLE REQUIERE CASCADE CONSTRAINTS;                   
DROP TABLE PRESTA CASCADE CONSTRAINTS;                        
DROP TABLE HACEPARTE CASCADE CONSTRAINTS;                                                        
DROP TABLE PREFERENCIACATEGORIA CASCADE CONSTRAINTS;         
DROP TABLE PREFERENCIASITIO CASCADE CONSTRAINTS;             
DROP TABLE ESPARA CASCADE CONSTRAINTS;
DROP TABLE SECOMPONEDE CASCADE CONSTRAINTS;
DROP TABLE CERTIFICADOSDEDEVOLUCION CASCADE CONSTRAINTS;
DROP TABLE ABONAMIENTOS CASCADE CONSTRAINTS;

DROP SEQUENCE IDABONAMIENTOS;
DROP SEQUENCE IDAPTOS;
DROP SEQUENCE IDBOLETAS;
DROP SEQUENCE IDCATEGORIAS;
DROP SEQUENCE IDCOMPANIASTEATRO;
DROP SEQUENCE IDDURACION;
DROP SEQUENCE IDESPECTACULOS;
DROP SEQUENCE IDFUNCIONES;
DROP SEQUENCE IDLOCALIDADES;
DROP SEQUENCE IDREQUERIMIENTOSTECNICOS;
DROP SEQUENCE IDROLES;
DROP SEQUENCE IDSILLETERIA;
DROP SEQUENCE IDSITIOS;
DROP SEQUENCE IDUSUARIOS;
DROP SEQUENCE IDCERTIFICADOSDEDEVOLUCION;

CREATE TABLE SILLETERIA 
(
  ID NUMBER NOT NULL ,
  NOMBRE VARCHAR2(70 BYTE) NOT NULL,
  CONSTRAINT SILLETERIA PRIMARY KEY(ID)
);

CREATE SEQUENCE IDSILLETERIA
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE TABLE ROLES 
(
  ID NUMBER NOT NULL ,
  NOMBRE VARCHAR2(70 BYTE) NOT NULL,
  CONSTRAINT ROLES PRIMARY KEY(ID)
);

CREATE SEQUENCE IDROLES
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE TABLE USUARIOSR 
(
  ID NUMBER NOT NULL ,
  IDROL NUMBER NOT NULL,
  NOMBRE VARCHAR2(70 BYTE) NOT NULL,
  CORREOELECTRONICO VARCHAR2(70),
  FECHANACIMIENTO DATE ,
  CONTRASENA VARCHAR2(30) NOT NULL,
  ACCES_TOKEN VARCHAR2(256) NOT NULL,
  CONSTRAINT USUARIOS PRIMARY KEY(ID),
  CONSTRAINT ROL_FK FOREIGN KEY (IDROL) REFERENCES ROLES(ID)
  
);

CREATE TABLE ACCES_TOKEN(
	ID NUMBER NOT NULL ,
	ACCES_TOKEN VARCHAR2(256) NOT NULL,
	CONSTRAINT USUARIOS PRIMARY KEY(ACCES_TOKEN),
  	CONSTRAINT ACCES_TOKEN_ID_FK FOREIGN KEY (ID) REFERENCES USUARIOS(ID)
);

CREATE VIEW USUARIOS as
SELECT * FROM USUARIOSR NATURAL JOIN ACCES_TOKEN;


CREATE SEQUENCE IDUSUARIOS
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE TABLE SITIOS 
(
  ID NUMBER NOT NULL ,
  NOMBRE VARCHAR2(70 BYTE) NOT NULL,
  ABIERTO char check (ABIERTO in ('N','Y')) NOT NULL,
  PROTECCIONLLUVIA char check (PROTECCIONLLUVIA in ('N','Y')) NOT NULL,
  HORAAPERTURA TIMESTAMP NOT NULL,
  HORACIERRE TIMESTAMP NOT NULL,
  IDSILLETERIA NUMBER NOT NULL,
  CONSTRAINT SITIOS PRIMARY KEY(ID),
  CONSTRAINT SITIOS2 CHECK (HORAAPERTURA<HORACIERRE),
  CONSTRAINT SILLETERIA_FK FOREIGN KEY (IDSILLETERIA) REFERENCES SILLETERIA(ID)

);

CREATE SEQUENCE IDSITIOS
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE TABLE COMPANIASTEATRO 
(
  ID NUMBER NOT NULL ,
  NOMBRE VARCHAR2(70 BYTE) NOT NULL,
  PAISORIGEN VARCHAR2(70 BYTE), 
  URLPAGINA VARCHAR2(70 BYTE), 
  FECHALLEGADA DATE NOT NULL,
  FECHASALIDA DATE NOT NULL,
  IDREPRESENTANTE NUMBER NOT NULL,
  CONSTRAINT COMPANIASTEATRO PRIMARY KEY (ID),
  CONSTRAINT COMPANIASTEATRO2 CHECK(FECHASALIDA> FECHALLEGADA),
  CONSTRAINT ID_REP FOREIGN KEY (IDREPRESENTANTE) REFERENCES USUARIOS(ID)
);

CREATE SEQUENCE IDCOMPANIASTEATRO
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE TABLE ESPECTACULOS 
(
  ID NUMBER NOT NULL ,
  NOMBRE VARCHAR2(70 BYTE) NOT NULL,
  DURACION NUMBER NOT NULL, 
  IDIOMA VARCHAR2(100 BYTE) NOT NULL, 
  COSTO BINARY_DOUBLE NOT NULL,
  DESCRIPCION VARCHAR2(1000 BYTE),
  CONSTRAINT ESPECTACULOS PRIMARY KEY(ID)
);
CREATE SEQUENCE IDESPECTACULOS
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE TABLE FUNCIONES 
(
  ID NUMBER NOT NULL ,
  FECHA TIMESTAMP NOT NULL,
  REALIZADO  char check (REALIZADO in ('N','Y')),
  IDESPECTACULO NUMBER NOT NULL,
  IDSITIO NUMBER NOT NULL,
  CONSTRAINT FUNCIONES PRIMARY KEY(ID),
  CONSTRAINT FK_FUNC_SITIO FOREIGN KEY (IDSITIO) REFERENCES SITIOS(ID),
  CONSTRAINT FK_FUNC_ESPECT FOREIGN KEY (IDESPECTACULO) REFERENCES ESPECTACULOS(ID)
);

CREATE SEQUENCE IDFUNCIONES
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE TABLE DURACION 
(
  ID NUMBER NOT NULL ,
  FECHAINICIO DATE,
  FECHAFIN DATE,
  CONSTRAINT DURACION PRIMARY KEY(ID),
  CONSTRAINT DURACION2 CHECK (FECHAINICIO<FECHAFIN)

);
CREATE SEQUENCE IDDURACION
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE TABLE LOCALIDADES 
(
  ID NUMBER NOT NULL ,
  TIPO VARCHAR2(70 BYTE) NOT NULL,
  ESNUMERADA char check (ESNUMERADA in ('N','Y')),
  NUMERODEPUESTOS NUMBER NOT NULL,
  FILAS NUMBER,
  SILLASPORFILA NUMBER,
  PRECIO BINARY_DOUBLE NOT NULL,
  CONSTRAINT LOCALIDADES PRIMARY KEY(ID)
 
);
CREATE SEQUENCE IDLOCALIDADES
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE TABLE ABONAMIENTOS(
	ID NUMBER NOT NULL,
    IDUSUARIO NUMBER NOT NULL,
  CONSTRAINT ABONAMIENTOS_PK PRIMARY KEY (ID),
  CONSTRAINT IDUSUARIO_FK_ABONAMIENTOS FOREIGN KEY (IDUSUARIO) REFERENCES USUARIOS(ID)
);

CREATE SEQUENCE IDABONAMIENTOS
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;


CREATE TABLE BOLETAS 
(
  ID NUMBER NOT NULL,
  VENDIDO char check (VENDIDO in ('N','Y')), 
  FILA NUMBER,
  SILLA NUMBER,
  IDLOCALIDAD NUMBER NOT NULL,
  IDFUNCION NUMBER NOT NULL,
  IDUSUARIO NUMBER,
  IDABONAMIENTO NUMBER,
  CONSTRAINT BOLETAS PRIMARY KEY (ID),
  CONSTRAINT LOC_FK FOREIGN KEY (IDLOCALIDAD) REFERENCES LOCALIDADES(ID),
  CONSTRAINT FUNC_FK FOREIGN KEY (IDFUNCION) REFERENCES FUNCIONES(ID),
  CONSTRAINT FUNC2_FK FOREIGN KEY (IDUSUARIO) REFERENCES USUARIOS(ID),
  CONSTRAINT ABONAMIENTO_BOLETA_FK FOREIGN KEY (IDABONAMIENTO) REFERENCES ABONAMIENTOS(ID)
);

CREATE SEQUENCE IDBOLETAS
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;
---
CREATE TABLE REQUERIMIENTOSTECNICOS 
(
  ID NUMBER NOT NULL ,
  NOMBRE VARCHAR2(70 BYTE) NOT NULL,
  CONSTRAINT REQUERIMIENTOSTECNICOS PRIMARY KEY(ID)

);

CREATE SEQUENCE IDREQUERIMIENTOSTECNICOS
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;


CREATE TABLE APTOS 
(
  ID NUMBER NOT NULL ,
  NOMBRE VARCHAR2(70 BYTE) NOT NULL,
  CONSTRAINT APTOS PRIMARY KEY(ID)
);

CREATE SEQUENCE IDAPTOS
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;



CREATE TABLE CATEGORIAS 
(
  ID NUMBER NOT NULL ,
  NOMBRE VARCHAR2(70 BYTE) NOT NULL,
  CONSTRAINT CATEGORIAS PRIMARY KEY(ID)
);

CREATE SEQUENCE IDCATEGORIAS
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;



CREATE TABLE PRESENTAN 
(
	IDCOMPANIA NUMBER NOT NULL,
	IDESPECTACULO NUMBER NOT NULL,
	CONSTRAINT PRESENTAN PRIMARY KEY (IDCOMPANIA,IDESPECTACULO),
	CONSTRAINT COMP_FK FOREIGN KEY (IDCOMPANIA) REFERENCES COMPANIASTEATRO(ID),
	CONSTRAINT ESP_FK FOREIGN KEY (IDESPECTACULO) REFERENCES ESPECTACULOS(ID)
);

CREATE TABLE REQUIERE 
(
	IDESPECTACULO NUMBER NOT NULL,
	IDREQUERIMIENTO NUMBER NOT NULL,
	CONSTRAINT REQUIERE PRIMARY KEY (IDESPECTACULO,IDREQUERIMIENTO),
	CONSTRAINT REQ_FK FOREIGN KEY (IDREQUERIMIENTO) REFERENCES REQUERIMIENTOSTECNICOS(ID),
	CONSTRAINT ESP_REQ_FK FOREIGN KEY (IDESPECTACULO) REFERENCES ESPECTACULOS(ID)
);

CREATE TABLE PRESTA 
(
	IDREQUERIMIENTO NUMBER NOT NULL,
	IDSITIO NUMBER NOT NULL,
	CONSTRAINT PRESTA PRIMARY KEY (IDREQUERIMIENTO,IDSITIO),
	CONSTRAINT PRESTA_FK FOREIGN KEY (IDREQUERIMIENTO) REFERENCES REQUERIMIENTOSTECNICOS(ID),
	CONSTRAINT PRESTA2_FK FOREIGN KEY (IDSITIO) REFERENCES SITIOS(ID)
);

CREATE TABLE HACEPARTE 
(
	IDESPECTACULO NUMBER NOT NULL,
	IDCATEGORIA NUMBER NOT NULL,
	CONSTRAINT HACEPARTE PRIMARY KEY (IDESPECTACULO,IDCATEGORIA),
	CONSTRAINT HACEPARTE_FK FOREIGN KEY (IDESPECTACULO) REFERENCES ESPECTACULOS(ID),
	CONSTRAINT HACEPARTE2_FK FOREIGN KEY (IDCATEGORIA) REFERENCES CATEGORIAS(ID)
);

CREATE TABLE PREFERENCIACATEGORIA 
(
	IDCATEGORIA NUMBER NOT NULL,
	IDUSUARIO NUMBER NOT NULL,
	CONSTRAINT PREFERENCIACATEGORIA PRIMARY KEY (IDCATEGORIA,IDUSUARIO),
	CONSTRAINT PREFERENCIACATEGORIA_FK FOREIGN KEY (IDCATEGORIA) REFERENCES CATEGORIAS(ID),
	CONSTRAINT PREFERENCIACATEGORIA2_FK FOREIGN KEY (IDUSUARIO) REFERENCES USUARIOS(ID)
);

CREATE TABLE PREFERENCIASITIO
(
	IDSITIO NUMBER NOT NULL,
	IDUSUARIO NUMBER NOT NULL,
	CONSTRAINT PREFERENCIASITIO PRIMARY KEY (IDSITIO,IDUSUARIO),
	CONSTRAINT PREFERENCIASITIO_FK FOREIGN KEY (IDSITIO) REFERENCES SITIOS(ID),
	CONSTRAINT PREFERENCIASITIO2_FK FOREIGN KEY (IDUSUARIO) REFERENCES USUARIOS(ID)
);

CREATE TABLE ESPARA
(
	IDSITIO NUMBER NOT NULL,
	IDAPTOS NUMBER NOT NULL,
	CONSTRAINT ESPARA PRIMARY KEY (IDSITIO,IDAPTOS),
	CONSTRAINT ESPARA_FK FOREIGN KEY (IDSITIO) REFERENCES SITIOS(ID),
	CONSTRAINT ESPARA2_FK FOREIGN KEY (IDAPTOS) REFERENCES APTOS(ID)
);

CREATE TABLE SECOMPONEDE
(
	IDSITIO NUMBER NOT NULL,
	IDLOCALIDAD NUMBER NOT NULL,
	CONSTRAINT SECOMPONEDE PRIMARY KEY (IDSITIO,IDLOCALIDAD),
	CONSTRAINT SECOMPONEDE_FK FOREIGN KEY (IDSITIO) REFERENCES SITIOS(ID),
	CONSTRAINT SECOMPONEDE2_FK FOREIGN KEY (IDLOCALIDAD) REFERENCES LOCALIDADES(ID)
);


CREATE TABLE CERTIFICADOSDEDEVOLUCION(
	ID NUMBER NOT NULL,
	IDUSUARIO NUMBER NOT NULL,
	IDBOLETA NUMBER ,
	VALOR BINARY_DOUBLE CHECK (VALOR >0) NOT NULL,
	CONSTRAINT CERTIFICADOS_PK PRIMARY KEY (ID),
	CONSTRAINT USUARIO_FK_CERT FOREIGN KEY (IDUSUARIO) REFERENCES USUARIOS(ID),
	CONSTRAINT BOLETA_FK_CERT FOREIGN KEY (IDBOLETA) REFERENCES BOLETAS(ID)
);

CREATE SEQUENCE IDCERTIFICADOSDEDEVOLUCION
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;


INSERT INTO ROLES (ID,NOMBRE) VALUES (IDROLES.NEXTVAL,'CLIENTE');
INSERT INTO ROLES (ID,NOMBRE) VALUES (IDROLES.NEXTVAL,'ADMINISTRADOR');
INSERT INTO ROLES (ID,NOMBRE) VALUES (IDROLES.NEXTVAL,'OPERARIO');

INSERT INTO SILLETERIA (ID,NOMBRE) VALUES (IDSILLETERIA.NEXTVAL, 'MOVIL');
INSERT INTO SILLETERIA (ID,NOMBRE) VALUES (IDSILLETERIA.NEXTVAL, 'FIJA');
INSERT INTO SILLETERIA (ID,NOMBRE) VALUES (IDSILLETERIA.NEXTVAL, 'REMOVIBLE');

INSERT INTO APTOS (ID,NOMBRE) VALUES (IDAPTOS.NEXTVAL, 'MENORES');
INSERT INTO APTOS (ID,NOMBRE) VALUES (IDAPTOS.NEXTVAL, 'MOVILIDAD');
INSERT INTO APTOS (ID,NOMBRE) VALUES (IDAPTOS.NEXTVAL, 'MAYORES');

INSERT INTO REQUERIMIENTOSTECNICOS (ID,NOMBRE) VALUES (IDREQUERIMIENTOSTECNICOS.NEXTVAL, 'AMPLIACION DE SONIDO');
INSERT INTO REQUERIMIENTOSTECNICOS (ID,NOMBRE) VALUES (IDREQUERIMIENTOSTECNICOS.NEXTVAL, 'EFECTOS DE LUZ');
INSERT INTO REQUERIMIENTOSTECNICOS (ID,NOMBRE) VALUES (IDREQUERIMIENTOSTECNICOS.NEXTVAL, 'FUEGOS ARTIFICIALES');
INSERT INTO REQUERIMIENTOSTECNICOS (ID,NOMBRE) VALUES (IDREQUERIMIENTOSTECNICOS.NEXTVAL, 'EFECTOS 4D');
INSERT INTO REQUERIMIENTOSTECNICOS (ID,NOMBRE) VALUES (IDREQUERIMIENTOSTECNICOS.NEXTVAL, 'EFECTOS 3D');
INSERT INTO REQUERIMIENTOSTECNICOS (ID,NOMBRE) VALUES (IDREQUERIMIENTOSTECNICOS.NEXTVAL, 'INTERACCION CON EL PUBLICO');
INSERT INTO REQUERIMIENTOSTECNICOS (ID,NOMBRE) VALUES (IDREQUERIMIENTOSTECNICOS.NEXTVAL, 'EFECTOS EN ALTURAS');
INSERT INTO REQUERIMIENTOSTECNICOS (ID,NOMBRE) VALUES (IDREQUERIMIENTOSTECNICOS.NEXTVAL, 'INSTALACION DE ESCENARIOS PROPIOS');
INSERT INTO REQUERIMIENTOSTECNICOS (ID,NOMBRE) VALUES (IDREQUERIMIENTOSTECNICOS.NEXTVAL, 'INSTALACIONES INTERACTIVAS');

INSERT INTO SITIOS (ID,NOMBRE,ABIERTO,PROTECCIONLLUVIA,HORAAPERTURA,HORACIERRE,IDSILLETERIA) VALUES (IDSITIOS.NEXTVAL,'el mejor lugar','Y','Y',TO_TIMESTAMP('00::00','HH24::MI'),TO_TIMESTAMP('23::59','HH24::MI'),1);

INSERT INTO CATEGORIAS (ID,NOMBRE) VALUES (IDCATEGORIAS.NEXTVAL,'DRAMA');
INSERT INTO CATEGORIAS (ID,NOMBRE) VALUES (IDCATEGORIAS.NEXTVAL,'COMEDIA');
INSERT INTO CATEGORIAS (ID,NOMBRE) VALUES (IDCATEGORIAS.NEXTVAL,'TEATRO MUDO');
INSERT INTO CATEGORIAS (ID,NOMBRE) VALUES (IDCATEGORIAS.NEXTVAL,'TITERES');
INSERT INTO CATEGORIAS (ID,NOMBRE) VALUES (IDCATEGORIAS.NEXTVAL,'COMPARSA');
INSERT INTO CATEGORIAS (ID,NOMBRE) VALUES (IDCATEGORIAS.NEXTVAL,'OPERA');
INSERT INTO CATEGORIAS (ID,NOMBRE) VALUES (IDCATEGORIAS.NEXTVAL,'ZARZUELA');
INSERT INTO CATEGORIAS (ID,NOMBRE) VALUES (IDCATEGORIAS.NEXTVAL,'MUSICAL');
INSERT INTO CATEGORIAS (ID,NOMBRE) VALUES (IDCATEGORIAS.NEXTVAL,'CIRCO');

INSERT INTO LOCALIDADES (ID,TIPO,ESNUMERADA,NUMERODEPUESTOS,FILAS,SILLASPORFILA,PRECIO) VALUES (IDLOCALIDADES.NEXTVAL,'VIP','Y',100,10,10,150000.000);
INSERT INTO LOCALIDADES (ID,TIPO,ESNUMERADA,NUMERODEPUESTOS,FILAS,SILLASPORFILA,PRECIO) VALUES (IDLOCALIDADES.NEXTVAL,'GENERAL','N',1000,NULL,NULL,50000.000);
INSERT INTO LOCALIDADES (ID,TIPO,ESNUMERADA,NUMERODEPUESTOS,FILAS,SILLASPORFILA,PRECIO) VALUES (IDLOCALIDADES.NEXTVAL,'PALCO','Y',10,5,2,300000.000);



COMMIT;


INSERT INTO Client (birth,cpf,email,name) VALUES (PARSEDATETIME('15/08/1991','dd/MM/yyyy'),16542568452,'klopafeg@provider.com','Klopaf Engel Gissap');
INSERT INTO Registry_User (id,email,username,password) VALUES ((SELECT id FROM Client WHERE name = 'Klopaf Engel Gissap'),'klopafeg@provider.com','klopafeg','$2a$10$ibUt.IWu4HaHkHG.N2sBW.eN6MgtFoCPknSkg1Ct/53RmsBobd9z.');

INSERT INTO Client (birth,cpf,email,name) VALUES (PARSEDATETIME('08/04/1988','dd/MM/yyyy'),3261975214,'alfagema@provider.com','Alfagem Arrah');
INSERT INTO Registry_User (id,email,username,password) VALUES ((SELECT id FROM Client WHERE name = 'Alfagem Arrah'), 'alfagema@provider.com','alfagema','$2a$10$ibUt.IWu4HaHkHG.N2sBW.eN6MgtFoCPknSkg1Ct/53RmsBobd9z.');

INSERT INTO Nutrient (id,name,unit) VALUES (1002,'Nitrogen','G');
INSERT INTO Nutrient (id,name,unit) VALUES (1003,'Protein','G');
INSERT INTO Nutrient (id,name,unit) VALUES (1004,'Total lipid (fat)','G');
INSERT INTO Nutrient (id,name,unit) VALUES (1005,'Carbohydrate, by difference','G');
INSERT INTO Nutrient (id,name,unit) VALUES (1008,'Energy','KCAL');
INSERT INTO Nutrient (id,name,unit) VALUES (1051,'Water','G');

INSERT INTO Nutrient (id,name,unit) VALUES (1079,'Fiber, total dietary','G');
INSERT INTO Nutrient (id,name,unit) VALUES (1087,'Calcium, Ca','MG');
INSERT INTO Nutrient (id,name,unit) VALUES (1089,'Iron, Fe','MG');
INSERT INTO Nutrient (id,name,unit) VALUES (1090,'Magnesium, Mg','MG');
INSERT INTO Nutrient (id,name,unit) VALUES (1092,'Potassium, K','MG');
INSERT INTO Nutrient (id,name,unit) VALUES (1093,'Sodium, Na','MG');
INSERT INTO Nutrient (id,name,unit) VALUES (1095,'Zinc, Zn','MG');
INSERT INTO Nutrient (id,name,unit) VALUES (1103,'Selenium, Se','UG');
INSERT INTO Nutrient (id,name,unit) VALUES (1175,'Vitamin B-6','MG');

INSERT INTO Chart (name, client_id) VALUES ('Chart 1', 1);
INSERT INTO Chart (name, client_id) VALUES ('Chart 2', 1);
INSERT INTO Chart (name, client_id) VALUES ('Chart 3', 1);

INSERT INTO Chart (name, client_id) VALUES ('Chart 4', 2);
INSERT INTO Chart (name, client_id) VALUES ('Chart 5', 2);
INSERT INTO Chart (name, client_id) VALUES ('Chart 6', 2);

INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (10.00, 1, 1002);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (12.00, 1, 1003);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (19.00, 1, 1004);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (28.00, 1, 1005);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (20.00, 1, 1008);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (22.00, 1, 1051);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (16.00, 1, 1087);

INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (18.00, 2, 1002);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (13.00, 2, 1003);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (39.00, 2, 1004);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (12.00, 2, 1005);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (28.00, 2, 1008);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (15.00, 2, 1051);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (15.00, 2, 1093);

INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (16.00, 3, 1002);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (16.00, 3, 1003);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (16.00, 3, 1004);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (85.00, 3, 1005);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (38.00, 3, 1008);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (15.00, 3, 1051);
INSERT INTO Chart_Nutrient (Nutrient_Total, chart_id, nutrient_id) VALUES (36.00, 3, 1175);

INSERT INTO Chart_Food (qtd, food_id, chart_id) VALUES (2, 747997, 1); --Ovo
INSERT INTO Chart_Food (qtd, food_id, chart_id) VALUES (2, 2258586, 1); --Cenoura
INSERT INTO Chart_Food (qtd, food_id, chart_id) VALUES (2, 2003586, 1); --Farinha

INSERT INTO Chart_Food (qtd, food_id, chart_id) VALUES (3, 2258586, 2); --Cenoura
INSERT INTO Chart_Food (qtd, food_id, chart_id) VALUES (3, 2261422, 2); --Batata
INSERT INTO Chart_Food (qtd, food_id, chart_id) VALUES (3, 746769, 2); --Alface

INSERT INTO Chart_Food (qtd, food_id, chart_id) VALUES (4, 2261422, 3); --Alface
INSERT INTO Chart_Food (qtd, food_id, chart_id) VALUES (4, 1999634, 3); --Tomate
INSERT INTO Chart_Food (qtd, food_id, chart_id) VALUES (4, 2258586, 3); --Cenoura

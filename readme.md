# MOP installation sur une nouvelle instance


NOTE : les items de la TODO ci-aprés sont détaillés dans cette documentation
* [ ] Créer la base de données [(voir chapitre relatif)](#cr%C3%A9ation-de-la-base-de-donn%C3%A9es)


# Configuration de l'application

## Base de données

### Création de la base de données

La base de données doit être créé sur l'environnement cible via ces commandes : 

```sql
CREATE USER runner WITH PASSWORD 'password';
```

```sql
CREATE DATABASE runner_db WITH OWNER = runner ENCODING = 'UTF8' TABLESPACE = pg_default CONNECTION LIMIT = -1;
```

### Liquibase

La création des tables, chargements de données, etc est géré via Liquibase.
Liquibase effectue les opérations de migration de la base au démarrage de l'application


##### Astuce: Liquibase
Pour extraire  un changeSet d'une table existante, jouez les commandes suivantes

```sh
cd src/main/resources/bin


./liquibase  --driver=org.postgresql.Driver   --classpath=./postgresql-42.2.6.jar    --url=$(cat ../application.properties | grep "spring.datasource.url" | cut -d'=' -f2)    --username=$(cat ../application.properties | grep "spring.datasource.username" |
cut -d'=' -f2)    --password=$(cat ../application.properties | grep "spring.datasource.password" | cut -d'=' -f2)     --changeLogFile=../../../../target/generated-sources/actual_database_schema.xml     generateChangeLog

```

Ce code génère le changlog de la la base de donnée entière.
Le fichier "actual_database_schema.xml" est généré au dessous de /target/generated-sources et la vous selectionnez la changeSet que vous cherchez .

 sources : 
- https://www.liquibase.org/documentation/generating_changelogs.html 
- https://www.mkyong.com/jdbc/how-do-connect-to-postgresql-with-jdbc-driver-java/ 
- https://jdbc.postgresql.org/download.html
 
 
### API Documentation avec Swagger

Pour documenter correctement les endpoints disponibles de l' API, ce qu'ils peuvent faire, les paramètres d'entrée dont ils ont besoin et ce qu'ils fourniront en sortie. Swagger est un standard populaire utilisé à cette fin.

Après avoir exécuté l'application, accédez à http://localhost:8080/go-api/swagger-ui.html

![img](src/main/resources/screen-shot/swagger.jpg "Title")

### TO IMPROVE / TODO
MCD / data save into a normalized db
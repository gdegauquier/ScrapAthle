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

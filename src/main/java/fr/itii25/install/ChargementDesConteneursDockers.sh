#!/bin/bash

# Afficher un message d'information
echo "Lancement des conteneurs Docker PostgreSQL et SakilaDB/MySQL..."

# Lancer le conteneur PostgreSQL
echo "Démarrage de PostgreSQL..."
docker run --rm -it -p 5432:5432 -d -e POSTGRES_USER="user" -e POSTGRES_PASSWORD="admin" postgres:latest

echo "PostgreSQL arrêté ou terminé."

# Lancer le conteneur SakilaDB/MySQL
echo "Démarrage de SakilaDB/MySQL..."
docker run --rm -it -p 3306:3306 -d sakiladb/mysql:latest

echo "SakilaDB/MySQL est lancé en mode détaché."

echo -e "\033[1;36mTous les conteneurs Docker sont démarrés ou arrêtés selon leur mode d'exécution.\033[0m"

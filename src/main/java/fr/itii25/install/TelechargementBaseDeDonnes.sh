#!/bin/bash
#!/bin/bash

# Afficher un message d'information
echo "Mise à jour des images Docker pour PostgreSQL et SakilaDB/MySQL..."

# Commande pour récupérer l'image Docker de PostgreSQL
echo "Téléchargement de l'image PostgreSQL..."
docker pull postgres

# Commande pour récupérer l'image Docker de SakilaDB/MySQL
echo "Téléchargement de l'image SakilaDB/MySQL..."
docker pull sakiladb/mysql

# Afficher un message de fin
echo "Mise à jour terminée."

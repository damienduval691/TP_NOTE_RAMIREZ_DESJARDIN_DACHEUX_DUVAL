# update-databases.ps1

Write-Host "Mise a jour des images Docker pour PostgreSQL et SakilaDB/MySQL..."

# Commande pour récupérer l'image Docker de PostgreSQL
Write-Host "Telechargement de l'image PostgreSQL..."
docker pull postgres

# Commande pour récupérer l'image Docker de SakilaDB/MySQL
Write-Host "Telechargement de l'image SakilaDB/MySQL..."
docker pull sakiladb/mysql

Write-Host "Mise à jour terminée."

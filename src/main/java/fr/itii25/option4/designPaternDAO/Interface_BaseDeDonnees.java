package fr.itii25.option4.designPaternDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Interface_BaseDeDonnees {

    /**
     * Charge le driver JDBC.
     * @param nomDriver Nom du driver JDBC
     */
    public void chargerDriver(String nomDriver) throws ClassNotFoundException;

    /**
     * Établit une connexion à la base de données.
     * @return Connection Objet Connection connecté à la base de données
     */
    public Connection connexion() throws SQLException;

    /**
     * Méthode pour vérifier l'état de la connexion.
     */
    public boolean isConnected() throws SQLException;


    /**
     * Méthode pour créer une table.
     * Création d'une table
     * Exemple d'application :
     * String structureTableau = "id SERIAL PRIMARY KEY, nom VARCHAR(50), age INTEGER";
     * dbConnection.creerTableau("Personnes", structureTableau);
     */
    public void creerTableau(String nomTableau, String structureTableau) throws SQLException;

    /**
     * Consulter les donnees à travers d'un requete
     * @param requete Requete de consultation
     * @return ResultSet Objet ResultSet de la consultation de la requete
     */
    public ResultSet consulterDonnees(String requete) throws SQLException;

    public void insererDeDonnees(String nomTableau, String structureTableau, String donneesTableau) throws SQLException;

    public void effacerDonnees(String requete) throws SQLException;
    /**
     * Ferme la connexion à la base de données.
     */
    public void disconnect() throws SQLException;





}

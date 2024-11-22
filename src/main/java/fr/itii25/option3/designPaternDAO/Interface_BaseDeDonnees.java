package fr.itii25.option3.designPaternDAO;
import java.sql.*;

public interface Interface_BaseDeDonnees {

    /**
     * Charge le driver JDBC.
     * @param nomDriver Nom du driver JDBC
     */
    public void chargerDriver(String nomDriver);

    /**
     * Établit une connexion à la base de données.
     * @return Connection Objet Connection connecté à la base de données
     */
    public Connection connexion();

    /**
     * Méthode pour vérifier l'état de la connexion.
     */
    public boolean estConnectee();

    /**
     * Creer une base de donnees
     * @param nomBaseDeDonnees nom de la base de donnees
     */
    public void creerBaseDeDonnees(String nomBaseDeDonnees);

    /**
     * Méthode pour créer une table.
     * Création d'une table
     * Exemple d'application :
     * String structureTableau = "id SERIAL PRIMARY KEY, nom VARCHAR(50), age INTEGER";
     * dbConnection.creerTableau("Personnes", structureTableau);
     */
    public void creerTableau(String nomTableau, String structureTableau);

    /**
     * Consulter les donnees à travers d'un requete
     * @param requete Requete de consultation
     * @return ResultSet Objet ResultSet de la consultation de la requete
     */
    public ResultSet consulterDonnees(String requete);



    /**
     * Ferme la connexion à la base de données.
     */
    public void deconectee();





}

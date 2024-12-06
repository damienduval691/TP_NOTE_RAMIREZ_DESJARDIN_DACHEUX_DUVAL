package fr.itii25.option3.designPaternDAO;

import java.sql.Connection;

public class BaseDeDonnees {
    /**
     * Informations pour la connexion à la base de donnees
     */
    private String urlJDBC;
    private String utilisateur;
    private String password;
    private Connection connection;

    public BaseDeDonnees(){

    }

    public String getUrlJDBC() {
        return urlJDBC;
    }

    public void setUrlJDBC(String urlJDBC) {
        this.urlJDBC = urlJDBC;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Constructeur d'ImplementerBaseDeDonnees
     * @param urlJDBC Nom du driver JDBC
     * @param utilisateur Nom de l'utilisateur de la base de donnees
     * @param password Mot de passe de la base de donnees
     *
     * Exemple d'utilisation ("jdbc:mysql://localhost:3306/user","user","password")
     */

    public BaseDeDonnees(String urlJDBC, String utilisateur, String password) {
        if((urlJDBC != null) && (!urlJDBC.isBlank()) &&
           (utilisateur != null) && (!utilisateur.isBlank()) &&
           (password != null) && (!password.isBlank())) {

            this.urlJDBC = urlJDBC;
            this.utilisateur = utilisateur;
            this.password = password;
        } else
            System.out.println("Erreur : urlJDBC, utilisateur, password null");
    }

}

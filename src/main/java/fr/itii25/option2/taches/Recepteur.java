package fr.itii25.option2.taches;

import fr.itii25.option2.message.Message;
import fr.itii25.option2.message.MessageDeCommande;
import fr.itii25.option2.message.MessageDeDonnees;

import java.sql.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Recepteur implements Runnable {


    private LinkedBlockingQueue<Message> canalDeCommunication;
    private boolean exit;

    public Recepteur(LinkedBlockingQueue<Message> canalDeCommunication) {
        this(canalDeCommunication,  true);
    }

    //Constructeur complet avec en passage le canal de communication
    // + vérification que le canal de communication existe bien en base
    private Recepteur(LinkedBlockingQueue<Message> canalDeCommunication, boolean exit) {
        if(canalDeCommunication != null){
            this.canalDeCommunication = canalDeCommunication;
            this.exit = exit;
        }
        else
            System.out.println("Le canal n'a pas été créé");
    }



    public void stop(){
        exit = false;
    }

    @Override
    public void run() {
        //Ajouts des drives des JDBC
        String nomDriverJDBCPostgre    = "org.postgresql.Driver";

        //Ajouts des liens vers les JDBC
        String urlJDBCPostgre = "jdbc:postgresql://localhost:5432/";

        //Chargement du driver pour la JDBC POSTGRE
        try {
            Class.forName(nomDriverJDBCPostgre);
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur de chargement du driver : nomDriverJDBCPostgre");
            e.printStackTrace();
        }

        //Connexion à la base de donnée POSTGRE
        Connection cxPOSTGRE = null;
        try {
            cxPOSTGRE = DriverManager.getConnection(urlJDBCPostgre, "user", "admin");
        } catch (SQLException e) {
            System.out.println("Erreur de connection avec la JDBC");
            e.printStackTrace();
        }

        //String requeteDataBase  = "CREATE DATABASE IF NOT EXISTS DataBase;";
        String requeteTable     = "CREATE TABLE IF NOT EXISTS Actor " +
                "(actor_id INT," +
                "first_name VARCHAR(50)," +
                "last_name VARCHAR(50)," +
                "last_update TIMESTAMP," +
                "actor_datecre TIMESTAMP);";


        Statement stmt=null;
        try {
            stmt=cxPOSTGRE.createStatement();
            System.out.println("Database créée");
        } catch (SQLException e) {
            System.out.println("Erreur database creation de Statement!");
            e.printStackTrace();
            System.exit(-1);
        }

//        try {
//            stmt.executeUpdate(requeteDataBase);
//            System.out.println("Database créée");
//        } catch (SQLException e) {
//            System.out.println("Erreur création database ");
//            e.printStackTrace();
//            System.exit(-2);
//        }

        try {
            stmt.executeUpdate(requeteTable);
        } catch (SQLException e) {
            System.out.println("Erreur création Table ");
            e.printStackTrace();
            System.exit(-3);
        }

        String requeteSelect = "SELECT * FROM Actor";

        try {
            //On envoie tant que l'utilisateur n'a pas tapé FIN
            while (exit) {
                Message message = canalDeCommunication.take(); // Récupération du message
                String requeteInsert = "";
                if (message instanceof MessageDeDonnees) {
                    ResultSet values = ((MessageDeDonnees<ResultSet>) message).getMessageDeDonnee();
                    try{
                        int i = 0;
                        String parametres = "";

                        while(values.next()){
                            i++;
                            int id                  = values.getInt(1);
                            String FName            = values.getString(2);
                            String LName            = values.getString(3);
                            Timestamp DateTime      = values.getTimestamp(4);

                            parametres = "("+id+",'"+FName+"','"+LName+"','"+DateTime+"',CURRENT_TIMESTAMP);";

                            requeteInsert = "INSERT INTO ACTOR " +
                                    "(actor_id, first_name, last_name, last_update, actor_datecre)" +
                                    "VALUES " + parametres;

                            try {
                                stmt.executeUpdate(requeteInsert);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    } catch (SQLException e){
                        System.out.println("Insert non réussi.");
                        e.printStackTrace();
                    }
                } else if (message instanceof MessageDeCommande) {
                    String command = ((MessageDeCommande) message).getMessageDeCommande();
                    System.out.println("Commande reçue : " + command);
                    if ("FIN".equals(command)) {
                        System.out.println("Tâche Réceptrice arrêtée.");
                        int i = 0;

                        ResultSet rs = null;
                        try {
                            rs = stmt.executeQuery(requeteSelect);

                        } catch (SQLException e) {
                            System.out.println("Erreur database SELECT !");
                            e.printStackTrace(); // affiche les informations sur la pile d'execution en cas de plantage
                            System.exit(-4);
                        }

                        try{
                            while(rs.next()){
                                String parametres = "";
                                i++;
                                int id              = rs.getInt(1);
                                String FName        = rs.getString(2);
                                String LName        = rs.getString(3);
                                Timestamp DateTime  = rs.getTimestamp(4);

                                System.out.println(id+" ,"+FName+" ,"+LName+" ,"+DateTime);

                            }

                            System.out.println("Voici la table actor !");
                        } catch (SQLException e){

                        }
                        stop();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Tâche Réceptrice interrompue.");
        }

        try {
            cxPOSTGRE.close();
        } catch (SQLException e) {
            System.out.println("Erreur de cloture de la JDBC POSTGRE.");
            e.printStackTrace();
        }
    }
}

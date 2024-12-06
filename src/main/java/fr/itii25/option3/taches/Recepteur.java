package fr.itii25.option3.taches;

import fr.itii25.option3.message.MessageDeCommande;
import fr.itii25.option3.message.MessageDeDonnees;
import fr.itii25.option3.designPaternDAO.ImplementerBaseDeDonnees;
import fr.itii25.option3.message.Message;

import java.sql.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Recepteur implements Runnable {

    private LinkedBlockingQueue<Message> canalDeCommunication;
    private boolean exit;
    private Lock verrou;

    public Recepteur(LinkedBlockingQueue<Message> canalDeCommunication, Lock verrou) {
        this(canalDeCommunication,  true, verrou);
    }

    //Constructeur complet avec en passage le canal de communication
    // + vérification que le canal de communication existe bien en base
    private Recepteur(LinkedBlockingQueue<Message> canalDeCommunication, boolean exit, Lock verrou) {
        if((canalDeCommunication != null)&&(verrou != null)){
            this.canalDeCommunication = canalDeCommunication;
            this.exit = exit;
            this.verrou = new ReentrantLock();
        }
        else
            System.out.println("Le canal ou le verrou n'a pas été créé");
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

        ImplementerBaseDeDonnees dbPostgre = new ImplementerBaseDeDonnees(urlJDBCPostgre,"user", "admin") {};

        dbPostgre.chargerDriver(nomDriverJDBCPostgre);
        dbPostgre.connexion();

        //Création de toutes les tables nécessaires
        dbPostgre.creerTableau("actor",
                "actor_id INT, " +
                "first_name VARCHAR(50)," +
                "last_name VARCHAR(50)," +
                "last_update TIMESTAMP," +
                "actor_datecre TIMESTAMP");

        dbPostgre.creerTableau("category",
                "category_id INT, " +
                "name VARCHAR(25)," +
                "last_update TIMESTAMP," +
                "category_datecre TIMESTAMP");

        dbPostgre.creerTableau("city",
                "city_id INT, " +
                "city VARCHAR(50)," +
                "contry_id INT," +
                "last_update TIMESTAMP," +
                "city_datecre TIMESTAMP");

        ResultSet rs = null;
        try {
            //On envoie tant que l'utilisateur n'a pas tapé les commandes indiquées
            while (exit) {
                Message message = canalDeCommunication.take(); // Récupération du message
                //if(verrou.tryLock()){
                    verrou.lock();
                    if (message instanceof MessageDeDonnees) {
                        ResultSet values = ((MessageDeDonnees<ResultSet>) message).getMessageDeDonnee();
                        ResultSetMetaData metaData = values.getMetaData();
                        if("actor".contains(metaData.getTableName(1))){
                            try {
                                int i = 0;
                                String parametres = "";

                                while (values.next()) {
                                    i++;
                                    int id = values.getInt(1);
                                    String FName = values.getString(2);
                                    String LName = values.getString(3);
                                    Timestamp DateTime = values.getTimestamp(4);

                                    parametres = id + ",'" + FName + "','" + LName + "','" + DateTime + "',CURRENT_TIMESTAMP";
                                    String structureTableau = "actor_id, first_name, last_name, last_update, actor_datecre";
                                    dbPostgre.insererDeDonnees("actor", structureTableau, parametres);

                                }
                                //System.out.println("Insertion de la base réussie.");
                            }catch (SQLException e){
                                System.out.println("Insert non réussi.");
                                e.printStackTrace();
                            }
                        } else if ("category".contains(metaData.getTableName(1))) {
                            try {
                                int i = 0;
                                String parametres = "";

                                while (values.next()) {
                                    i++;
                                    int id = values.getInt(1);
                                    String Name = values.getString(2);
                                    Timestamp DateTime = values.getTimestamp(3);

                                    parametres = id + ",'" + Name + "','" + DateTime + "',CURRENT_TIMESTAMP";
                                    String structureTableau = "category_id, name, last_update, category_datecre";
                                    dbPostgre.insererDeDonnees("category", structureTableau, parametres);

                                }
                                //System.out.println("Insertion de la base réussie.");
                            }catch (SQLException e){
                                System.out.println("Insert non réussi.");
                                e.printStackTrace();
                            }

                        } else if ("city".contains(metaData.getTableName(1))) {
                            try {
                                int i = 0;
                                String parametres = "";

                                while (values.next()) {
                                    i++;
                                    int id = values.getInt(1);
                                    String city = values.getString(2);
                                    int city_ID = values.getInt(3);
                                    Timestamp DateTime = values.getTimestamp(4);

                                    parametres = id + ",'" + city + "','" + city_ID + "','" + DateTime + "',CURRENT_TIMESTAMP";
                                    String structureTableau = "city_id, city, contry_id, last_update, city_datecre";
                                    dbPostgre.insererDeDonnees("city", structureTableau, parametres);

                                }

                                //System.out.println("Insertion de la base réussie.");
                            }catch (SQLException e){
                                System.out.println("Insert non réussi.");
                                e.printStackTrace();
                            }
                        }


                    } else if (message instanceof MessageDeCommande) {
                        //On récupère, en string, le string de la commande : ex : 2.actor ou encore 3.city
                        String command = ((MessageDeCommande) message).getMessageDeCommande();
                        if ("FIN".equals(command)) {    //On passe au message de commande FIN : on finit le programme
                            System.out.println("Tâche Réceptrice arrêtée.");
                            stop();
                        } else if (command.contains("2")) { //On passe au message de commande 2 : Visualisation des donénes d'un table
                            //On récupère l'index où le . se situe, et grâce à ça, on peut récupérer le nom de la table à l'aide du substring
                            int dotIndex = command.indexOf('.');
                            String tableName = command.substring(dotIndex + 1);

                            int i = 0;

                            rs = null;
                            //On récupère la table
                            rs = dbPostgre.consulterDonnees("*", tableName);
                            System.out.println("Voici le contenue de la table "+ tableName+" : ");
                            //Ce switch nous permet de passer d'une table à l'autre, pour récupérer les données voulues
                            try{
                                //Selon la table passée, on affiche le contenu de la table
                                switch (tableName){
                                    case "actor":
                                        while(rs.next()){
                                            i++;
                                            int id              = rs.getInt(1);
                                            String FName        = rs.getString(2);
                                            String LName        = rs.getString(3);
                                            Timestamp DateTime  = rs.getTimestamp(4);
                                            Timestamp datecre   = rs.getTimestamp(5);

                                            System.out.println(id+" | "+FName+" | "+LName+" | "+DateTime + " | "+datecre);
                                        }
                                    case "category":
                                        while(rs.next()){
                                            i++;
                                            int id                      = rs.getInt(1);
                                            String Name                 = rs.getString(2);
                                            Timestamp last_update       = rs.getTimestamp(3);
                                            Timestamp datecre           = rs.getTimestamp(4);


                                            System.out.println(id+" | "+Name+" | "+last_update+" | "+datecre);
                                        }

                                    case "city":
                                        while(rs.next()){
                                            i++;
                                            int id                  = rs.getInt(1);
                                            String city             = rs.getString(2);
                                            int contry_id           = rs.getInt(3);
                                            Timestamp last_update   = rs.getTimestamp(4);
                                            Timestamp datecre      = rs.getTimestamp(5);

                                            System.out.println(id+" | "+city+" | "+contry_id+" | "+last_update + " | " + datecre);
                                        }
                                    default:
                                        break;
                                }
                                //On regarde si des données existent, si non, on indique que la table est vide
                                if(i==0)
                                    System.out.println("La table est vide.");
                                else
                                    System.out.println("\nFin du contenue de la table "+ tableName +". Il y a " + i + " ligne(s).");
                            } catch (SQLException e){
                                System.out.println("Une erreur est survenue lors de l'affichage des données !");
                                e.printStackTrace();
                            }
                        } else if (command.contains("3")) {
                            int dotIndex = command.indexOf('.');
                            String tableName = command.substring(dotIndex + 1);

                            dbPostgre.effacerDonnees(tableName);
                            System.out.println("Le contenu de la table "+ tableName +" a été effacé !");
                        }else
                            System.out.println("Commande inconnue.");
                    }
                    verrou.unlock();
                //}
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Tâche Réceptrice interrompue.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        dbPostgre.disconnect();

    }
}

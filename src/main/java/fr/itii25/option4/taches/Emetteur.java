package fr.itii25.option4.taches;

import fr.itii25.option4.designPaternDAO.ImplementerBaseDeDonnes;
import fr.itii25.option4.message.Message;
import fr.itii25.option4.message.MessageDeCommande;
import fr.itii25.option4.message.MessageDeDonnees;

import java.sql.ResultSet;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Emetteur implements Runnable {

    private LinkedBlockingQueue<Message> canalDonnees;
    private boolean exit;

    public Emetteur(LinkedBlockingQueue<Message> canalDeCommunication) {
        this(canalDeCommunication, true);
    }

    //Constructeur complet avec en passage le canal de communication
    // + vérification que le canal de communication existe bien en base
    private Emetteur(LinkedBlockingQueue<Message> canalDeCommunication, boolean exit) {
        if((canalDeCommunication != null)){
            this.canalDonnees = canalDeCommunication;
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
        String nomDriverJDBCMYSQL      = "com.mysql.cj.jdbc.Driver";
        //Ajouts des liens vers les JDBC
        String urlJDBCMYSQL = "jdbc:mysql://localhost:3306/sakila";
        // Connexion à la base de donnees
        ImplementerBaseDeDonnes dbMySQL = new ImplementerBaseDeDonnes(urlJDBCMYSQL,"sakila","p_ssW0rd") {};

        dbMySQL.chargerDriver(nomDriverJDBCMYSQL);
        dbMySQL.connexion();

        String requeteGetAllActor = "SELECT * FROM actor;";
        ResultSet rs = null;

        //Récupération des interactions utilisateurs
        try (Scanner scanner = new Scanner(System.in)) {
            //On donne des options de commande à l'utilisateur :
            System.out.println("1 - Lancez l'acquisition des données");
            System.out.println("2 - Afficher les données de la table de réception");
            System.out.println("3 - Supprimez les données de la table de réception");
            System.out.println("FIN - Terminer le programme");
            System.out.print("Choisissez une option : ");
            while (exit) {
                String input = scanner.nextLine();
                if ("FIN".equalsIgnoreCase(input)) {
                    MessageDeCommande messageToSend = new MessageDeCommande("FIN");
                    canalDonnees.put(messageToSend); // Envoi d'un message de commande
                    stop();
                } else if ("1".equalsIgnoreCase(input)) {
                    try {
                        //Récupération des données de la tables actor et envoie dans le canal de communication
                        rs = dbMySQL.consulterDonnees(requeteGetAllActor);
                        MessageDeDonnees messageToSend = new MessageDeDonnees(rs);
                        canalDonnees.put(messageToSend); // Envoi d'un message de données
                        System.out.println("Message envoyé.");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if ("2".equalsIgnoreCase(input)) {
                    MessageDeCommande messageToSend = new MessageDeCommande("2");
                    canalDonnees.put(messageToSend); // Envoi d'un message de commande
                } else if ("3".equalsIgnoreCase(input)) {
                MessageDeCommande messageToSend = new MessageDeCommande("3");
                canalDonnees.put(messageToSend); // Envoi d'un message de commande
                }
                Thread.sleep(10);
                System.out.println("");
                System.out.println("#######################################################");
                System.out.println("");
                System.out.println("1 - Lancez l'acquisition des données");
                System.out.println("2 - Afficher les données de la table de réception");
                System.out.println("3 - Supprimez les données de la table de réception");
                System.out.println("FIN - Terminer le programme");
                System.out.print("Choisissez une option : ");
            }
        } catch (InterruptedException E){
            Thread.currentThread().interrupt();
            System.out.println("Interruption du Thread en cours");
        }

        dbMySQL.disconnect();

    }
}






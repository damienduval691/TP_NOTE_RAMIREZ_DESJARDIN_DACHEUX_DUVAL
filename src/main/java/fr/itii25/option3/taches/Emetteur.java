package fr.itii25.option3.taches;

import fr.itii25.option3.message.MessageDeCommande;
import fr.itii25.option3.message.MessageDeDonnees;
import fr.itii25.option3.designPaternDAO.ImplementerBaseDeDonnes;
import fr.itii25.option3.message.Message;


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

        dbMySQL.chargerDriver("com.mysql.cj.jdbc.Driver");
        dbMySQL.connexion();
        ResultSet rs = null;
        try (Scanner scanner = new Scanner(System.in)) {
            //On envoie tant que l'utilisateur n'a pas tapé FIN
            System.out.println("Tapez 'FIN' pour terminer le programme :");
            while (exit) {
                String input = scanner.nextLine();
                if ("FIN".equalsIgnoreCase(input)) {
                    MessageDeCommande messageToSend = new MessageDeCommande("FIN");
                    canalDonnees.put(messageToSend);// Envoi d'un message de commande
                    stop();
                } else {
                    rs = dbMySQL.consulterDonnees("SELECT * FROM actor");
                    MessageDeDonnees messageToSend = new MessageDeDonnees(rs);

                    canalDonnees.put(messageToSend);// Envoi d'un message de données
                }
            }
        } catch (InterruptedException E){
            Thread.currentThread().interrupt();
            System.out.println("Interruption du Thread en cours");
        }

        dbMySQL.deconectee();

    }
}






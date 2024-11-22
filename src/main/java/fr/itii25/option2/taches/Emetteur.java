package fr.itii25.option2.taches;

import fr.itii25.option2.message.Message;
import fr.itii25.option2.message.MessageDeCommande;
import fr.itii25.option2.message.MessageDeDonnees;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Emetteur implements Runnable {

    private LinkedBlockingQueue<Message> canalDonnees;
    private boolean exit;

    public Emetteur(LinkedBlockingQueue<Message> canalDeCommunication) {
        this(canalDeCommunication, false);
    }

    //Constructeur complet avec en passage le canal de communication
    // + vérification que le canal de communication existe bien en base
    private Emetteur(LinkedBlockingQueue<Message> canalDeCommunication,  boolean exit) {
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
        exit = true;
        try (Scanner scanner = new Scanner(System.in)) {
            //On envoie tant que l'utilisateur n'a pas tapé FIN
            System.out.println("Entrez votre message (tapez 'FIN' pour terminer) :");
            while (exit) {
                String input = scanner.nextLine();
                if ("FIN".equalsIgnoreCase(input)) {
                    MessageDeCommande messageToSend = new MessageDeCommande("FIN");
                    canalDonnees.put(messageToSend); // Envoi d'un message de commande
                    stop();
                } else {
                    try {
                        //Récupération des données de la tables actor et envoie dans le canal de communication
                        rs = stmtMYSQL.executeQuery(requeteGetAllActor);
                        System.out.println(rs.getString(1));
                        MessageDeDonnees messageToSend = new MessageDeDonnees(rs);
                        canalDonnees.put(messageToSend); // Envoi d'un message de données
                        System.out.println("Message envoyé.");
                    } catch (SQLException e) {
                        System.out.println("Erreur avec executeQuery, impossible de récupérer les données.");
                        e.printStackTrace();
                    }
                }
            }
        } catch (InterruptedException E){
            Thread.currentThread().interrupt();
            System.out.println("Interruption du Thread en cours");
        }
    }
}

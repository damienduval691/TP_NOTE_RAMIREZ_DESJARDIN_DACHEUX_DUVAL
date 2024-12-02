package fr.itii25.option1.taches;

import fr.itii25.option1.message.Message;
import fr.itii25.option1.message.MessageDeCommande;
import fr.itii25.option1.message.MessageDeDonnees;

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
        try (Scanner scanner = new Scanner(System.in)) {
            //On envoie tant que l'utilisateur n'a pas tapé FIN
            System.out.print("Entrez votre message (tapez 'FIN' pour terminer) : ");
            while (exit) {

                String input = scanner.nextLine();
                if ("FIN".equalsIgnoreCase(input)) {
                    MessageDeCommande messageToSend = new MessageDeCommande("FIN");
                    canalDonnees.put(messageToSend); // Envoi d'un message de commande
                    stop();
                } else {
                    MessageDeDonnees messageToSend = new MessageDeDonnees(input);
                    canalDonnees.put(messageToSend); // Envoi d'un message de données
                    Thread.sleep(10);
                    System.out.print("Entrez votre message (tapez 'FIN' pour terminer) : ");
                }


            }
        } catch (InterruptedException E){
            Thread.currentThread().interrupt();
            System.out.println("Interruption du Thread en cours");
        }
    }
}

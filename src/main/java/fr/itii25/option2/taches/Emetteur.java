package fr.itii25.option2.taches;

import fr.itii25.option2.message.Message;
import fr.itii25.option2.message.MessageDeCommande;
import fr.itii25.option2.message.MessageDeDonnees;

import java.awt.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import java.sql.*;

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
        ResultSet resultatSelect = null;
        //Ajouts des drives des JDBC
        String nomDriverJDBCMYSQL      = "com.mysql.cj.jdbc.Driver";

        //Ajouts des liens vers les JDBC
        String urlJDBCMYSQL = "jdbc:mysql://localhost:3306/sakila";

        //Chargement du driver pour la JDBC MYSQL

        try {
            Class.forName(nomDriverJDBCMYSQL);
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur de chargement du driver : nomDriverJDBCMYSQL");
            e.printStackTrace();
        }

        //Connexion à la base de donnée MYSQL
        Connection cxMYSQL = null;
        try {
            cxMYSQL = DriverManager.getConnection(urlJDBCMYSQL,"sakila","p_ssW0rd");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur de connexion : JDBCMYSQL");
            return;
        }

        //Utilisation de l'interface statement
        Statement stmtMYSQL = null;
        try {
            stmtMYSQL = cxMYSQL.createStatement();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création du statement MYSQL");
            e.printStackTrace();
        }

        //Déclaration des objets pour récupérer les données de la table acteur
        String requeteGetAllActor = "SELECT * FROM actor;";
        ResultSet rs = null;
        try {
            //Récupération des données de la tables actor et envoie dans le canal de communication
            rs = stmtMYSQL.executeQuery(requeteGetAllActor);
            MessageDeDonnees messageToSend = new MessageDeDonnees(rs);
            canalDonnees.put(messageToSend); // Envoi d'un message de données
            System.out.println("Message envoyé.");
        } catch (SQLException e) {
            System.out.println("Erreur avec executeQuery, impossible de récupérer les données.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Récupération des interactions utilisateurs
        try (Scanner scanner = new Scanner(System.in)) {
            //On envoie tant que l'utilisateur n'a pas tapé FIN
            System.out.print("Tapez 'FIN' pour terminer le programme :");
            while (exit) {
                String input = scanner.nextLine();
                if ("FIN".equalsIgnoreCase(input)) {
                    MessageDeCommande messageToSend = new MessageDeCommande("FIN");
                    canalDonnees.put(messageToSend); // Envoi d'un message de commande
                    stop();
                }
            }
        } catch (InterruptedException E){
            Thread.currentThread().interrupt();
            System.out.println("Interruption du Thread en cours");
        }
    }
}

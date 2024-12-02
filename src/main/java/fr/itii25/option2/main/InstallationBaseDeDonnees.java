package fr.itii25.option2.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class InstallationBaseDeDonnees {

    public static void main(String[] args) {
        try {
            // Chemin relatif vers le fichier PowerShell
            String scriptPath = "../../install/TelechargementBaseDeDonnees.ps1";

            // Commande pour exécuter le script PowerShell
            String[] command = {
                    "powershell.exe", // Commande pour PowerShell
                    "-NoProfile",    // Exécute sans charger le profil utilisateur
                    "-ExecutionPolicy", "Bypass", // Ignore les restrictions d'exécution
                    "-File", scriptPath // Chemin relatif du script
            };

            // Création du processus
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // Définir le répertoire de travail (racine du projet)
            processBuilder.directory(new File(System.getProperty("user.dir")));

            // Démarrer le processus
            Process process = processBuilder.start();

            // Lire la sortie du script
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                // Sortie standard
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                // Sortie d'erreur
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
            }

            // Attendre la fin de l'exécution
            int exitCode = process.waitFor();
            System.out.println("Processus terminé avec le code : " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

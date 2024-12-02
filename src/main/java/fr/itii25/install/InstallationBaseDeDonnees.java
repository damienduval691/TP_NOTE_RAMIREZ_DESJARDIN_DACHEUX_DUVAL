package fr.itii25.install;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class InstallationBaseDeDonnees {

    public static void main(String[] args) {
        try {
            // Chemin relatif vers le fichier PowerShell
            String os = System.getProperty("os.name").toLowerCase();
            String[] command = {};

            String scriptPath = "";


            if(os.contains("win")){
                scriptPath = System.getProperty("user.dir")
                        + File.separator + "src"
                        + File.separator + "main"
                        + File.separator + "java"
                        + File.separator + "fr"
                        + File.separator + "itii25"
                        + File.separator + "install"
                        + File.separator + "TelechargementBaseDeDonnees.ps1";
                command = new String[]{
                        "powershell.exe", // Commande pour PowerShell
                        "-NoProfile",    // Exécute sans charger le profil utilisateur
                        "-ExecutionPolicy", "Bypass", // Ignore les restrictions d'exécution
                        "-File", scriptPath// Chemin relatif du script
                };
            }
            else  {
                scriptPath = System.getProperty("user.dir")
                        + File.separator + "src"
                        + File.separator + "main"
                        + File.separator + "java"
                        + File.separator + "fr"
                        + File.separator + "itii25"
                        + File.separator + "install"
                        + File.separator + "TelechargementBaseDeDonnees.sh";

                File scriptFile = new File(scriptPath);
                 command = new String[]{
                        "/bin/bash", // Interpréteur Bash
                        scriptFile.getAbsolutePath()
                };

            }
            // Commande pour exécuter le script PowerShell


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

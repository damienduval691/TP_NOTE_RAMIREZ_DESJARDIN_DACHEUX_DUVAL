package fr.itii25.install;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LancementDesConteneursDocker {

    public static void main(String[] args) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String[] command = {};

            String scriptPath = "";

            String chemin = File.separator + "src"
                    + File.separator + "main"
                    + File.separator + "java"
                    + File.separator + "fr"
                    + File.separator + "itii25"
                    + File.separator + "install"
                    + File.separator;


            if(os.contains("win")){
                scriptPath = System.getProperty("user.dir")
                        + chemin
                        + File.separator + "ChargementDesConteneursDockers.ps1";
                command = new String[]{
                        "powershell.exe", // Commande pour PowerShell
                        "-NoProfile",    // Exécute sans charger le profil utilisateur
                        "-ExecutionPolicy", "Bypass", // Ignore les restrictions d'exécution
                        "-File", scriptPath// Chemin relatif du script
                };
            }
            else  {
                scriptPath = System.getProperty("user.dir")
                        + chemin
                        + File.separator + "ChargementDesConteneursDockers.sh";
                File scriptFile = new File(scriptPath);

                // Vérifier si le fichier PowerShell existe
                if (!scriptFile.exists()) {
                    System.err.println("Fichier PowerShell introuvable : " + scriptFile.getAbsolutePath());
                    return;
                }

                command = new String[]{
                        "/bin/bash", // Interpréteur Bash
                        scriptFile.getAbsolutePath()
                };

            }

            // Configuration et exécution du processus
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(System.getProperty("user.dir")));

            Process process = processBuilder.start();

            // Lire les sorties (stdout et stderr)
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
            }

            // Attendre la fin du processus
            int exitCode = process.waitFor();
            System.out.println("Processus terminé avec le code : " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

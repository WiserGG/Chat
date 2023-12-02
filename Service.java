/* classe per la gestione dello storico dei messaggi, ha funzioni che permettono di restituire l'intero storico che verrà poi inviato ai client connessi
 * una funzione che appenderà l'orario al messaggio inviato dal client all'arrivo di quest'ultimo (viene registrato l'orario di arrivo del messaggio al server dunque)
 * una funzione che salva tutti i messaggi in un file di testo non appena vengono inviati da un client
 * questa classe possiede un thread, che si crea non appena va su il server, che PERIODICAMENTE fa una copia dei messaggi 
 * e degli utenti in un file di backup
 * 
 * Informazioni aggiuntive: 
 * - il file originale/backup dello storico e degli username deve essere nella stessa posizione del progetto
*/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.text.DateFormat;

public class Service extends Thread {
    /*
     * assegno a originale e backup un oggetto di tipo file che come parametri ha il percorso dei file "Messaggi.txt" e "backupMessaggi.bak",
     * quest'ultimi verranno utilizzati successivamente per eseguire operazioni su quei file direttamente tramite la classe "Files"
     * i percorsi vengono inseriti come stringhe e poi convertiti in un "abstract pathname" quindi percorso astratto
     */
    static File originale = new File("Messaggi.txt");
    static File backup = new File("backupMessaggi.bak");
    static File userList = new File("userList.txt");

    /*alla creazione dell'oggetto di tipo Service si controlla se il file degli utenti esiste, in caso viene creato
     * e aggiunto automaticamente una riga per garantire che la lettura nel file possa avvenire.
     * La stessa operazione deve avvenire per il file originale dello storico dei messaggi
    */
    public Service() {
        try {
            if(!originale.exists()){
                originale.createNewFile();
                System.out.println("File originale dello storico dei messaggi creato");
            }
            else System.out.println("File originale dello storico dei messaggi già esistente");

            if(!userList.exists()){
                userList.createNewFile();
                System.out.println("File userList creata");
                PrintWriter fOUT = new PrintWriter(new FileWriter(Service.userList));
                fOUT.println(new utente("admin", "admin"));
                fOUT.close();
            }
            else System.out.println("File userList già esistente");

            start();
        } catch (IOException e) { System.out.println(e); }
    }

    //thread che gestisce il backup periodico in maniera automatica
    @Override
    public void run() {
        //ogni 20000 ms = 20 s chiama la funzione per effettuare il backup
        try {
            sleep(20000);
            Backup();
        } catch (InterruptedException e) { System.out.println(e); }
    }

    public static void Backup() {
        try {
            Files.copy(originale.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File di backup creato");
        } catch (IOException e) { System.out.println("Errore nel backup del file: "+ e); }
    }

    public static void Ripristina() {
        try {
            Files.copy(backup.toPath(), originale.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File d'origine ripristinato");
        } catch (IOException e) { System.out.println("Errore nel ripristino del file: "+e); }
    }

    public static void ScriviMessaggio(String messaggio) {
        try {
            PrintWriter fOUT = new PrintWriter(new FileWriter(Service.userList, true), true);
            Date date = new Date(DateFormat.SHORT);
            fOUT.println(date+" "+messaggio);
            fOUT.close();
        } catch (Exception e) { System.out.println("Errore nella scrittura del messaggio nel file: "+e); }
    }
    
    public static void AggionrnaUserList(String utente){
        try {
            PrintWriter fOUT = new PrintWriter(new FileWriter(userList, true), true);
            //inseriamo l'utente alla fine della lista
            fOUT.append(utente);
            fOUT.close();
        } catch (IOException e) { System.out.println(e); }
    }
}
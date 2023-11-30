/* classe per la gestione dello storico dei messaggi, ha funzioni che permettono di restituire l'intero storico che verrà poi inviato ai client connessi
 * una funzione che appenderà l'orario al messaggio inviato dal client all'arrivo di quest'ultimo (viene registrato l'orario di arrivo del messaggio al server dunque)
 * una funzione che salva tutti i messaggi in un file di testo non appena vengono inviati da un client
 * questa classe possiede un thread, che si crea non appena va su il server, che PERIODICAMENTE fa una copia dei messaggi in un file di backup
 * 
 * Informazioni aggiuntive: 
 * - il file originale/backup dello storico e degli username deve essere nella stessa posizione del progetto
*/

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class chatService {
    /*
     * assegno a originale e backup un oggetto di tipo file che come parametri ha il percorso dei file "Messaggi.txt" e "backupMessaggi.bak",
     * quest'ultimi verranno utilizzati successivamente per eseguire operazioni su quei file direttamente tramite la classe "Files"
     * i percorsi vengono inseriti come stringhe e poi convertiti in un "abstract pathname" quindi percorso astratto
     */
    static File originale;
    static File backup;

    public chatService(String originale, String backup) {
        
    }

    public void Backup() {
        try {
            Files.copy(originale.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File di backup creato");
        } catch (IOException e) { System.out.println("Errore nel backup del file: "+ e); }
    }

    public void Ripristina() {
        try {
            Files.copy(backup.toPath(), originale.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File d'origine ripristinato");
        } catch (IOException e) { System.out.println("Errore nel ripristino del file: "+e); }
    }

    public void ScriviMessaggio() {
        try {

        } catch (Exception e) { System.out.println("Errore nel ripristino del file: "+e); }
    }
}

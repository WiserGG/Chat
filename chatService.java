/* classe per la gestione dello storico dei messaggi, ha funzioni che permettono di restituire l'intero storico che verrà poi inviato ai client connessi
 * una funzione che appenderà l'orario al messaggio inviato dal client all'arrivo di quest'ultimo (viene registrato l'orario di arrivo del messaggio al server dunque)
 * una funzione che salva tutti i messaggi in un file di testo non appena vengono inviati da un client
 * questa classe possiede un thread, che si crea non appena va su il server, che PERIODICAMENTE fa una copia dei messaggi in un file di backup
 * 
 * Informazioni aggiuntive: 
 * - il file originale/backup dello storico e degli username deve essere nella stessa posizione del progetto
*/

public class chatService {
    
}

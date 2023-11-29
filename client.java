import java.io.*;
import java.net.*;
import java.util.Scanner;

/* il client si connette al server, il server chiede di inserire un nome utente, il client lo inserisce e lo manda indietro,
 * il server legge il nome utente e lo confronta con una lista di nomi utente che ha salvata in un file di teso, se dispononibile (
 * utilizzare un vettore può aiutare poichè ha il metodo contains()  ) lo salva e comunica che il nome utente è disponibile al cliente.
 * dopo questa operazione necessaria per tener traccia dei messaggi il server spedisce al cliente tutto lo storico dei messaggi 
*/

public class client extends Thread {
    static BufferedReader in;
    static PrintWriter out;
    //Codici per la registrazione 0 - 9

    //ODR = Operazione di Registrazione
    static final int ODR = 0;

    //UND = Nome Non Disponibile --> cerca di fare la registrazione ma l'username esiste già
    static final int NND = 1;

    //UGE = Utente Già Esistente --> cerca di fare la registrazione ma risulta che quell'utente è già registrato --> accesso automatico con quell
    static final int UGE = 2;


    //Codici per l'accesso 10 - 20

    //ODA = Operazione di Accesso
    static final int ODA = 10;

    //UNT = Utente Non Trovato --> prova a fare l'accesso ma non risulta nessun utente con le credenziali inserite --> deve registrarsi o riprovare 
    static final int UNT = 11;

    //PNV = Password Non Valida --> prova a fare l'accesso, l'username esiste ma non la password --> deve inserire nuovamente la password
    static final int PNV = 12;
    
    //Codice per procedere

    //PRI = Prosegui --> prosegue nelle varie operazioni
    static final int PRG = 69;

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(InetAddress.getByName("zuzu.sytes.net"), 3000));
        System.out.println("Client Socket: "+ socket);

        //creazione stream di input e output 
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter((new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))), true);
        
        //primo messaggio del client che da conferma sull'apertura della connessione
        System.out.print(in.readLine());
        
        Scanner sc = new Scanner(System.in);

        String rispostaDefault;

        //accedi(1) o registrati(2), se credenziali non corrette invio codice dal server al client, mostra il messaggio di errore (rimane su accesso, decidere carattere per tornare alla sceolta accesso/registrazione)
        //schermata grafica pulsante accedi/registrati, se non riesce ad accedere messaggio errore, possibilità di riprovare senza tornare alla schermata di scelta, con pulsante per tornare indietro se hanno sbagliato 
        while (true) {
            String username = sc.next();
            out.println(username);
            
            
            int codice = Integer.parseInt(in.readLine());
            //se il codice ricevuto dal server è uguale UGE mostrare "Username non disponibile" altrimenti break
            if(codice == UGE){
                        System.out.print("Username non disponibile");
            }
            else break;
            
        }
    }

}

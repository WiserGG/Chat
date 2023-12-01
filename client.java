import java.io.*;
import java.net.*;
import java.util.Scanner;

/* il client si connette al server, il client a seconda dell'operazione che vuole effettuare manda un codice STANDARD al server,
 * quest'ultimo a seconda di cosa riceva elabora la risposta e ne restituisce un altro a sua volta che verrà poi gestito lato client
 * per alleggerire il carico sul server.
 * 
 * --------------------------------------------
 * Il server effettua i controlli sul nome utente e la password che è stata inviata dal client confrontandoli con una lista di nomi utente e password presenti in un file di testo.
 * --------------------------------------------
 * L'univocità di un utente è determinata dal fatto che nessun utente può avere il medesimo username o la medesima password (controllo rimuovibile).
 * --------------------------------------------
 * Ogni operazione che viene richiesta dal client viene elaborata e DEVE obbligatoriamente restituire un codice di STATO dell'operazione.
 * --------------------------------------------
 * Lo storico dei messaggi può essere restituito solo e SOLAMENTE SE l'operazione di registrazione o accesso sono TERMINATE
 * --------------------------------------------
 * Il backup dei messaggi viene effettuato ogni 1 minuto (può variare il tempo oppure a seconda dell'utilizzo della CPU)
 * --------------------------------------------
 * I messaggi inviati dal client verranno ritrasmessi a tutti gli utenti collegati ESCLUSO il mittente.
*/


public class client extends Thread {
    static BufferedReader in;
    static PrintWriter out;
    //Codici per la registrazione 0 - 9

    //ODR = Operazione di Registrazione
    static final int ODR = 0;

    //UND = Nome Non Disponibile --> cerca di fare la registrazione ma l'username esiste già
    static final int NND = 1;


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
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 3000));
        System.out.println("Client Socket: "+ socket);

        //creazione stream di input e output 
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
        
        //primo messaggio del client che da conferma sull'apertura della connessione
        System.out.print(in.readLine());
        
        Scanner sc = new Scanner(System.in);

        String rispostaDefault;
        //accedi(1) o registrati(2), se credenziali non corrette invio codice dal server al client, mostra il messaggio di errore (rimane su accesso, decidere carattere per tornare alla sceolta accesso/registrazione)
        //schermata grafica pulsante accedi/registrati, se non riesce ad accedere messaggio errore, possibilità di riprovare senza tornare alla schermata di scelta, con pulsante per tornare indietro se hanno sbagliato 
        while (true) {
            
            //valore scelta accesso(1) o registrazione(2)
            System.out.println("1) Accesso\t2) Registrazione");
            int ar = sc.nextInt();
            String username;
            String password;
            switch (ar) {
                case 1:
                    Acc();
                    break;
                case 2:
                    
                    Reg();
                default:
                    break;
            }
        }
    }

    private static void Acc() {
        Scanner input=new Scanner(System.in);
        String username = input.next();
        String password = input.next();
    }

    private static void Reg() {
        Scanner input=new Scanner(System.in);
        System.out.print("Inserisci un username: ");
        String username = input.nextLine();
        out.println(ODR);
        out.println(username);
        try {
            if(Integer.parseInt(in.readLine()) == NND){
                System.out.println("Username non disponibile.");
                Reg();
            }
            else{
                String password = input.next();
                out.println(password);
                
            }
        } catch (NumberFormatException e) {System.out.println(e);
        }catch (IOException e) {System.out.println(e);}
        String password = input.next();

    }   
}

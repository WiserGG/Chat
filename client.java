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

    //Codice per procedere

    //PRI = Prosegui --> prosegue nelle varie operazioni
    static final int PRG = 69;

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket();
                                            //InetAddress.getByName("zuzu.sytes.net")
                                            //InetAddress.getLocalHost()
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 3000));
        System.out.println("Client Socket: "+ socket);

        //creazione stream di input e output 
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
        
        Scanner sc = new Scanner(System.in);

        //accedi(1) o registrati(2), se credenziali non corrette invio codice dal server al client, mostra il messaggio di errore (rimane su accesso, decidere carattere per tornare alla sceolta accesso/registrazione)
        //schermata grafica pulsante accedi/registrati, se non riesce ad accedere messaggio errore, possibilità di riprovare senza tornare alla schermata di scelta, con pulsante per tornare indietro se hanno sbagliato 
        
        //valore scelta accesso(1) o registrazione(2)
        System.out.println("1) Accesso\t2) Registrazione");
        int ar = sc.nextInt();
        switch (ar) {
            case 1:
                Acc();
                break;
            case 2:
                Reg();
                System.out.println("Registrazione effettuata.");
                break;
            default:
                break;
        }
        //accesso o registrazione conclusa, preleviamo lo storico dei messaggi
        new client();

    }


    public client() {
        start();
    }

    //il thread è in costante lettura di messaggi in arrivo dal server
    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(in.readLine());
            } catch (IOException e) { System.out.println("Errore nella lettura del messaggio: "+e); }
        }
    }


    private static void Acc() {
        int codice = 0;
        out.println(ODA);
        Scanner input=new Scanner(System.in);
        System.out.print("Inserire username:");
        String username = input.nextLine();
        out.println(username);
        System.out.print("Inserire password:");
        String password = input.nextLine();
        out.println(password);
        try {
            while (true) {
                codice = Integer.parseInt(in.readLine());
                System.out.println(codice); 
                if(codice == UNT){
                    System.out.println("Utente o Password errata.");
                    System.out.println("1) Registrazione\t2) Riprova");
                    int ar = input.nextInt();
                    switch (ar) {
                        case 1:
                            out.println(ODR);
                            Reg();
                            System.out.println("Registrazione effettuata.");
                            break;
                        case 2:
                            Acc();
                            break;
                        default:
                            break;
                    }
                }
                else System.out.println("Accesso effettuato");
            }
        } 
        catch (NumberFormatException e) {System.out.println(e); } 
        catch (IOException e) {System.out.println(e);}
    }

    private static void Reg() {
        Scanner input=new Scanner(System.in);
        System.out.print("Inserisci un username: ");
        String username = input.nextLine();
        out.println(username);
        try {
            if(Integer.parseInt(in.readLine()) == NND){
                System.out.println("Username non disponibile.");
                Reg();
            }
            else{
                System.out.print("Crea password: ");
                String password = input.nextLine();
                out.println(password);
            }
        } 
        catch (NumberFormatException e) {System.out.println(e); }
        catch (IOException e) {System.out.println(e);}
    }   
}

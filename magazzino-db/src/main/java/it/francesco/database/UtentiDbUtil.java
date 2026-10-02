package it.francesco.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import it.francesco.models.Utente;

public class UtentiDbUtil extends DbUtil {

    // costruttore della classe con i parametri del db per connettersi
    public UtentiDbUtil(String host, String dbName, String userName, String passWord, int portNumber) {
        super(host, dbName, userName, passWord, portNumber);
    }

    // operazioni CRUD
    // metodo publico per salvare utenti nel db
    public Utente salvaUtenti(Utente utente) {
        try {
            // Definisce una query SQL per inserire un nuovo record nella tabella Utenti
            // ????? sono degli segnaposto, placeholder, per evitare sql injection
            String querySalvaUtente = "insert into utenti(chiaveRicerca,cognome,nome, username, password) values(?,?,?,?,?)";
            // PreparedStatement-> prepara lo statement, crea una variabile da eseguire dopo
            // SQL che coinvolgono dati dinamici
            PreparedStatement statementSalvaUtente = connection.prepareStatement(querySalvaUtente);
            // Imposta i valori dei primi quattro segnaposto della query con i dati estratti
            // dall’oggetto utente, Fornisce i dati effettivi da inserire nel database,
            // setString-> escaping di caratteri speciali
            // parameterIndex->posizione valore nei punti di domanda
            statementSalvaUtente.setString(1, utente.getChiaveRicerca());
            statementSalvaUtente.setString(2, utente.getCognome());
            statementSalvaUtente.setString(3, utente.getNome());
            statementSalvaUtente.setString(4, utente.getUsername());
            // con questo modifichi la password in crittografia
            // MessageDigest.getInstance("MD5") inizializza l’algoritmo MD5.
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.update carica la password come array di byte.
            md.update(utente.getPassword().getBytes());
            // md.digest genera l’hash come array di byte.
            byte[] digest = md.digest();
            // classe x costruire stringhe, per ogni byte utilizza il for
            // Imposta il quinto segnaposto della query con l’hash della password (un array
            // di byte),Associa la password crittografata alla query per l’inserimento
            statementSalvaUtente.setBytes(5, digest);
            // Esegue la query di inserimento e restituisce il numero di righe modificate,
            // Effettua l’inserimento effettivo nel database.
            int risultato = statementSalvaUtente.executeUpdate();
            // Controlla se l’inserimento ha avuto successo (cioè se almeno una riga è stata
            // modificata), Verifica che l’operazione sia andata a buon fine prima di
            // procedere
            if (risultato > 0) {
                // Definisce una query per selezionare l’utente appena inserito usando
                // l’username,Prepara un PreparedStatement e imposta l’username come parametro.
                // Esegue la query e restituisce un ResultSet con i risultati.
                // Serve per recuperare l’ID generato automaticamente dal database per il nuovo
                // utente
                String queryCercaUtente = "select * from utenti where username = ?";
                PreparedStatement statementCercaUtente = connection.prepareStatement(queryCercaUtente);
                statementCercaUtente.setString(1, utente.getUsername());
                ResultSet resultQuery = statementCercaUtente.executeQuery();
                // Se il ResultSet contiene un risultato (cioè l’utente è stato trovato):
                // Recupera l’ID dal campo IdUtente. Imposta l’ID sull’oggetto utente.
                if (resultQuery.next()) {
                    int idUtente = resultQuery.getInt("IdUtente");
                    utente.setIdUtente(idUtente);
                }

            }
            statementSalvaUtente.close();
            // Gestisce qualsiasi eccezione generica, stampando la traccia dello stack
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Restituisce l’oggetto utente, che è stato aggiornato con l’ID.
        return utente;

    }

    // Definisce un metodo pubblico che recupera un utente dal database in base al
    // suo ID
    public Utente recuperaUtentiConIdUtente(int idUtente) {
        // Crea un nuovo oggetto Utenti vuoto, Serve come contenitore per i dati che
        // verranno recuperati dal database
        Utente utente = new Utente();
        try {
            // Definisce una query SQL per selezionare un utente in base all’ID, il
            // segnaposto ? funge da "variabile" che verrà sostituita con il valore
            // effettivo tramite un PreparedStatement
            String queryCercaUtente = "select * from utenti where idUtente = ?";
            // Prepara un PreparedStatement con la query, Imposta l’ID come parametro,
            // Esegue la query e restituisce un ResultSet,Esegue la query per ottenere i
            // dati dell’utente
            PreparedStatement statementCercaUtente = connection.prepareStatement(queryCercaUtente);
            // inietto parametri
            statementCercaUtente.setInt(1, idUtente);
            // esegui query nel db
            ResultSet resultQuery = statementCercaUtente.executeQuery();
            if (resultQuery.next()) {
                // Se il ResultSet contiene un risultato,Recupera i valori delle colonne nome,
                // cognome, chiaveRicerca e userName, Imposta questi valori sull’oggetto utente,
                // insieme all’ID passato come parametro, Popola l’oggetto utente con i dati
                // estratti dal database
                String nome = resultQuery.getString("nome");
                String cognome = resultQuery.getString("cognome");
                String chiaveRicerca = resultQuery.getString("chiaveRicerca");
                String userName = resultQuery.getString("userName");
                utente.setChiaveRicerca(chiaveRicerca);
                utente.setCognome(cognome);
                utente.setIdUtente(idUtente);
                utente.setNome(nome);
                utente.setUsername(userName);

            }
            statementCercaUtente.close();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return utente;

    }

    // Definisce un metodo pubblico chiamato cercaUtenti che accetta un parametro
    // chiaveRicerca di tipo String e restituisce un ArrayList di oggetti di tipo
    // Utente
    public ArrayList<Utente> cercaUtentiConChiaveRicerca(String chiaveRicerca) {
        // Crea un nuovo oggetto ArrayList di tipo Utente chiamato utenti,Serve come
        // contenitore per memorizzare gli oggetti Utente che verranno recuperati dal
        // database in base alla query
        ArrayList<Utente> utenti = new ArrayList<>();
        try {
            // Preparare la query per cercare utenti in base a una corrispondenza parziale
            // della chiave di ricerca.
            String queryCercaUtente = "select * from utenti where chiaveRicerca like ?";
            // Preparare l'esecuzione della query SQL in modo che possa essere
            // parametrizzata con il valore di chiaveRicerca
            PreparedStatement statementCercaUtente = connection.prepareStatement(queryCercaUtente);
            // inietto parametri nel placeholder
            statementCercaUtente.setString(1, "%" + chiaveRicerca + "%");
            // Esegue la query SQL preparata e restituisce i risultati in un oggetto
            // ResultSet, che contiene le righe restituite dal database.
            ResultSet resultQuery = statementCercaUtente.executeQuery();
            // Itera attraverso le righe del ResultSet utilizzando il metodo next(), che
            // sposta il cursore alla riga successiva e restituisce true se c'è una riga
            // valida
            while (resultQuery.next()) {
                // Preparare un contenitore per i dati di un singolo utente prima di popolare i
                // suoi attributi
                Utente utente = new Utente();
                // Popola l’oggetto utente con i dati estratti dal database get per prenderli e
                // set per impostarli nell'oggetto Utente
                String nome = resultQuery.getString("nome");
                String cognome = resultQuery.getString("cognome");
                String chiaveRicercaRisultato = resultQuery.getString("chiaveRicerca");
                String userName = resultQuery.getString("userName");
                int idUtente = resultQuery.getInt("idUtente");
                utente.setChiaveRicerca(chiaveRicercaRisultato);
                utente.setCognome(cognome);
                utente.setIdUtente(idUtente);
                utente.setNome(nome);
                utente.setUsername(userName);
                utenti.add(utente);
            }

            statementCercaUtente.close(); 

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    public Utente modificaUtente(Utente utente) {

        try {
            // creazione query x db
            String queryModificaUtente = utente.getPassword() != null
                    // espressione ternarie
                    ? " update utenti set chiaveRicerca = ?, cognome = ?, nome = ?, username = ?, password = ? where idUtente = ?"
                    : " update utenti set chiaveRicerca = ?, cognome = ?, nome = ?, username = ? where idUtente = ?";

            // Prepariamo un PreparedStatement per eseguire la query in modo sicuro.
            PreparedStatement statementModificaUtente = connection.prepareStatement(queryModificaUtente);
            // Impostazione dei parametri della query
            statementModificaUtente.setString(1, utente.getChiaveRicerca());
            statementModificaUtente.setString(2, utente.getCognome());
            statementModificaUtente.setString(3, utente.getNome());
            statementModificaUtente.setString(4, utente.getUsername());
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (utente.getPassword() != null) {
                md.update(utente.getPassword().getBytes());
                byte[] digest = md.digest();
                statementModificaUtente.setBytes(5, digest);
                statementModificaUtente.setInt(6, utente.getIdUtente());
            }
            statementModificaUtente.setInt(5, utente.getIdUtente());
            int risultato = statementModificaUtente.executeUpdate();
            if (risultato == 0) {
                System.out.println("idUtente non trovato");
            }
            statementModificaUtente.close();

        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return utente;
    }

    public boolean cancellaUtente(int idUtente) {

        try {
            String queryCancellaUtente = "delete from utenti where idUtente = ?";
            PreparedStatement statementCancellaUtente = connection.prepareStatement(queryCancellaUtente);
            statementCancellaUtente.setInt(1, idUtente);
            int risultato = statementCancellaUtente.executeUpdate();
            if (risultato == 0) {
                throw new SQLException("Nessun utente eliminato, idUtente non trovato: " + idUtente);
            }

            return true; // qua non mi chiude statement= anche in cancella articolo---------------

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }

    }

}

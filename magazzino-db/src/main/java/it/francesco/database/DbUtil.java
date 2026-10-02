package it.francesco.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    // variabile private che rappresenta la connessione al db
    protected Connection connection;

    // costruttore della classe con i parametri del db per connettersi
    public DbUtil(String host, String dbName, String userName, String passWord, int portNumber) {
        // L'URL è necessario per specificare al DriverManager dove si trova il database
        // e a quale database connettersi
        // così si puo cambiare piu facilmente i parametri se cambiano
        String url = "jdbc:mysql://" + host + ":" + portNumber + "/" + dbName;
        // blocco try per gestire eventuali eccezzioni durante la connessione al
        // database e per non fare terminare il programma
        try {
            // Carica il driver JDBC per MySQL. Il driver è una libreria necessaria per
            // comunicare con il database MySQL.
            // La stringa "com.mysql.cj.jdbc.Driver" è il nome della classe del driver
            // Class.forName non cambia
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Usa la classe DriverManager per stabilire una connessione al database
            // utilizzando l'URL, il nome utente e la password forniti.
            // Il risultato viene assegnato alla variabile connection
            connection = DriverManager.getConnection(url, userName, passWord);

            // Controlla se la connessione è stata stabilita con successo se si stampa
            // connected
            if (connection != null) {
                System.out.println("Connected to the database!");

            }
            // eccezione driver db non trovato
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver not found.");
            e.printStackTrace();
            // eccezione se c'è un problema con la connessione al database (es. URL errato,
            // credenziali sbagliate)
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}

package it.francesco.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.francesco.models.Articolo;
import it.francesco.models.Locazioni;

public class LocazioniDbUtil extends DbUtil {

    // costruttore della classe con i parametri del db per connettersi
    public LocazioniDbUtil(String host, String dbName, String userName, String passWord, int portNumber) {
        super(host, dbName, userName, passWord, portNumber);

    }

    public Locazioni salvaLocazione(Locazioni locazioni) {

        String querySalvaLocazioni = "insert into locazioni (corsia, colonna, ripiano) values (?, ?, ?)";

        try (PreparedStatement statementsalvaLocazione = connection.prepareStatement(querySalvaLocazioni);) {

            statementsalvaLocazione.setString(1, locazioni.getCorsia());
            statementsalvaLocazione.setString(2, locazioni.getColonna());
            statementsalvaLocazione.setString(3, locazioni.getRipiano());

            int risultato = statementsalvaLocazione.executeUpdate();
            if (risultato > 0) {

            }

        } catch (Exception e) {

        }
        return locazioni;

    }

    public Locazioni recuperLocazioneConId(int idLocazione) {

        // Query SQL per selezionare i campi dalla tabella locazioni
        String queryRecuperaLocazione = "select * from locazioni where idLocazione = ?";
        try (PreparedStatement statementRecuperaLocazione = connection.prepareStatement(queryRecuperaLocazione)) {
            statementRecuperaLocazione.setInt(1, idLocazione);
            // domanda su Resultset-- perchè mi da un array?
            // ------------------------------------
            try (ResultSet resultQuery = statementRecuperaLocazione.executeQuery();) {
                // Se c'è un risultato, creo e restituisco un oggetto Locazioni
                if (resultQuery.next()) {
                    String corsia = resultQuery.getString("corsia");
                    String colonna = resultQuery.getString("colonna");
                    String ripiano = resultQuery.getString("ripiano");
                    return new Locazioni(idLocazione, corsia, colonna, ripiano);
                } else {
                    // Nessuna locazione trovata, restituisco null
                    return null;
                }
            }
        } catch (SQLException e) {
            // Gestisco l'eccezione stampando lo stack trace
            e.printStackTrace();
            return null;
        }
    }

    public Locazioni modificaLocazioneConId(Locazioni locazioni) {

        String queryModificaLocazione = "update locazioni set corsia = ?, colonna = ?, ripiano = ? where idLocazione = ?";

        try (PreparedStatement statementModificaLocazione = connection.prepareStatement(queryModificaLocazione);) {
            // Imposta i parametri della query
            statementModificaLocazione.setString(1, locazioni.getCorsia()); // non mi dava con la l minuscola ---
            statementModificaLocazione.setString(2, locazioni.getColonna());
            statementModificaLocazione.setString(3, locazioni.getRipiano());
            statementModificaLocazione.setInt(4, locazioni.getIdLocazione());

            // Esegue la query
            int risultato = statementModificaLocazione.executeUpdate();

            // Verifica se l'aggiornamento ha avuto successo
            if (risultato > 0) {
                return locazioni; // Restituisce l'oggetto locazioni aggiornato
            } else {
                System.out.println(
                        "Nessuna locazione modificata, idLocazione non trovato: " + locazioni.getIdLocazione());
                return null; // Nessuna riga aggiornata (idLocazione non esiste)
            }
        } catch (SQLException e) {
            // Gestisce le eccezioni SQL
            e.printStackTrace();
            return null; // Errore durante l'aggiornamento
        }
    }

    public boolean cancellaLocazioniConId(int idLocazione) {

        String queryCancellaLocazione = "DELETE FROM locazioni WHERE idLocazione = ?";

        try (PreparedStatement statementCancellaLocazione = connection.prepareStatement(queryCancellaLocazione)) {
            // Imposta il parametro idLocazione nella query
            statementCancellaLocazione.setInt(1, idLocazione);

            // Esegue la query di eliminazione e ottiene il numero di righe eliminate
            int risultato = statementCancellaLocazione.executeUpdate();

            // Verifica se l'eliminazione ha avuto successo
            if (risultato > 0) {
                return true;
            } else {
                System.out.println("Nessuna locazione eliminata, idLocazione non trovato: " + idLocazione);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Errore SQL durante l'eliminazione della locazione: " + e.getMessage());
            return false;
        }
    }

}

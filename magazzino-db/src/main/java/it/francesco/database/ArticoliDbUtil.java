package it.francesco.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.francesco.models.Articolo;
import it.francesco.exceptions.DataLayerException;

public class ArticoliDbUtil extends DbUtil {

    // costruttore della classe con i parametri del db per connettersi
    public ArticoliDbUtil(String host, String dbName, String userName, String passWord, int portNumber) {
        super(host, dbName, userName, passWord, portNumber);
    }

    public Articolo salvaArticolo(Articolo articolo) throws DataLayerException {
        String query = "insert into articoli (descrizione, numPezziMax, barcode) values(?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, articolo.getDescrizione());
            statement.setInt(2, articolo.getNumPezziMax());
            statement.setString(3, articolo.getBarcode());
            statement.executeUpdate();

            try (ResultSet chiaviGenerate = statement.getGeneratedKeys()) {
                if (chiaviGenerate.next()) {
                    articolo.setIdArticolo(chiaviGenerate.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataLayerException();
        }
        return articolo;
    }

    public Articolo recuperaArticoloConId(int idArticolo) {

        Articolo articolo = new Articolo();
        String queryRecuperaArticolo = "select * from articoli where idArticolo = ?";
        try (PreparedStatement statementRecuperaArticolo = connection.prepareStatement(queryRecuperaArticolo);) {

            statementRecuperaArticolo.setInt(1, idArticolo);
            ResultSet resultQuery = statementRecuperaArticolo.executeQuery();
            if (resultQuery.next()) {
                // ripassa sto pezzo di codice
                int id = resultQuery.getInt("idArticolo");
                String descrizione = resultQuery.getString("descrizione");
                int numPezziMax = resultQuery.getInt("numPezziMax");
                String barcode = resultQuery.getString("barcode");
                articolo.setIdArticolo(id);
                articolo.setDescrizione(descrizione);
                articolo.setNumPezziMax(numPezziMax);
                articolo.setBarcode(barcode);

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return articolo;

    }

    public Articolo recuperaArticoloConBarcode(String barcode) {

        Articolo articolo = new Articolo();
        String queryrecuperaArticoloConBarcode = "select * from articoli where barcode = ?";
        try (PreparedStatement statementRecuperaArticoloConBarcode = connection
                .prepareStatement(queryrecuperaArticoloConBarcode);) {

            statementRecuperaArticoloConBarcode.setString(1, barcode);
            ResultSet resultQuery = statementRecuperaArticoloConBarcode.executeQuery();
            if (resultQuery.next()) {
                // ripassa sto pezzo di codice
                int id = resultQuery.getInt("idArticolo");
                String descrizione = resultQuery.getString("descrizione");
                int numPezziMax = resultQuery.getInt("numPezziMax");
                articolo.setIdArticolo(id);
                articolo.setDescrizione(descrizione);
                articolo.setNumPezziMax(numPezziMax);
                articolo.setBarcode(barcode);

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return articolo;

    }

    public Articolo modificaArticolo(Articolo articolo) {

        String queryModificaArticolo = "update articoli set descrizione = ?, numPezziMax = ? where idArticolo = ? ";

        try (PreparedStatement statementModificaArticolo = connection.prepareStatement(queryModificaArticolo)) {
            statementModificaArticolo.setString(1, articolo.getDescrizione());
            statementModificaArticolo.setInt(2, articolo.getNumPezziMax());
            statementModificaArticolo.setInt(3, articolo.getIdArticolo());

            int risultato = statementModificaArticolo.executeUpdate();

            // Controllo se l'aggiornamento ha avuto successo
            if (risultato == 0) {
                System.out.println("idArticolo non trovato");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return articolo;

    }

    public boolean cancellaArticoloConId(int idArticolo) {

        String querycancellaArticolo = "delete from articoli where idArticolo = ?";

        try (PreparedStatement statementcancellaArticolo = connection.prepareStatement(querycancellaArticolo);) {

            // Imposta il parametro idArticolo nella query
            statementcancellaArticolo.setInt(1, idArticolo);
            // Esegue la query di eliminazione e ottiene il numero di righe eliminate
            int risultato = statementcancellaArticolo.executeUpdate();
            // Verifica se l'eliminazione ha avuto successo
            if (risultato > 0) {
                return true;
            } else {
                System.out.println("Nessun articolo eliminato, idArticolo non trovato: " + idArticolo);
                return false;
            }

        } catch (SQLException e) {
            // Gestisce le eccezioni SQL stampando lo stack trace
            e.printStackTrace();
            return false;
        }

    }

    public boolean cancellaArticoloConBarcode(String barcode) {

        String querycancellaArticolo = "delete from articoli where barcode = ?";

        try (PreparedStatement statementcancellaArticolo = connection.prepareStatement(querycancellaArticolo);) {

            // Imposta il parametro idArticolo nella query
            statementcancellaArticolo.setString(1, barcode);
            // Esegue la query di eliminazione e ottiene il numero di righe eliminate
            int risultato = statementcancellaArticolo.executeUpdate();
            // Verifica se l'eliminazione ha avuto successo
            if (risultato > 0) {
                return true;
            } else {
                System.out.println("Nessun articolo eliminato, idArticolo non trovato: " + barcode);
                return false;
            }

        } catch (SQLException e) {
            // Gestisce le eccezioni SQL stampando lo stack trace
            e.printStackTrace();
            return false;
        }

    }

}

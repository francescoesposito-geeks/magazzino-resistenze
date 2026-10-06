package it.francesco.database;

import java.beans.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import it.francesco.exceptions.DataLayerException;
import it.francesco.models.MissioniE;
import it.francesco.models.MissioniU;
import it.francesco.models.Movimento;

public class MovimentoDbUtil extends DbUtil {

    public MovimentoDbUtil(String host, String dbName, String userName, String passWord, int portNumber) {
        super(host, dbName, userName, passWord, portNumber);

    }

    public synchronized Movimento salvaMovimento(Movimento movimento) throws DataLayerException {
        String querySalvaMovimento = "INSERT INTO movimenti (confermatoE, confermatoU, dataOraE, dataOraU, idArticolo, idLocazione, codiceInterno) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statementSalvaMovimento = connection.prepareStatement(querySalvaMovimento,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            movimento.setCodiceInterno(creaCodiceInterno());
            // Imposta i parametri della query
            statementSalvaMovimento.setBoolean(1, movimento.isConfermatoE());
            statementSalvaMovimento.setBoolean(2, movimento.isConfermatoU());
            statementSalvaMovimento.setObject(3, movimento.getDataOraE());
            statementSalvaMovimento.setObject(4, movimento.getDataOraU());
            statementSalvaMovimento.setInt(5, movimento.getIdArticolo());
            statementSalvaMovimento.setInt(6, movimento.getIdLocazione());
            statementSalvaMovimento.setInt(7, movimento.getCodiceInterno());

            // Esegue l'inserimento
            int risultato = statementSalvaMovimento.executeUpdate();

            if (risultato > 0) {
                // Recupera l'idMovimento generato
                try (ResultSet generatedKeys = statementSalvaMovimento.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idMovimento = generatedKeys.getInt(1);
                        movimento.setIdMovimento(idMovimento);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataLayerException();
        }

        return movimento;
    }

    public Movimento recuperaMovimentoConId(int idMovimento) throws DataLayerException {
        String queryRecuperaMovimento = "SELECT * FROM movimenti WHERE idMovimento = ?";
        try (PreparedStatement statementRecuperaMovimento = connection.prepareStatement(queryRecuperaMovimento)) {
            statementRecuperaMovimento.setInt(1, idMovimento);
            try (ResultSet resultQuery = statementRecuperaMovimento.executeQuery()) {
                if (resultQuery.next()) {
                    boolean confermatoE = resultQuery.getBoolean("confermatoE");
                    boolean confermatoU = resultQuery.getBoolean("confermatoU");
                    LocalDateTime dataOraE = resultQuery.getObject("dataOraE", LocalDateTime.class);
                    LocalDateTime dataOraU = resultQuery.getObject("dataOraU", LocalDateTime.class);
                    int idArticolo = resultQuery.getInt("idArticolo");
                    int idLocazione = resultQuery.getInt("idLocazione");
                    int codiceInterno = resultQuery.getInt("codiceInterno");
                    return new Movimento(idMovimento, confermatoE, confermatoU, dataOraE, dataOraU, idArticolo,
                            idLocazione, codiceInterno);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataLayerException();
        }
    }

    public Movimento modificaMovimentoConId(Movimento movimento) throws DataLayerException {
        String queryModificaMovimento = "UPDATE movimenti SET confermatoE = ?, confermatoU = ?, dataOraE = ?, dataOraU = ?, idArticolo = ?, idLocazione = ?, codiceInterno = ? WHERE idMovimento = ?";

        try (PreparedStatement statementModificaMovimento = connection.prepareStatement(queryModificaMovimento)) {
            // Imposta i parametri della query
            statementModificaMovimento.setBoolean(1, movimento.isConfermatoE());
            statementModificaMovimento.setBoolean(2, movimento.isConfermatoU());
            statementModificaMovimento.setObject(3, movimento.getDataOraE());
            statementModificaMovimento.setObject(4, movimento.getDataOraU());
            statementModificaMovimento.setInt(5, movimento.getIdArticolo());
            statementModificaMovimento.setInt(6, movimento.getIdLocazione());
            statementModificaMovimento.setInt(7, movimento.getCodiceInterno());
            statementModificaMovimento.setInt(8, movimento.getIdMovimento());

            // Esegue l'aggiornamento
            int risultato = statementModificaMovimento.executeUpdate();

            if (risultato > 0) {
                return movimento; // Restituisce l'oggetto movimento aggiornato
            } else {
                System.out
                        .println("Nessun movimento modificato, idMovimento non trovato: " + movimento.getIdMovimento());
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataLayerException();
        }
    }

    public boolean cancellaMovimentoConId(int idMovimento) throws DataLayerException {

        String queryCancellaMovimento = "DELETE FROM movimenti WHERE idMovimento = ?";

        try (PreparedStatement statementCancellaMovimento = connection.prepareStatement(queryCancellaMovimento)) {
            statementCancellaMovimento.setInt(1, idMovimento);

            int risultato = statementCancellaMovimento.executeUpdate();

            if (risultato > 0) {
                return true;
            } else {
                System.out.println("Nessun movimento eliminato, idMovimento non trovato: " + idMovimento);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataLayerException();
        }
    }

    public Movimento cercaMovimentoPerCodiceInterno(int codiceInterno) throws DataLayerException {
        String queryCercaMovimentoPerCodiceInterno = " SELECT * from movimenti where codiceInterno = ?";

        try (PreparedStatement statementCercaMovimentoPerCodiceInterno = connection
                .prepareStatement(queryCercaMovimentoPerCodiceInterno)) {
            statementCercaMovimentoPerCodiceInterno.setInt(1, codiceInterno);
            ResultSet resultQuery = statementCercaMovimentoPerCodiceInterno.executeQuery();
            if (resultQuery.next()) {
                int idMovimento = resultQuery.getInt("idMovimento");
                boolean confermatoE = resultQuery.getBoolean("confermatoE");
                boolean confermatoU = resultQuery.getBoolean("confermatoU");
                LocalDateTime dataOraE = resultQuery.getObject("dataOraE", LocalDateTime.class);
                LocalDateTime dataOraU = resultQuery.getObject("dataOraU", LocalDateTime.class);
                int idArticolo = resultQuery.getInt("idArticolo");
                int idLocazione = resultQuery.getInt("idLocazione");

                return new Movimento(idMovimento, confermatoE, confermatoU, dataOraE, dataOraU, idArticolo,
                        idLocazione, codiceInterno);

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataLayerException();

        }
        return null;
    }

    public ArrayList<MissioniE> missioneEntrata() throws DataLayerException {
        ArrayList<MissioniE> response = new ArrayList<>();
        String queryMissioneEntrata = "select a.barcode, a.descrizione, m.codiceInterno, m.idLocazione, l.corsia, l.colonna, l.ripiano from movimenti as m inner join locazioni as l on l.idLocazione = m.idLocazione inner join articoli as a on a.idArticolo = m.idArticolo where m.ConfermatoE = 0 order by m.codiceInterno, l.corsia, l.colonna, l.ripiano";

        try (PreparedStatement statementMissioneEntrata = connection.prepareStatement(queryMissioneEntrata)) {

            ResultSet resultQuery = statementMissioneEntrata.executeQuery();
            while (resultQuery.next()) {
                String barcode = resultQuery.getString("barcode");
                String descrizione = resultQuery.getString("descrizione");
                int codiceInterno = resultQuery.getInt("codiceInterno");
                int idLocazione = resultQuery.getInt("idLocazione");
                String corsia = resultQuery.getString("corsia");
                String colonna = resultQuery.getString("colonna");
                String ripiano = resultQuery.getString("ripiano");

                MissioniE missioneE = new MissioniE(barcode, descrizione, codiceInterno, idLocazione, corsia, colonna,
                        ripiano);
                response.add(missioneE);

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataLayerException();
        }
        return response;

    }

    public ArrayList<MissioniU> missioneUscita() throws DataLayerException {
        ArrayList<MissioniU> response = new ArrayList<>();
        String queryMissioneUscita = "select a.barcode, a.descrizione, m.codiceInterno, m.idLocazione, l.corsia, l.colonna, l.ripiano from movimenti as m inner join locazioni as l on l.idLocazione = m.idLocazione inner join articoli as a on a.idArticolo = m.idArticolo where m.ConfermatoE = 1 and m.ConfermatoU = 0 order by m.codiceInterno, l.corsia, l.colonna, l.ripiano";

        try (PreparedStatement statementMissioneUscita = connection.prepareStatement(queryMissioneUscita)) {

            ResultSet resultQuery = statementMissioneUscita.executeQuery();
            while (resultQuery.next()) {
                String barcode = resultQuery.getString("barcode");
                String descrizione = resultQuery.getString("descrizione");
                int codiceInterno = resultQuery.getInt("codiceInterno");
                int idLocazione = resultQuery.getInt("idLocazione");
                String corsia = resultQuery.getString("corsia");
                String colonna = resultQuery.getString("colonna");
                String ripiano = resultQuery.getString("ripiano");

                MissioniU missioneU = new MissioniU(barcode, descrizione, codiceInterno, idLocazione, corsia, colonna,
                        ripiano);
                response.add(missioneU);

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataLayerException();
        }
        return response;

    }

    private int creaCodiceInterno() throws DataLayerException {
        String queryCreaCodiceInterno = "SELECT MAX(codiceinterno) as codiceInterno FROM movimenti";

        try (PreparedStatement statementCreaCodiceInterno = connection.prepareStatement(queryCreaCodiceInterno)) {
            ResultSet resultQuery = statementCreaCodiceInterno.executeQuery();
            // se la query è andata a buon fine
            if (resultQuery.next()) {
                System.out.println(resultQuery);
                int maxCodiceInterno = resultQuery.getInt("codiceInterno");
                maxCodiceInterno++;
                return maxCodiceInterno;

            }
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataLayerException();

        }

    }

}

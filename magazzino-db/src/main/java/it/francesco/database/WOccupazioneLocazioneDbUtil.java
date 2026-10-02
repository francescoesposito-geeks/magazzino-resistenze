package it.francesco.database;

import it.francesco.database.DbUtil;
import it.francesco.models.ViewOccupazioneLocazioni;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;

public class WOccupazioneLocazioneDbUtil extends DbUtil {

    public WOccupazioneLocazioneDbUtil(String host, String dbName, String userName, String passWord, int portNumber) {
       super(host, dbName, userName, passWord, portNumber);
    }

    public ArrayList<ViewOccupazioneLocazioni> getOccupazioneLocazioni() {
        ArrayList<ViewOccupazioneLocazioni> risultati = new ArrayList<>();
        String queryRecuperaLocazioniOccupazioni = "SELECT * FROM vw_occupazione_locazioni WHERE NumeroPezzi IS NULL OR NumeroPezzi = 0 ORDER BY Corsia, Colonna, Ripiano Limit 1";

        try (PreparedStatement statementRecuperaLocazioniOccupazioni = connection
                .prepareStatement(queryRecuperaLocazioniOccupazioni)) {

            ResultSet resultQuery = statementRecuperaLocazioniOccupazioni.executeQuery();
            if (resultQuery.next()) {
                int idLocazione = resultQuery.getInt("IdLocazione");
                String corsia = resultQuery.getString("Corsia");
                String colonna = resultQuery.getString("Colonna");
                String ripiano = resultQuery.getString("Ripiano");
                int numeroPezzi = resultQuery.getInt("NumeroPezzi");
                int idArticolo = resultQuery.getInt("IdArticolo");

                ViewOccupazioneLocazioni viewOccupazioniLocazioni = new ViewOccupazioneLocazioni(idLocazione, corsia,
                        colonna, ripiano, numeroPezzi, idArticolo);

                risultati.add(viewOccupazioniLocazioni);
            }
            return risultati;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;

    }

}

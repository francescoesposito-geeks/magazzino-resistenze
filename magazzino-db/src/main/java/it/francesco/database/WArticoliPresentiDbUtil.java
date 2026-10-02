package it.francesco.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import it.francesco.models.LocationNumber;

import java.sql.Connection;

public class WArticoliPresentiDbUtil extends DbUtil {

    public WArticoliPresentiDbUtil(String host, String dbName, String userName, String passWord, int portNumber) {
        super(host, dbName, userName, passWord, portNumber);
    }

    public LocationNumber getIdLocazione(int idArticolo) {

        String queryCercaLocazione = "SELECT IdLocazione, NumeroPezzi FROM vw_articoli_presenti WHERE IdArticolo = ? ORDER BY NumeroPezzi";

        try (PreparedStatement statementCercaLocazione = connection.prepareStatement(queryCercaLocazione)) {

            statementCercaLocazione.setInt(1, idArticolo);
            ResultSet resultQuery = statementCercaLocazione.executeQuery();
            if (resultQuery.next()) {
                int idLocazione = resultQuery.getInt(1);
                int numeroPezzi = resultQuery.getInt(2);
                return new LocationNumber(idLocazione, numeroPezzi);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;

    }

}

package it.francesco;

import it.francesco.database.UtentiDbUtil;

/**
 * Classe di prova della libreria magazzino-db: verifica la connessione al database.
 * Prima di eseguirla, inserire le proprie credenziali MySQL al posto dei segnaposto.
 * L'applicazione Spring Boot non usa questa classe.
 */
public class Main {
    public static void main(String[] args) {
        UtentiDbUtil dbUtil = new UtentiDbUtil("localHost", "MagazzinoResistenza", "IL_TUO_UTENTE", "LA_TUA_PASSWORD", 3306);
    }
}
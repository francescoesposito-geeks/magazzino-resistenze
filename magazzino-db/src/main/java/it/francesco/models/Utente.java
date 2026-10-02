package it.francesco.models;

public class Utente {
    // Dichiara sei variabili di istanza private che rappresentano gli attributi di
    // un utente, Sono privati per garantire l'incapsulamento
    private int idUtente;
    private String cognome;
    private String nome;
    private String username;
    private String password;
    private String chiaveRicerca;

    // Definisce un costruttore pubblico che accetta cinque parametri per
    // inizializzare un oggetto Utenti con i valori specificati, Permette di creare
    // un oggetto Utenti con valori iniziali per tutti gli attributi tranne
    // chiaveRicerca, che viene calcolata internamente.
    public Utente(int idUtente,
            String cognome,
            String nome,
            String username,
            String password) {
        // Assegna i valori dei parametri del costruttore agli attributi corrispondenti
        // dell'oggetto, usando il prefisso this per distinguere gli attributi di
        // istanza dai parametri.
        this.cognome = cognome;
        this.nome = nome;
        this.username = username;
        this.password = password;
        // ti aiutera quando faremo con i like x cercare nel db
        this.chiaveRicerca = nome + "&" + cognome;
    }

    // creare un oggetto Utenti senza specificare valori iniziali, utile in
    // situazioni in cui i dati non sono ancora disponibili (es. quando si recupera
    // un utente dal database e si popola l'oggetto successivamente, come in
    // DbUtil.recuperaUtenti
    public Utente() {
        // Inizializzare gli attributi a valori predefiniti evita errori (es.
        // NullPointerException)
        this.cognome = null;
        this.nome = null;
        this.username = null;
        this.password = null;
        this.chiaveRicerca = null;
        this.idUtente = 0;
    }

    // Sovrascrive il metodo toString della superclasse Object per restituire una
    // rappresentazione testuale dell'oggetto Utenti, Sovrascrivere toString è una
    // best practice in Java per rendere gli oggetti più leggibili quando vengono
    // stampati (es. con System.out.println)
    @Override
    public String toString() {
        return cognome + " " + nome + " " + username + " " + chiaveRicerca + " " + idUtente;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNome() {
        return nome;
    }

    public String getChiaveRicerca() {
        return chiaveRicerca;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
        this.chiaveRicerca = nome + "&" + cognome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        this.chiaveRicerca = nome + "&" + cognome;
    }

    public void setChiaveRicerca(String chiaveRicerca) {
        this.chiaveRicerca = chiaveRicerca;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

}

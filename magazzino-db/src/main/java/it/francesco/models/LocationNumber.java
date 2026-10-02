package it.francesco.models;

public class LocationNumber {
   private int idLocazione;
   private int numeroPezzi;

    public LocationNumber (int idLocazione, int numeroPezzi){
        this.idLocazione = idLocazione;
        this.numeroPezzi = numeroPezzi;
    }

    public int getIdLocazione(){
        return idLocazione;
    }
    public int getNumeroPezzi(){
        return numeroPezzi;
    }
    public void setIdLocazione(int idLocazione){
        this.idLocazione = idLocazione;
    }
    public void setNumeroPezzi(int numeroPezzi){
        this.numeroPezzi = numeroPezzi;
    }
}

package it.francesco.magazzino.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Missione {

    @JsonProperty("barcode")
    private String barcode;
    @JsonProperty("descrizione")
    private String descrizione;
    @JsonProperty("codiceInterno")
    private int codiceInterno;
    @JsonProperty("idLocazione")
    private int idLocazione;
    @JsonProperty("corsia")
    private String corsia;
    @JsonProperty("colonna")
    private String colonna;
    @JsonProperty("ripiano")
    private String ripiano;

}

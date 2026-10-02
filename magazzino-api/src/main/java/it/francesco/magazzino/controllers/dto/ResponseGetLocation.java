package it.francesco.magazzino.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ResponseGetLocation {

    @JsonProperty("idLocazione")
    private int idLocazione;
    @JsonProperty("corsia")
    private String corsia;
    @JsonProperty("colonna")
    private String colonna;
    @JsonProperty("ripiano")
    private String ripiano;
    @JsonProperty("idMovimento")
    private int idMovimento;
     @JsonProperty("codiceInterno")
    private int codiceInterno;


}

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
public class RichiestaInserisciBarcode {
    @JsonProperty("barcode")
    String barcode;
    @JsonProperty("descrizione")
    String descrizione;

}

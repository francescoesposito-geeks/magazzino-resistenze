package it.francesco.magazzino.services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Missione {

    private String barcode;
    private String descrizione;
    private int codiceInterno;
    private int idLocazione;
    private String corsia;
    private String colonna;
    private String ripiano;

}

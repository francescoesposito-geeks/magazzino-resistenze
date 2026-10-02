package it.francesco.magazzino.services.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocationInfo {
    private int idLocazione;
    private String corsia;
    private String colonna;
    private String ripiano;
    private int idMovimento;
    private int codiceInterno;

}


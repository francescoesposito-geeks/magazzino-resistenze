package it.francesco.magazzino.controllers.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import it.francesco.magazzino.controllers.dto.Missione;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ResponseMissione {

    @JsonProperty("recordTotali")
    int recordTotali;
    @JsonProperty("missioni")
    ArrayList<Missione> missioni;

}

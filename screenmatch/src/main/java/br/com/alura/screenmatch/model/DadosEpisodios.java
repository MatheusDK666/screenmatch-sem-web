package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodios(@JsonAlias("Title") String titulo,@JsonAlias("Episode") Integer episodio,@JsonAlias("Season") String temporada,
                            @JsonAlias("Runtime") String duracao,@JsonAlias("imdbRating") String avaliacao ,@JsonAlias("Released")  String dataLancamento) {
}

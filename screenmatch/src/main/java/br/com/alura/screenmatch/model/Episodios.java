package br.com.alura.screenmatch.model;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Episodios {
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double avaliacao;
    private LocalDate dataLamcamento;

    public Episodios(Integer numeroTemporada, DadosEpisodios dadosEpisodios) {
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodios.titulo();
        this.numeroEpisodio = dadosEpisodios.episodio();
        try{
            this.avaliacao = Double.valueOf(dadosEpisodios.avaliacao());
        }catch (NumberFormatException e) {
            this.avaliacao = 0.0;
        }
        try{
            this.dataLamcamento = LocalDate.parse(dadosEpisodios.dataLancamento());
        }catch (DateTimeException | NullPointerException e){
            this.dataLamcamento = null;
        }
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLamcamento() {
        return dataLamcamento;
    }

    public void setDataLamcamento(LocalDate dataLamcamento) {
        this.dataLamcamento = dataLamcamento;
    }

    @Override
    public String toString() {
        return "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLamcamento;
    }
}

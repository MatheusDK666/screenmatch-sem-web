package br.com.alura.screenmatch.principal;


import br.com.alura.screenmatch.model.DadosEpisodios;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodios;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;

public class PrincipalMenu {
    private final ConverteDados conversor = new ConverteDados();
    private final ConsumoApi consumo = new ConsumoApi();
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=850e6244";
    private final Scanner leitor = new Scanner(System.in);

    public void exibeMenu(){
        System.out.println("Digite o nome da serie para busca: ");
        var nomeSerie = leitor.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> listaTemporada = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas(); i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			listaTemporada.add(dadosTemporada);
		}
		listaTemporada.forEach(System.out::println);

        for (int i = 0; i < dados.totalTemporadas(); i++){
            List<DadosEpisodios> episodiosTemporada = listaTemporada.get(i).episodios();
            for (int j =0; j < episodiosTemporada.size(); j++){
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }
        listaTemporada.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodios> episodios = listaTemporada.stream()
                .flatMap(t -> t.episodios().stream())
                .toList();

        System.out.println("\n Top 5 Episodios:");
        episodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodios> listaEpisodio = listaTemporada.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodios(t.numero(), d))
                ).toList();
        listaEpisodio.forEach(System.out::println);

        //Demonstracao de Stream
//        List<String> nomes = Arrays.asList("Java", "C++", "C#", "PYTHON", "Pascal", "C", "Lua", "PHP");
//
//        nomes.stream()
//                .sorted()
//                .forEach(System.out::println);
    }
}

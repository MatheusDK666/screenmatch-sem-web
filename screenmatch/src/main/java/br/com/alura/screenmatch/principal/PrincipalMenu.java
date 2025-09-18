package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.Repository.SerieRepository;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodios;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class PrincipalMenu {
    private final ConverteDados conversor = new ConverteDados();
    private final ConsumoApi consumo = new ConsumoApi();
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=850e6244";
    private final Scanner leitor = new Scanner(System.in);
    private final List<DadosSerie> seriesBuscadas = new ArrayList<>();

    private final SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();

    public PrincipalMenu(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var escolha = -1;
        System.out.println("Bem vindo ao ScreenMatch!");
        while (escolha != 0) {
            var opcoes = """
                    
                    1 - Buscar uma série
                    2 - Buscar Episodios
                    3 - Listar séries buscadas
                    
                    0 - Sair
                    """;
            System.out.println(opcoes);
            System.out.println("Escolha uma opção:");
            escolha = leitor.nextInt();
            leitor.nextLine();
            switch (escolha) {
                case 1:
                    System.out.println("Digite o nome da série:");
                    buscarSerieWeb();
                    break;
                case 2:
                    System.out.println("Digite o nome do episodio:");
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Finalizando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //seriesBuscadas.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        String nomeSerie = leitor.nextLine();
        String json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitor.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(
                        ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodios> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodios(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setListaDeEpisodio(episodios);
            repositorio.save(serieEncontrada);
        }else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}

package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
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
                                        buscarSerieWeb();
                                        break;
                                case 2:
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
                seriesBuscadas.add(dados);
                System.out.println(dados);
        }

        private DadosSerie getDadosSerie() {
                System.out.println("Digite o nome da série:");
                String nomeSerie = leitor.nextLine();
                String json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
                DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
                return dados;
        }

        private void buscarEpisodioPorSerie() {
                DadosSerie dadosSerie = getDadosSerie();
                List<DadosTemporada> temporadas = new ArrayList<>();
                for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
                        var json = consumo.obterDados(
                                        ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
                        DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                        temporadas.add(dadosTemporada);
                }
                temporadas.forEach(System.out::println);
        }
    private void listarSeriesBuscadas(){
        List<Serie> series;
        series = new ArrayList<>();
        series = seriesBuscadas.stream()
                .map(Serie::new)
                .toList();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}

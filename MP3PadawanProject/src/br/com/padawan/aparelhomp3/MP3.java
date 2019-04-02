package br.com.padawan.aparelhomp3;

import br.com.padawan.musicas.Musica;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class MP3 {

    private Scanner respostaUsuario = new Scanner(System.in);
    private Set<Musica> musicas = new LinkedHashSet<>();
    private int opcaoEscolhida;

    private void exibirMenu() {

        System.out.println("Menu: ");
        System.out.println();
        System.out.println("[1] - Cadastrar músicas." +
                System.lineSeparator() + "[2] - Buscar músicas." +
                System.lineSeparator() + "[3] - Excluir músicas." +
                System.lineSeparator() + "[4] - Biblioteca de músicas" +
                System.lineSeparator() + "Escolha uma opção :");

    }

    private void pegarOpcaoSelecionada() {
        try {
           opcaoEscolhida = respostaUsuario.nextInt();
        } catch (InputMismatchException e) {
            if (!respostaUsuario.hasNextInt()) {
                System.out.println("Por favor, digite apenas os digitos indicados antes das opções.");
                System.out.println();
                respostaUsuario.next();
                pegarOpcaoSelecionada();
            }
        }
    }

    public void liga() {


        exibirMenu();

        pegarOpcaoSelecionada();
        verificarOpcao(opcaoEscolhida);

    }

    private void verificarOpcao(int op) {

          if (op == 1) {

              cadastrarMusica();
          } else if (op == 2) {
              buscarMusica(musicas);
          } else if (op == 3) {
              excluirMusica();
          }else if (op == 4) {
              abrirBiblioteca();
          } else {
              System.out.println("Opção inválida.");
              liga();
          }
    }

    private void abrirBiblioteca(){

        System.out.println("[1] - Tocar todas as musicas" + System.lineSeparator() +
                           "[2] - Escolher uma playlist"  + System.lineSeparator() +
                           "Escolha uma opção: " + System.lineSeparator());
        int opcaoSelecionada = respostaUsuario.nextInt();

        if (opcaoSelecionada == 1){
            System.out.println(musicas);
            configurarListaReprodução();
        }

    }


    private void configurarListaReprodução() {

        System.out.println("Configuração de reprodução:");
        System.out.println("O padrão é sequencial deseja alterar para aleatório? (S/N)");
        String alterarAletorio = respostaUsuario.next();

        if(alterarAletorio.equalsIgnoreCase("n")){
            System.out.println(musicas);
            tocarListaReproducao(musicas);
        }else if(alterarAletorio.equalsIgnoreCase("s")){
            Set<Musica> musicasEmbaralhadas = new HashSet<>();
            for (Musica musica : musicas) {
                musicasEmbaralhadas.add(musica);
            }
            System.out.println(musicasEmbaralhadas);
            tocarListaReproducao(musicasEmbaralhadas);
        }

    }

    public void tocarListaReproducao(Set<Musica> musicas){
        musicas.forEach(musica -> tocarMusica(musica));
    }


    private void cadastrarMusica() {
        System.out.println("Digite o nome da música: ");
        String nomeMusica = respostaUsuario.next();

        respostaUsuario.nextLine();

        System.out.println("Digite o artista da música: ");
        String nomeArtistaMusica = respostaUsuario.nextLine();

        System.out.println("Digite a duração da música: ");
        int horaMusica = 0;

        System.out.println("Digite os minutos: ");
        int minutoMusica = respostaUsuario.nextInt();

        System.out.println("Digite os segundos: ");
        int segundoMusica = respostaUsuario.nextInt();

        LocalTime duracao = LocalTime.of(horaMusica, minutoMusica, segundoMusica);

        Musica novaMusica = new Musica(nomeMusica, duracao, nomeArtistaMusica);

        musicas.add(novaMusica);

        System.out.println();

        System.out.println("Você que cadastrar uma nova musica?(S/N)");

        String resposta = respostaUsuario.next();

        if (resposta.equalsIgnoreCase("S")) {

            cadastrarMusica();

        } else {
            liga();
        }
    }

    private void buscarMusica(Set<Musica> musicas) {

        Optional<Musica> musicaEncontrada = verificaSeExisteMusica(musicas);

        if (musicaEncontrada.isPresent()) {
            System.out.println(musicaEncontrada.get());
            System.out.println("Deseja tocar a música?(S/N)");
            String tocarMusica = respostaUsuario.next();

            if (tocarMusica.equalsIgnoreCase("s")) {
                tocarMusica(musicaEncontrada);
            } else if (tocarMusica.equalsIgnoreCase("n")) {
                liga();
            } else {
                System.out.println("Opção inválida!");
                liga();
            }
        } else {
            System.out.println("Música não encontrada, deseja buscar novamente?(S/N)");
            String buscarNovamente = respostaUsuario.next();

            if (buscarNovamente.equalsIgnoreCase("s")) {
                buscarMusica(musicas);
            } else if (buscarNovamente.equalsIgnoreCase("n")) {
                liga();
            } else {
                System.out.println("Opção inválida!");
                liga();
            }
        }
    }

    private Optional<Musica> verificaSeExisteMusica(Set<Musica> musicas) {
        System.out.println("Digite o nome da música: ");
        String nomeMusica = respostaUsuario.next();

        respostaUsuario.nextLine();

        System.out.println("Digite o artista da música: ");
        String nomeArtistaMusica = respostaUsuario.nextLine();

        Optional<Musica> musicaSelecionada = musicas.stream()
                .filter(musica -> musica.getNome().equals(nomeMusica) && musica.getArtista().equals(nomeArtistaMusica))
                .findFirst();

        return musicaSelecionada;
    }

    private void tocarMusica(Optional<Musica> musicaEncontrada) {
        try {
            System.out.println("Tocando " + musicaEncontrada.get().getNome() + " de " + musicaEncontrada.get().getArtista());
            Thread.sleep(10000);

            liga();

        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }

    private void tocarMusica(Musica musica) {
        try {
            System.out.println("Tocando " + musica.getNome() + " de " + musica.getArtista());
            Thread.sleep(10000);

        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }

    private void excluirMusica() {

        Optional<Musica> musicaEncontrada = verificaSeExisteMusica(musicas);

        if (musicaEncontrada.isPresent()) {
            musicas.remove(musicaEncontrada.get());
            System.out.println("Música excluída com sucesso.");
            System.out.println(musicas);
            liga();
        } else {
            System.out.println("A música que você deseja excluir não existe.");
            liga();
        }
    }
}

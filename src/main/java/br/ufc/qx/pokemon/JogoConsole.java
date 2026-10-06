package br.ufc.qx.pokemon;

import java.util.Scanner;

public class JogoConsole {

  private static final String COMANDO_SAIR = "sair";

  private final Jogo jogo;

  public JogoConsole(Jogo jogo) {
    this.jogo = jogo;
  }

  public void iniciar() {
    Scanner scanner = new Scanner(System.in);
    boolean sair = false;
    while (!sair) {
      System.out.print(jogo.renderizarMapa());
      System.out.println("Informe a direção para onde queres ir");
      String opcao = scanner.nextLine();
      if (opcao.equalsIgnoreCase(COMANDO_SAIR)) {
        sair = true;
      } else {
        if (Direcao.eDirecaoValida(opcao)) {
          Batalha batalha = jogo.mover(Direcao.get(opcao));
          if(batalha != null) {
            gerenciarBatalha(batalha, scanner);
          }
        } else {
          System.out.println("Valor invalido");
        }
      }
    }
  }

  private void gerenciarBatalha(Batalha batalha, Scanner scanner) {
    String opcao;
    System.out.println("Voce encontrou um " + descrever(batalha.getPokemon()));
    while(!batalha.terminou()) {
      System.out.println("O que deseja fazer ?");
      System.out.println("  - [F]ugir");
      System.out.println("  - Arremessar [P]okebola");
      opcao = scanner.nextLine();
      if("F".equalsIgnoreCase(opcao)) {
        batalha.fugir();
      } else if("P".equalsIgnoreCase(opcao)) {
        switch (batalha.tentarCaptura()) {
          case EQUIPE_CHEIA -> System.out.println("Sua equipe está cheia");
          case SEM_POKEBOLA -> System.out.println("Você não tem mais pokebolas");
          case CAPTURADO -> System.out.println("Uma " + batalha.getUltimaPokebola().getNome()
              + " foi arremessada. Parabens vc capturou um " + descrever(batalha.getPokemon()));
          case ESCAPOU -> {
            System.out.println("Uma " + batalha.getUltimaPokebola().getNome()
                + " foi arremessada, mas o pokemon quebrou a pokebola");
            if (batalha.terminou()) {
              System.out.println("Suas pokebolas acabaram e o pokemon fugiu");
            }
          }
        }
      } else {
          System.out.println("Opção inválida");
      }
    }
  }

  private String descrever(Pokemon pokemon) {
    return pokemon.getNome() + " (nível " + pokemon.getNivel() + ")";
  }
}

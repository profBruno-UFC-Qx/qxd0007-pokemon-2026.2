package br.ufc.qx.pokemon;

import javax.sound.midi.SysexMessage;
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
          jogo.mover(Direcao.get(opcao));
          if(jogo.encontrouPokemon()) {
            gerenciarBatalha(scanner);
          }
        } else {
          System.out.println("Valor invalido");
        }
      }
    }
  }

  private void gerenciarBatalha(Scanner scanner) {
    String opcao;
    Batalha batalha = jogo.iniciarBatalha();
    System.out.println("Voce encontrou um " + batalha.getPokemon());
    while(!batalha.terminou()) {
      System.out.println("O que deseja fazer ?");
      System.out.println("  - [F]ugir");
      System.out.println("  - Arremesar [P]okebola");
      opcao = scanner.nextLine();
      if("F".equalsIgnoreCase(opcao)) {
        batalha.fugir();
      } else if("P".equalsIgnoreCase(opcao)) {
        if (batalha.tentarCaptura()) {
          System.out.println("Parabens vc capturou um " +  batalha.getPokemon());
        } else {
          System.out.println("O pokemon quebrou a pokebola");
        }
      } else {
          System.out.println("Opção inválida");
      }
    }
  }
}

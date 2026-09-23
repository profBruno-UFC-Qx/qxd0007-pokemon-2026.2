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
          jogo.mover(Direcao.get(opcao));
        } else {
          System.out.println("Valor invalido");
        }
      }
    }
  }
}

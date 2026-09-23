package br.ufc.qx.pokemon;

import java.util.Scanner;

public class JogoConsole {

  private final Jogo jogo;

  public JogoConsole(Jogo jogo) {
    this.jogo = jogo;
  }

  public void iniciar() {
    Scanner scanner = new Scanner(System.in);
    Direcao direcao;
    do {
      System.out.print(jogo.renderizarMapa());
      System.out.println("Informe a direção para onde queres ir");
      direcao = jogo.processarComando(scanner.nextLine());
      if (direcao == null) {
        System.out.println("Valor invalido");
      }
    } while (direcao != Direcao.SAIR);
  }
}

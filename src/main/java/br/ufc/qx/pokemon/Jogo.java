package br.ufc.qx.pokemon;

import java.util.Scanner;

public class Jogo {

  private Treinador treinador;
  private Mapa mapa;

  public Jogo(String nome) {
    treinador = new Treinador(nome);
    mapa = new Mapa(10, 5);
  }

  public void iniciar() {
    Scanner scanner = new Scanner(System.in);
    String opcao;
    int novoX = treinador.getX();
    int novoY = treinador.getY();
    do {
      mapa.exibirMapa(treinador.getX(), treinador.getY());
      System.out.println("Informe a direção para onde queres ir");
      opcao = scanner.nextLine();
      if (opcao.equalsIgnoreCase("CIMA")) {
        novoY = treinador.getY() - 1;
      } else if (opcao.equalsIgnoreCase("BAIXO")) {
        novoY = treinador.getY() + 1;
      } else if (opcao.equalsIgnoreCase("DIR")) {
        novoX = treinador.getX() + 1;
      } else if (opcao.equalsIgnoreCase("ESQ")) {
        novoX = treinador.getX() - 1;
      } else if (!opcao.equalsIgnoreCase("SAIR")) {
        System.out.println("Valor invalido");
      }
      if(mapa.ePosicaoValida(novoX, novoY)) {
        treinador.moverPara(novoX, novoY);
      }

    } while("sair".equals(opcao) == false);

  }
}









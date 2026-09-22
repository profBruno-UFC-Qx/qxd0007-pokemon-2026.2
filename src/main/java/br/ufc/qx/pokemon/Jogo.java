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
    Direcao direcao = null;
    int novoX = treinador.getX();
    int novoY = treinador.getY();
    do {
      mapa.exibirMapa(treinador.getX(), treinador.getY());
      System.out.println("Informe a direção para onde queres ir");
      String opcao = scanner.nextLine();
      try {
        direcao = Direcao.valueOf(opcao.toUpperCase());
      } catch (IllegalArgumentException e) {
        System.out.println("Valor invalido");
        continue;
      }
      switch (direcao) {
        case CIMA -> novoY = treinador.getY() - 1;
        case BAIXO -> novoY = treinador.getY() + 1;
        case DIR -> novoX = treinador.getX() + 1;
        case ESQ -> novoX = treinador.getX() - 1;
        case SAIR -> {}
      }
      if (mapa.ePosicaoValida(novoX, novoY)) {
        treinador.moverPara(novoX, novoY);
      }

    } while (direcao != Direcao.SAIR);

  }
}









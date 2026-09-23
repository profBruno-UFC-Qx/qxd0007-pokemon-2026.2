package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Mapa;

import java.util.Scanner;

public class Jogo {

  private Treinador treinador;
  private Mapa mapa;

  public Jogo(String nome) {
    treinador = new Treinador(nome);
    mapa = new Mapa(10, 5);
  }

  public Treinador getTreinador() {
    return treinador;
  }

  public void iniciar() {
    Scanner scanner = new Scanner(System.in);
    Direcao direcao;
    do {
      mapa.exibirMapa(treinador.getX(), treinador.getY());
      System.out.println("Informe a direção para onde queres ir");
      String opcao = scanner.nextLine();
      direcao = processarComando(opcao);
    } while (direcao != Direcao.SAIR);

  }

  public Direcao processarComando(String opcao) {
    Direcao direcao;
    try {
      direcao = Direcao.valueOf(opcao.toUpperCase());
    } catch (IllegalArgumentException e) {
      System.out.println("Valor invalido");
      return null;
    }
    int novoX = treinador.getX();
    int novoY = treinador.getY();
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
    return direcao;
  }
}









package br.ufc.qx.pokemon.mapa;

import java.util.Random;

public class Mapa {

  private char[][] mapa;
  private final int largura;
  private final int altura;

  public Mapa(int largura, int altura) {
    this(largura, altura, new Random());
  }

  public Mapa(int largura, int altura, Random random) {
    this.largura = largura;
    this.altura = altura;
    this.mapa = new char[this.altura][this.largura];
    inicializarMapa(random);
  }

  private void inicializarMapa(Random r) {
    for (int i = 0; i < this.mapa.length; i++) {
      for (int j = 0; j < this.mapa[i].length; j++) {
        if(r.nextFloat() < 0.8) {
          this.mapa[i][j] = ' ';
        } else {
          this.mapa[i][j] = 'w';
        }
      }
    }
  }

  // Sem modificador de acesso: visível apenas para o pacote mapa (e seus testes),
  // não para o Jogo.
  String renderizar(int x, int y) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.altura; i++) {
      sb.append('|');
      for (int j = 0; j < this.largura; j++) {
        sb.append(i == y && j == x ? 'T' : this.mapa[i][j]).append(' ');
      }
      sb.append("|\n");
    }
    return sb.toString();
  }

  public void exibirMapa(int x, int y) {
    System.out.print(renderizar(x, y));
  }

  public boolean ePosicaoValida(int x, int y) {
    return x >= 0 && x < largura && y >= 0 && y < altura;
  }
}

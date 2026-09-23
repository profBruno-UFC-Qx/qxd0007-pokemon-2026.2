package br.ufc.qx.pokemon.mapa;

import java.util.Random;

public class Mapa {

  private static final double PROBABILIDADE_LIVRE = 0.8;
  private static final char LIVRE = ' ';
  private static final char GRAMA = 'w';
  private static final char TREINADOR = 'T';

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
        if(r.nextFloat() < PROBABILIDADE_LIVRE) {
          this.mapa[i][j] = LIVRE;
        } else {
          this.mapa[i][j] = GRAMA;
        }
      }
    }
  }

  public String renderizar(int x, int y) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.altura; i++) {
      sb.append('|');
      for (int j = 0; j < this.largura; j++) {
        sb.append(i == y && j == x ? TREINADOR : this.mapa[i][j]).append(' ');
      }
      sb.append("|\n");
    }
    return sb.toString();
  }

  public boolean ePosicaoValida(int x, int y) {
    return x >= 0 && x < largura && y >= 0 && y < altura;
  }
}

package br.ufc.qx.pokemon.mapa;

import java.util.Random;

public class Mapa {

  private static final double PROBABILIDADE_LIVRE = 0.2;
  private static final char LIVRE = ' ';
  private static final char GRAMA = 'w';
  private static final char TREINADOR = 'T';
  private static final double PROBABILIDADE_ENCONTRO = 0.5;

  private final char[][] mapa;
  private final int largura;
  private final int altura;
  private final Random random;

  public Mapa(int largura, int altura) {
    this(largura, altura, new Random());
  }

  public Mapa(int largura, int altura, Random random) {
    this.largura = largura;
    this.altura = altura;
    this.mapa = new char[this.altura][this.largura];
    this.random = random;
    inicializarMapa();
  }

  public boolean sortearEncontro(Posicao posicao) {
    return mapa[posicao.getY()][posicao.getX()] == GRAMA && random.nextFloat() < PROBABILIDADE_ENCONTRO;
  }

  private void inicializarMapa() {
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
        if(random.nextFloat() < PROBABILIDADE_LIVRE) {
          this.mapa[i][j] = LIVRE;
        } else {
          this.mapa[i][j] = GRAMA;
        }
      }
    }
  }

  public String renderizar(Posicao posicaoTreinador) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.altura; i++) {
      sb.append('|');
      for (int j = 0; j < this.largura; j++) {
        boolean treinadorAqui = i == posicaoTreinador.getY() && j == posicaoTreinador.getX();
        sb.append(treinadorAqui ? TREINADOR : this.mapa[i][j]).append(' ');
      }
      sb.append("|\n");
    }
    return sb.toString();
  }

  public boolean ePosicaoValida(Posicao posicao) {
    return posicao.getX() >= 0 && posicao.getX() < largura
        && posicao.getY() >= 0 && posicao.getY() < altura;
  }
}

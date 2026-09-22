package br.ufc.qx.pokemon;

import java.util.Random;

public class Mapa {

  private char[][] mapa;
  private final int largura;
  private final int altura;

  public Mapa(int largura, int altura) {
    this.largura = largura;
    this.altura = altura;
    this.mapa = new char[this.altura][this.largura];
    inicializarMapa();
  }

  private void inicializarMapa() {
    Random r = new Random();
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

  public void exibirMapa(int x, int y) {
    char anterior = this.mapa[y][x];
    this.mapa[y][x] = 'T';
    for (int i = 0; i < this.mapa.length; i++) {
      System.out.print("|");
      for (int j = 0; j < this.mapa[i].length; j++) {
        System.out.print(this.mapa[i][j] + " ");
      }
      System.out.println("|");
    }
    this.mapa[y][x] = anterior;
  }

  public boolean ePosicaoValida(int x, int y) {
    return x >= 0 && x < largura && y >= 0 && y < altura;
  }
}

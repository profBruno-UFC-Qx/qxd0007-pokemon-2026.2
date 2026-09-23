package br.ufc.qx.pokemon.mapa;

import java.util.Objects;

public final class Posicao {

  private final int x;
  private final int y;

  public Posicao(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Posicao deslocar(int dx, int dy) {
    return new Posicao(x + dx, y + dy);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Posicao posicao = (Posicao) o;
    return x == posicao.x && y == posicao.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "Posicao{x=" + x + ", y=" + y + '}';
  }
}

package br.ufc.qx.pokemon;

import java.util.Objects;

public class Pokemon {

  private static final int NIVEL_INICIAL = 1;

  private final String nome;
  private final int nivel;
  private final int hpMax;
  private int hp;

  public Pokemon(String nome) {
    this(nome, NIVEL_INICIAL);
  }

  public Pokemon(String nome, int nivel) {
    this.nome = nome;
    this.nivel = nivel;
    this.hpMax = calcularHpMax(nivel);
    setHp(this.hpMax);
  }

  private static int calcularHpMax(int nivel) {
    return nivel * (25 + nivel);
  }

  public int getHp() {
    return this.hp;
  }

  public void setHp(int hp) {
    if(hp >= 0 && hp <= hpMax) {
      this.hp = hp;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Pokemon pokemon = (Pokemon) o;
    return nivel == pokemon.nivel && Objects.equals(nome, pokemon.nome);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, nivel);
  }

  @Override
  public String toString() {
    return "Pokemon{" +
            "nome='" + nome + '\'' +
            ", hp=" + hp +
            ", nivel=" + nivel +
            ", hpMax=" + hpMax +
            '}';
  }
}

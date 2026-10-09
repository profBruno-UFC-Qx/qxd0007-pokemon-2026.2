package br.ufc.qx.pokemon;

import java.util.Objects;

public class Pokemon {

  public static final int NIVEL_MAXIMO = 100;
  private static final int NIVEL_INICIAL = 1;

  private final Especie especie;
  private final int nivel;
  private final int hpMax;
  private int hp;

  public Pokemon(Especie especie) {
    this(especie, NIVEL_INICIAL);
  }

  public Pokemon(Especie especie, int nivel) {
    this.especie = especie;
    this.nivel = nivel;
    this.hpMax = calcularHpMax(especie, nivel);
    setHp(this.hpMax);
  }

  private static int calcularHpMax(Especie especie, int nivel) {
    return 2 * especie.getHpBase() * nivel / 100 + nivel + 10;
  }

  public String getNome() {
    return especie.getNome();
  }

  public int getTaxaDaEspecie() {
    return especie.getTaxaDeCaptura();
  }

  public int getVelocidade() {
    return especie.getVelocidade();
  }

  public double getPeso() {
    return especie.getPeso();
  }

  public int getNivel() {
    return nivel;
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
    return nivel == pokemon.nivel && especie == pokemon.especie;
  }

  @Override
  public int hashCode() {
    return Objects.hash(especie, nivel);
  }

  @Override
  public String toString() {
    return "Pokemon{" +
            "nome='" + getNome() + '\'' +
            ", hp=" + hp +
            ", nivel=" + nivel +
            ", hpMax=" + hpMax +
            '}';
  }
}

package br.ufc.qx.pokemon;

import java.util.Objects;

public class Pokemon {
  private final String name;
  private float height;
  private float weight;
  private int hp;
  private int hpMax;
  private String t1, t2;
  private int level;

  public Pokemon(String name, float height, float weight, int hp, String types) {
    this.name = name;
    this.height = height;
    this.weight = weight;
    setHp(hp);
    this.level = 1;
    String[] tipos = types.split("/");
    this.t1 = tipos[0];
    if(tipos.length > 1) {
      this.t2 = tipos[1];
    }
  }

  public Pokemon(String name, String types, int level) {
    this(name, 10f, 10f, level*(25 + level), types );
    this.level = level;
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
    return level == pokemon.level && Objects.equals(name, pokemon.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, level);
  }

  @Override
  public String toString() {
    return "Pokemon{" +
            "name='" + name + '\'' +
            ", hp=" + hp +
            ", level=" + level +
            ", hpMax=" + hpMax +
            '}';
  }
}

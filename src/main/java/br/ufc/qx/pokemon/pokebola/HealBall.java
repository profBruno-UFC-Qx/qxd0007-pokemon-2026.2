package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Pokemon;

import java.util.Random;

public class HealBall extends  PokebolaComEfeito{

  public HealBall() {
    this(new Random());
  }

  HealBall(Random random) {
    super("HealBall", 1, random);
  }
  @Override
  protected void aplicarEfeito(Pokemon pokemon) {
    pokemon.setHp(pokemon.getHpMax());
  }
}

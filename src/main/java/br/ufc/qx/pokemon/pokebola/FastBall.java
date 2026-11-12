package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Pokemon;

import java.util.Random;

public class FastBall extends Pokebola {

  public FastBall() {
    this(new Random());
  }

  FastBall(Random random) {
    super("FastBall", 1, random);
  }

  @Override
  protected double getBonus(Pokemon pokemon) {
    return pokemon.getVelocidade() >= 100 ? 4 : 1;
  }
}

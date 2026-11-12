package br.ufc.qx.pokemon.pokebola;

import java.util.Random;

public class GreatBall extends Pokebola {

  public GreatBall() {
    this(new Random());
  }

  GreatBall(Random random) {
    super("GreatBall", 1.5, random);
  }
}

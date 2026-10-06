package br.ufc.qx.pokemon.pokebola;

import java.util.Random;

public class UltraBall extends Pokebola {

  public UltraBall() {
    this(new Random());
  }

  UltraBall(Random random) {
    super("UltraBall", 0.8, random);
  }
}

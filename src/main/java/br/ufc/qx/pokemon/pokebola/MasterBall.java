package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Pokemon;

import java.util.Random;

public class MasterBall extends Pokebola {

  public MasterBall() {
    this(new Random());
  }

  MasterBall(Random random) {
    super("MasterBall", 1, random);
  }

  @Override
  protected double getTaxaDeCaptura(Pokemon p) {
    return 1.0;
  }
}

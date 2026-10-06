package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Pokemon;

public class MasterBall extends Pokebola {

  public MasterBall() {
    super("MasterBall", 1);
  }

  @Override
  protected double getTaxaDeCaptura(Pokemon p) {
    return 1.0;
  }
}

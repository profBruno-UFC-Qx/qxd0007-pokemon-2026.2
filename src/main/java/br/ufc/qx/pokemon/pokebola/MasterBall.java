package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Pokemon;

public class MasterBall extends Pokebola {

  public MasterBall() {
    super("MasterBall", 1);
  }

  @Override
  public boolean capturar(Pokemon p) {
    return true;
  }
}

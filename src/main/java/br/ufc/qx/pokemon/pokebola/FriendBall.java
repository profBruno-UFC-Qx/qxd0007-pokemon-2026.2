package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Pokemon;

import java.util.Random;

public class FriendBall extends PokebolaComEfeito{

  public FriendBall() {
    this(new Random());
  }

  FriendBall(Random random) {
    super("FriendBall", 1, random);
  }

  @Override
  protected void aplicarEfeito(Pokemon pokemon) {
    pokemon.setAmizade(200);
  }
}

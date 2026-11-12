package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Pokemon;

import java.util.Random;

public abstract class PokebolaComEfeito extends Pokebola {

  protected PokebolaComEfeito(String nome, double bonus, Random random) {
    super(nome, bonus, random);
  }
  protected abstract void aplicarEfeito(Pokemon pokemon);

  @Override
  public final boolean capturar(Pokemon pokemon) {
    if (super.capturar(pokemon)) {
      aplicarEfeito(pokemon);
      return true;
    }
    return false;
  }
}

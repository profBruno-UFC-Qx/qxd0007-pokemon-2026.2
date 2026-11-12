package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Pokemon;

import java.util.Random;

public class HeavyBall extends Pokebola {

  private static final int TAXA_DA_ESPECIE_MINIMA = 1;

  public HeavyBall() {
    this(new Random());
  }

  HeavyBall(Random random) {
    super("HeavyBall", 1, random);
  }

  @Override
  protected int getTaxaDaEspecie(Pokemon pokemon) {
    return Math.max(TAXA_DA_ESPECIE_MINIMA, pokemon.getTaxaDaEspecie() + ajustePorPeso(pokemon.getPeso()));
  }

  private static int ajustePorPeso(double peso) {
    if (peso < 100) {
      return -20;
    }
    if (peso < 200) {
      return 0;
    }
    if (peso < 300) {
      return 20;
    }
    return 30;
  }
}

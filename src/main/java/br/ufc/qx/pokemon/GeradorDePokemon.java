package br.ufc.qx.pokemon;

import java.util.Random;

public class GeradorDePokemon {

  private static final int NIVEL_MIN_SELVAGEM = 1;
  private static final int NIVEL_MAX_SELVAGEM = 30;

  private final Random random;

  public GeradorDePokemon() {
    this(new Random());
  }

  public GeradorDePokemon(Random random) {
    this.random = random;
  }

  public Pokemon gerar() {
    Especie[] especies = Especie.values();
    Especie especie = especies[random.nextInt(especies.length)];
    int nivel = NIVEL_MIN_SELVAGEM + random.nextInt(NIVEL_MAX_SELVAGEM - NIVEL_MIN_SELVAGEM + 1);
    return new Pokemon(especie, nivel);
  }
}

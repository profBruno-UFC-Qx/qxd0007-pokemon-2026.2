package br.ufc.qx.pokemon;

import java.util.Random;

public class GeradorDePokemon {

  private static final String[] NOMES = {
      "Mewto", "Mew", "Zapdos", "Articuno", "Moltres", "Bellsprout", "Pikachu", "Eevee"
  };
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
    String nome = NOMES[random.nextInt(NOMES.length)];
    int nivel = NIVEL_MIN_SELVAGEM + random.nextInt(NIVEL_MAX_SELVAGEM - NIVEL_MIN_SELVAGEM + 1);
    return new Pokemon(nome, nivel);
  }
}

package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Pokemon;

import java.util.Random;

public class Pokebola {

  protected static final int TAXA_MINIMA = 1;
  protected static final int TAXA_MAXIMA = 255;
  private static final int SORTEIO_MAXIMO = 255;
  private final String nome;
  private final double bonus;
  private final Random random;

  public  Pokebola() {
    this(new Random());
  }

  Pokebola(Random random) {
    this("Pokebola", 1, random);
  }

  protected Pokebola(String nome, double bonus, Random random) {
    this.nome = nome;
    this.bonus = bonus;
    this.random = random;
  }

  public String getNome() {
    return nome;
  }

  protected double getBonus(Pokemon p) {
    return bonus;
  }

  protected int getTaxaDaEspecie(Pokemon p) {
    return p.getTaxaDaEspecie();
  }

  protected double getTaxaDeCaptura(Pokemon p) {
    double fatorHp = (3.0 * p.getHpMax() - 2.0 * p.getHp()) / (3.0 * p.getHpMax());
    double taxaDeCaptura = fatorHp * getTaxaDaEspecie(p) * getBonus(p);
    return Math.min(Math.max(TAXA_MINIMA, taxaDeCaptura), TAXA_MAXIMA);
  }

  public boolean capturar(Pokemon p) {
    int sorteio = random.nextInt(SORTEIO_MAXIMO + 1);
    return sorteio <= getTaxaDeCaptura(p);
  }
}

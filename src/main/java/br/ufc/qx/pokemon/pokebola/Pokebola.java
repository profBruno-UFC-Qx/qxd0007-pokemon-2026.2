package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Pokemon;

import java.util.Random;

public class Pokebola {

  private static final double CHANCE_MINIMA = 0.05;
  private String nome;
  private double taxa;
  private final Random random;

  public Pokebola() {
    this(new Random());
  }

  Pokebola(Random random) {
    this("Pokebola", 0.4, random);
  }

  protected Pokebola(String nome, double taxa, Random random) {
    this.nome = nome;
    this.taxa = taxa;
    this.random = random;
  }

  public String getNome() {
    return nome;
  }

  protected double getTaxaDeCaptura(Pokemon p) {
    return taxa - taxa * (p.getNivel()* 1.0/ Pokemon.NIVEL_MAXIMO) + CHANCE_MINIMA;
  }

  public final boolean capturar(Pokemon p) {
    return random.nextDouble() < getTaxaDeCaptura(p);
  }


}












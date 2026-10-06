package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Pokemon;

import java.util.Random;

public class Pokebola {

  private static final double CHANCE_MINIMA = 0.05;
  private String nome;
  private double taxa;

  public Pokebola() {
    this("Pokebola", 0.4);
  }

  protected Pokebola(String nome, double taxa) {
    this.nome = nome;
    this.taxa = taxa;
  }

  public String getNome() {
    return nome;
  }

  protected double getTaxaDeCaptura(Pokemon p) {
    return taxa - taxa * (p.getNivel()* 1.0/ Pokemon.NIVEL_MAXIMO) + CHANCE_MINIMA;
  }

  public final boolean capturar(Pokemon p) {
    Random r = new Random();
    return r.nextDouble() < getTaxaDeCaptura(p);
  }


}












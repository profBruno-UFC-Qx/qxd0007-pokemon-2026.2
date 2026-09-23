package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Posicao;

import java.util.Arrays;
import java.util.List;

public class Treinador {

  private static final int MAX_POKEMONS = 6;

  private String nome;
  private Posicao posicao;
  private Pokemon[] pokemons;
  private int qtdPokemons;

  public Treinador(String nome) {
    this.nome = nome;
    this.posicao = new Posicao(0, 0);
    this.pokemons = new Pokemon[MAX_POKEMONS];
  }

  public String getNome() {
    return nome;
  }

  public Posicao getPosicao() {
    return posicao;
  }

  public void moverPara(Posicao destino) {
    this.posicao = destino;
  }

  public List<Pokemon> getPokemons() {
    return List.copyOf(Arrays.asList(pokemons).subList(0, qtdPokemons));
  }

  public boolean capturar(Pokemon pokemon) {
    if (qtdPokemons < pokemons.length) {
      pokemons[qtdPokemons++] = pokemon;
      return true;
    }
    return false;
  }

}

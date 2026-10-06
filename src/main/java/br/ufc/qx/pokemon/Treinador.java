package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Posicao;
import br.ufc.qx.pokemon.pokebola.Pokebola;

import java.util.Arrays;
import java.util.List;

public class Treinador {

  private static final int MAX_POKEMONS = 6;
  private static final int MAX_POKEBOLAS = 6;

  private String nome;
  private Posicao posicao;
  private Pokemon[] pokemons;
  private Pokebola[] pokebolas;
  private int qtdPokemons;

  public Treinador(String nome) {
    this.nome = nome;
    this.posicao = new Posicao(0, 0);
    this.pokemons = new Pokemon[MAX_POKEMONS];
    this.pokebolas = new Pokebola[MAX_POKEBOLAS];
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

  public boolean adicionarPokebola(Pokebola pokebola) {
    if(pokebola == null) return false;
    for(int i = 0; i < MAX_POKEBOLAS; i++) {
      if (pokebolas[i] == null) {
        pokebolas[i] = pokebola;
        return true;
      }
    }
    return false;
  }

  public Pokebola arremessarPokebola() {
    for(int i = MAX_POKEBOLAS - 1; i >= 0; i--) {
      if (pokebolas[i] != null) {
        Pokebola p = pokebolas[i];
        pokebolas[i] = null;
        return p;
      }
    }
    return null;
  }

  public boolean temPokebola() {
    for(Pokebola p: pokebolas) {
      if (p != null) {
        return true;
      }
    }
    return false;
  }

}

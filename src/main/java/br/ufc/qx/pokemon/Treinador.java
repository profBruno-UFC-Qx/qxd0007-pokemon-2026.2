package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Posicao;
import br.ufc.qx.pokemon.pokebola.Pokebola;

import java.util.ArrayList;
import java.util.List;

public class Treinador {

  private static final int MAX_POKEMONS = 6;
  private static final int MAX_POKEBOLAS = 6;

  private String nome;
  private Posicao posicao;
  private Pokemon[] pokemons;
  private Pokebola[] pokebolas;

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
    List<Pokemon> capturados = new ArrayList<>();
    for(Pokemon p: pokemons) {
      if (p != null) {
        capturados.add(p);
      }
    }
    return List.copyOf(capturados);
  }

  public boolean adicionarPokemon(Pokemon pokemon) {
    if(pokemon == null) return false;
    for(int i = 0; i < MAX_POKEMONS; i++) {
      if (pokemons[i] == null) {
        pokemons[i] = pokemon;
        return true;
      }
    }
    return false;
  }

  public boolean equipeCheia() {
    for(Pokemon p: pokemons) {
      if (p == null) {
        return false;
      }
    }
    return true;
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

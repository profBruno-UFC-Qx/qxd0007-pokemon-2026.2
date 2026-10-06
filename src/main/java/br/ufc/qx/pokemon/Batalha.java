package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.pokebola.Pokebola;

public class Batalha {
  private Treinador treinador;
  private Pokemon pokemon;
  private boolean encerrada;

  public Batalha(Treinador treinador, Pokemon pokemon) {
    this.treinador = treinador;
    this.pokemon = pokemon;
    encerrada = false;
  }

  public Pokemon getPokemon() {
    return pokemon;
  }

  public boolean tentarCaptura() {
    Pokebola pokebola = treinador.arremessarPokebola();
    if(pokebola != null && pokebola.capturar(pokemon)) {
      System.out.println("Uma " + pokebola.getNome() + " foi arremesada");
      if (treinador.capturar(pokemon)) {
        encerrada = true;
        return true;
      }
    }
    if(!treinador.temPokebola()) {
      encerrada = true;
    }
    return false;
  }

  public void fugir() {
    encerrada = true;
  }

  public boolean terminou() {
    return encerrada;
  }
}

package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.pokebola.Pokebola;

public class Batalha {
  private Treinador treinador;
  private Pokemon pokemon;
  private boolean encerrada;
  private Pokebola ultimaPokebola;

  public Batalha(Treinador treinador, Pokemon pokemon) {
    this.treinador = treinador;
    this.pokemon = pokemon;
    encerrada = false;
  }

  public Pokemon getPokemon() {
    return pokemon;
  }

  public Pokebola getUltimaPokebola() {
    return ultimaPokebola;
  }

  public ResultadoCaptura tentarCaptura() {
    if (treinador.equipeCheia()) {
      return ResultadoCaptura.EQUIPE_CHEIA;
    }
    ultimaPokebola = treinador.arremessarPokebola();
    if (ultimaPokebola == null) {
      encerrada = true;
      return ResultadoCaptura.SEM_POKEBOLA;
    }
    if (ultimaPokebola.capturar(pokemon)) {
      treinador.capturar(pokemon);
      encerrada = true;
      return ResultadoCaptura.CAPTURADO;
    }
    if (!treinador.temPokebola()) {
      encerrada = true;
    }
    return ResultadoCaptura.ESCAPOU;
  }

  public void fugir() {
    encerrada = true;
  }

  public boolean terminou() {
    return encerrada;
  }
}

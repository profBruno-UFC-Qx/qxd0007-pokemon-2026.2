package br.ufc.qx;

import br.ufc.qx.pokemon.Jogo;
import br.ufc.qx.pokemon.JogoConsole;
import br.ufc.qx.pokemon.Pokemon;

public class Main {
  public static void main(String[] args) {
    Jogo jogo = new Jogo("Ash");
    new JogoConsole(jogo).iniciar();
  }
}

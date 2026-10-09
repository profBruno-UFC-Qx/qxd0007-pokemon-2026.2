package br.ufc.qx.pokemon;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TesteGeradorDePokemon {

  @Test
  public void geradoresComMesmaSementeGeramOsMesmosPokemons() {
    GeradorDePokemon geradorA = new GeradorDePokemon(new Random(42));
    GeradorDePokemon geradorB = new GeradorDePokemon(new Random(42));

    for (int i = 0; i < 10; i++) {
      assertEquals(geradorA.gerar(), geradorB.gerar());
    }
  }

  @Test
  public void nivelDoPokemonGeradoFicaEntre1e30() {
    GeradorDePokemon gerador = new GeradorDePokemon(new Random(7));
    boolean gerouNivel1 = false;
    boolean gerouNivel30 = false;

    for (int i = 0; i < 1000; i++) {
      int nivel = gerador.gerar().getNivel();
      assertTrue(nivel >= 1 && nivel <= 30, "Nível fora da faixa: " + nivel);
      gerouNivel1 |= nivel == 1;
      gerouNivel30 |= nivel == 30;
    }
    assertTrue(gerouNivel1 && gerouNivel30, "Os extremos da faixa deveriam ser sorteados");
  }

  @Test
  public void geradorSorteiaEntreAs151Especies() {
    GeradorDePokemon gerador = new GeradorDePokemon(new Random(7));
    boolean[] sorteada = new boolean[Especie.values().length];

    for (int i = 0; i < 5000; i++) {
      Pokemon pokemon = gerador.gerar();
      for (Especie especie : Especie.values()) {
        if (especie.getNome().equals(pokemon.getNome())) {
          sorteada[especie.ordinal()] = true;
        }
      }
    }

    assertEquals(151, sorteada.length);
    for (Especie especie : Especie.values()) {
      assertTrue(sorteada[especie.ordinal()], "Espécie nunca sorteada: " + especie);
    }
  }
}

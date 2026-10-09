package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Posicao;
import br.ufc.qx.pokemon.pokebola.GreatBall;
import br.ufc.qx.pokemon.pokebola.Pokebola;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TesteTreinador {

  @Test
  public void moverParaBaixo() {
    Treinador treinador = new Treinador("Ash");
    treinador.moverPara(new Posicao(0, 1));
    assertEquals(new Posicao(0, 1), treinador.getPosicao());
  }

  @Test
  public void treinadorComecaNaOrigemDoMapa() {
    Treinador treinador = new Treinador("Ash");
    assertEquals(new Posicao(0, 0), treinador.getPosicao());
  }

  @Test
  public void getNomeRetornaNomeInformadoNoConstrutor() {
    Treinador treinador = new Treinador("Ash");
    assertEquals("Ash", treinador.getNome());
  }

  @Test
  public void adicionarPokemonAceitaEnquantoHouverEspaco() {
    Treinador treinador = new Treinador("Ash");
    for (int i = 0; i < 6; i++) {
      assertTrue(treinador.adicionarPokemon(new Pokemon(Especie.values()[i], 1)));
    }
  }

  @Test
  public void adicionarPokemonFalhaQuandoEquipeEstaCheia() {
    Treinador treinador = new Treinador("Ash");
    for (int i = 0; i < 6; i++) {
      treinador.adicionarPokemon(new Pokemon(Especie.values()[i], 1));
    }
    assertFalse(treinador.adicionarPokemon(new Pokemon(Especie.MEW, 1)));
  }

  @Test
  public void getPokemonsDevolveApenasOsPokemonsCapturados() {
    Treinador treinador = new Treinador("Ash");
    Pokemon pikachu = new Pokemon(Especie.PIKACHU, 5);
    treinador.adicionarPokemon(pikachu);

    List<Pokemon> pokemons = treinador.getPokemons();

    assertEquals(List.of(pikachu), pokemons);
  }

  @Test
  public void getPokemonsDevolveListaVaziaQuandoNadaFoiCapturado() {
    Treinador treinador = new Treinador("Ash");

    assertTrue(treinador.getPokemons().isEmpty());
  }

  @Test
  public void getPokemonsNaoPermiteAlterarAEquipeDoTreinador() {
    Treinador treinador = new Treinador("Ash");

    assertThrows(UnsupportedOperationException.class,
        () -> treinador.getPokemons().add(new Pokemon(Especie.MEW, 1)));
  }

  @Test
  public void adicionarPokemonRejeitaNulo() {
    Treinador treinador = new Treinador("Ash");

    assertFalse(treinador.adicionarPokemon(null));
    assertTrue(treinador.getPokemons().isEmpty());
  }

  @Test
  public void getPokemonsDevolveOsPokemonsNaOrdemDeCaptura() {
    Treinador treinador = new Treinador("Ash");
    Pokemon pikachu = new Pokemon(Especie.PIKACHU, 5);
    Pokemon eevee = new Pokemon(Especie.EEVEE, 3);
    treinador.adicionarPokemon(pikachu);
    treinador.adicionarPokemon(eevee);

    assertEquals(List.of(pikachu, eevee), treinador.getPokemons());
  }

  @Test
  public void equipeCheiaSoQuandoTodasAsVagasEstaoOcupadas() {
    Treinador treinador = new Treinador("Ash");
    for (int i = 0; i < 6; i++) {
      assertFalse(treinador.equipeCheia());
      treinador.adicionarPokemon(new Pokemon(Especie.values()[i], 1));
    }
    assertTrue(treinador.equipeCheia());
  }

  @Test
  public void treinadorComecaSemPokebola() {
    Treinador treinador = new Treinador("Ash");

    assertFalse(treinador.temPokebola());
  }

  @Test
  public void adicionarPokebolaAceitaEnquantoHouverEspaco() {
    Treinador treinador = new Treinador("Ash");
    for (int i = 0; i < 6; i++) {
      assertTrue(treinador.adicionarPokebola(new Pokebola()));
    }
    assertTrue(treinador.temPokebola());
  }

  @Test
  public void adicionarPokebolaFalhaQuandoNaoHaMaisEspaco() {
    Treinador treinador = new Treinador("Ash");
    for (int i = 0; i < 6; i++) {
      treinador.adicionarPokebola(new Pokebola());
    }
    assertFalse(treinador.adicionarPokebola(new Pokebola()));
  }

  @Test
  public void adicionarPokebolaRejeitaNulo() {
    Treinador treinador = new Treinador("Ash");

    assertFalse(treinador.adicionarPokebola(null));
    assertFalse(treinador.temPokebola());
  }

  @Test
  public void arremessarPokebolaDevolveAUltimaAdicionada() {
    Treinador treinador = new Treinador("Ash");
    Pokebola pokebola = new Pokebola();
    Pokebola greatBall = new GreatBall();
    treinador.adicionarPokebola(pokebola);
    treinador.adicionarPokebola(greatBall);

    assertSame(greatBall, treinador.arremessarPokebola());
    assertSame(pokebola, treinador.arremessarPokebola());
  }

  @Test
  public void arremessarPokebolaGastaAPokebola() {
    Treinador treinador = new Treinador("Ash");
    treinador.adicionarPokebola(new Pokebola());

    treinador.arremessarPokebola();

    assertFalse(treinador.temPokebola());
  }

  @Test
  public void arremessarPokebolaDevolveNuloQuandoNaoHaPokebola() {
    Treinador treinador = new Treinador("Ash");

    assertNull(treinador.arremessarPokebola());
  }

  @Test
  public void arremessarPokebolaLiberaEspacoParaNovaPokebola() {
    Treinador treinador = new Treinador("Ash");
    for (int i = 0; i < 6; i++) {
      treinador.adicionarPokebola(new Pokebola());
    }
    treinador.arremessarPokebola();

    assertTrue(treinador.adicionarPokebola(new Pokebola()));
  }
}

package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.pokebola.MasterBall;
import br.ufc.qx.pokemon.pokebola.Pokebola;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TesteBatalha {

  // Pokebola de teste: nunca captura, para os cenários de falha não dependerem de sorteio.
  private static class PokebolaQueSempreFalha extends Pokebola {

    PokebolaQueSempreFalha() {
      super("PokebolaQueSempreFalha", 0, new Random());
    }

    @Override
    protected double getTaxaDeCaptura(Pokemon p) {
      return 0;
    }
  }

  private static final int MAX_POKEMONS = 6;

  private final Pokemon pikachu = new Pokemon(Especie.PIKACHU, 5);

  @Test
  public void batalhaComecaEmAndamento() {
    Batalha batalha = new Batalha(new Treinador("Ash"), pikachu);

    assertFalse(batalha.terminou());
  }

  @Test
  public void getPokemonDevolveOPokemonEncontrado() {
    Batalha batalha = new Batalha(new Treinador("Ash"), pikachu);

    assertSame(pikachu, batalha.getPokemon());
  }

  @Test
  public void fugirEncerraABatalha() {
    Batalha batalha = new Batalha(new Treinador("Ash"), pikachu);

    batalha.fugir();

    assertTrue(batalha.terminou());
  }

  @Test
  public void capturaBemSucedidaAdicionaPokemonAEquipeEEncerraABatalha() {
    Treinador treinador = new Treinador("Ash");
    treinador.adicionarPokebola(new MasterBall());
    Batalha batalha = new Batalha(treinador, pikachu);

    assertEquals(ResultadoCaptura.CAPTURADO, batalha.tentarCaptura());
    assertEquals(List.of(pikachu), treinador.getPokemons());
    assertTrue(batalha.terminou());
  }

  @Test
  public void pokemonEscapaEBatalhaContinuaEnquantoHouverPokebola() {
    Treinador treinador = new Treinador("Ash");
    treinador.adicionarPokebola(new PokebolaQueSempreFalha());
    treinador.adicionarPokebola(new PokebolaQueSempreFalha());
    Batalha batalha = new Batalha(treinador, pikachu);

    assertEquals(ResultadoCaptura.ESCAPOU, batalha.tentarCaptura());
    assertTrue(treinador.getPokemons().isEmpty());
    assertFalse(batalha.terminou());
  }

  @Test
  public void batalhaEncerraQuandoAUltimaPokebolaFalha() {
    Treinador treinador = new Treinador("Ash");
    treinador.adicionarPokebola(new PokebolaQueSempreFalha());
    Batalha batalha = new Batalha(treinador, pikachu);

    assertEquals(ResultadoCaptura.ESCAPOU, batalha.tentarCaptura());
    assertTrue(treinador.getPokemons().isEmpty());
    assertTrue(batalha.terminou());
  }

  @Test
  public void tentarCapturaSemPokebolaEncerraABatalha() {
    Treinador treinador = new Treinador("Ash");
    Batalha batalha = new Batalha(treinador, pikachu);

    assertEquals(ResultadoCaptura.SEM_POKEBOLA, batalha.tentarCaptura());
    assertNull(batalha.getUltimaPokebola());
    assertTrue(batalha.terminou());
  }

  @Test
  public void equipeCheiaNaoGastaPokebolaNemEncerraABatalha() {
    Treinador treinador = new Treinador("Ash");
    for (int i = 0; i < MAX_POKEMONS; i++) {
      treinador.adicionarPokemon(new Pokemon(Especie.values()[i], 1));
    }
    treinador.adicionarPokebola(new MasterBall());
    Batalha batalha = new Batalha(treinador, pikachu);

    assertEquals(ResultadoCaptura.EQUIPE_CHEIA, batalha.tentarCaptura());
    assertTrue(treinador.temPokebola());
    assertFalse(treinador.getPokemons().contains(pikachu));
    assertFalse(batalha.terminou());
  }

  @Test
  public void getUltimaPokebolaDevolveAPokebolaArremessada() {
    Treinador treinador = new Treinador("Ash");
    Pokebola masterBall = new MasterBall();
    treinador.adicionarPokebola(masterBall);
    Batalha batalha = new Batalha(treinador, pikachu);

    batalha.tentarCaptura();

    assertSame(masterBall, batalha.getUltimaPokebola());
  }

  @Test
  public void getUltimaPokebolaENulaAntesDoPrimeiroArremesso() {
    Batalha batalha = new Batalha(new Treinador("Ash"), pikachu);

    assertNull(batalha.getUltimaPokebola());
  }
}

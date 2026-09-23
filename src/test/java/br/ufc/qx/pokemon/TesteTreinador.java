package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Posicao;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
  public void capturarAdicionaPokemonEnquantoHouverEspaco() {
    Treinador treinador = new Treinador("Ash");
    for (int i = 0; i < 6; i++) {
      assertTrue(treinador.capturar(new Pokemon("Pokemon" + i, "normal", 1)));
    }
  }

  @Test
  public void capturarFalhaQuandoEquipeEstaCheia() {
    Treinador treinador = new Treinador("Ash");
    for (int i = 0; i < 6; i++) {
      treinador.capturar(new Pokemon("Pokemon" + i, "normal", 1));
    }
    assertFalse(treinador.capturar(new Pokemon("Extra", "normal", 1)));
  }

  @Test
  public void getPokemonsDevolveApenasOsPokemonsCapturados() {
    Treinador treinador = new Treinador("Ash");
    Pokemon pikachu = new Pokemon("Pikachu", "electric", 5);
    treinador.capturar(pikachu);

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
        () -> treinador.getPokemons().add(new Pokemon("Intruso", "normal", 1)));
  }
}

package br.ufc.qx.pokemon.mapa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestePosicao {

  @Test
  public void posicoesComMesmasCoordenadasSaoIguais() {
    assertEquals(new Posicao(2, 3), new Posicao(2, 3));
  }

  @Test
  public void posicoesIguaisTemMesmoHashCode() {
    assertEquals(new Posicao(2, 3).hashCode(), new Posicao(2, 3).hashCode());
  }

  @Test
  public void posicoesComCoordenadasDiferentesSaoDiferentes() {
    assertNotEquals(new Posicao(2, 3), new Posicao(3, 2));
  }

  @Test
  public void deslocarDevolveNovaPosicaoComAsCoordenadasSomadas() {
    Posicao deslocada = new Posicao(2, 3).deslocar(1, -1);

    assertEquals(new Posicao(3, 2), deslocada);
  }

  @Test
  public void deslocarNaoAlteraAPosicaoOriginal() {
    Posicao original = new Posicao(2, 3);

    original.deslocar(1, 1);

    assertEquals(new Posicao(2, 3), original);
  }
}

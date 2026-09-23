package br.ufc.qx.pokemon.mapa;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TesteMapa {

  @Test
  public void moverParaPosicaoValida() {
    Mapa mapa = new Mapa(5, 5);
    assertTrue(mapa.ePosicaoValida(2, 2), "A posição é válida");
  }

  @Test
  public void moverParaPosicaoInvalida() {
    Mapa mapa = new Mapa(5, 5);
    assertFalse(mapa.ePosicaoValida(0, 5), "Essa posição é inválida");
  }

  @Test
  public void renderizarNaoLancaExcecaoQuandoLarguraEAlturaSaoDiferentes() {
    // largura=10, altura=5: x pode chegar a 9, maior que o limite de altura (5),
    // cenário que expunha o bug de índices trocados (mapa[x][y]).
    Mapa mapa = new Mapa(10, 5);

    String[] linhas = mapa.renderizar(7, 1).split("\n");

    assertEquals(5, linhas.length);
  }

  @Test
  public void renderizarMarcaTreinadorNaPosicaoInformada() {
    Mapa mapa = new Mapa(4, 3, new Random(1));

    String[] linhas = mapa.renderizar(2, 1).split("\n");

    // Cada célula ocupa 2 caracteres ("c "), depois do '|' inicial.
    assertEquals('T', linhas[1].charAt(1 + 2 * 2));
    assertFalse(linhas[0].contains("T"));
    assertFalse(linhas[2].contains("T"));
  }

  @Test
  public void renderizarNaoAlteraOMapa() {
    Mapa mapa = new Mapa(3, 3, new Random(42));

    mapa.renderizar(0, 0);
    String[] linhas = mapa.renderizar(1, 1).split("\n");

    assertFalse(linhas[0].contains("T"), "A posição anterior não deveria manter o treinador");
  }

  @Test
  public void mapasComMesmoSeedGeramMesmoConteudo() {
    Mapa mapaA = new Mapa(4, 4, new Random(7));
    Mapa mapaB = new Mapa(4, 4, new Random(7));

    assertEquals(mapaA.renderizar(0, 0), mapaB.renderizar(0, 0));
  }
}

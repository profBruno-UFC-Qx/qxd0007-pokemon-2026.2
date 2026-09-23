package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Posicao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TesteDirecao {

  private final Posicao origem = new Posicao(2, 2);

  @Test
  public void cimaDiminuiY() {
    assertEquals(new Posicao(2, 1), Direcao.CIMA.aplicarEm(origem));
  }

  @Test
  public void baixoAumentaY() {
    assertEquals(new Posicao(2, 3), Direcao.BAIXO.aplicarEm(origem));
  }

  @Test
  public void esqDiminuiX() {
    assertEquals(new Posicao(1, 2), Direcao.ESQ.aplicarEm(origem));
  }

  @Test
  public void dirAumentaX() {
    assertEquals(new Posicao(3, 2), Direcao.DIR.aplicarEm(origem));
  }

  @Test
  public void deInterpretaTextoSemDiferenciarMaiusculas() {
    assertEquals(Direcao.DIR, Direcao.get("dir"));
    assertEquals(Direcao.ESQ, Direcao.get("ESQ"));
    assertEquals(Direcao.CIMA, Direcao.get("Cima"));
  }

  @Test
  public void eDirecaoValidaAceitaTextoDeUmaDirecaoSemDiferenciarMaiusculas() {
    assertTrue(Direcao.eDirecaoValida("baixo"));
    assertTrue(Direcao.eDirecaoValida("BAIXO"));
  }

  @Test
  public void eDirecaoValidaRejeitaTextoQueNaoEDirecao() {
    assertFalse(Direcao.eDirecaoValida("voar"));
    assertFalse(Direcao.eDirecaoValida(""));
  }

  @Test
  public void sairNaoEUmaDirecao() {
    assertFalse(Direcao.eDirecaoValida("sair"));
  }

  @Test
  public void deLancaExcecaoParaTextoQueNaoEDirecao() {
    assertThrows(IllegalArgumentException.class, () -> Direcao.get("voar"));
  }
}

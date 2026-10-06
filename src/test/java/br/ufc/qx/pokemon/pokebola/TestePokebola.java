package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Pokemon;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestePokebola {

  // Random de teste: devolve sempre o mesmo valor, para o sorteio da captura ser previsível.
  private static class SorteioFixo extends Random {

    private final double valor;

    SorteioFixo(double valor) {
      this.valor = valor;
    }

    @Override
    public double nextDouble() {
      return valor;
    }
  }

  private final Pokemon nivel1 = new Pokemon("Pikachu", 1);
  private final Pokemon nivel50 = new Pokemon("Pikachu", 50);
  private final Pokemon nivel100 = new Pokemon("Pikachu", Pokemon.NIVEL_MAXIMO);

  @Test
  public void getNomeIdentificaCadaTipoDePokebola() {
    assertEquals("Pokebola", new Pokebola().getNome());
    assertEquals("GreatBall", new GreatBall().getNome());
    assertEquals("UltraBall", new UltraBall().getNome());
    assertEquals("MasterBall", new MasterBall().getNome());
  }

  // Nível 50: taxa = 0.4 * 0.5 + 0.05 = 0.25
  @Test
  public void pokebolaCapturaQuandoSorteioFicaAbaixoDaTaxa() {
    assertTrue(new Pokebola(new SorteioFixo(0.24)).capturar(nivel50));
  }

  @Test
  public void pokebolaNaoCapturaQuandoSorteioFicaAcimaDaTaxa() {
    assertFalse(new Pokebola(new SorteioFixo(0.26)).capturar(nivel50));
  }

  // Nível 50: taxa = 0.6 * 0.5 + 0.05 = 0.35
  @Test
  public void greatBallCapturaQuandoSorteioFicaAbaixoDaTaxa() {
    assertTrue(new GreatBall(new SorteioFixo(0.34)).capturar(nivel50));
  }

  @Test
  public void greatBallNaoCapturaQuandoSorteioFicaAcimaDaTaxa() {
    assertFalse(new GreatBall(new SorteioFixo(0.36)).capturar(nivel50));
  }

  // Nível 50: taxa = 0.8 * 0.5 + 0.05 = 0.45
  @Test
  public void ultraBallCapturaQuandoSorteioFicaAbaixoDaTaxa() {
    assertTrue(new UltraBall(new SorteioFixo(0.44)).capturar(nivel50));
  }

  @Test
  public void ultraBallNaoCapturaQuandoSorteioFicaAcimaDaTaxa() {
    assertFalse(new UltraBall(new SorteioFixo(0.46)).capturar(nivel50));
  }

  @Test
  public void masterBallCapturaMesmoComOPiorSorteio() {
    Random piorSorteio = new SorteioFixo(Math.nextDown(1.0));

    assertTrue(new MasterBall(piorSorteio).capturar(nivel1));
    assertTrue(new MasterBall(piorSorteio).capturar(nivel100));
  }

  @Test
  public void pokebolasMelhoresCapturamComSorteiosEmQueAsPioresFalham() {
    assertFalse(new Pokebola(new SorteioFixo(0.30)).capturar(nivel50));
    assertTrue(new GreatBall(new SorteioFixo(0.30)).capturar(nivel50));

    assertFalse(new GreatBall(new SorteioFixo(0.40)).capturar(nivel50));
    assertTrue(new UltraBall(new SorteioFixo(0.40)).capturar(nivel50));

    assertFalse(new UltraBall(new SorteioFixo(0.50)).capturar(nivel50));
    assertTrue(new MasterBall(new SorteioFixo(0.50)).capturar(nivel50));
  }

  // Com o mesmo sorteio, o nível 1 (taxa 0.446) é capturado e o nível 50 (taxa 0.25) não.
  @Test
  public void quantoMaiorONivelDoPokemonMaisDificilACaptura() {
    assertTrue(new Pokebola(new SorteioFixo(0.30)).capturar(nivel1));
    assertFalse(new Pokebola(new SorteioFixo(0.30)).capturar(nivel50));
  }

  // No nível máximo sobra apenas a chance mínima de 0.05, qualquer que seja a pokebola.
  @Test
  public void pokemonDeNivelMaximoAindaPodeSerCapturadoPelaChanceMinima() {
    assertTrue(new Pokebola(new SorteioFixo(0.04)).capturar(nivel100));
    assertTrue(new GreatBall(new SorteioFixo(0.04)).capturar(nivel100));
    assertTrue(new UltraBall(new SorteioFixo(0.04)).capturar(nivel100));
  }

  @Test
  public void pokemonDeNivelMaximoEscapaQuandoSorteioPassaDaChanceMinima() {
    assertFalse(new Pokebola(new SorteioFixo(0.06)).capturar(nivel100));
    assertFalse(new GreatBall(new SorteioFixo(0.06)).capturar(nivel100));
    assertFalse(new UltraBall(new SorteioFixo(0.06)).capturar(nivel100));
  }

  @Test
  public void pokebolasComMesmaSementeTemOMesmoResultado() {
    Pokebola pokebolaA = new Pokebola(new Random(42));
    Pokebola pokebolaB = new Pokebola(new Random(42));

    for (int i = 0; i < 20; i++) {
      assertEquals(pokebolaA.capturar(nivel50), pokebolaB.capturar(nivel50));
    }
  }
}

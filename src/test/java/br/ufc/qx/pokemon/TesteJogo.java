package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Posicao;
import br.ufc.qx.pokemon.pokebola.MasterBall;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TesteJogo {

  // Random de teste: devolve sempre o mesmo float, para controlar o terreno e os encontros.
  private static class SorteioFixo extends Random {

    private final float valor;

    SorteioFixo(float valor) {
      this.valor = valor;
    }

    @Override
    public float nextFloat() {
      return valor;
    }
  }

  @Test
  public void moverDirMoveTreinadorParaDireita() {
    Jogo jogo = new Jogo("Ash");
    jogo.mover(Direcao.DIR);
    assertEquals(new Posicao(1, 0), jogo.getTreinador().getPosicao());
  }

  @Test
  public void moverEsqNaoMoveTreinadorParaForaDoMapa() {
    Jogo jogo = new Jogo("Ash");
    jogo.mover(Direcao.ESQ);
    assertEquals(new Posicao(0, 0), jogo.getTreinador().getPosicao());
  }

  @Test
  public void moverCimaNaoMoveTreinadorParaForaDoMapa() {
    Jogo jogo = new Jogo("Ash");
    jogo.mover(Direcao.CIMA);
    assertEquals(new Posicao(0, 0), jogo.getTreinador().getPosicao());
  }

  @Test
  public void moverEmSequenciaAcumulaOsDeslocamentos() {
    Jogo jogo = new Jogo("Ash");
    jogo.mover(Direcao.DIR);
    jogo.mover(Direcao.BAIXO);
    jogo.mover(Direcao.CIMA);
    assertEquals(new Posicao(1, 0), jogo.getTreinador().getPosicao());
  }

  @Test
  public void renderizarMapaMostraTreinadorNaPosicaoAtual() {
    Jogo jogo = new Jogo("Ash");
    jogo.mover(Direcao.DIR);

    String[] linhas = jogo.renderizarMapa().split("\n");

    // Treinador em x=1, y=0: cada célula ocupa 2 caracteres, depois do '|' inicial.
    assertEquals('T', linhas[0].charAt(1 + 2 * 1));
  }

  @Test
  public void jogosComMesmaSementeGeramOMesmoMapa() {
    Jogo jogoA = new Jogo("Ash", new Random(42));
    Jogo jogoB = new Jogo("Ash", new Random(42));

    assertEquals(jogoA.renderizarMapa(), jogoB.renderizarMapa());
  }

  @Test
  public void jogosComMesmaSementeEncontramOsMesmosPokemons() {
    Jogo jogoA = new Jogo("Ash", new Random(42));
    Jogo jogoB = new Jogo("Ash", new Random(42));
    int encontros = 0;

    for (int i = 0; i < 40; i++) {
      Direcao direcao = i % 2 == 0 ? Direcao.DIR : Direcao.ESQ;
      Batalha batalhaA = jogoA.mover(direcao);
      Batalha batalhaB = jogoB.mover(direcao);

      assertEquals(batalhaA == null, batalhaB == null);
      if (batalhaA != null) {
        assertEquals(batalhaA.getPokemon(), batalhaB.getPokemon());
        encontros++;
      }
    }
    assertTrue(encontros > 0, "Pelo menos um encontro deveria acontecer em 40 movimentos");
  }

  // sorteio 0.3: todo o mapa é grama (>= 0.2) e todo sorteio de encontro dá certo (< 0.5)
  @Test
  public void moverParaGramaIniciaBatalhaQuandoOSorteioDaEncontro() {
    Jogo jogo = new Jogo("Ash", new SorteioFixo(0.3f));

    Batalha batalha = jogo.mover(Direcao.DIR);

    assertNotNull(batalha);
    assertNotNull(batalha.getPokemon());
    assertFalse(batalha.terminou());
  }

  // sorteio 0.9: todo o mapa é grama, mas nenhum sorteio de encontro dá certo
  @Test
  public void moverParaGramaNaoIniciaBatalhaQuandoOSorteioNaoDaEncontro() {
    Jogo jogo = new Jogo("Ash", new SorteioFixo(0.9f));

    assertNull(jogo.mover(Direcao.DIR));
  }

  // sorteio 0.1: todo o mapa é livre (< 0.2), então não há onde encontrar pokémon
  @Test
  public void moverParaPosicaoLivreNaoIniciaBatalha() {
    Jogo jogo = new Jogo("Ash", new SorteioFixo(0.1f));

    assertNull(jogo.mover(Direcao.DIR));
  }

  @Test
  public void movimentoBloqueadoPelaBordaNaoIniciaBatalha() {
    Jogo jogo = new Jogo("Ash", new SorteioFixo(0.3f));

    assertNull(jogo.mover(Direcao.ESQ));
    assertNull(jogo.mover(Direcao.CIMA));
  }

  @Test
  public void batalhaIniciadaPeloMovimentoEDoTreinadorDoJogo() {
    Jogo jogo = new Jogo("Ash", new SorteioFixo(0.3f));
    Batalha batalha = jogo.mover(Direcao.DIR);

    jogo.getTreinador().adicionarPokebola(new MasterBall());
    batalha.tentarCaptura();

    assertEquals(List.of(batalha.getPokemon()), jogo.getTreinador().getPokemons());
  }
}

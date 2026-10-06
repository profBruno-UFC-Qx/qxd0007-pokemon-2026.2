package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Posicao;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TesteJogo {

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

    for (int i = 0; i < 10; i++) {
      assertEquals(jogoA.iniciarBatalha().getPokemon(), jogoB.iniciarBatalha().getPokemon());
    }
  }
}

import br.ufc.qx.pokemon.Direcao;
import br.ufc.qx.pokemon.Jogo;
import br.ufc.qx.pokemon.mapa.Posicao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TesteJogo {

  @Test
  public void processarComandoDirMoveTreinadorParaDireita() {
    Jogo jogo = new Jogo("Ash");
    Direcao direcao = jogo.processarComando("dir");
    assertEquals(Direcao.DIR, direcao);
    assertEquals(new Posicao(1, 0), jogo.getTreinador().getPosicao());
  }

  @Test
  public void processarComandoEsqNaoMoveTreinadorParaForaDoMapa() {
    Jogo jogo = new Jogo("Ash");
    jogo.processarComando("ESQ");
    assertEquals(new Posicao(0, 0), jogo.getTreinador().getPosicao());
  }

  @Test
  public void processarComandoSairRetornaDirecaoSairSemMover() {
    Jogo jogo = new Jogo("Ash");
    Direcao direcao = jogo.processarComando("SAIR");
    assertEquals(Direcao.SAIR, direcao);
    assertEquals(new Posicao(0, 0), jogo.getTreinador().getPosicao());
  }

  @Test
  public void processarComandoInvalidoNaoMoveERetornaNulo() {
    Jogo jogo = new Jogo("Ash");

    Direcao direcao = jogo.processarComando("VOAR");

    assertNull(direcao);
    assertEquals(new Posicao(0, 0), jogo.getTreinador().getPosicao());
  }

  @Test
  public void processarComandoCimaMoveTreinadorParaCima() {
    Jogo jogo = new Jogo("Ash");
    jogo.processarComando("DIR");
    jogo.processarComando("BAIXO");
    jogo.processarComando("CIMA");
    assertEquals(new Posicao(1, 0), jogo.getTreinador().getPosicao());
  }

  @Test
  public void renderizarMapaMostraTreinadorNaPosicaoAtual() {
    Jogo jogo = new Jogo("Ash");
    jogo.processarComando("DIR");

    String[] linhas = jogo.renderizarMapa().split("\n");

    // Treinador em x=1, y=0: cada célula ocupa 2 caracteres, depois do '|' inicial.
    assertEquals('T', linhas[0].charAt(1 + 2 * 1));
  }
}

import br.ufc.qx.pokemon.Direcao;
import br.ufc.qx.pokemon.Jogo;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TesteJogo {

  @Test
  public void processarComandoDirMoveTreinadorParaDireita() {
    Jogo jogo = new Jogo("Ash");
    Direcao direcao = jogo.processarComando("dir");
    assertEquals(Direcao.DIR, direcao);
    assertEquals(1, jogo.getTreinador().getX());
  }

  @Test
  public void processarComandoEsqNaoMoveTreinadorParaForaDoMapa() {
    Jogo jogo = new Jogo("Ash");
    jogo.processarComando("ESQ");
    assertEquals(0, jogo.getTreinador().getX());
    assertEquals(0, jogo.getTreinador().getY());
  }

  @Test
  public void processarComandoSairRetornaDirecaoSairSemMover() {
    Jogo jogo = new Jogo("Ash");
    Direcao direcao = jogo.processarComando("SAIR");
    assertEquals(Direcao.SAIR, direcao);
    assertEquals(0, jogo.getTreinador().getX());
    assertEquals(0, jogo.getTreinador().getY());
  }

  @Test
  public void processarComandoInvalidoNaoMoveERetornaNulo() {
    Jogo jogo = new Jogo("Ash");

    ByteArrayOutputStream saida = new ByteArrayOutputStream();
    PrintStream original = System.out;
    System.setOut(new PrintStream(saida));
    Direcao direcao;
    try {
      direcao = jogo.processarComando("VOAR");
    } finally {
      System.setOut(original);
    }

    assertNull(direcao);
    assertEquals(0, jogo.getTreinador().getX());
    assertEquals(0, jogo.getTreinador().getY());
    assertTrue(saida.toString().contains("Valor invalido"));
  }

  @Test
  public void processarComandoCimaMoveTreinadorParaCima() {
    Jogo jogo = new Jogo("Ash");
    jogo.processarComando("DIR");
    jogo.processarComando("BAIXO");
    jogo.processarComando("CIMA");
    assertEquals(1, jogo.getTreinador().getX());
    assertEquals(0, jogo.getTreinador().getY());
  }
}

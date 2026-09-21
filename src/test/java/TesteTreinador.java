import br.ufc.qx.pokemon.Treinador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TesteTreinador {

  @Test
  public void moverParaBaixo() {
    Treinador treinador = new Treinador("Ash");
    treinador.mover("BAIXO");
    assertEquals(treinador.getX(), 0);
    assertEquals(treinador.getY(), 1);
  }
}

import br.ufc.qx.pokemon.Mapa;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TesteMapa {

  @Test
  public void moverParaPosicaoValida() {
    Mapa mapa = new Mapa(5,5 );
    assertTrue(mapa.ePosicaoValida(2, 2), "A posição é válida");
  }

  @Test
  public void moverParaPosicaoInvalida() {
    Mapa mapa = new Mapa(5,5 );
    assertFalse(mapa.ePosicaoValida(0, 5), "Essa posição é inválida");
  }
}

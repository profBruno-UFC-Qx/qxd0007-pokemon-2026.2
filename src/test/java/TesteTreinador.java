import br.ufc.qx.pokemon.Pokemon;
import br.ufc.qx.pokemon.Treinador;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TesteTreinador {

  @Test
  public void moverParaBaixo() {
    Treinador treinador = new Treinador("Ash");
    treinador.moverPara(0, 1);
    assertEquals(treinador.getX(), 0);
    assertEquals(treinador.getY(), 1);
  }

  @Test
  public void getNomeRetornaNomeInformadoNoConstrutor() {
    Treinador treinador = new Treinador("Ash");
    assertEquals("Ash", treinador.getNome());
  }

  @Test
  public void capturarAdicionaPokemonEnquantoHouverEspaco() {
    Treinador treinador = new Treinador("Ash");
    for (int i = 0; i < 6; i++) {
      assertTrue(treinador.capturar(new Pokemon("Pokemon" + i, "normal", 1)));
    }
  }

  @Test
  public void capturarFalhaQuandoEquipeEstaCheia() {
    Treinador treinador = new Treinador("Ash");
    for (int i = 0; i < 6; i++) {
      treinador.capturar(new Pokemon("Pokemon" + i, "normal", 1));
    }
    assertFalse(treinador.capturar(new Pokemon("Extra", "normal", 1)));
  }

  @Test
  public void listarNaoImprimeSlotsVaziosDaEquipe() {
    Treinador treinador = new Treinador("Ash");
    treinador.capturar(new Pokemon("Pikachu", "electric", 5));

    ByteArrayOutputStream saida = new ByteArrayOutputStream();
    PrintStream original = System.out;
    System.setOut(new PrintStream(saida));
    try {
      treinador.listar();
    } finally {
      System.setOut(original);
    }

    String[] linhas = saida.toString().trim().split(System.lineSeparator());
    assertEquals(1, linhas.length);
    assertFalse(saida.toString().contains("null"));
  }
}

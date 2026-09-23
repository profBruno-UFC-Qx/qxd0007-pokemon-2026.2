package br.ufc.qx.pokemon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestePokemon {

  @Test
  public void pokemonNasceComVidaCheiaEmFuncaoDoNivel() {
    Pokemon pikachu = new Pokemon("Pikachu", "electric", 5);

    // level * (25 + level) = 5 * 30
    assertEquals(150, pikachu.getHp());
  }

  @Test
  public void pokemonCriadoSemNivelComecaNoNivelUm() {
    Pokemon bulbasaur = new Pokemon("Bulbasaur", 0.7f, 6.9f, "grass/poison");

    // level * (25 + level) = 1 * 26
    assertEquals(26, bulbasaur.getHp());
  }

  @Test
  public void setHpAcimaDoMaximoEIgnorado() {
    Pokemon pikachu = new Pokemon("Pikachu", "electric", 5);

    pikachu.setHp(151);

    assertEquals(150, pikachu.getHp());
  }

  @Test
  public void setHpAbaixoDoMaximoEAceito() {
    Pokemon pikachu = new Pokemon("Pikachu", "electric", 5);

    pikachu.setHp(40);

    assertEquals(40, pikachu.getHp());
  }
}

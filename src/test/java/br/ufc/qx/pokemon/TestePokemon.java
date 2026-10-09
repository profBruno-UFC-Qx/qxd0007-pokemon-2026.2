package br.ufc.qx.pokemon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestePokemon {

  @Test
  public void pokemonNasceComVidaCheiaEmFuncaoDaEspecieEDoNivel() {
    Pokemon pikachu = new Pokemon(Especie.PIKACHU, 5);

    // 2 * hpBase * nivel / 100 + nivel + 10 = 2 * 35 * 5 / 100 + 5 + 10
    assertEquals(18, pikachu.getHp());
  }

  @Test
  public void pokemonCriadoSemNivelComecaNoNivelUm() {
    Pokemon bulbasaur = new Pokemon(Especie.BULBASAUR);

    // 2 * hpBase * nivel / 100 + nivel + 10 = 2 * 45 * 1 / 100 + 1 + 10
    assertEquals(11, bulbasaur.getHp());
  }

  @Test
  public void setHpAcimaDoMaximoEIgnorado() {
    Pokemon pikachu = new Pokemon(Especie.PIKACHU, 5);

    pikachu.setHp(19);

    assertEquals(18, pikachu.getHp());
  }

  @Test
  public void setHpAbaixoDoMaximoEAceito() {
    Pokemon pikachu = new Pokemon(Especie.PIKACHU, 5);

    pikachu.setHp(10);

    assertEquals(10, pikachu.getHp());
  }

  @Test
  public void getNomeEGetNivelRetornamValoresDoConstrutor() {
    Pokemon pikachu = new Pokemon(Especie.PIKACHU, 5);

    assertEquals("Pikachu", pikachu.getNome());
    assertEquals(5, pikachu.getNivel());
  }

  @Test
  public void caracteristicasVemDaEspecie() {
    Pokemon snorlax = new Pokemon(Especie.SNORLAX, 5);

    assertEquals("Snorlax", snorlax.getNome());
    assertEquals(25, snorlax.getTaxaDaEspecie());
    assertEquals(30, snorlax.getVelocidade());
    assertEquals(460.0, snorlax.getPeso());
  }
}

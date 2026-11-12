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

  @Test
  public void getHpMaxDependeDaEspecieEDoNivel() {
    Pokemon pikachu = new Pokemon(Especie.PIKACHU, 50);

    // 2 * hpBase * nivel / 100 + nivel + 10 = 2 * 35 * 50 / 100 + 50 + 10
    assertEquals(95, pikachu.getHpMax());
  }

  @Test
  public void getHpMaxNaoMudaQuandoOHpDiminui() {
    Pokemon pikachu = new Pokemon(Especie.PIKACHU, 50);

    pikachu.setHp(1);

    assertEquals(95, pikachu.getHpMax());
  }

  @Test
  public void pokemonComecaComAAmizadeBaseDaEspecie() {
    assertEquals(70, new Pokemon(Especie.PIKACHU, 5).getAmizade());
    assertEquals(100, new Pokemon(Especie.MEW, 5).getAmizade());
    assertEquals(0, new Pokemon(Especie.MEWTWO, 5).getAmizade());
  }

  @Test
  public void setAmizadeAceitaValoresDe0A255() {
    Pokemon pikachu = new Pokemon(Especie.PIKACHU, 5);

    pikachu.setAmizade(0);
    assertEquals(0, pikachu.getAmizade());

    pikachu.setAmizade(255);
    assertEquals(255, pikachu.getAmizade());
  }

  @Test
  public void setAmizadeForaDaFaixaEIgnorado() {
    Pokemon pikachu = new Pokemon(Especie.PIKACHU, 5);

    pikachu.setAmizade(-1);
    assertEquals(70, pikachu.getAmizade());

    pikachu.setAmizade(256);
    assertEquals(70, pikachu.getAmizade());
  }

  @Test
  public void setAmizadeAceitaValoresAbaixoDaAmizadeBase() {
    Pokemon pikachu = new Pokemon(Especie.PIKACHU, 5);

    pikachu.setAmizade(10);

    assertEquals(10, pikachu.getAmizade());
  }

  @Test
  public void setAmizadeForaDaFaixaMantemOValorAtual() {
    Pokemon pikachu = new Pokemon(Especie.PIKACHU, 5);
    pikachu.setAmizade(200);

    pikachu.setAmizade(300);
    assertEquals(200, pikachu.getAmizade());

    pikachu.setAmizade(-5);
    assertEquals(200, pikachu.getAmizade());
  }
}

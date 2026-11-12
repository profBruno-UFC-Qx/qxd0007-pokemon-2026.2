package br.ufc.qx.pokemon.pokebola;

import br.ufc.qx.pokemon.Especie;
import br.ufc.qx.pokemon.Pokemon;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestePokebola {

  private static final double DELTA = 0.01;

  // Random de teste: devolve sempre o mesmo inteiro, para o sorteio da captura ser previsível.
  // Sobrescreve as duas formas de sortear um inteiro numa faixa, nextInt(256) e nextInt(0, 256),
  // e falha se a faixa pedida não for de 0 a 255.
  private static class SorteioFixo extends Random {

    private final int valor;

    SorteioFixo(int valor) {
      this.valor = valor;
    }

    @Override
    public int nextInt(int bound) {
      return nextInt(0, bound);
    }

    @Override
    public int nextInt(int origin, int bound) {
      assertEquals(0, origin, "O sorteio deve começar em 0");
      assertEquals(256, bound, "O sorteio deve ir até 255 (inclusive)");
      return valor;
    }
  }

  private static final int MELHOR_SORTEIO = 0;
  private static final int PIOR_SORTEIO = 255;

  // HP máximo do Pikachu no nível 50: 2 * 35 * 50 / 100 + 50 + 10 = 95
  private final Pokemon pikachu = new Pokemon(Especie.PIKACHU, 50);

  private static Pokemon comUmDeHp(Especie especie) {
    Pokemon pokemon = new Pokemon(especie, 50);
    pokemon.setHp(1);
    return pokemon;
  }

  @Test
  public void getNomeIdentificaCadaTipoDePokebola() {
    assertEquals("Pokebola", new Pokebola().getNome());
    assertEquals("GreatBall", new GreatBall().getNome());
    assertEquals("UltraBall", new UltraBall().getNome());
    assertEquals("MasterBall", new MasterBall().getNome());
    assertEquals("FastBall", new FastBall().getNome());
    assertEquals("HeavyBall", new HeavyBall().getNome());
    assertEquals("FriendBall", new FriendBall().getNome());
    assertEquals("HealBall", new HealBall().getNome());
  }

  // ---------- Fórmula de captura ----------

  // HP cheio: (3 * 95 - 2 * 95) / (3 * 95) * 190 * 1 = 63.33
  @Test
  public void taxaDaPokebolaComHpCheioEUmTercoDaTaxaDaEspecie() {
    assertEquals(63.33, new Pokebola().getTaxaDeCaptura(pikachu), DELTA);
  }

  // HP 1: (3 * 95 - 2 * 1) / (3 * 95) * 190 * 1 = 188.67
  @Test
  public void taxaAumentaQuandoOPokemonEstaComPoucoHp() {
    assertEquals(188.67, new Pokebola().getTaxaDeCaptura(comUmDeHp(Especie.PIKACHU)), DELTA);
  }

  // Com HP cheio o primeiro fator vale sempre 1/3, qualquer que seja o nível.
  @Test
  public void nivelNaoInfluenciaATaxaQuandoOHpEstaCheio() {
    Pokebola pokebola = new Pokebola();
    Pokemon nivel1 = new Pokemon(Especie.PIKACHU, 1);
    Pokemon nivel100 = new Pokemon(Especie.PIKACHU, Pokemon.NIVEL_MAXIMO);

    assertEquals(pokebola.getTaxaDeCaptura(nivel1), pokebola.getTaxaDeCaptura(nivel100), DELTA);
  }

  // HP 1: (3 * 95 - 2 * 1) / (3 * 95) * 255 * 2 = 506.42, limitado a 255
  @Test
  public void taxaNuncaPassaDe255() {
    assertEquals(255, new UltraBall().getTaxaDeCaptura(comUmDeHp(Especie.BELLSPROUT)), DELTA);
  }

  // Zapdos pesa 52.6 kg: taxa da espécie 3 - 20 = -17, que vira 1; 1/3 * 1 * 1 = 0.33, limitado a 1
  @Test
  public void taxaNuncaFicaAbaixoDe1() {
    assertEquals(1, new HeavyBall().getTaxaDeCaptura(new Pokemon(Especie.ZAPDOS, 50)), DELTA);
  }

  // ---------- Sorteio ----------

  // Taxa 63.33: captura para os sorteios de 0 a 63.
  @Test
  public void pokebolaCapturaQuandoSorteioEMenorOuIgualATaxa() {
    assertTrue(new Pokebola(new SorteioFixo(0)).capturar(pikachu));
    assertTrue(new Pokebola(new SorteioFixo(63)).capturar(pikachu));
  }

  @Test
  public void pokebolaNaoCapturaQuandoSorteioPassaDaTaxa() {
    assertFalse(new Pokebola(new SorteioFixo(64)).capturar(pikachu));
  }

  // Mewtwo: 1/3 * 3 * 1 = 1, então só os sorteios 0 e 1 capturam.
  @Test
  public void sorteioIgualATaxaCaptura() {
    Pokemon mewtwo = new Pokemon(Especie.MEWTWO, 50);

    assertTrue(new Pokebola(new SorteioFixo(1)).capturar(mewtwo));
    assertFalse(new Pokebola(new SorteioFixo(2)).capturar(mewtwo));
  }

  @Test
  public void pokemonComPoucoHpECapturadoComSorteioEmQueOHpCheioEscapa() {
    assertFalse(new Pokebola(new SorteioFixo(100)).capturar(pikachu));
    assertTrue(new Pokebola(new SorteioFixo(100)).capturar(comUmDeHp(Especie.PIKACHU)));
  }

  // Com uma semente qualquer, o sorteio precisa cobrir de 0 a 255, inclusive os extremos.
  @Test
  public void sorteioDaCapturaVaiDe0A255() {
    Set<Integer> sorteados = new HashSet<>();
    Random random = new Random(42) {
      @Override
      public int nextInt(int bound) {
        int n = super.nextInt(bound);
        sorteados.add(n);
        return n;
      }

      @Override
      public int nextInt(int origin, int bound) {
        int n = super.nextInt(origin, bound);
        sorteados.add(n);
        return n;
      }
    };
    Pokebola pokebola = new Pokebola(random);

    for (int i = 0; i < 10_000; i++) {
      pokebola.capturar(pikachu);
    }

    assertEquals(256, sorteados.size());
    assertTrue(sorteados.contains(0));
    assertTrue(sorteados.contains(255));
  }

  @Test
  public void pokebolasComMesmaSementeTemOMesmoResultado() {
    Pokebola pokebolaA = new Pokebola(new Random(42));
    Pokebola pokebolaB = new Pokebola(new Random(42));

    for (int i = 0; i < 20; i++) {
      assertEquals(pokebolaA.capturar(pikachu), pokebolaB.capturar(pikachu));
    }
  }

  // ---------- GreatBall, UltraBall e MasterBall ----------

  // 1/3 * 190 * 1.5 = 95
  @Test
  public void greatBallMultiplicaATaxaPor1Emeio() {
    assertEquals(95, new GreatBall().getTaxaDeCaptura(pikachu), DELTA);
    assertTrue(new GreatBall(new SorteioFixo(95)).capturar(pikachu));
    assertFalse(new GreatBall(new SorteioFixo(96)).capturar(pikachu));
  }

  // 1/3 * 190 * 2 = 126.67
  @Test
  public void ultraBallMultiplicaATaxaPor2() {
    assertEquals(126.67, new UltraBall().getTaxaDeCaptura(pikachu), DELTA);
    assertTrue(new UltraBall(new SorteioFixo(126)).capturar(pikachu));
    assertFalse(new UltraBall(new SorteioFixo(127)).capturar(pikachu));
  }

  @Test
  public void masterBallTemTaxa255() {
    assertEquals(255, new MasterBall().getTaxaDeCaptura(new Pokemon(Especie.MEWTWO, 50)), DELTA);
  }

  @Test
  public void masterBallCapturaMesmoComOPiorSorteio() {
    Pokemon mewtwo = new Pokemon(Especie.MEWTWO, Pokemon.NIVEL_MAXIMO);

    assertTrue(new MasterBall(new SorteioFixo(PIOR_SORTEIO)).capturar(mewtwo));
  }

  @Test
  public void pokebolasMelhoresCapturamComSorteiosEmQueAsPioresFalham() {
    assertFalse(new Pokebola(new SorteioFixo(80)).capturar(pikachu));
    assertTrue(new GreatBall(new SorteioFixo(80)).capturar(pikachu));

    assertFalse(new GreatBall(new SorteioFixo(110)).capturar(pikachu));
    assertTrue(new UltraBall(new SorteioFixo(110)).capturar(pikachu));

    assertFalse(new UltraBall(new SorteioFixo(200)).capturar(pikachu));
    assertTrue(new MasterBall(new SorteioFixo(200)).capturar(pikachu));
  }

  // ---------- FastBall ----------

  // Mew tem velocidade 100: 1/3 * 45 * 4 = 60
  @Test
  public void fastBallMultiplicaPor4QuandoAVelocidadeEPeloMenos100() {
    assertEquals(60, new FastBall().getTaxaDeCaptura(new Pokemon(Especie.MEW, 50)), DELTA);
  }

  // Pikachu tem velocidade 90: 1/3 * 190 * 1 = 63.33
  @Test
  public void fastBallFuncionaComoPokebolaComumQuandoAVelocidadeEMenorQue100() {
    assertEquals(63.33, new FastBall().getTaxaDeCaptura(pikachu), DELTA);
  }

  @Test
  public void fastBallCapturaPokemonRapidoComSorteioEmQueAPokebolaFalha() {
    Pokemon mew = new Pokemon(Especie.MEW, 50);

    assertFalse(new Pokebola(new SorteioFixo(30)).capturar(mew));
    assertTrue(new FastBall(new SorteioFixo(30)).capturar(mew));
  }

  // ---------- HeavyBall ----------

  // Pikachu pesa 6 kg: 1/3 * (190 - 20) = 56.67
  @Test
  public void heavyBallSubtrai20DaTaxaDaEspecieAbaixoDe100Kg() {
    assertEquals(56.67, new HeavyBall().getTaxaDeCaptura(pikachu), DELTA);
  }

  // Venusaur pesa 100 kg: 1/3 * (45 + 0) = 15
  @Test
  public void heavyBallNaoAlteraATaxaDaEspecieEntre100E200Kg() {
    assertEquals(15, new HeavyBall().getTaxaDeCaptura(new Pokemon(Especie.VENUSAUR, 50)), DELTA);
  }

  // Onix pesa 210 kg: 1/3 * (45 + 20) = 21.67
  @Test
  public void heavyBallSoma20ATaxaDaEspecieEntre200E300Kg() {
    assertEquals(21.67, new HeavyBall().getTaxaDeCaptura(new Pokemon(Especie.ONIX, 50)), DELTA);
  }

  // Golem pesa 300 kg: 1/3 * (45 + 30) = 25
  @Test
  public void heavyBallSoma30ATaxaDaEspecieAPartirDe300Kg() {
    assertEquals(25, new HeavyBall().getTaxaDeCaptura(new Pokemon(Especie.GOLEM, 50)), DELTA);
  }

  // Snorlax pesa 460 kg: 1/3 * (25 + 30) = 18.33
  @Test
  public void heavyBallCapturaPokemonPesadoComSorteioEmQueAPokebolaFalha() {
    Pokemon snorlax = new Pokemon(Especie.SNORLAX, 50);

    assertFalse(new Pokebola(new SorteioFixo(18)).capturar(snorlax));
    assertTrue(new HeavyBall(new SorteioFixo(18)).capturar(snorlax));
  }

  // ---------- FriendBall ----------

  @Test
  public void friendBallTemTaxaDePokebolaComum() {
    assertEquals(63.33, new FriendBall().getTaxaDeCaptura(pikachu), DELTA);
  }

  @Test
  public void friendBallMudaAAmizadeDoPokemonCapturadoPara200() {
    assertTrue(new FriendBall(new SorteioFixo(MELHOR_SORTEIO)).capturar(pikachu));

    assertEquals(200, pikachu.getAmizade());
  }

  @Test
  public void friendBallNaoMudaAAmizadeQuandoOPokemonEscapa() {
    assertFalse(new FriendBall(new SorteioFixo(PIOR_SORTEIO)).capturar(pikachu));

    assertEquals(70, pikachu.getAmizade());
  }

  // ---------- HealBall ----------

  @Test
  public void healBallTemTaxaDePokebolaComum() {
    assertEquals(63.33, new HealBall().getTaxaDeCaptura(pikachu), DELTA);
  }

  @Test
  public void healBallRestauraOHpDoPokemonCapturado() {
    Pokemon ferido = comUmDeHp(Especie.PIKACHU);

    assertTrue(new HealBall(new SorteioFixo(MELHOR_SORTEIO)).capturar(ferido));

    assertEquals(ferido.getHpMax(), ferido.getHp());
  }

  @Test
  public void healBallNaoRestauraOHpQuandoOPokemonEscapa() {
    Pokemon ferido = comUmDeHp(Especie.PIKACHU);

    assertFalse(new HealBall(new SorteioFixo(PIOR_SORTEIO)).capturar(ferido));

    assertEquals(1, ferido.getHp());
  }
}

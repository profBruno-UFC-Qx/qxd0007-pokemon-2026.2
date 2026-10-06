package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Mapa;
import br.ufc.qx.pokemon.mapa.Posicao;
import br.ufc.qx.pokemon.pokebola.GreatBall;
import br.ufc.qx.pokemon.pokebola.MasterBall;
import br.ufc.qx.pokemon.pokebola.Pokebola;
import br.ufc.qx.pokemon.pokebola.UltraBall;

public class Jogo {

  private static final int LARGURA_MAPA = 10;
  private static final int ALTURA_MAPA = 5;

  private Treinador treinador;
  private Mapa mapa;
  private GeradorDePokemon geradorDePokemon;

  public Jogo(String nome) {
    treinador = new Treinador(nome);
    treinador.adicionarPokebola(new Pokebola());
    treinador.adicionarPokebola(new GreatBall());
    treinador.adicionarPokebola(new UltraBall());
    treinador.adicionarPokebola(new MasterBall());

    mapa = new Mapa(LARGURA_MAPA, ALTURA_MAPA);
    geradorDePokemon = new GeradorDePokemon();
  }

  public Treinador getTreinador() {
    return treinador;
  }

  public String renderizarMapa() {
    return mapa.renderizar(treinador.getPosicao());
  }

  public void mover(Direcao direcao) {
    Posicao destino = direcao.aplicarEm(treinador.getPosicao());
    if (mapa.ePosicaoValida(destino)) {
      treinador.moverPara(destino);
    }
  }

  public Batalha iniciarBatalha() {
    Pokemon encontrado = geradorDePokemon.gerar();
    return new Batalha(treinador, encontrado);
  }

  public boolean encontrouPokemon() {
    return mapa.encontrouPokemon(treinador.getPosicao());
  }
}

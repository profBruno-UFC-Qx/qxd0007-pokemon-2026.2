package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Mapa;
import br.ufc.qx.pokemon.mapa.Posicao;

public class Jogo {

  private static final int LARGURA_MAPA = 10;
  private static final int ALTURA_MAPA = 5;

  private Treinador treinador;
  private Mapa mapa;

  public Jogo(String nome) {
    treinador = new Treinador(nome);
    mapa = new Mapa(LARGURA_MAPA, ALTURA_MAPA);
  }

  public Treinador getTreinador() {
    return treinador;
  }

  public String renderizarMapa() {
    return mapa.renderizar(treinador.getPosicao());
  }

  public Direcao processarComando(String opcao) {
    Direcao direcao;
    try {
      direcao = Direcao.valueOf(opcao.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
    Posicao atual = treinador.getPosicao();
    Posicao destino = switch (direcao) {
      case CIMA -> atual.deslocar(0, -1);
      case BAIXO -> atual.deslocar(0, 1);
      case DIR -> atual.deslocar(1, 0);
      case ESQ -> atual.deslocar(-1, 0);
      case SAIR -> atual;
    };
    if (mapa.ePosicaoValida(destino)) {
      treinador.moverPara(destino);
    }
    return direcao;
  }
}

package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Mapa;

public class Jogo {

  private Treinador treinador;
  private Mapa mapa;

  public Jogo(String nome) {
    treinador = new Treinador(nome);
    mapa = new Mapa(10, 5);
  }

  public Treinador getTreinador() {
    return treinador;
  }

  public String renderizarMapa() {
    return mapa.renderizar(treinador.getX(), treinador.getY());
  }

  public Direcao processarComando(String opcao) {
    Direcao direcao;
    try {
      direcao = Direcao.valueOf(opcao.toUpperCase());
    } catch (IllegalArgumentException e) {
      return null;
    }
    int novoX = treinador.getX();
    int novoY = treinador.getY();
    switch (direcao) {
      case CIMA -> novoY = treinador.getY() - 1;
      case BAIXO -> novoY = treinador.getY() + 1;
      case DIR -> novoX = treinador.getX() + 1;
      case ESQ -> novoX = treinador.getX() - 1;
      case SAIR -> {}
    }
    if (mapa.ePosicaoValida(novoX, novoY)) {
      treinador.moverPara(novoX, novoY);
    }
    return direcao;
  }
}

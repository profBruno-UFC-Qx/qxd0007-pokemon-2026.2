package br.ufc.qx.pokemon;

import br.ufc.qx.pokemon.mapa.Posicao;

public enum Direcao {
  CIMA(0, -1),
  BAIXO(0, 1),
  ESQ(-1, 0),
  DIR(1, 0);

  private final int dx;
  private final int dy;

  Direcao(int dx, int dy) {
    this.dx = dx;
    this.dy = dy;
  }

  public Posicao aplicarEm(Posicao origem) {
    return origem.deslocar(dx, dy);
  }

  public static boolean eDirecaoValida(String texto) {
    return procurar(texto) != null;
  }

  public static Direcao get(String texto) {
    Direcao direcao = procurar(texto);
    if (direcao == null) {
      throw new IllegalArgumentException("Direção inválida: " + texto);
    }
    return direcao;
  }

  private static Direcao procurar(String texto) {
    for (Direcao direcao : values()) {
      if (direcao.name().equalsIgnoreCase(texto)) {
        return direcao;
      }
    }
    return null;
  }
}

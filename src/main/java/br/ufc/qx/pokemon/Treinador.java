package br.ufc.qx.pokemon;

public class Treinador {

  private String nome;
  private int x,y;
  private Pokemon[] pokemons;
  private int qtdPokemons;

  public Treinador(String nome) {
    this.nome = nome;
    this.x = 0;
    this.y = 0;
    this.pokemons = new Pokemon[6];
  }

  public String getNome() {
    return nome;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void moverPara(int x, int y) {
    setX(x);
    setY(y);
  }

  public void listar() {
    for(Pokemon pokemon : pokemons) {
      System.out.println(pokemon);
    }
  }

  public boolean capturar(Pokemon pokemon) {
    if(pokemons[pokemons.length - 1] == null) {
      pokemons[qtdPokemons++] = pokemon;
      return true;
    }
    return false;
  }

}

package br.ufc.qx.pokemon;

// As 151 espécies da primeira geração, com os dados do jogo publicados pela PokeAPI (https://pokeapi.co).
public enum Especie {
  BULBASAUR("Bulbasaur", 45, 45, 45, 6.9, 70),
  IVYSAUR("Ivysaur", 45, 60, 60, 13.0, 70),
  VENUSAUR("Venusaur", 45, 80, 80, 100.0, 70),
  CHARMANDER("Charmander", 45, 39, 65, 8.5, 70),
  CHARMELEON("Charmeleon", 45, 58, 80, 19.0, 70),
  CHARIZARD("Charizard", 45, 78, 100, 90.5, 70),
  SQUIRTLE("Squirtle", 45, 44, 43, 9.0, 70),
  WARTORTLE("Wartortle", 45, 59, 58, 22.5, 70),
  BLASTOISE("Blastoise", 45, 79, 78, 85.5, 70),
  CATERPIE("Caterpie", 255, 45, 45, 2.9, 70),
  METAPOD("Metapod", 120, 50, 30, 9.9, 70),
  BUTTERFREE("Butterfree", 45, 60, 70, 32.0, 70),
  WEEDLE("Weedle", 255, 40, 50, 3.2, 70),
  KAKUNA("Kakuna", 120, 45, 35, 10.0, 70),
  BEEDRILL("Beedrill", 45, 65, 75, 29.5, 70),
  PIDGEY("Pidgey", 255, 40, 56, 1.8, 70),
  PIDGEOTTO("Pidgeotto", 120, 63, 71, 30.0, 70),
  PIDGEOT("Pidgeot", 45, 83, 101, 39.5, 70),
  RATTATA("Rattata", 255, 30, 72, 3.5, 70),
  RATICATE("Raticate", 127, 55, 97, 18.5, 70),
  SPEAROW("Spearow", 255, 40, 70, 2.0, 70),
  FEAROW("Fearow", 90, 65, 100, 38.0, 70),
  EKANS("Ekans", 255, 35, 55, 6.9, 70),
  ARBOK("Arbok", 90, 60, 80, 65.0, 70),
  PIKACHU("Pikachu", 190, 35, 90, 6.0, 70),
  RAICHU("Raichu", 75, 60, 110, 30.0, 70),
  SANDSHREW("Sandshrew", 255, 50, 40, 12.0, 70),
  SANDSLASH("Sandslash", 90, 75, 65, 29.5, 70),
  NIDORAN_F("Nidoran♀", 235, 55, 41, 7.0, 70),
  NIDORINA("Nidorina", 120, 70, 56, 20.0, 70),
  NIDOQUEEN("Nidoqueen", 45, 90, 76, 60.0, 70),
  NIDORAN_M("Nidoran♂", 235, 46, 50, 9.0, 70),
  NIDORINO("Nidorino", 120, 61, 65, 19.5, 70),
  NIDOKING("Nidoking", 45, 81, 85, 62.0, 70),
  CLEFAIRY("Clefairy", 150, 70, 35, 7.5, 140),
  CLEFABLE("Clefable", 25, 95, 60, 40.0, 140),
  VULPIX("Vulpix", 190, 38, 65, 9.9, 70),
  NINETALES("Ninetales", 75, 73, 100, 19.9, 70),
  JIGGLYPUFF("Jigglypuff", 170, 115, 20, 5.5, 70),
  WIGGLYTUFF("Wigglytuff", 50, 140, 45, 12.0, 70),
  ZUBAT("Zubat", 255, 40, 55, 7.5, 70),
  GOLBAT("Golbat", 90, 75, 90, 55.0, 70),
  ODDISH("Oddish", 255, 45, 30, 5.4, 70),
  GLOOM("Gloom", 120, 60, 40, 8.6, 70),
  VILEPLUME("Vileplume", 45, 75, 50, 18.6, 70),
  PARAS("Paras", 190, 35, 25, 5.4, 70),
  PARASECT("Parasect", 75, 60, 30, 29.5, 70),
  VENONAT("Venonat", 190, 60, 45, 30.0, 70),
  VENOMOTH("Venomoth", 75, 70, 90, 12.5, 70),
  DIGLETT("Diglett", 255, 10, 95, 0.8, 70),
  DUGTRIO("Dugtrio", 50, 35, 120, 33.3, 70),
  MEOWTH("Meowth", 255, 40, 90, 4.2, 70),
  PERSIAN("Persian", 90, 65, 115, 32.0, 70),
  PSYDUCK("Psyduck", 190, 50, 55, 19.6, 70),
  GOLDUCK("Golduck", 75, 80, 85, 76.6, 70),
  MANKEY("Mankey", 190, 40, 70, 28.0, 70),
  PRIMEAPE("Primeape", 75, 65, 95, 32.0, 70),
  GROWLITHE("Growlithe", 190, 55, 60, 19.0, 70),
  ARCANINE("Arcanine", 75, 90, 95, 155.0, 70),
  POLIWAG("Poliwag", 255, 40, 90, 12.4, 70),
  POLIWHIRL("Poliwhirl", 120, 65, 90, 20.0, 70),
  POLIWRATH("Poliwrath", 45, 90, 70, 54.0, 70),
  ABRA("Abra", 200, 25, 90, 19.5, 70),
  KADABRA("Kadabra", 100, 40, 105, 56.5, 70),
  ALAKAZAM("Alakazam", 50, 55, 120, 48.0, 70),
  MACHOP("Machop", 180, 70, 35, 19.5, 70),
  MACHOKE("Machoke", 90, 80, 45, 70.5, 70),
  MACHAMP("Machamp", 45, 90, 55, 130.0, 70),
  BELLSPROUT("Bellsprout", 255, 50, 40, 4.0, 70),
  WEEPINBELL("Weepinbell", 120, 65, 55, 6.4, 70),
  VICTREEBEL("Victreebel", 45, 80, 70, 15.5, 70),
  TENTACOOL("Tentacool", 190, 40, 70, 45.5, 70),
  TENTACRUEL("Tentacruel", 60, 80, 100, 55.0, 70),
  GEODUDE("Geodude", 255, 40, 20, 20.0, 70),
  GRAVELER("Graveler", 120, 55, 35, 105.0, 70),
  GOLEM("Golem", 45, 80, 45, 300.0, 70),
  PONYTA("Ponyta", 190, 50, 90, 30.0, 70),
  RAPIDASH("Rapidash", 60, 65, 105, 95.0, 70),
  SLOWPOKE("Slowpoke", 190, 90, 15, 36.0, 70),
  SLOWBRO("Slowbro", 75, 95, 30, 78.5, 70),
  MAGNEMITE("Magnemite", 190, 25, 45, 6.0, 70),
  MAGNETON("Magneton", 60, 50, 70, 60.0, 70),
  FARFETCHD("Farfetch'd", 45, 52, 60, 15.0, 70),
  DODUO("Doduo", 190, 35, 75, 39.2, 70),
  DODRIO("Dodrio", 45, 60, 110, 85.2, 70),
  SEEL("Seel", 190, 65, 45, 90.0, 70),
  DEWGONG("Dewgong", 75, 90, 70, 120.0, 70),
  GRIMER("Grimer", 190, 80, 25, 30.0, 70),
  MUK("Muk", 75, 105, 50, 30.0, 70),
  SHELLDER("Shellder", 190, 30, 40, 4.0, 70),
  CLOYSTER("Cloyster", 60, 50, 70, 132.5, 70),
  GASTLY("Gastly", 190, 30, 80, 0.1, 70),
  HAUNTER("Haunter", 90, 45, 95, 0.1, 70),
  GENGAR("Gengar", 45, 60, 110, 40.5, 70),
  ONIX("Onix", 45, 35, 70, 210.0, 70),
  DROWZEE("Drowzee", 190, 60, 42, 32.4, 70),
  HYPNO("Hypno", 75, 85, 67, 75.6, 70),
  KRABBY("Krabby", 225, 30, 50, 6.5, 70),
  KINGLER("Kingler", 60, 55, 75, 60.0, 70),
  VOLTORB("Voltorb", 190, 40, 100, 10.4, 70),
  ELECTRODE("Electrode", 60, 60, 150, 66.6, 70),
  EXEGGCUTE("Exeggcute", 90, 60, 40, 2.5, 70),
  EXEGGUTOR("Exeggutor", 45, 95, 55, 120.0, 70),
  CUBONE("Cubone", 190, 50, 35, 6.5, 70),
  MAROWAK("Marowak", 75, 60, 45, 45.0, 70),
  HITMONLEE("Hitmonlee", 45, 50, 87, 49.8, 70),
  HITMONCHAN("Hitmonchan", 45, 50, 76, 50.2, 70),
  LICKITUNG("Lickitung", 45, 90, 30, 65.5, 70),
  KOFFING("Koffing", 190, 40, 35, 1.0, 70),
  WEEZING("Weezing", 60, 65, 60, 9.5, 70),
  RHYHORN("Rhyhorn", 120, 80, 25, 115.0, 70),
  RHYDON("Rhydon", 60, 105, 40, 120.0, 70),
  CHANSEY("Chansey", 30, 250, 50, 34.6, 140),
  TANGELA("Tangela", 45, 65, 60, 35.0, 70),
  KANGASKHAN("Kangaskhan", 45, 105, 90, 80.0, 70),
  HORSEA("Horsea", 225, 30, 60, 8.0, 70),
  SEADRA("Seadra", 75, 55, 85, 25.0, 70),
  GOLDEEN("Goldeen", 225, 45, 63, 15.0, 70),
  SEAKING("Seaking", 60, 80, 68, 39.0, 70),
  STARYU("Staryu", 225, 30, 85, 34.5, 70),
  STARMIE("Starmie", 60, 60, 115, 80.0, 70),
  MR_MIME("Mr. Mime", 45, 40, 90, 54.5, 70),
  SCYTHER("Scyther", 45, 70, 105, 56.0, 70),
  JYNX("Jynx", 45, 65, 95, 40.6, 70),
  ELECTABUZZ("Electabuzz", 45, 65, 105, 30.0, 70),
  MAGMAR("Magmar", 45, 65, 93, 44.5, 70),
  PINSIR("Pinsir", 45, 65, 85, 55.0, 70),
  TAUROS("Tauros", 45, 75, 110, 88.4, 70),
  MAGIKARP("Magikarp", 255, 20, 80, 10.0, 70),
  GYARADOS("Gyarados", 45, 95, 81, 235.0, 70),
  LAPRAS("Lapras", 45, 130, 60, 220.0, 70),
  DITTO("Ditto", 35, 48, 48, 4.0, 70),
  EEVEE("Eevee", 45, 55, 55, 6.5, 70),
  VAPOREON("Vaporeon", 45, 130, 65, 29.0, 70),
  JOLTEON("Jolteon", 45, 65, 130, 24.5, 70),
  FLAREON("Flareon", 45, 65, 65, 25.0, 70),
  PORYGON("Porygon", 45, 65, 40, 36.5, 70),
  OMANYTE("Omanyte", 45, 35, 35, 7.5, 70),
  OMASTAR("Omastar", 45, 70, 55, 35.0, 70),
  KABUTO("Kabuto", 45, 30, 55, 11.5, 70),
  KABUTOPS("Kabutops", 45, 60, 80, 40.5, 70),
  AERODACTYL("Aerodactyl", 45, 80, 130, 59.0, 70),
  SNORLAX("Snorlax", 25, 160, 30, 460.0, 70),
  ARTICUNO("Articuno", 3, 90, 85, 55.4, 35),
  ZAPDOS("Zapdos", 3, 90, 100, 52.6, 35),
  MOLTRES("Moltres", 3, 90, 90, 60.0, 35),
  DRATINI("Dratini", 45, 41, 50, 3.3, 35),
  DRAGONAIR("Dragonair", 45, 61, 70, 16.5, 35),
  DRAGONITE("Dragonite", 45, 91, 80, 210.0, 35),
  MEWTWO("Mewtwo", 3, 106, 130, 122.0, 0),
  MEW("Mew", 45, 100, 100, 4.0, 100);

  private final String nome;
  private final int taxaDeCaptura;
  private final int hpBase;
  private final int velocidade;
  private final double peso;
  private final int amizadeBase;

  Especie(String nome, int taxaDeCaptura, int hpBase, int velocidade, double peso, int amizadeBase) {
    this.nome = nome;
    this.taxaDeCaptura = taxaDeCaptura;
    this.hpBase = hpBase;
    this.velocidade = velocidade;
    this.peso = peso;
    this.amizadeBase = amizadeBase;
  }

  public String getNome() {
    return nome;
  }

  public int getTaxaDeCaptura() {
    return taxaDeCaptura;
  }

  public int getHpBase() {
    return hpBase;
  }

  public int getVelocidade() {
    return velocidade;
  }

  public double getPeso() {
    return peso;
  }

  public int getAmizadeBase() {
    return amizadeBase;
  }
}

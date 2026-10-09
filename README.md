# Pokemon — do código tudo-em-um à separação de responsabilidades em Java

Um treinador anda por um mapa desenhado no terminal e tenta capturar os Pokémon que encontra na grama. O jogo é pequeno de propósito: este repositório existe para ser **lido**, não jogado. Ele é dirigido a quem já escreveu classes com atributos, construtores e métodos em Java e agora precisa decidir *onde cada responsabilidade mora*. Ao acompanhar a evolução do código, você vê como o encapsulamento, a testabilidade, o objeto de valor, a herança e a separação entre regras do jogo e entrada/saída surgem de problemas concretos, e não de regras decoradas.

Os trechos abaixo vêm dos commits indicados (`git show <commit>:<arquivo>`), na versão de cada estágio, e podem diferir do código atual.

---

## 🏗️ Evolução da Arquitetura

O código parte de um laço único que lê texto, decide, move e imprime, e chega a classes pequenas, cada uma com uma razão para mudar e testes que não dependem do terminal.

### 1. Do texto solto ao `enum`: `Direcao` e `switch`

No começo (`2fe966c`), `Jogo.iniciar()` comparava o texto digitado com uma cadeia de `if / else if`, e o critério de parada era outra comparação, escrita de outro jeito:

`Jogo.java` @ `2fe966c`
```java
      } else if (!opcao.equalsIgnoreCase("SAIR")) {
        System.out.println("Valor invalido");
      }
      // ...
    } while("sair".equals(opcao) == false);
```

A primeira comparação ignora maiúsculas; a do laço, não. Digitar `SAIR` passava pela validação e o jogo continuava perguntando. O mesmo commit trazia um `enum` `Direcao` que ninguém usava. A correção (`3f857ff`) faz o texto virar `Direcao` num único lugar, e o resto do código passa a falar com o tipo:

`Direcao.java` @ `43d320f`
```java
public enum Direcao {
  CIMA, BAIXO, ESQ, DIR, SAIR
}
```

`Jogo.java` @ `43d320f`
```java
      String opcao = scanner.nextLine();
      try {
        direcao = Direcao.valueOf(opcao.toUpperCase());
      } catch (IllegalArgumentException e) {
        System.out.println("Valor invalido");
        continue;
      }
      switch (direcao) {
        case CIMA -> novoY = treinador.getY() - 1;
        case BAIXO -> novoY = treinador.getY() + 1;
        case DIR -> novoX = treinador.getX() + 1;
        case ESQ -> novoX = treinador.getX() - 1;
        case SAIR -> {}
      }
      // ...
    } while (direcao != Direcao.SAIR);
```



**Por que essa decisão?**
* Com `String`, escrever `"dirr"` compila e só falha (ou passa em silêncio) em tempo de execução. Com `enum`, um nome inexistente não compila, e o `switch` deixa claro quais casos existem.
* A regra "o que é uma direção válida" passa a morar em um lugar só. Na versão original havia duas comparações que discordavam sobre `SAIR`.

### 2. Código testável: `processarComando`, `Random` injetável e `renderizar`

Até aqui, testar o jogo exigia simular o teclado, e testar o mapa era impossível: o sorteio dos terrenos usava um `Random` criado dentro da classe. Além disso, `novoX` e `novoY` viviam fora do laço:

`Jogo.java` @ `43d320f`
```java
    Direcao direcao = null;
    int novoX = treinador.getX();
    int novoY = treinador.getY();
    do {
```

Esse estado guardado entre voltas do laço é um bug real. Com o treinador em `(0, 0)`, digitar `esq` deixava `novoX = -1` (recusado pelo mapa), e o `baixo` seguinte já saía com o `novoX` errado e também era recusado: o treinador ficava preso. O commit `914051c` extrai a decisão para um método que calcula tudo a partir do estado atual:

`Jogo.java` @ `914051c`
```java
  public Direcao processarComando(String opcao) {
    Direcao direcao;
    try {
      direcao = Direcao.valueOf(opcao.toUpperCase());
    } catch (IllegalArgumentException e) {
      System.out.println("Valor invalido");
      return null;
    }
    int novoX = treinador.getX();
    int novoY = treinador.getY();
```

No `Mapa`, o `Random` passa a ser um parâmetro, e desenhar o mapa vira "montar um texto" separado de "imprimir":

`Mapa.java` @ `914051c`
```java
  public Mapa(int largura, int altura) {
    this(largura, altura, new Random());
  }

  public Mapa(int largura, int altura, Random random) {
```

`Mapa.java` @ `914051c`
```java
  // Sem modificador de acesso: visível apenas para o pacote mapa (e seus testes),
  // não para o Jogo.
  String renderizar(int x, int y) {
```

`Mapa.java` @ `914051c`
```java
  public void exibirMapa(int x, int y) {
    System.out.print(renderizar(x, y));
  }
```

Agora um teste pode fixar a semente e comparar textos:

`TesteMapa.java` @ `914051c`
```java
  @Test
  public void mapasComMesmoSeedGeramMesmoConteudo() {
    Mapa mapaA = new Mapa(4, 4, new Random(7));
    Mapa mapaB = new Mapa(4, 4, new Random(7));

    assertEquals(mapaA.renderizar(0, 0), mapaB.renderizar(0, 0));
  }
```

**Por que essa decisão?**
* Um `Random` fixo torna o resultado previsível. Criar o `new Random()` dentro do construtor esconde uma dependência que o teste não consegue controlar; recebê-lo como parâmetro é o que permite o teste. O construtor de dois argumentos continua existindo, então o resto do código não muda.
* Devolver uma `String` é mais simples de testar.
* `renderizar` ficou sem modificador de acesso e `Mapa` foi movido para o subpacote `mapa`: só o pacote e seus testes o enxergam. A interface pública fica pequena, e o teste ainda chega ao método. O estágio 4 mostra quando essa fronteira deixa de valer a pena.

### 3. Calculando o HP máximo a partir do nível: `calcularHpMax`

Como `hpMax` valia 0, qualquer vida positiva era descartada em silêncio: todo Pokémon nascia com `hp` igual a 0. O commit `7d4b8f3` remove o `hp` do construtor e deriva `hpMax` do nível:

`Pokemon.java` @ `7d4b8f3`
```java
  private Pokemon(String name, float height, float weight, String types, int level) {
    this.name = name;
    this.height = height;
    this.weight = weight;
    this.level = level;
    this.hpMax = calcularHpMax(level);
    setHp(this.hpMax);
```

`Pokemon.java` @ `7d4b8f3`
```java
  private static int calcularHpMax(int level) {
    return level * (25 + level);
  }
```

`TestePokemon.java` @ `7d4b8f3`
```java
  @Test
  public void pokemonNasceComVidaCheiaEmFuncaoDoNivel() {
    Pokemon pikachu = new Pokemon("Pikachu", "electric", 5);

    // level * (25 + level) = 5 * 30
    assertEquals(150, pikachu.getHp());
  }
```

**Por que essa decisão?**
* O HP máximo é uma *consequência* do nível, e não um dado que o chamador informa. Se o construtor recebe os dois, alguém pode passar um `hp` incoerente com o nível.
* A ordem das linhas importa: `hpMax` é definido antes de `setHp`, porque `setHp` consulta `hpMax`. Tornar `hpMax` e `level` `final` faz o compilador garantir que eles não mudam depois da construção.

### 4. Domínio sem entrada e saída: `JogoConsole`

Ao fim do estágio 3, `Jogo`, `Mapa` e `Treinador` ainda imprimiam (`exibirMapa`, `listar`) e `Jogo` ainda lia do teclado. O commit `38efe30` faz as classes de domínio *devolverem* dados, e uma classe nova cuida do terminal:

`JogoConsole.java` @ `38efe30`
```java
  public void iniciar() {
    Scanner scanner = new Scanner(System.in);
    Direcao direcao;
    do {
      System.out.print(jogo.renderizarMapa());
      System.out.println("Informe a direção para onde queres ir");
      direcao = jogo.processarComando(scanner.nextLine());
      if (direcao == null) {
        System.out.println("Valor invalido");
      }
    } while (direcao != Direcao.SAIR);
  }
```

`Jogo.java` @ `38efe30`
```java
  public String renderizarMapa() {
    return mapa.renderizar(treinador.getX(), treinador.getY());
  }
```

`Treinador.java` @ `38efe30`
```java
  public List<Pokemon> getPokemons() {
    return List.copyOf(Arrays.asList(pokemons).subList(0, qtdPokemons));
  }
```

**Por que essa decisão?**
* Uma regra do jogo que imprime só pode ser testada capturando a saída (`System.setOut`). Uma que devolve um valor se testa com `assertEquals`.
* `renderizar` voltou a ser `public`: `Jogo` precisa dele.
* `getPokemons()` devolve uma **cópia imutável**, então quem chama não consegue alterar a equipe do treinador. (O `<Pokemon>` em `List<Pokemon>` é um tipo genérico, tema que ainda virá: por ora, leia como "lista de Pokemon".)
* `JogoConsole` só liga peças que já têm teste. 

### 5. Constantes nomeadas: `MAX_POKEMONS`, `PROBABILIDADE_LIVRE`

Números e caracteres soltos (`6`, `0.8`, `'w'`, `'T'`) obrigavam o leitor a adivinhar o significado. O commit `3193a55` os nomeia onde o significado não é óbvio:

`Mapa.java` @ `3193a55`
```java
  private static final double PROBABILIDADE_LIVRE = 0.8;
  private static final char LIVRE = ' ';
  private static final char GRAMA = 'w';
  private static final char TREINADOR = 'T';
```

`Mapa.java` @ `3193a55`
```java
        if(r.nextFloat() < PROBABILIDADE_LIVRE) {
          this.mapa[i][j] = LIVRE;
        } else {
          this.mapa[i][j] = GRAMA;
        }
```

`Treinador.java` @ `3193a55`
```java
  private static final int MAX_POKEMONS = 6;
```

`Pokemon.java` @ `3193a55`
```java
  private static final int NIVEL_INICIAL = 1;
  private static final float ALTURA_PADRAO = 10f;
  private static final float PESO_PADRAO = 10f;
```

**Por que essa decisão?**
* `new Pokemon[6]` diz *quantos*, mas não *por quê*. `MAX_POKEMONS` diz o que o número significa e o deixa num lugar só.
* `PROBABILIDADE_LIVRE` é `double`, e não `float`, para manter exatamente a comparação que existia com `nextFloat()`: mudar o tipo poderia alterar mapas gerados com a mesma semente.
* Nem todo número virou constante. A fórmula `level * (25 + level)` continua inteira em `calcularHpMax`: o significado é o da fórmula, e nomear só o `25` não ajudaria. `0` e `1` em índices e no movimento também ficaram, por serem autoexplicativos.
* Repare em `ALTURA_PADRAO` e `PESO_PADRAO`. O estágio 8 mostra por que dar nome a um valor não justifica manter um campo que ninguém lê.

### 6. Objeto de valor imutável: `Posicao`

O par `(x, y)` viajava separado por todo o código (`getX()`, `getY()`, `moverPara(x, y)`, `ePosicaoValida(x, y)`, `renderizar(x, y)`), e trocar a ordem dos argumentos não gerava erro de compilação. O commit `0a107fc` cria uma classe para o par:

`Posicao.java` @ `0a107fc`
```java
public final class Posicao {

  private final int x;
  private final int y;

  public Posicao(int x, int y) {
    this.x = x;
    this.y = y;
  }
  // ...
  public Posicao deslocar(int dx, int dy) {
    return new Posicao(x + dx, y + dy);
  }
```

`Jogo.java` @ `0a107fc`
```java
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
```

`Treinador.java` @ `0a107fc`
```java
  public Posicao getPosicao() {
    return posicao;
  }

  public void moverPara(Posicao destino) {
    this.posicao = destino;
  }
```

`TestePosicao.java` @ `0a107fc`
```java
  @Test
  public void deslocarNaoAlteraAPosicaoOriginal() {
    Posicao original = new Posicao(2, 3);

    original.deslocar(1, 1);

    assertEquals(new Posicao(2, 3), original);
  }
```

**Por que essa decisão?**
* **Imutável, e não uma classe com `setX`/`setY`.** `deslocar` devolve uma posição *nova*. O `Jogo` calcula um destino candidato, pergunta ao `Mapa` se ele vale, e só então o treinador troca de posição. Com uma `Posicao` mutável, seria preciso alterar a posição do treinador e desfazer se a validação recusasse, e entre as duas etapas o objeto ficaria inválido.
* **Sem vazamento pelo getter.** `getPosicao()` devolve a referência interna, mas, como o objeto não muda, ninguém consegue burlar a validação do mapa. Com dois `int`, isso era garantido porque cada `int` é copiado; a imutabilidade preserva essa propriedade agora que os dois viraram um objeto.
* **`equals` e `hashCode` por valor.** Duas posições com as mesmas coordenadas são iguais, o que permite `assertEquals(new Posicao(1, 0), treinador.getPosicao())` e, no futuro, usar posições como chave de coleções.
* **A classe é `final`**, para que ninguém a estenda e acrescente estado mutável.
* Um efeito colateral bem-vindo: `setX` e `setY` deixaram de existir em `Treinador`, e a interface pública ficou menor.
* `Posicao` mora no pacote `mapa` porque `Mapa` a recebe como parâmetro, e `mapa` não deve depender de `pokemon`.

### 7. Enum com comportamento: `Direcao.aplicarEm` e `eDirecaoValida`

O `switch` do estágio 6 ainda espalhava em `Jogo` o significado de cada direção (`-1`, `0`, `1`), e `SAIR` continuava dentro de `Direcao` sem ser uma direção: o `case SAIR -> atual` existia só para fechar o `switch`. O commit `6a36aee` move o deslocamento para o `enum` e tira `SAIR` de lá:

`Direcao.java` @ `6a36aee`
```java
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
```

`Direcao.java` @ `6a36aee`
```java
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
```

`Jogo.java` @ `6a36aee`
```java
  public void mover(Direcao direcao) {
    Posicao destino = direcao.aplicarEm(treinador.getPosicao());
    if (mapa.ePosicaoValida(destino)) {
      treinador.moverPara(destino);
    }
  }
```

`JogoConsole.java` @ `6a36aee`
```java
      String opcao = scanner.nextLine();
      if (opcao.equalsIgnoreCase(COMANDO_SAIR)) {
        sair = true;
      } else {
        if (Direcao.eDirecaoValida(opcao)) {
          jogo.mover(Direcao.get(opcao));
        } else {
          System.out.println("Valor invalido");
        }
      }
```

**Por que essa decisão?**
* **Cada direção sabe o que significa.** Acrescentar uma direção (uma diagonal, por exemplo) muda só o `enum`. O `switch` de `Jogo` desapareceu, e `mover(Direcao)` não recebe mais texto nem devolve `null`.
* **`SAIR` é um comando, e não uma direção.** Ele passa a ser tratado onde o texto é lido, em `JogoConsole`. Misturar os dois conceitos era o motivo do `case SAIR -> atual` que não fazia nada.
* **Verificar antes de usar (`eDirecaoValida`, depois `get`)** segue o mesmo padrão de `ePosicaoValida`, seguido de `moverPara`: uma pergunta que devolve `boolean`, e só então a ação. Uma alternativa seria devolver `Optional<Direcao>`, mas isso exige generics, que a turma ainda não viu. Devolver `null` foi descartado porque nada obriga o chamador a checar.
* `procurar` existe para o laço de busca aparecer uma vez só, e `eDirecaoValida` e `get` o reutilizam. É uma decisão discutível (os três métodos são muito parecidos): um bom ponto para você pensar em alternativas.

### 8. Modelo enxuto e uma fonte de verdade: `Pokemon` e `Mapa`

Depois das refatorações, sobraram duas inconsistências. Em `Pokemon`, quatro campos (`height`, `weight`, `t1`, `t2`) eram gravados e nunca lidos; e os nomes misturavam inglês e português. O commit `294f27a` remove o que ninguém usa e traduz o restante:

`Pokemon.java` @ `294f27a`
```java
public class Pokemon {

  private static final int NIVEL_INICIAL = 1;

  private final String nome;
  private final int nivel;
  private final int hpMax;
  private int hp;

  public Pokemon(String nome) {
    this(nome, NIVEL_INICIAL);
  }

  public Pokemon(String nome, int nivel) {
    this.nome = nome;
    this.nivel = nivel;
    this.hpMax = calcularHpMax(nivel);
    setHp(this.hpMax);
  }
```

Em `Mapa`, o tamanho estava guardado duas vezes: nos campos `altura` e `largura` e nas dimensões do próprio array. `inicializarMapa` percorria o array, e `renderizar` e `ePosicaoValida` usavam os campos. O commit `fc26318` unifica tudo nos campos:

`Mapa.java` @ `fc26318`
```java
  private void inicializarMapa(Random r) {
    for (int i = 0; i < this.altura; i++) {
      for (int j = 0; j < this.largura; j++) {
```

`TesteMapa.java` @ `fc26318`
```java
  @Test
  public void renderizarPreencheTodasAsCelulasDeUmMapaNaoQuadrado() {
    Mapa mapa = new Mapa(10, 5);

    String[] linhas = mapa.renderizar(new Posicao(0, 0)).split("\n");

    for (String linha : linhas) {
      // '|' + 10 células de 2 caracteres + '|'
      assertEquals(1 + 10 * 2 + 1, linha.length());
      assertFalse(linha.contains("\u0000"), "Toda célula deveria ter sido inicializada");
    }
  }
```

**Por que essa decisão?**
* **Remover em vez de nomear.** No estágio 5, `ALTURA_PADRAO` e `PESO_PADRAO` deram nome a valores de campos que nada consultava. Um campo sem leitor não precisa de nome melhor: precisa deixar de existir. Menos dados, menos coisas para manter coerentes. Se um dia o jogo usar altura e peso, o campo volta com um uso.
* **`hpMax` continua sempre calculado do nível**, agora com um único caminho de construção: `Pokemon(nome, nivel)`. O construtor `Pokemon(nome)` só delega, com nível inicial.
* **Uma fonte de verdade para o tamanho do mapa.** Enquanto o tamanho existir em dois lugares, eles podem divergir. O teste usa um mapa **não quadrado** (10×5), onde uma troca entre altura e largura apareceria, e confere que toda célula foi preenchida.
* **Ficou de fora, de propósito:** validar o nível ou as dimensões (o natural seria lançar uma exceção) e operações de HP como `receberDano`, `curar` e `estaDerrotado`. Elas ficam como exercício, abaixo.

### 9. Criação isolada e sorteio controlável: `GeradorDePokemon`

A próxima etapa do jogo é encontrar Pokémon selvagens na grama e tentar capturá-los com pokébolas. Para isso, alguém precisa *criar* o Pokémon selvagem, e a chance de captura depende do nível dele. O commit `0ab005e` (tag `v0.2.1`, ponto de partida da atividade 03) prepara esse terreno com uma classe só para a criação:

`GeradorDePokemon.java` @ `0ab005e`
```java
  private static final String[] NOMES = {
      "Pidgey", "Rattata", "Caterpie", "Weedle", "Oddish", "Bellsprout", "Pikachu", "Eevee"
  };
  private static final int NIVEL_MIN_SELVAGEM = 1;
  private static final int NIVEL_MAX_SELVAGEM = 30;

  private final Random random;

  public GeradorDePokemon() {
    this(new Random());
  }

  public GeradorDePokemon(Random random) {
    this.random = random;
  }

  public Pokemon gerar() {
    String nome = NOMES[random.nextInt(NOMES.length)];
    int nivel = NIVEL_MIN_SELVAGEM + random.nextInt(NIVEL_MAX_SELVAGEM - NIVEL_MIN_SELVAGEM + 1);
    return new Pokemon(nome, nivel);
  }
```

`Pokemon.java` @ `0ab005e`
```java
  public static final int NIVEL_MAXIMO = 100;
  private static final int NIVEL_INICIAL = 1;
```

`TesteGeradorDePokemon.java` @ `0ab005e`
```java
  @Test
  public void geradoresComMesmaSementeGeramOsMesmosPokemons() {
    GeradorDePokemon geradorA = new GeradorDePokemon(new Random(42));
    GeradorDePokemon geradorB = new GeradorDePokemon(new Random(42));

    for (int i = 0; i < 10; i++) {
      assertEquals(geradorA.gerar(), geradorB.gerar());
    }
  }
```

**Por que essa decisão?**
* **Uma classe própria, e não mais um método em `Jogo`.** A lista de nomes e a faixa de níveis são regras de criação que mudam por motivos próprios (um mapa novo, uma dificuldade diferente). Em `Jogo`, que já coordena mapa e treinador, elas seriam mais uma responsabilidade misturada. Com o gerador separado, mudar quais Pokémon aparecem não toca em `Jogo`.
* **O mesmo padrão do `Random` do estágio 2.** O construtor sem argumentos cria um `new Random()` para o jogo; o que recebe o `Random` existe para o teste. Com um `new Random()` escondido dentro de `gerar()`, nenhum teste conseguiria prever o resultado. Repare que o teste compara Pokémon com `assertEquals`: funciona porque `Pokemon` define `equals` por nome e nível.
* **Nível selvagem de 1 a 30, e não de 1 a `NIVEL_MAXIMO`.** A chance de captura cai com o nível e chega perto de 5% no nível 100, com qualquer pokébola comum. Com a faixa inteira, quase metade dos encontros seria quase impossível e a diferença entre as pokébolas desapareceria. Até o nível 30, a pokébola comum fica em torno de 33% a 45% e as melhores continuam visivelmente melhores.
* **`NIVEL_MAXIMO` é `public`; `NIVEL_INICIAL` continua `private`.** As outras constantes do projeto são privadas porque só a própria classe as usa. O nível máximo é uma regra do domínio que a fórmula de captura, fora de `Pokemon`, vai consultar. Expor só o que alguém de fora precisa mantém a interface pequena.
* **`getNome` e `getNivel` chegam junto com quem os lê.** O estágio 8 removeu campos que ninguém consultava; aqui o movimento é o inverso: os getters entram porque agora há leitores previstos (a mensagem "um Pidgey selvagem apareceu" e o cálculo da chance de captura). Um getter sem leitor seria só interface pública a mais para manter.

### 10. Herança: `Pokebola`, `super(...)` e o construtor `protected`

Com o gerador pronto, a atividade 03 faz o treinador encontrar Pokémon na grama e tentar capturá-los. Há quatro pokébolas, e elas diferem em quase nada: o nome e a taxa. O commit `60c66f8` escreve a regra de captura uma vez, em `Pokebola`, e cria uma subclasse para cada variação:

`Pokebola.java` @ `60c66f8`
```java
  public Pokebola() {
    this("Pokebola", 0.4);
  }

  protected Pokebola(String nome, double taxa) {
    this.nome = nome;
    this.taxa = taxa;
  }
  // ...
  protected double getTaxaDeCaptura(Pokemon p) {
    return taxa - taxa * (p.getNivel()* 1.0/ Pokemon.NIVEL_MAXIMO) + 0.05;
  }

  public boolean capturar(Pokemon p) {
    Random r = new Random();
    return r.nextDouble() < getTaxaDeCaptura(p);
  }
```

`GreatBall.java` @ `60c66f8`
```java
public class GreatBall extends Pokebola {

  public GreatBall() {
    super("GreatBall", 0.6);
  }
}
```

`MasterBall.java` @ `60c66f8`
```java
  @Override
  public boolean capturar(Pokemon p) {
    return true;
  }
```

A classe `Batalha` liga o treinador ao Pokémon encontrado:

`Batalha.java` @ `60c66f8`
```java
  public boolean tentarCaptura() {
    Pokebola pokebola = treinador.arremessarPokebola();
    if(pokebola != null && pokebola.capturar(pokemon)) {
      System.out.println("Uma " + pokebola.getNome() + " foi arremesada");
      if (treinador.capturar(pokemon)) {
        encerrada = true;
        return true;
      }
    }
    if(!treinador.temPokebola()) {
      encerrada = true;
    }
    return false;
  }
```

**Por que essa decisão?**
* **A regra de captura mora em um lugar só.** Sem herança, cada pokébola repetiria a fórmula e o sorteio. `GreatBall` tem cinco linhas porque herda tudo e só informa o que muda: `super("GreatBall", 0.6)`.
* **O construtor com nome e taxa é `protected`.** Se fosse `public`, qualquer código poderia escrever `new Pokebola("Trapaça", 50)`. Assim, quem está fora só cria as pokébolas que existem; quem estende `Pokebola` consegue chamar `super(...)`.
* **`getTaxaDeCaptura` também é `protected`.** A taxa é um detalhe do cálculo: quem usa a pokébola só precisa de `capturar`. `protected` deixa a subclasse enxergar o método sem colocá-lo na interface pública.
* **`* 1.0` na fórmula evita a divisão inteira.** `getNivel()` e `NIVEL_MAXIMO` são `int`; sem o `1.0`, a divisão daria 0 para qualquer nível abaixo de 100.
* **As pokébolas ficam no pacote `pokebola`**, separadas do resto do jogo, como `Mapa` e `Posicao` no pacote `mapa`.
* **Esta primeira versão funciona, mas guarda três problemas.** `Batalha` imprime; um `boolean` não diz por que a captura falhou; e a `MasterBall` sobrescreve o método que contém o sorteio. Os três próximos estágios tratam de cada um.

### 11. Um `enum` para o resultado: `ResultadoCaptura`

Na versão do estágio 10, `tentarCaptura` devolvia `false` em três situações diferentes: o Pokémon escapou, o treinador não tinha pokébola ou a equipe estava cheia. Neste último caso havia um bug: a pokébola era gasta, a captura dava certo no sorteio, e o jogador lia "O pokemon quebrou a pokebola". Além disso, o `println` dentro de `Batalha` desfazia a separação do estágio 4. O commit `77df791` troca o `boolean` por um tipo que nomeia cada desfecho:

`ResultadoCaptura.java` @ `77df791`
```java
public enum ResultadoCaptura {
  CAPTURADO,
  ESCAPOU,
  SEM_POKEBOLA,
  EQUIPE_CHEIA
}
```

`Batalha.java` @ `77df791`
```java
  public ResultadoCaptura tentarCaptura() {
    if (treinador.equipeCheia()) {
      return ResultadoCaptura.EQUIPE_CHEIA;
    }
    ultimaPokebola = treinador.arremessarPokebola();
    if (ultimaPokebola == null) {
      encerrada = true;
      return ResultadoCaptura.SEM_POKEBOLA;
    }
    if (ultimaPokebola.capturar(pokemon)) {
      treinador.capturar(pokemon);
      encerrada = true;
      return ResultadoCaptura.CAPTURADO;
    }
    if (!treinador.temPokebola()) {
      encerrada = true;
    }
    return ResultadoCaptura.ESCAPOU;
  }
```

`JogoConsole.java` @ `77df791`
```java
        switch (batalha.tentarCaptura()) {
          case EQUIPE_CHEIA -> System.out.println("Sua equipe está cheia");
          case SEM_POKEBOLA -> System.out.println("Você não tem mais pokebolas");
          case CAPTURADO -> System.out.println("Uma " + batalha.getUltimaPokebola().getNome()
              + " foi arremessada. Parabens vc capturou um " + batalha.getPokemon());
```

`TesteBatalha.java` @ `77df791`
```java
  @Test
  public void equipeCheiaNaoGastaPokebolaNemEncerraABatalha() {
    Treinador treinador = new Treinador("Ash");
    for (int i = 0; i < MAX_POKEMONS; i++) {
      treinador.capturar(new Pokemon("Pokemon" + i, 1));
    }
    treinador.adicionarPokebola(new MasterBall());
    Batalha batalha = new Batalha(treinador, pikachu);

    assertEquals(ResultadoCaptura.EQUIPE_CHEIA, batalha.tentarCaptura());
    assertTrue(treinador.temPokebola());
    assertFalse(treinador.getPokemons().contains(pikachu));
    assertFalse(batalha.terminou());
  }
```

**Por que essa decisão?**
* **Quatro desfechos não cabem em dois valores.** Com `boolean`, `JogoConsole` teria de adivinhar o motivo do `false`, ou perguntar de novo ao treinador o que a `Batalha` já sabia. O `enum` devolve a resposta inteira, e o `switch` mostra um caso por desfecho.
* **Um `enum`, e não um texto.** Devolver `"equipe cheia"` funcionaria, mas é o problema do estágio 1: um erro de digitação só apareceria jogando.
* **A equipe é conferida antes do arremesso.** É a ordem das linhas que corrige o bug: se não há lugar para o Pokémon, a pokébola nem sai da mochila.
* **`Batalha` voltou a não imprimir.** A mensagem precisa do nome da pokébola usada, e por isso entra `getUltimaPokebola()`: a `Batalha` guarda o dado e `JogoConsole` monta o texto.
* **Agora dá para testar.** `TesteBatalha` nasce neste commit. Para os cenários de falha não dependerem de sorte, o teste cria a sua própria subclasse, `PokebolaQueSempreFalha`. Herança serve também para isso: trocar uma peça por outra previsível.

### 12. O que a subclasse pode mudar: `capturar` `final`

A `MasterBall` do estágio 10 garantia a captura sobrescrevendo `capturar`. Funciona, mas `capturar` é o método que contém o sorteio: uma subclasse que o sobrescreve pode ignorar a regra inteira, de propósito ou por descuido. O commit `9080594` separa o que é igual para todas as pokébolas do que varia:

`Pokebola.java` @ `9080594`
```java
  private static final double CHANCE_MINIMA = 0.05;
  // ...
  protected double getTaxaDeCaptura(Pokemon p) {
    return taxa - taxa * (p.getNivel()* 1.0/ Pokemon.NIVEL_MAXIMO) + CHANCE_MINIMA;
  }

  public final boolean capturar(Pokemon p) {
    Random r = new Random();
    return r.nextDouble() < getTaxaDeCaptura(p);
  }
```

`MasterBall.java` @ `9080594`
```java
  @Override
  protected double getTaxaDeCaptura(Pokemon p) {
    return 1.0;
  }
```

`TesteBatalha.java` @ `9080594`
```java
  private static class PokebolaQueSempreFalha extends Pokebola {

    PokebolaQueSempreFalha() {
      super("PokebolaQueSempreFalha", 0);
    }

    @Override
    protected double getTaxaDeCaptura(Pokemon p) {
      return 0;
    }
  }
```

**Por que essa decisão?**
* **`final` transforma uma combinação em regra.** "Não sobrescreva `capturar`" num comentário depende de alguém ler. Com `final`, a tentativa não compila.
* **A variação fica num ponto só.** Toda pokébola sorteia do mesmo jeito; o que muda é a taxa. `MasterBall` passa a dizer "minha taxa é 1.0", e o sorteio continua acontecendo para ela também. Como `nextDouble()` devolve um valor sempre menor que 1, a captura segue garantida.
* **O próprio teste dependia da brecha.** `PokebolaQueSempreFalha` também sobrescrevia `capturar`. Ao fechar o método, o compilador apontou o segundo lugar que precisava mudar.
* **`0.05` ganhou nome.** `CHANCE_MINIMA` diz por que o número está na fórmula: mesmo no nível máximo sobra uma chance de captura.
* Nem todo método deve ser `final`. `getTaxaDeCaptura` continua aberto porque é justamente o ponto que as subclasses precisam alterar.

### 13. Sorteio controlável na hierarquia: `Random` nos construtores

`capturar` ainda criava um `new Random()` a cada chamada. É o problema dos estágios 2 e 9, agora dentro de uma hierarquia: nenhum teste conseguia dizer se a `GreatBall` captura mais que a `Pokebola`. O commit `9bf0562` aplica a mesma solução, e cada subclasse repassa o `Random` para cima:

`Pokebola.java` @ `9bf0562`
```java
  public Pokebola() {
    this(new Random());
  }

  Pokebola(Random random) {
    this("Pokebola", 0.4, random);
  }

  protected Pokebola(String nome, double taxa, Random random) {
    this.nome = nome;
    this.taxa = taxa;
    this.random = random;
  }
```

`GreatBall.java` @ `9bf0562`
```java
  public GreatBall() {
    this(new Random());
  }

  GreatBall(Random random) {
    super("GreatBall", 0.6, random);
  }
```

`Jogo.java` @ `9bf0562`
```java
  public Jogo(String nome) {
    this(nome, new Random());
  }

  public Jogo(String nome, Random random) {
    // ...
    mapa = new Mapa(LARGURA_MAPA, ALTURA_MAPA, random);
    geradorDePokemon = new GeradorDePokemon(random);
  }
```

`TestePokebola.java` @ `9bf0562`
```java
  // Random de teste: devolve sempre o mesmo valor, para o sorteio da captura ser previsível.
  private static class SorteioFixo extends Random {

    private final double valor;

    SorteioFixo(double valor) {
      this.valor = valor;
    }

    @Override
    public double nextDouble() {
      return valor;
    }
  }
```

`TestePokebola.java` @ `9bf0562`
```java
  // Nível 50: taxa = 0.4 * 0.5 + 0.05 = 0.25
  @Test
  public void pokebolaCapturaQuandoSorteioFicaAbaixoDaTaxa() {
    assertTrue(new Pokebola(new SorteioFixo(0.24)).capturar(nivel50));
  }

  @Test
  public void pokebolaNaoCapturaQuandoSorteioFicaAcimaDaTaxa() {
    assertFalse(new Pokebola(new SorteioFixo(0.26)).capturar(nivel50));
  }
```

**Por que essa decisão?**
* **`this(...)` e `super(...)` formam uma corrente.** `new GreatBall()` chama `GreatBall(Random)`, que chama o construtor `protected` de `Pokebola`. Os atributos são atribuídos num único lugar, o último elo.
* **O construtor com `Random` não tem modificador de acesso.** Só o pacote `pokebola` o enxerga, e os testes ficam nesse pacote. O jogo continua criando pokébolas com `new GreatBall()`, sem saber que o outro construtor existe.
* **`SorteioFixo` é mais preciso que uma semente.** Com `new Random(42)`, o teste saberia apenas que o resultado se repete. Com um valor escolhido, ele confere a fronteira: 0.24 captura e 0.26 não, porque a taxa é 0.25. De novo, herança usada para trocar uma peça por outra previsível.
* **O `Jogo` repassa o mesmo `Random` ao mapa e ao gerador.** Com uma semente, o terreno, os encontros e os Pokémon de uma partida inteira se repetem.

### 14. Uma operação, um método: `Jogo.mover` devolve a `Batalha`

Para andar e talvez batalhar, `JogoConsole` fazia três chamadas que só funcionavam nesta ordem: `jogo.mover(...)`, `jogo.encontrouPokemon()` e `jogo.iniciarBatalha()`. Nada impedia chamar `iniciarBatalha()` sem encontro. E `encontrouPokemon()` parecia uma pergunta, mas sorteava: duas chamadas seguidas podiam dar respostas diferentes. O commit `e13055a` junta as três numa operação:

`Jogo.java` @ `e13055a`
```java
  public Batalha mover(Direcao direcao) {
    Posicao destino = direcao.aplicarEm(treinador.getPosicao());
    if (!mapa.ePosicaoValida(destino)) {
      return null;
    }
    treinador.moverPara(destino);
    if (mapa.sortearEncontro(destino)) {
      return new Batalha(treinador, geradorDePokemon.gerar());
    }
    return null;
  }
```

`JogoConsole.java` @ `e13055a`
```java
          Batalha batalha = jogo.mover(Direcao.get(opcao));
          if(batalha != null) {
            gerenciarBatalha(batalha, scanner);
          }
```

O mesmo commit arruma o `Treinador`, que guardava pokémons com um contador e pokébolas procurando a primeira posição vazia:

`Treinador.java` @ `e13055a`
```java
  public boolean adicionarPokemon(Pokemon pokemon) {
    if(pokemon == null) return false;
    for(int i = 0; i < MAX_POKEMONS; i++) {
      if (pokemons[i] == null) {
        pokemons[i] = pokemon;
        return true;
      }
    }
    return false;
  }
```

`TesteJogo.java` @ `e13055a`
```java
  // sorteio 0.3: todo o mapa é grama (>= 0.2) e todo sorteio de encontro dá certo (< 0.5)
  @Test
  public void moverParaGramaIniciaBatalhaQuandoOSorteioDaEncontro() {
    Jogo jogo = new Jogo("Ash", new SorteioFixo(0.3f));

    Batalha batalha = jogo.mover(Direcao.DIR);

    assertNotNull(batalha);
    assertNotNull(batalha.getPokemon());
    assertFalse(batalha.terminou());
  }
```

**Por que essa decisão?**
* **Quem chama não precisa mais saber a ordem.** O encontro é consequência do movimento, então quem move é quem decide se há batalha. O uso errado (batalha sem encontro, dois sorteios para o mesmo passo) deixou de ser possível.
* **O nome avisa que há sorteio.** `encontrouPokemon` soava como consulta; `sortearEncontro` diz que cada chamada pode dar um resultado diferente.
* **Devolver `null` é discutível.** O estágio 7 evitou `null` porque nada obriga quem chama a conferir, e isso continua verdadeiro aqui. A alternativa seria `Optional<Batalha>`, que exige generics. Fica como ponto para você pensar.
* **`adicionarPokemon` em vez de `capturar`.** Havia `Treinador.capturar` e `Pokebola.capturar` fazendo coisas diferentes. Quem captura é a pokébola; o treinador apenas guarda o Pokémon.
* **Uma estratégia só para os dois vetores.** O contador `qtdPokemons` saiu, e pokémons e pokébolas passaram a ser guardados do mesmo jeito. O preço é que `equipeCheia()` e `getPokemons()` agora percorrem o vetor; com seis posições, isso não pesa.
* **Atributos que não mudam viraram `final`** em `Batalha` e `Pokebola`, como já acontecia em `Pokemon` e `Posicao`.

### 15. Enum com dados: `Especie` e as 151 espécies

A atividade 04 troca a fórmula de captura por uma que depende da espécie: taxa de captura, velocidade e peso. Até aqui, um `Pokemon` só sabia o próprio nome, e o gerador sorteava esse nome de uma lista de oito textos. O commit `4b6033d` (ponto de partida da atividade 04) dá um tipo à espécie:

`Especie.java` @ `4b6033d`
```java
// As 151 espécies da primeira geração, com os dados do jogo publicados pela PokeAPI (https://pokeapi.co).
public enum Especie {
  BULBASAUR("Bulbasaur", 45, 45, 45, 6.9, 70),
  IVYSAUR("Ivysaur", 45, 60, 60, 13.0, 70),
  // ...
  MEWTWO("Mewtwo", 3, 106, 130, 122.0, 0),
  MEW("Mew", 45, 100, 100, 4.0, 100);

  private final String nome;
  private final int taxaDeCaptura;
  private final int hpBase;
  private final int velocidade;
  private final double peso;
  private final int amizadeBase;
```

`Pokemon.java` @ `4b6033d`
```java
  public Pokemon(Especie especie, int nivel) {
    this.especie = especie;
    this.nivel = nivel;
    this.hpMax = calcularHpMax(especie, nivel);
    setHp(this.hpMax);
  }

  private static int calcularHpMax(Especie especie, int nivel) {
    return 2 * especie.getHpBase() * nivel / 100 + nivel + 10;
  }

  public String getNome() {
    return especie.getNome();
  }

  public int getTaxaDaEspecie() {
    return especie.getTaxaDeCaptura();
  }
```

`GeradorDePokemon.java` @ `4b6033d`
```java
  public Pokemon gerar() {
    Especie[] especies = Especie.values();
    Especie especie = especies[random.nextInt(especies.length)];
    int nivel = NIVEL_MIN_SELVAGEM + random.nextInt(NIVEL_MAX_SELVAGEM - NIVEL_MIN_SELVAGEM + 1);
    return new Pokemon(especie, nivel);
  }
```

`TestePokemon.java` @ `4b6033d`
```java
  @Test
  public void caracteristicasVemDaEspecie() {
    Pokemon snorlax = new Pokemon(Especie.SNORLAX, 5);

    assertEquals("Snorlax", snorlax.getNome());
    assertEquals(25, snorlax.getTaxaDaEspecie());
    assertEquals(30, snorlax.getVelocidade());
    assertEquals(460.0, snorlax.getPeso());
  }
```

**Por que essa decisão?**
* **Um tipo para a espécie, e não mais parâmetros no construtor.** A alternativa seria `new Pokemon("Snorlax", 5, 25, 30, 460.0)`: cinco valores soltos, e nada impediria um Snorlax de 6 kg. Com `Especie`, os dados de cada espécie são escritos uma vez e viajam juntos. É o mesmo raciocínio do estágio 6, em que `x` e `y` viraram `Posicao`.
* **Um `enum`, porque o conjunto é fechado e conhecido.** O estágio 7 já mostrou um `enum` com atributos (`Direcao`, com `dx` e `dy`); aqui são 151 constantes em vez de quatro. Um nome inexistente, como `Especie.PIKACHUU`, não compila.
* **Os dados ficam no código, e não num arquivo.** Ler as espécies de um arquivo exigiria entrada e saída e tratamento de exceções, que a turma ainda não viu. As linhas do `enum` foram geradas a partir das tabelas da PokeAPI, e não digitadas à mão.
* **`Pokemon` pergunta à espécie em vez de copiar os dados.** `getTaxaDaEspecie()`, `getVelocidade()` e `getPeso()` só repassam a pergunta. Copiar os valores para atributos de `Pokemon` criaria uma segunda fonte de verdade, o problema que o estágio 8 removeu do `Mapa`.
* **O HP máximo continua sendo calculado, agora a partir de dois dados.** O estágio 3 tirou o `hp` do construtor porque ele era consequência do nível. A regra se mantém: `hpMax` é consequência da espécie e do nível, e quem cria o Pokémon não informa nenhum HP. A fórmula é a do jogo, sem os valores individuais e de esforço.
* **A ordem das operações importa na fórmula.** Tudo ali é `int`. `2 * hpBase * nivel / 100` multiplica antes de dividir; escrever `nivel / 100` primeiro daria 0 para qualquer nível abaixo de 100.
* **`equals` compara as espécies com `==`.** Cada constante de um `enum` existe uma única vez, então comparar referências é correto e não quebra com `null`.
* **Dados que chegam antes de quem os lê.** `velocidade`, `peso` e `taxaDeCaptura` já têm getters em `Pokemon`, mas quem vai lê-los são as pokébolas da atividade 04. `amizadeBase` nem isso: é o dado que a atividade pede para você usar ao criar a amizade do Pokémon. Compare com o estágio 8, que removeu campos sem leitor: a diferença é que aqui o leitor já está definido.

---

## 🛠️ Tecnologias Utilizadas
* **Java:** a linguagem. O `switch` com `->` exige Java 14 ou superior, e o Gradle 9.2 exige JDK 17 ou superior para rodar.
* **Gradle (wrapper 9.2.0):** compila o projeto e roda os testes, sem instalar nada além do JDK.
* **JUnit 5 (5.10.0):** os testes de unidade, que servem de especificação de cada classe.

---

## ▶️ Como executar

Requer JDK 17 ou superior.

```bash
git clone https://github.com/profBruno-UFC-Qx/qxd0007-pokemon-2026.2.git
cd qxd0007-pokemon-2026.2
./gradlew test      # roda todos os testes
./gradlew classes   # compila
java -cp build/classes/java/main br.ufc.qx.Main
```

No jogo, digite `cima`, `baixo`, `esq` ou `dir` para mover o `T` e `sair` para encerrar. Ao encontrar um Pokémon na grama, digite `p` para arremessar uma pokébola ou `f` para fugir. Maiúsculas e minúsculas são indiferentes. Para ler a história, use `git log --oneline` e `git show <commit>`, com os commits citados em cada estágio.

---

## 📖 Como usar este repositório para estudo
* Observe como os estágios 1 e 2 corrigem bugs que **nenhum teste pegava**. Escreva, para o código de `2fe966c`, o teste que teria falhado (dica: um mapa de largura diferente da altura).
* Compare `iniciar()` em `2fe966c` com `JogoConsole.iniciar()` no estágio 7. Conte quantas responsabilidades cada um tem e diga o que muda se o jogo passar a ter uma interface gráfica.
* Estude por que `Posicao` é imutável. Tente reescrevê-la com `setX`/`setY` e veja o que acontece com a validação do movimento em `Jogo.mover`.
* Depois de ver `record`, reescreva `Posicao` como um `record` e confira que `TestePosicao` continua passando sem mudanças.
* Escreva `receberDano`, `curar` e `estaDerrotado` em `Pokemon`. Decida o que fazer quando o dano passa do HP restante, e compare com o que `setHp` faz hoje quando o valor é inválido.
* Depois de ver exceções, faça o construtor de `Mapa` recusar largura ou altura menor que 1, e o de `Pokemon`, um nível menor que 1. Escreva os testes primeiro.
* Rode `TesteGeradorDePokemon` e troque a semente de um dos geradores. Explique por que o teste passa a falhar. Depois, imagine `gerar()` criando o próprio `Random`: ainda seria possível escrever esse teste?
* Compare `MasterBall` em `60c66f8` e em `9080594`. Tire o `final` de `capturar`, escreva uma pokébola que o sobrescreva sem sortear e veja quais testes de `TestePokebola` deixariam de proteger a regra.
* Leia `TesteBatalha` e `TestePokebola` e encontre as duas subclasses criadas só para os testes. Diga o que cada uma substitui e por que uma semente fixa não bastaria.
* `Jogo.mover` devolve `null` quando não há batalha. Depois de ver generics, reescreva-o com `Optional<Batalha>` e compare o código de `JogoConsole` nas duas versões.
* Abra `Especie` e procure uma espécie com mais de 200 kg e outra com velocidade de pelo menos 100. Crie um `Pokemon` de cada no nível 50 e calcule à mão o HP máximo antes de conferir com `getHp()`.
* Reescreva `calcularHpMax` dividindo antes de multiplicar e rode `TestePokemon`. Explique o valor que o teste passa a receber.
* Reflita: `JogoConsole` não tem testes. Que tipo de mudança faria você querer testá-la, e onde essa lógica deveria morar?

---

Desenvolvido por [Bruno Mateus](https://github.com/brunomateus) para fins didáticos.

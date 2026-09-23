# Pokemon — do código tudo-em-um à separação de responsabilidades em Java

Um treinador anda por um mapa desenhado no terminal. O jogo é pequeno de propósito: este repositório existe para ser **lido**, não jogado. Ele é dirigido a quem já escreveu classes com atributos, construtores e métodos em Java e agora precisa decidir *onde cada responsabilidade mora*. Ao acompanhar a evolução do código, você vê como o encapsulamento, a testabilidade, o objeto de valor e a separação entre regras do jogo e entrada/saída surgem de problemas concretos, e não de regras decoradas.

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

### 3. Calculando o

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

No jogo, digite `cima`, `baixo`, `esq` ou `dir` para mover o `T` e `sair` para encerrar. Maiúsculas e minúsculas são indiferentes. Para ler a história, use `git log --oneline` e `git show <commit>`, com os commits citados em cada estágio.

---

## 📖 Como usar este repositório para estudo
* Observe como os estágios 1 e 2 corrigem bugs que **nenhum teste pegava**. Escreva, para o código de `2fe966c`, o teste que teria falhado (dica: um mapa de largura diferente da altura).
* Compare `iniciar()` em `2fe966c` com `JogoConsole.iniciar()` no estágio 7. Conte quantas responsabilidades cada um tem e diga o que muda se o jogo passar a ter uma interface gráfica.
* Estude por que `Posicao` é imutável. Tente reescrevê-la com `setX`/`setY` e veja o que acontece com a validação do movimento em `Jogo.mover`.
* Depois de ver `record`, reescreva `Posicao` como um `record` e confira que `TestePosicao` continua passando sem mudanças.
* Escreva `receberDano`, `curar` e `estaDerrotado` em `Pokemon`. Decida o que fazer quando o dano passa do HP restante, e compare com o que `setHp` faz hoje quando o valor é inválido.
* Depois de ver exceções, faça o construtor de `Mapa` recusar largura ou altura menor que 1, e o de `Pokemon`, um nível menor que 1. Escreva os testes primeiro.
* Reflita: `JogoConsole` não tem testes. Que tipo de mudança faria você querer testá-la, e onde essa lógica deveria morar?

---

Desenvolvido por [Bruno Mateus](https://github.com/brunomateus) para fins didáticos.

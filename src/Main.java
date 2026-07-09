import estrutura_arvore.ArvoreAVL;
import estrutura_arvore.ArvoreBinaria;
import leitura_arquivo.TitleBasic;
import leitura_arquivo.TitleBasicLoader;
import metodos_insercao.ListaArranjo;
import metodos_insercao.ListaDuplaEncadeada;
import metodos_insercao.ListaEncadeada;
import metodos_ordenacao.MergeSort;
import metodos_pesquisa.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {                                                                                       // nao tem simple
            ArrayList<TitleBasic> dadosOriginais = TitleBasicLoader.load("DatabaseTeste/title.basics.sample.tsv");

            Scanner scanner = new Scanner(System.in);
            int opcao;

            do {
                limparTela();
                exibirMenuPrincipal(dadosOriginais.size());
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1 -> menuBenchmarking(dadosOriginais, scanner);
                    case 2 -> new ExploradorIMDb(dadosOriginais, scanner).iniciar();
                    case 0 -> { limparTela(); System.out.println("  Encerrando. Ate logo!"); }
                    default -> { System.out.println("  Opcao invalida!"); pausar(scanner); }
                }

            } while (opcao != 0);

            scanner.close();

        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo: " + e.getMessage());
        }
    }



    private static void exibirMenuPrincipal(int totalRegistros) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.printf( "║   IMDb Database System                   ║%n");
        System.out.printf( "║   Registros carregados: %-6d           ║%n", totalRegistros);
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1.  Benchmarking Algoritmico            ║");
        System.out.println("║  2.  Explorador de Catalogo IMDb         ║");
        System.out.println("║  0.  Sair                                ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print("  Escolha: ");
    }


    // Limpeza de tela e pausa (static para uso em ExploradorIMDb)

    static void limparTela() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    static void pausar(Scanner scanner) {
        System.out.println("\n  Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    // Cronometro ao vivo — Thread auxiliar com flag boolean[]

    private static long executarComCronometro(Runnable tarefa, String nomeAlg) {
        boolean[] executando = {true};
        long inicio = System.currentTimeMillis();

        Thread cronometro = new Thread(() -> {
            while (executando[0]) {
                long seg = (System.currentTimeMillis() - inicio) / 1000;
                System.out.printf("\r  [%3ds] %-17s aguarde...", seg, nomeAlg);
                System.out.flush();
                try { Thread.sleep(500); } catch (InterruptedException e) { break; }
            }
        });
        cronometro.setDaemon(true);
        cronometro.start();

        tarefa.run();

        executando[0] = false;
        try { cronometro.join(1000); } catch (InterruptedException ignored) {}
        System.out.print("\r" + " ".repeat(55) + "\r");

        return System.currentTimeMillis() - inicio;
    }


    // Modulo 1 — Menu Benchmarking Algoritmico

    private static void menuBenchmarking(ArrayList<TitleBasic> dadosOriginais, Scanner scanner) {
        int opcao;
        do {
            limparTela();
            exibirMenuBenchmarking();
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> { testarArvoreBinaria(dadosOriginais);       pausar(scanner); }
                case 2 -> { testarArvoreAVL(dadosOriginais);           pausar(scanner); }
                case 3 -> { testarListaArranjo(dadosOriginais);        pausar(scanner); }
                case 4 -> { testarListaEncadeada(dadosOriginais);      pausar(scanner); }
                case 5 -> { testarListaDuplaEncadeada(dadosOriginais); pausar(scanner); }
                case 6 -> { executarTodos(dadosOriginais);             pausar(scanner); }
                case 7 -> { testarPesquisaSequencial(dadosOriginais, scanner); pausar(scanner); }
                case 8 -> { testarPesquisaBinaria(dadosOriginais, scanner);    pausar(scanner); }
                case 9 -> { testarComparativoBuscas(dadosOriginais);            pausar(scanner); }
                case 0 -> {} // retorna ao hub
                default -> { System.out.println("  Opcao invalida!"); pausar(scanner); }
            }

        } while (opcao != 0);
    }

    private static void exibirMenuBenchmarking() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   BENCHMARKING ALGORITMICO - IMDb    ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. ArvoreBinaria (BST)              ║");
        System.out.println("║  2. ArvoreAVL                        ║");
        System.out.println("║  3. ListaArranjo (todos algoritmos)  ║");
        System.out.println("║  4. ListaEncadeada (todos algoritmos)║");
        System.out.println("║  5. ListaDuplaEncadeada (todos alg.) ║");
        System.out.println("║  6. Executar TODOS (arvores + listas)║");
        System.out.println("║  7. Pesquisa Sequencial (por ID)     ║");
        System.out.println("║  8. Pesquisa Binaria (por ID)        ║");
        System.out.println("║  9. Comparativo de Buscas            ║");
        System.out.println("║  0. Voltar ao menu principal         ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("  Escolha: ");
    }


    // Listas (metodos_insercao.ListaArranjo, metodos_insercao.ListaEncadeada, metodos_insercao.ListaDuplaEncadeada)
    // guardam leitura_arquivo.TitleBasic e ordenam pelo ano de lancamento (startYear)


    private static ListaArranjo construirListaArranjo(ArrayList<TitleBasic> original) {
        ListaArranjo lista = new ListaArranjo(original.size());
        for (TitleBasic t : original) {
            lista.InsereFinal(t);
        }
        return lista;
    }

    private static ListaEncadeada construirListaEncadeada(ArrayList<TitleBasic> original) {
        ListaEncadeada lista = new ListaEncadeada();
        for (TitleBasic t : original) {
            lista.InsereFinal(t);
        }
        return lista;
    }

    private static ListaDuplaEncadeada construirListaDuplaEncadeada(ArrayList<TitleBasic> original) {
        ListaDuplaEncadeada lista = new ListaDuplaEncadeada();
        for (TitleBasic t : original) {
            lista.InsereFinal(t);
        }
        return lista;
    }

    private static final String[] ALGORITMOS = {
            "metodos_ordenacao.BubbleSort", "metodos_ordenacao.SelectionSort", "metodos_ordenacao.InsertionSort", "metodos_ordenacao.MergeSort", "metodos_ordenacao.QuickSort", "HeapSort"
    };

    private static void aplicarAlgoritmo(ListaArranjo lista, String algoritmo) {
        switch (algoritmo) {
            case "metodos_ordenacao.BubbleSort" -> lista.bubbleSort();
            case "metodos_ordenacao.SelectionSort" -> lista.selectionSort();
            case "metodos_ordenacao.InsertionSort" -> lista.insertionSort();
            case "metodos_ordenacao.MergeSort" -> lista.mergeSort();
            case "metodos_ordenacao.QuickSort" -> lista.quickSort();
            case "HeapSort" -> lista.heapSort();
        }
    }

    private static void aplicarAlgoritmo(ListaEncadeada lista, String algoritmo) {
        switch (algoritmo) {
            case "metodos_ordenacao.BubbleSort" -> lista.bubbleSort();
            case "metodos_ordenacao.SelectionSort" -> lista.selectionSort();
            case "metodos_ordenacao.InsertionSort" -> lista.insertionSort();
            case "metodos_ordenacao.MergeSort" -> lista.mergeSort();
            case "metodos_ordenacao.QuickSort" -> lista.quickSort();
            case "HeapSort" -> lista.heapSort();
        }
    }

    private static void aplicarAlgoritmo(ListaDuplaEncadeada lista, String algoritmo) {
        switch (algoritmo) {
            case "metodos_ordenacao.BubbleSort" -> lista.bubbleSort();
            case "metodos_ordenacao.SelectionSort" -> lista.selectionSort();
            case "metodos_ordenacao.InsertionSort" -> lista.insertionSort();
            case "metodos_ordenacao.MergeSort" -> lista.mergeSort();
            case "metodos_ordenacao.QuickSort" -> lista.quickSort();
            case "HeapSort" -> lista.heapSort();
        }
    }

    // Extrai o nome curto do algoritmo para exibicao (ex: "metodos_ordenacao.BubbleSort" -> "BubbleSort")
    private static String nomeExibicao(String algoritmo) {
        return algoritmo.substring(algoritmo.lastIndexOf('.') + 1);
    }

    private static void imprimirCabecalhoTabela(String titulo) {
        System.out.println("\n" + titulo);
        System.out.println("┌─────────────────────┬────────────┬───────────────┬──────────────┐");
        System.out.printf( "│ %-19s │ %10s │ %13s │ %12s │%n",
                "Algoritmo", "Tempo (ms)", "Movimentacoes", "Comparacoes");
        System.out.println("├─────────────────────┼────────────┼───────────────┼──────────────┤");
    }

    private static void imprimirRodapeTabela() {
        System.out.println("└─────────────────────┴────────────┴───────────────┴──────────────┘");
    }

    private static void testarListaArranjo(ArrayList<TitleBasic> original) {
        imprimirCabecalhoTabela("--- ListaArranjo: Comparativo de Ordenacao (por ano) ---");

        for (String algoritmo : ALGORITMOS) {
            ListaArranjo lista = construirListaArranjo(original);
            long tempoMs = executarComCronometro(() -> aplicarAlgoritmo(lista, algoritmo), nomeExibicao(algoritmo));
            System.out.printf("│ %-19s │ %10d │ %13d │ %12d │%n",
                    nomeExibicao(algoritmo), tempoMs, lista.getMovimentacoes(), lista.getComparacoes());
        }

        imprimirRodapeTabela();
    }

    private static void testarListaEncadeada(ArrayList<TitleBasic> original) {
        imprimirCabecalhoTabela("--- ListaEncadeada: Comparativo de Ordenacao (por ano) ---");

        for (String algoritmo : ALGORITMOS) {
            ListaEncadeada lista = construirListaEncadeada(original);
            long tempoMs = executarComCronometro(() -> aplicarAlgoritmo(lista, algoritmo), nomeExibicao(algoritmo));
            System.out.printf("│ %-19s │ %10d │ %13d │ %12d │%n",
                    nomeExibicao(algoritmo), tempoMs, lista.getMovimentacoes(), lista.getComparacoes());
        }

        imprimirRodapeTabela();
    }

    private static void testarListaDuplaEncadeada(ArrayList<TitleBasic> original) {
        imprimirCabecalhoTabela("--- ListaDuplaEncadeada: Comparativo de Ordenacao (por ano) ---");

        for (String algoritmo : ALGORITMOS) {
            ListaDuplaEncadeada lista = construirListaDuplaEncadeada(original);
            long tempoMs = executarComCronometro(() -> aplicarAlgoritmo(lista, algoritmo), nomeExibicao(algoritmo));
            System.out.printf("│ %-19s │ %10d │ %13d │ %12d │%n",
                    nomeExibicao(algoritmo), tempoMs, lista.getMovimentacoes(), lista.getComparacoes());
        }

        imprimirRodapeTabela();
    }

    private static void testarArvoreBinaria(ArrayList<TitleBasic> original) {
        ArvoreBinaria arvore = new ArvoreBinaria();
        long tempoMs = executarComCronometro(() -> arvore.inserirTodos(original), "ArvoreBinaria");

        System.out.println("\n--- Arvore.ArvoreBinaria (BST) ---");
        System.out.println("┌─────────────────────────┬──────────────────────┐");
        System.out.printf( "│ %-23s │ %20s │%n", "Metrica", "Valor");
        System.out.println("├─────────────────────────┼──────────────────────┤");
        System.out.printf( "│ %-23s │ %18d ms │%n", "Tempo de insercao",    tempoMs);
        System.out.printf( "│ %-23s │ %20d │%n", "Altura da arvore",       arvore.altura());
        System.out.printf( "│ %-23s │ %20d │%n", "Comparacoes (insercao)", arvore.getComparacoes());
        System.out.println("└─────────────────────────┴──────────────────────┘");
    }

    private static void testarArvoreAVL(ArrayList<TitleBasic> original) {
        ArvoreAVL arvore = new ArvoreAVL();
        long tempoMs = executarComCronometro(() -> arvore.inserirTodos(original), "ArvoreAVL");

        System.out.println("\n--- Arvore.ArvoreAVL ---");
        System.out.println("┌─────────────────────────┬──────────────────────┐");
        System.out.printf( "│ %-23s │ %20s │%n", "Metrica", "Valor");
        System.out.println("├─────────────────────────┼──────────────────────┤");
        System.out.printf( "│ %-23s │ %18d ms │%n", "Tempo de insercao",         tempoMs);
        System.out.printf( "│ %-23s │ %20d │%n", "Altura da arvore",             arvore.altura());
        System.out.printf( "│ %-23s │ %20d │%n", "Fat. balanceamento (raiz)",    arvore.fatorBalanceamento());
        System.out.printf( "│ %-23s │ %20d │%n", "Comparacoes (insercao)",       arvore.getComparacoes());
        System.out.println("└─────────────────────────┴──────────────────────┘");
    }

    private static void executarTodos(ArrayList<TitleBasic> original) {
        System.out.println("\n--- Comparativo: Arvores ---");
        System.out.println("┌─────────────────┬────────────┬────────┬──────────────┐");
        System.out.printf( "│ %-15s │ %10s │ %6s │ %12s │%n", "Estrutura", "Tempo (ms)", "Altura", "Comparacoes");
        System.out.println("├─────────────────┼────────────┼────────┼──────────────┤");

        ArvoreBinaria bst = new ArvoreBinaria();
        long t1 = executarComCronometro(() -> bst.inserirTodos(original), "ArvoreBinaria");
        System.out.printf("│ %-15s │ %10d │ %6d │ %12d │%n",
                "ArvoreBinaria", t1, bst.altura(), bst.getComparacoes());

        ArvoreAVL avl = new ArvoreAVL();
        long t2 = executarComCronometro(() -> avl.inserirTodos(original), "ArvoreAVL");
        System.out.printf("│ %-15s │ %10d │ %6d │ %12d │%n",
                "ArvoreAVL", t2, avl.altura(), avl.getComparacoes());

        System.out.println("└─────────────────┴────────────┴────────┴──────────────┘");

        testarListaArranjo(original);
        testarListaEncadeada(original);
        testarListaDuplaEncadeada(original);
    }

    private static void testarPesquisaSequencial(ArrayList<TitleBasic> original, Scanner scanner) {
        System.out.print("  Digite o ID numerico do tconst (ex: 1, 42, 100): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consome o \n restante apos nextInt

        ArrayList<TitleBasic> copia = new ArrayList<>(original);
        MergeSort sorter = new MergeSort();
        sorter.mergeSortPorTconstId(copia);

        PesquisaSequencial pesq = new PesquisaSequencial();
        long inicio = System.nanoTime();
        TitleBasic resultado = pesq.buscar(copia, id);
        long fim = System.nanoTime();

        System.out.println("\n--- Pesquisa Sequencial ---");
        System.out.println("┌───────────────────────────┬─────────────────────────────────────────────┐");
        System.out.printf( "│ %-25s │ %-43s │%n", "Campo", "Valor");
        System.out.println("├───────────────────────────┼─────────────────────────────────────────────┤");
        System.out.printf( "│ %-25s │ %-43d │%n", "ID buscado", id);
        String tituloResultado = (resultado != null)
                ? resultado.getPrimaryTitle() + " (" + resultado.getStartYear() + ")"
                : "Nao encontrado";
        System.out.printf( "│ %-25s │ %-43s │%n", "Resultado", tituloResultado);
        System.out.printf( "│ %-25s │ %-41.4f ms │%n", "Tempo", (fim - inicio) / 1_000_000.0);
        System.out.printf( "│ %-25s │ %-43d │%n", "Comparacoes", pesq.getConsultas());
        System.out.println("└───────────────────────────┴─────────────────────────────────────────────┘");
    }

    private static void testarPesquisaBinaria(ArrayList<TitleBasic> original, Scanner scanner) {
        System.out.print("  Digite o ID numerico do tconst (ex: 1, 42, 100): ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consome o \n restante apos nextInt

        ArrayList<TitleBasic> copia = new ArrayList<>(original);
        MergeSort sorter = new MergeSort();
        sorter.mergeSortPorTconstId(copia);

        PesquisaBinaria pesq = new PesquisaBinaria();
        long inicio = System.nanoTime();
        TitleBasic resultado = pesq.buscar(copia, id);
        long fim = System.nanoTime();

        System.out.println("\n--- Pesquisa Binaria ---");
        System.out.println("┌───────────────────────────┬─────────────────────────────────────────────┐");
        System.out.printf( "│ %-25s │ %-43s │%n", "Campo", "Valor");
        System.out.println("├───────────────────────────┼─────────────────────────────────────────────┤");
        System.out.printf( "│ %-25s │ %-43d │%n", "ID buscado", id);
        String tituloResultado = (resultado != null)
                ? resultado.getPrimaryTitle() + " (" + resultado.getStartYear() + ")"
                : "Nao encontrado";
        System.out.printf( "│ %-25s │ %-43s │%n", "Resultado", tituloResultado);
        System.out.printf( "│ %-25s │ %-41.4f ms │%n", "Tempo", (fim - inicio) / 1_000_000.0);
        System.out.printf( "│ %-25s │ %-43d │%n", "Comparacoes", pesq.getConsultas());
        System.out.println("└───────────────────────────┴─────────────────────────────────────────────┘");
    }


    // Opcao 9 — Comparativo de Buscas (pior caso: ultimo elemento)

    private static void testarComparativoBuscas(ArrayList<TitleBasic> original) {
        // Alvo = ultimo elemento da base carregada (pior caso para busca sequencial)
        TitleBasic alvo = original.get(original.size() - 1);
        int idAlvo = alvo.getTconstId();

        System.out.println("\n--- Comparativo de Buscas (Pior Caso) ---");
        System.out.printf("  Alvo: \"%s\" | %s | Ano: %s%n",
                alvo.getPrimaryTitle(), alvo.getTconst(),
                alvo.getStartYear() != null ? alvo.getStartYear() : "N/A");
        System.out.println("  (Pior caso = ultimo elemento da base carregada)");

        // Prepara copia ordenada por tconstId para as buscas em lista
        ArrayList<TitleBasic> listaOrdenada = new ArrayList<>(original);
        MergeSort sorter = new MergeSort();
        sorter.mergeSortPorTconstId(listaOrdenada);

        // Constroi BST e AVL com cronometro (so indexacao; a busca sera medida separadamente)
        ArvoreBinaria bst = new ArvoreBinaria();
        executarComCronometro(() -> bst.inserirTodos(original), "BST build");

        ArvoreAVL avl = new ArvoreAVL();
        executarComCronometro(() -> avl.inserirTodos(original), "AVL build");

        // Cabecalho da tabela
        System.out.println();
        System.out.println("┌────────────────────────────────────┬──────────────────┬──────────────┐");
        System.out.printf( "│ %-34s │ %16s │ %12s │%n", "Algoritmo", "Tempo (ms)", "Comparacoes");
        System.out.println("├────────────────────────────────────┼──────────────────┼──────────────┤");

        // 1. Pesquisa Sequencial O(n)
        PesquisaSequencial pesqSeq = new PesquisaSequencial();
        long t1i = System.nanoTime();
        pesqSeq.buscar(listaOrdenada, idAlvo);
        long t1f = System.nanoTime();
        System.out.printf("│ %-34s │ %16.4f │ %12d │%n",
                "Sequencial O(n)", (t1f - t1i) / 1_000_000.0, pesqSeq.getConsultas());

        // 2. Pesquisa Binaria O(log n) — lista ja ordenada por tconstId
        PesquisaBinaria pesqBin = new PesquisaBinaria();
        long t2i = System.nanoTime();
        pesqBin.buscar(listaOrdenada, idAlvo);
        long t2f = System.nanoTime();
        System.out.printf("│ %-34s │ %16.4f │ %12d │%n",
                "Binaria O(log n)", (t2f - t2i) / 1_000_000.0, pesqBin.getConsultas());

        // 3. Busca na Arvore Binaria (BST)
        bst.resetComparacoes();
        long t3i = System.nanoTime();
        bst.pesquisa(alvo);
        long t3f = System.nanoTime();
        System.out.printf("│ %-34s │ %16.4f │ %12d │%n",
                "Arvore Binaria BST (O(log n)*)", (t3f - t3i) / 1_000_000.0, bst.getComparacoes());

        // 4. Busca na Arvore AVL
        avl.resetComparacoes();
        long t4i = System.nanoTime();
        avl.pesquisa(alvo);
        long t4f = System.nanoTime();
        System.out.printf("│ %-34s │ %16.4f │ %12d │%n",
                "Arvore AVL (O(log n))", (t4f - t4i) / 1_000_000.0, avl.getComparacoes());

        System.out.println("└────────────────────────────────────┴──────────────────┴──────────────┘");
        System.out.println("  * BST nao balanceada: altura pode degenerar para O(n) em casos extremos.");
    }

}
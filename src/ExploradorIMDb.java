import estrutura_arvore.ArvoreAVL;
import estrutura_arvore.TNodoAVL;
import leitura_arquivo.TitleBasic;
import metodos_insercao.ListaArranjo;

import java.util.ArrayList;
import java.util.Scanner;

public class ExploradorIMDb {

    private final ArvoreAVL indiceAVL;
    private final ListaArranjo catalogo;
    private int totalRegistros;
    private final Scanner scanner;


    // Construtor — Indexacao Composta (AVL + ListaArranjo)


    public ExploradorIMDb(ArrayList<TitleBasic> dados, Scanner scanner) {
        this.scanner = scanner;
        this.totalRegistros = dados.size();
        this.indiceAVL = new ArvoreAVL();
        this.catalogo = new ListaArranjo(dados.size() + 1000); // +1000 de margem para insercoes

        System.out.println("\n  Indexando " + totalRegistros + " registros...");
        for (TitleBasic t : dados) {
            indiceAVL.insere(t);
            catalogo.InsereFinal(t);
        }
        System.out.println("  Indexacao concluida! AVL e ListaArranjo prontas.");
        Main.pausar(scanner);
    }


    // Loop principal do Explorador


    public void iniciar() {
        int opcao;
        do {
            Main.limparTela();
            exibirMenuExplorador();
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> consultarPorId();
                case 2 -> filtrarPorAno();
                case 3 -> inserirFilme();
                case 4 -> removerFilme();
                case 0 -> {} // retorna ao hub principal
                default -> System.out.println("  Opcao invalida!");
            }

            if (opcao != 0) Main.pausar(scanner);

        } while (opcao != 0);
    }

    private void exibirMenuExplorador() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.printf( "║   EXPLORADOR DE CATALOGO IMDb                  ║%n");
        System.out.printf( "║   Acervo atual: %-6d filmes                  ║%n", totalRegistros);
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║  1.  Consultar Filme por Ano + ID  (AVL)       ║");
        System.out.println("║  2.  Filtrar Catalogo por Ano de Lancamento    ║");
        System.out.println("║  3.  Inserir Novo Filme                        ║");
        System.out.println("║  4.  Remover Filme por Ano + ID                ║");
        System.out.println("║  0.  Voltar ao Menu Principal                  ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.print("  Escolha: ");
    }


    // Opcao 1 — Consultar por Ano + tconst (busca O(log n) honesta na AVL)


    private void consultarPorId() {
        System.out.println("\n  --- Consulta por Ano + ID (Arvore AVL) ---");
        System.out.print("  Digite o Ano de lancamento do filme (Exemplo: 1994): ");
        int ano = scanner.nextInt();
        scanner.nextLine();
        System.out.print("  Digite o ID do filme (Exemplo: tt0111161): ");
        String tconst = scanner.nextLine().trim();

        // Objeto dummy com apenas Ano e tconst: suficiente para a chave (ano, tconst) da AVL
        TitleBasic dummy = new TitleBasic(tconst, "", "", "", false, ano, null, null, null);

        indiceAVL.resetComparacoes();
        long inicio = System.nanoTime();
        TNodoAVL resultado = indiceAVL.pesquisa(dummy);
        long fim = System.nanoTime();

        int comparacoes = indiceAVL.getComparacoes();
        double tempoMs = (fim - inicio) / 1_000_000.0;

        System.out.println();
        if (resultado != null) {
            TitleBasic filme = resultado.item;
            String generos = filme.getGenres().isEmpty() ? "N/A" : String.join(", ", filme.getGenres());
            String duracao = filme.getRuntimeMinutes() != null ? filme.getRuntimeMinutes() + " min" : "N/A";

            System.out.println("  ┌──────────────────────┬────────────────────────────────────┐");
            System.out.printf( "  │ %-20s │ %-34s │%n", "Campo",     "Valor");
            System.out.println("  ├──────────────────────┼────────────────────────────────────┤");
            System.out.printf( "  │ %-20s │ %-34s │%n", "tconst",    filme.getTconst());
            System.out.printf( "  │ %-20s │ %-34s │%n", "Titulo",    truncar(filme.getPrimaryTitle(), 34));
            System.out.printf( "  │ %-20s │ %-34s │%n", "Tipo",      filme.getTitleType());
            System.out.printf( "  │ %-20s │ %-34d │%n", "Ano",       filme.getStartYear());
            System.out.printf( "  │ %-20s │ %-34s │%n", "Duracao",   duracao);
            System.out.printf( "  │ %-20s │ %-34s │%n", "Generos",   truncar(generos, 34));
            System.out.println("  └──────────────────────┴────────────────────────────────────┘");
        } else {
            System.out.println("  Filme nao encontrado na arvore.");
        }

        exibirTelemetria("Busca AVL — O(log n)", tempoMs, comparacoes);
    }


    // Opcao 2 — Filtrar por Ano (ListaArranjo + QuickSort)


    private void filtrarPorAno() {
        System.out.println("\n  --- Filtrar Catalogo por Ano de Lancamento ---");
        System.out.print("  Ano desejado: ");
        int ano = scanner.nextInt();
        scanner.nextLine();

        // Ordena o catalogo por ano via QuickSort (in-place)
        catalogo.quickSort();
        int compsSort = catalogo.getComparacoes();
        long inicio = System.currentTimeMillis();

        // Coleta filmes do ano (lista ja ordenada)
        ArrayList<TitleBasic> encontrados = new ArrayList<>();
        for (int i = catalogo.Primeiro; i < catalogo.Ultimo; i++) {
            TitleBasic t = catalogo.Item[i];
            Integer anoFilme = t.getStartYear();
            if (anoFilme != null && anoFilme == ano) {
                encontrados.add(t);
            }
        }
        long fim = System.currentTimeMillis();

        System.out.printf("%n  Resultados para o ano %d: %d filme(s) encontrado(s)%n%n", ano, encontrados.size());

        if (encontrados.isEmpty()) {
            System.out.println("  Nenhum filme encontrado para este ano.");
        } else {
            System.out.println("  ┌────────────────┬───────────────────────────────────────┬──────────┐");
            System.out.printf( "  │ %-14s │ %-37s │ %-8s │%n", "ID", "Titulo", "Tipo");
            System.out.println("  ├────────────────┼───────────────────────────────────────┼──────────┤");

            int exibidos = 0;
            for (TitleBasic t : encontrados) {
                if (exibidos >= 20) break;
                System.out.printf("  │ %-14s │ %-37s │ %-8s │%n",
                        t.getTconst(),
                        truncar(t.getPrimaryTitle(), 37),
                        t.getTitleType());
                exibidos++;
            }
            System.out.println("  └────────────────┴───────────────────────────────────────┴──────────┘");

            if (encontrados.size() > 20) {
                System.out.printf("  (exibindo 20 de %d resultados)%n", encontrados.size());
            }
        }

        exibirTelemetria("QuickSort + Filtro O(n log n)", (double)(fim - inicio), compsSort);
    }


    // Opcao 3 — Inserir Novo Filme (AVL + ListaArranjo)


    private void inserirFilme() {
        System.out.println("\n  --- Inserir Novo Filme ---");
        System.out.print("  tconst (ex: tt9999999): ");
        String tconst = scanner.nextLine().trim();
        System.out.print("  Titulo principal: ");
        String titulo = scanner.nextLine().trim();
        System.out.print("  Tipo (movie / short / tvSeries / ...): ");
        String tipo = scanner.nextLine().trim();
        System.out.print("  Ano de lancamento: ");
        int ano = scanner.nextInt();
        scanner.nextLine();

        TitleBasic novo = new TitleBasic(tconst, tipo, titulo, titulo, false, ano, null, null, null);

        indiceAVL.resetComparacoes();
        long inicio = System.nanoTime();
        indiceAVL.insere(novo);
        long fim = System.nanoTime();
        int comps = indiceAVL.getComparacoes();

        catalogo.InsereFinal(novo);
        totalRegistros++;

        double tempoMs = (fim - inicio) / 1_000_000.0;
        System.out.printf("%n  Filme \"%s\" inserido com sucesso!%n", titulo);
        System.out.printf("  Total de registros no acervo: %d%n", totalRegistros);
        exibirTelemetria("Insercao AVL — O(log n)", tempoMs, comps);
    }


    // Opcao 4 — Remover Filme por Ano + tconst (AVL + ListaArranjo)


    private void removerFilme() {
        System.out.println("\n  --- Remover Filme por Ano + ID ---");
        System.out.print("  Digite o Ano de lancamento do filme (Exemplo: 1994): ");
        int ano = scanner.nextInt();
        scanner.nextLine();
        System.out.print("  Digite o ID do filme (Exemplo: tt0111161): ");
        String tconst = scanner.nextLine().trim();

        TitleBasic dummy = new TitleBasic(tconst, "", "", "", false, ano, null, null, null);

        // Verifica existencia antes de remover
        indiceAVL.resetComparacoes();
        TNodoAVL encontrado = indiceAVL.pesquisa(dummy);

        if (encontrado == null) {
            System.out.println("\n  Filme nao encontrado. Nenhuma remocao realizada.");
            exibirTelemetria("Busca (nao encontrado)", 0.0, indiceAVL.getComparacoes());
            return;
        }

        String tituloFilme = encontrado.item.getPrimaryTitle();

        // Remove da AVL
        indiceAVL.resetComparacoes();
        long inicio = System.nanoTime();
        indiceAVL.remove(dummy);
        long fim = System.nanoTime();
        int comps = indiceAVL.getComparacoes();

        // Remove da ListaArranjo pelo tconst
        catalogo.PesquisaRemove(tconst);
        totalRegistros--;

        double tempoMs = (fim - inicio) / 1_000_000.0;
        System.out.printf("%n  Filme \"%s\" removido com sucesso!%n", tituloFilme);
        System.out.printf("  Total de registros no acervo: %d%n", totalRegistros);
        exibirTelemetria("Remocao AVL — O(log n)", tempoMs, comps);
    }


    // Telemetria com grafico de barras ASCII


    private void exibirTelemetria(String operacao, double tempoMs, int comparacoes) {
        final int BAR_MAX = 20;
        final String BLOCOS = "████████████████████"; // 20 blocos

        // Barra da operacao: proporcional a comparacoes/totalRegistros (escala log para visualizacao)
        int barraOp = Math.max(1, Math.min(BAR_MAX, (int)(BAR_MAX * (double) comparacoes / Math.max(1, totalRegistros))));
        // Barra linear O(n) = sempre o maximo (referencia)
        int barraLinear = BAR_MAX;

        String barraOpStr     = BLOCOS.substring(0, barraOp);
        String barraLinearStr = BLOCOS.substring(0, barraLinear);

        // Padding para alinhar as barras
        String padOp     = " ".repeat(BAR_MAX - barraOp);
        String padLinear = " ".repeat(BAR_MAX - barraLinear);

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║          TELEMETRIA DE DESEMPENHO                   ║");
        System.out.println("  ╠══════════════════════════════════════════════════════╣");
        System.out.printf( "  ║  Operacao   : %-39s║%n", truncar(operacao, 39));
        System.out.printf( "  ║  Tempo      : %-36.4f ms ║%n", tempoMs);
        System.out.printf( "  ║  Comparacoes: %-39d║%n", comparacoes);
        System.out.printf( "  ║  Total (n)  : %-39d║%n", totalRegistros);
        System.out.println("  ╠══════════════════════════════════════════════════════╣");
        System.out.println("  ║  Eficiencia Relativa  (escala: █ = n/20 registros)  ║");
        System.out.printf( "  ║  Esta op. [%s%s] %6d comparacoes ║%n", barraOpStr, padOp, comparacoes);
        System.out.printf( "  ║  Linear   [%s%s] %6d registros  ║%n", barraLinearStr, padLinear, totalRegistros);
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
    }


    // Utilitario — trunca strings longas para exibicao em tabela


    private String truncar(String texto, int maxLen) {
        if (texto == null) return "N/A";
        if (texto.length() <= maxLen) return texto;
        return texto.substring(0, maxLen - 3) + "...";
    }
}

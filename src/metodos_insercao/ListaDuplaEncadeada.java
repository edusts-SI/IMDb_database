package metodos_insercao;

import leitura_arquivo.TitleBasic;

public class ListaDuplaEncadeada {

    public static class NodoLista {
        public TitleBasic Item;
        public NodoLista Anterior;
        public NodoLista Proximo;

        public NodoLista(TitleBasic Item) {
            this.Item = Item;
            this.Anterior = null;
            this.Proximo = null;
        }
    }

    public NodoLista Primeiro; // célula cabeça (sentinela)
    public NodoLista Ultimo;   // último nó de verdade da lista

    private int movimentacoes = 0; // usado pelos métodos de ordenação
    private int comparacoes = 0;

    public ListaDuplaEncadeada() {
        Cria();
    }

    // ======================================================
    // Cria / Vazia
    // ======================================================

    public void Cria() {
        Primeiro = new NodoLista(null);
        Ultimo = Primeiro;
    }

    public boolean Vazia() {
        return Primeiro.Proximo == null;
    }

    public int getMovimentacoes() {
        return movimentacoes;
    }

    public int getComparacoes() {
        return comparacoes;
    }

    // ======================================================
    // Inserção
    // ======================================================

    public void InsereInicio(TitleBasic Item) {
        NodoLista p = new NodoLista(Item);
        p.Proximo = Primeiro.Proximo;
        p.Anterior = Primeiro;

        if (p.Proximo != null) {
            p.Proximo.Anterior = p;
        } else {
            Ultimo = p; // lista estava vazia: o novo nó também é o último
        }

        Primeiro.Proximo = p;
    }

    public void InserePosicao(int p, TitleBasic Item) {
        NodoLista atual = Primeiro;
        int i = 0;

        while (i < p && atual.Proximo != null) {
            atual = atual.Proximo;
            i++;
        }

        if (i < p) {
            System.out.println("Erro: Posicao invalida");
            return;
        }

        NodoLista novo = new NodoLista(Item);
        novo.Proximo = atual.Proximo;
        novo.Anterior = atual;

        if (atual.Proximo != null) {
            atual.Proximo.Anterior = novo;
        } else {
            Ultimo = novo;
        }

        atual.Proximo = novo;
    }

    public void InsereFinal(TitleBasic Item) {
        NodoLista p = new NodoLista(Item);
        p.Anterior = Ultimo;
        Ultimo.Proximo = p;
        Ultimo = p;
    }

    // ======================================================
    // Remoção
    // ======================================================

    public void RemoveInicio() {
        if (Vazia()) {
            System.out.println("Erro : A lista esta vazia.");
            return;
        }

        NodoLista p = Primeiro.Proximo;
        System.out.println("Elemento Removido=" + p.Item.getPrimaryTitle() + " (" + p.Item.getStartYear() + ")");
        Primeiro.Proximo = p.Proximo;

        if (p.Proximo != null) {
            p.Proximo.Anterior = Primeiro;
        } else {
            Ultimo = Primeiro; // lista ficou vazia
        }
    }

    public void RemoveFinal() {
        if (Vazia()) {
            System.out.println("Erro : A lista esta vazia.");
            return;
        }

        NodoLista p = Ultimo;
        Ultimo = p.Anterior;
        Ultimo.Proximo = null;
    }

    // busca e remove pelo identificador unico do filme (tconst)
    public void PesquisaRemove(String tconst) {
        if (Vazia()) {
            System.out.println("Lista vazia");
            return;
        }

        NodoLista p = Primeiro.Proximo;
        while (p != null && !p.Item.getTconst().equals(tconst)) {
            p = p.Proximo;
        }

        if (p == null) {
            System.out.println("Elemento nao encontrado");
            return;
        }

        p.Anterior.Proximo = p.Proximo;
        if (p.Proximo != null) {
            p.Proximo.Anterior = p.Anterior;
        } else {
            Ultimo = p.Anterior;
        }
    }

    // ======================================================
    // Impressão
    // ======================================================

    public void Imprime() {
        System.out.print("[");
        NodoLista p = Primeiro.Proximo;
        while (p != null) {
            System.out.print(p.Item.getPrimaryTitle() + " (" + p.Item.getStartYear() + ")");
            if (p.Proximo != null) System.out.print(", ");
            p = p.Proximo;
        }
        System.out.println("]");
    }

    public void ImprimeInverso() {
        System.out.print("[");
        NodoLista p = Ultimo;
        while (p != null && p != Primeiro) {
            System.out.print(p.Item.getPrimaryTitle() + " (" + p.Item.getStartYear() + ")");
            if (p.Anterior != null && p.Anterior != Primeiro) System.out.print(", ");
            p = p.Anterior;
        }
        System.out.println("]");
    }

    // ---------- Bubble Sort ----------
    public void bubbleSort() {
        movimentacoes = 0;
        comparacoes = 0;
        if (Vazia()) return;

        boolean trocou;
        do {
            trocou = false;
            NodoLista out = Primeiro.Proximo;
            while (out != null && out.Proximo != null) {

                Integer ano1 = out.Item.getStartYear();
                Integer ano2 = out.Proximo.Item.getStartYear();

                // Trata anos nulos como o maior valor(IMDB deixa registrar sem ano de lançamento)
                if (ano1 == null) ano1 = Integer.MAX_VALUE;
                if (ano2 == null) ano2 = Integer.MAX_VALUE;

                comparacoes++;
                if (ano1 > ano2) {
                    swap(out, out.Proximo);
                    trocou = true;
                }
                out = out.Proximo;
            }
        } while (trocou);
    }

    // ---------- Selection Sort ----------
    public void selectionSort() {
        movimentacoes = 0;
        comparacoes = 0;

        NodoLista out = Primeiro.Proximo;
        while (out != null) {
            NodoLista min = out;
            NodoLista in = out.Proximo;
            while (in != null) {

                Integer ano1 = in.Item.getStartYear();
                Integer ano2 = min.Item.getStartYear();

                // Trata anos nulos como o maior valor(IMDB deixa registrar sem ano de lançamento)
                if (ano1 == null) ano1 = Integer.MAX_VALUE;
                if (ano2 == null) ano2 = Integer.MAX_VALUE;

                comparacoes++;
                if (ano1 < ano2) {
                    min = in;
                }
                in = in.Proximo;
            }
            swap(out, min);
            out = out.Proximo;
        }
    }

    // ---------- Insertion Sort (deslocamento andando pra trás com Anterior) ----------
    public void insertionSort() {
        movimentacoes = 0;
        comparacoes = 0;
        if (Vazia() || Primeiro.Proximo.Proximo == null) return;

        NodoLista out = Primeiro.Proximo.Proximo;
        while (out != null) {
            TitleBasic temp = out.Item;
            Integer anoTemp = temp.getStartYear();
            if (anoTemp == null) anoTemp = Integer.MAX_VALUE;

            NodoLista in = out.Anterior;

            while (in != Primeiro) {
                Integer anoAnterior = in.Item.getStartYear();
                if (anoAnterior == null) anoAnterior = Integer.MAX_VALUE;

                comparacoes++;
                if (anoAnterior > anoTemp) {
                    in.Proximo.Item = in.Item; // empurra o valor de "in" pra frente
                    movimentacoes++;
                    in = in.Anterior;
                } else {
                    break;
                }
            }

            in.Proximo.Item = temp;
            movimentacoes++;
            out = out.Proximo;
        }
    }

    // ---------- Merge Sort ----------
    public void mergeSort() {
        movimentacoes = 0;
        comparacoes = 0;
        if (Vazia()) return;

        Primeiro.Proximo = recMergeSort(Primeiro.Proximo);
        atualizaLigacoes();
    }

    private NodoLista recMergeSort(NodoLista lowerBound) {
        if (lowerBound == null || lowerBound.Proximo == null) {
            return lowerBound;
        }

        NodoLista mid = acharMeio(lowerBound);
        NodoLista upperBound = mid.Proximo;
        mid.Proximo = null;

        NodoLista esquerda = recMergeSort(lowerBound);
        NodoLista direita = recMergeSort(upperBound);

        return merge(esquerda, direita);
    }

    private NodoLista acharMeio(NodoLista inicio) {
        NodoLista lento = inicio;
        NodoLista rapido = inicio.Proximo;

        while (rapido != null && rapido.Proximo != null) {
            lento = lento.Proximo;
            rapido = rapido.Proximo.Proximo;
        }
        return lento;
    }

    private NodoLista merge(NodoLista lowPtr, NodoLista highPtr) {
        NodoLista workSpace = new NodoLista(null);
        NodoLista j = workSpace;

        while (lowPtr != null && highPtr != null) {

            // pega os dois anos para comparar depois
            Integer ano1 = lowPtr.Item.getStartYear();
            Integer ano2 = highPtr.Item.getStartYear();

            // Trata anos nulos como o maior valor
            if (ano1 == null) ano1 = Integer.MAX_VALUE;
            if (ano2 == null) ano2 = Integer.MAX_VALUE;

            // começa a comparação
            comparacoes++;
            if (ano1 <= ano2) {
                j.Proximo = lowPtr;
                lowPtr = lowPtr.Proximo;
            } else {
                j.Proximo = highPtr;
                highPtr = highPtr.Proximo;
            }
            j = j.Proximo;
            movimentacoes++;
        }

        j.Proximo = (lowPtr != null) ? lowPtr : highPtr;
        return workSpace.Proximo;
    }

    // ---------- Quick Sort ----------
    public void quickSort() {
        movimentacoes = 0;
        comparacoes = 0;
        if (Vazia()) return;

        Primeiro.Proximo = recQuickSort(Primeiro.Proximo);
        atualizaLigacoes();
    }

    private NodoLista recQuickSort(NodoLista left) {
        if (left == null || left.Proximo == null) {
            return left;
        }

        NodoLista pivot = left;
        Integer pivotYear = pivot.Item.getStartYear();
        if (pivotYear == null) pivotYear = Integer.MAX_VALUE;

        NodoLista menorInicio = null, menorFim = null;
        NodoLista maiorInicio = null, maiorFim = null;

        NodoLista i = left.Proximo;
        while (i != null) {
            NodoLista proximo = i.Proximo;
            i.Proximo = null;

            Integer anoI = i.Item.getStartYear();
            if (anoI == null) anoI = Integer.MAX_VALUE;

            comparacoes++;
            if (anoI < pivotYear) {
                if (menorInicio == null) menorInicio = menorFim = i;
                else { menorFim.Proximo = i; menorFim = i; }
            } else {
                if (maiorInicio == null) maiorInicio = maiorFim = i;
                else { maiorFim.Proximo = i; maiorFim = i; }
            }
            movimentacoes++;
            i = proximo;
        }

        menorInicio = recQuickSort(menorInicio);
        maiorInicio = recQuickSort(maiorInicio);

        pivot.Proximo = maiorInicio;

        if (menorInicio == null) {
            return pivot;
        }

        NodoLista fimMenor = menorInicio;
        while (fimMenor.Proximo != null) {
            fimMenor = fimMenor.Proximo;
        }
        fimMenor.Proximo = pivot;
        return menorInicio;
    }

    // ---------- Heap Sort (via array temporário) ----------
    public void heapSort() {
        movimentacoes = 0;
        comparacoes = 0;
        if (Vazia()) return;

        TitleBasic[] vetor = paraArray();
        heapSortArray(vetor);
        regravar(vetor);
    }

    private TitleBasic[] paraArray() {
        int n = 0;
        for (NodoLista p = Primeiro.Proximo; p != null; p = p.Proximo) n++;

        TitleBasic[] vetor = new TitleBasic[n];
        int i = 0;
        for (NodoLista p = Primeiro.Proximo; p != null; p = p.Proximo) {
            vetor[i++] = p.Item;
        }
        return vetor;
    }

    private void regravar(TitleBasic[] vetor) {
        int i = 0;
        for (NodoLista p = Primeiro.Proximo; p != null; p = p.Proximo) {
            p.Item = vetor[i++];
        }
    }

    private void heapSortArray(TitleBasic[] v) {
        int n = v.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(v, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            TitleBasic temp = v[0];
            v[0] = v[i];
            v[i] = temp;
            movimentacoes++;
            heapify(v, i, 0);
        }
    }

    private void heapify(TitleBasic[] v, int n, int i) {
        int maior = i;
        int esq = 2 * i + 1;
        int dir = 2 * i + 2;

        Integer anoMaior = v[maior].getStartYear();
        if (anoMaior == null) anoMaior = Integer.MAX_VALUE;

        if (esq < n) {
            Integer anoEsq = v[esq].getStartYear();
            if (anoEsq == null) anoEsq = Integer.MAX_VALUE;
            comparacoes++;
            if (anoEsq > anoMaior) {
                maior = esq;
                anoMaior = anoEsq;
            }
        }

        if (dir < n) {
            Integer anoDir = v[dir].getStartYear();
            if (anoDir == null) anoDir = Integer.MAX_VALUE;
            comparacoes++;
            if (anoDir > anoMaior) {
                maior = dir;
            }
        }

        if (maior != i) {
            TitleBasic temp = v[i];
            v[i] = v[maior];
            v[maior] = temp;
            movimentacoes++;
            heapify(v, n, maior);
        }
    }

    // ---------- métodos auxiliares ----------

    private void swap(NodoLista a, NodoLista b) {
        TitleBasic temp = a.Item;
        a.Item = b.Item;
        b.Item = temp;
        movimentacoes++;
    }

    // depois de reconectar só os "Proximo" (merge/quick), reconstrói os
    // "Anterior" e o "Ultimo" numa única passada.
    private void atualizaLigacoes() {
        NodoLista anterior = Primeiro;
        NodoLista atual = Primeiro.Proximo;

        while (atual != null) {
            atual.Anterior = anterior;
            anterior = atual;
            atual = atual.Proximo;
        }

        Ultimo = anterior;
    }
}
package metodos_insercao;

import leitura_arquivo.TitleBasic;

public class ListaArranjo {

    public TitleBasic[] Item;
    public int Primeiro;
    public int Ultimo;
    public final int Tmax;

    private int movimentacoes = 0;
    private int comparacoes = 0;

    public ListaArranjo(int tamanho) {
        Tmax = tamanho;
        Item = new TitleBasic[Tmax];
        Primeiro = 0;
        Ultimo = 0;
    }

    // ======================================================
    // Estado da lista
    // ======================================================

    public boolean Cheia() {
        return Ultimo >= Tmax;
    }

    public boolean Vazia() {
        return Primeiro == Ultimo;
    }

    public int getMovimentacoes() {
        return movimentacoes;
    }

    public int getComparacoes() {
        return comparacoes;
    }

    // ======================================================
    // Impressão
    // ======================================================

    public void Imprime() {
        System.out.print("[");
        for (int i = Primeiro; i < Ultimo; i++) {
            System.out.print(Item[i].getPrimaryTitle() + " (" + Item[i].getStartYear() + ")");
            if (i < Ultimo - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    // ======================================================
    // Inserção
    // ======================================================

    public void InsereInicio(TitleBasic Item) {
        if (Cheia()) {
            System.out.println("Erro: A lista esta cheia");
            return;
        }

        for (int i = Ultimo; i > Primeiro; i--) {
            this.Item[i] = this.Item[i - 1];
        }

        this.Item[Primeiro] = Item;
        Ultimo++;
    }

    public void InserePosicao(int p, TitleBasic Item) {
        if (Cheia()) {
            System.out.println("Erro: Lista cheia");
            return;
        }

        if (p < Primeiro || p > Ultimo) {
            System.out.println("Erro: Posicao invalida");
            return;
        }

        for (int i = Ultimo; i > p; i--) {
            this.Item[i] = this.Item[i - 1];
        }

        this.Item[p] = Item;
        Ultimo++;
    }

    public void InsereFinal(TitleBasic Item) {
        if (Cheia()) {
            System.out.println("Erro: Lista cheia");
            return;
        }

        this.Item[Ultimo] = Item;
        Ultimo++;
    }

    // ======================================================
    // Remoção
    // ======================================================
    public void RemoveInicio() {
        if (Vazia()) {
            System.out.println("Erro: Lista vazia");
            return;
        }

        for (int i = Primeiro; i < Ultimo - 1; i++) {
            Item[i] = Item[i + 1];
        }

        Ultimo--;
    }

    public void RemoveFinal() {
        if (Vazia()) {
            System.out.println("Erro: Lista vazia");
            return;
        }

        Ultimo--;
    }

    // busca e remove pelo identificador unico do filme (tconst)
    public void PesquisaRemove(String tconst) {
        if (Vazia()) {
            System.out.println("Lista vazia");
            return;
        }

        int pos = -1;

        for (int i = Primeiro; i < Ultimo; i++) {
            if (Item[i].getTconst().equals(tconst)) {
                pos = i;
                break;
            }
        }

        if (pos == -1) {
            System.out.println("Elemento nao encontrado");
            return;
        }

        for (int i = pos; i < Ultimo - 1; i++) {
            Item[i] = Item[i + 1];
        }

        Ultimo--;
    }

    // ---------- Bubble Sort ----------
    public void bubbleSort() {
        movimentacoes = 0;
        comparacoes = 0;
        int out, in;

        for (out = Ultimo - 1; out > Primeiro; out--) {
            for (in = Primeiro; in < out; in++) {

                Integer ano1 = Item[in].getStartYear();
                Integer ano2 = Item[in + 1].getStartYear();

                // Trata anos nulos como o maior valor(IMDB deixa registrar sem ano de lançamento)
                if (ano1 == null) ano1 = Integer.MAX_VALUE;
                if (ano2 == null) ano2 = Integer.MAX_VALUE;

                comparacoes++;
                if (ano1 > ano2) {
                    swap(in, in + 1);
                }
            }
        }
    }

    // ---------- Selection Sort ----------
    public void selectionSort() {
        movimentacoes = 0;
        comparacoes = 0;
        int in, out, min;

        for (out = Primeiro; out < Ultimo - 1; out++) {
            min = out;
            for (in = out + 1; in < Ultimo; in++) {

                Integer ano1 = Item[in].getStartYear();
                Integer ano2 = Item[min].getStartYear();

                // Trata anos nulos como o maior valor(IMDB deixa registrar sem ano de lançamento)
                if (ano1 == null) ano1 = Integer.MAX_VALUE;
                if (ano2 == null) ano2 = Integer.MAX_VALUE;

                comparacoes++;
                if (ano1 < ano2) {
                    min = in;
                }
            }
            swap(out, min);
        }
    }

    // ---------- Insertion Sort ----------
    public void insertionSort() {
        movimentacoes = 0;
        comparacoes = 0;
        int in, out;

        for (out = Primeiro + 1; out < Ultimo; out++) {
            TitleBasic temp = Item[out];
            Integer anoTemp = temp.getStartYear();
            if (anoTemp == null) anoTemp = Integer.MAX_VALUE;

            in = out;

            while (in > Primeiro) {
                Integer anoAnterior = Item[in - 1].getStartYear();
                if (anoAnterior == null) {
                    anoAnterior = Integer.MAX_VALUE;
                }
                comparacoes++;
                if (anoAnterior >= anoTemp) {
                    Item[in] = Item[in - 1];
                    movimentacoes++;
                    in--;
                } else {
                    break;
                }
            }

            Item[in] = temp;
            movimentacoes++;
        }
    }

    // ---------- Quick Sort ----------
    public void quickSort() {
        movimentacoes = 0;
        comparacoes = 0;
        recQuickSort(Primeiro, Ultimo - 1);
    }

    public void recQuickSort(int left, int right) {

        if (left >= right) return;

        int partition = partitionIt(left, right);

        recQuickSort(left, partition - 1);
        recQuickSort(partition + 1, right);
    }

    private int partitionIt(int left, int right) {

        int i = left - 1;
        int j = right;

        TitleBasic pivot = Item[right];

        Integer pivotYear = pivot.getStartYear();
        if (pivotYear == null) pivotYear = Integer.MAX_VALUE;

        while (true) {

            while (true) {
                i++;

                Integer anoI = Item[i].getStartYear();
                if (anoI == null) anoI = Integer.MAX_VALUE;

                comparacoes++;
                if (anoI >= pivotYear) break;

            }

            while (true) {
                if (j == Primeiro) break;

                j--;

                Integer anoJ = Item[j].getStartYear();
                if (anoJ == null) anoJ = Integer.MAX_VALUE;

                comparacoes++;
                if (anoJ <= pivotYear) break;
            }

            if (i >= j) {
                break;
            } else {
                swap(i, j);
            }
        }
        swap(i, right);
        return i;
    }

    // ---------- Merge Sort ----------
    public void mergeSort() {
        movimentacoes = 0;
        comparacoes = 0;
        TitleBasic[] workSpace = new TitleBasic[Ultimo - Primeiro];

        recMergeSort(workSpace, Primeiro, Ultimo - 1);
    }

    private void recMergeSort(TitleBasic[] workSpace, int lowerBound, int upperBound) {
        if (lowerBound >= upperBound) {
            return;
        } else {
            int mid = (lowerBound + upperBound) / 2;
            recMergeSort(workSpace, lowerBound, mid);
            recMergeSort(workSpace, mid + 1, upperBound);
            merge(workSpace, lowerBound, mid + 1, upperBound);
        }
    }

    private void merge(TitleBasic[] workSpace, int lowPtr, int highPtr, int upperBound) {
        int j = 0;
        int lowerBound = lowPtr;
        int mid = highPtr - 1;
        int n = upperBound - lowerBound + 1;

        while (lowPtr <= mid && highPtr <= upperBound) {

            // pega os dois anos para comparar depois
            Integer ano1 = Item[lowPtr].getStartYear();
            Integer ano2 = Item[highPtr].getStartYear();

            // Trata anos nulos como o maior valor
            if (ano1 == null)
                ano1 = Integer.MAX_VALUE;

            if (ano2 == null)
                ano2 = Integer.MAX_VALUE;

            // começa a comparação
            comparacoes++;
            if (ano1 < ano2) {
                workSpace[j++] = Item[lowPtr++];
                movimentacoes++;
            } else {
                workSpace[j++] = Item[highPtr++];
                movimentacoes++;
            }
        }

        while (lowPtr <= mid) {
            workSpace[j++] = Item[lowPtr++];
            movimentacoes++;
        }

        while (highPtr <= upperBound) {
            workSpace[j++] = Item[highPtr++];
            movimentacoes++;
        }

        for (j = 0; j < n; j++) {
            Item[lowerBound + j] = workSpace[j];
            movimentacoes++;
        }
    }

    // ---------- Heap Sort ----------
    public void heapSort() {
        movimentacoes = 0;
        comparacoes = 0;
        int n = Ultimo - Primeiro;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            swap(Primeiro, Primeiro + i);
            heapify(i, 0);
        }
    }

    private void heapify(int n, int i) {
        int maior = i;
        int esq = 2 * i + 1;
        int dir = 2 * i + 2;

        Integer anoEsq = esq < n ? Item[Primeiro + esq].getStartYear() : null;
        Integer anoDir = dir < n ? Item[Primeiro + dir].getStartYear() : null;
        Integer anoMaior = Item[Primeiro + maior].getStartYear();
        if (anoMaior == null) anoMaior = Integer.MAX_VALUE;

        if (esq < n) {
            if (anoEsq == null) anoEsq = Integer.MAX_VALUE;
            comparacoes++;
            if (anoEsq > anoMaior) {
                maior = esq;
                anoMaior = anoEsq;
            }
        }

        if (dir < n) {
            if (anoDir == null) anoDir = Integer.MAX_VALUE;
            comparacoes++;
            if (anoDir > anoMaior) {
                maior = dir;
            }
        }

        if (maior != i) {
            swap(Primeiro + i, Primeiro + maior);
            heapify(n, maior);
        }
    }

    // ---------- metodo para trocas ----------
    private void swap(int i, int j) {
        TitleBasic temp = Item[i];
        Item[i] = Item[j];
        Item[j] = temp;
        movimentacoes++;
    }
}
package metodos_ordenacao;

import leitura_arquivo.TitleBasic;

import java.util.ArrayList;

public class QuickSort {

    private int movimentacoes = 0;
    private int comparacoes = 0;

    public void quickSort(ArrayList<TitleBasic> titles) {
        movimentacoes = 0;
        comparacoes = 0;
        recQuickSort(titles, 0, titles.size() - 1);
    }

    public void recQuickSort(ArrayList<TitleBasic> titles, int left, int right) {

        if (left >= right) return;

        int partition = partitionIt(titles, left, right);

        recQuickSort(titles, left, partition - 1);
        recQuickSort(titles, partition + 1, right);
    }

    private int partitionIt(ArrayList<TitleBasic> titles, int left, int right) {

        int i = left - 1;
        int j = right;

        TitleBasic pivot = titles.get(right);

        Integer pivotYear = pivot.getStartYear();
        if (pivotYear == null) pivotYear = Integer.MAX_VALUE;

        while (true) {

            while (true) {
                i++;

                Integer anoI = titles.get(i).getStartYear();
                if (anoI == null) anoI = Integer.MAX_VALUE;

                comparacoes++;
                if (anoI >= pivotYear) break;

            }

            while (true) {
            	if (j == 0) break;
            	
                j--;

                Integer anoJ = titles.get(j).getStartYear();
                if (anoJ == null) anoJ = Integer.MAX_VALUE;

                comparacoes++;
                if (anoJ <= pivotYear) break;
            }

            if (i >= j) {
                break;
            } else {
            	swap(titles, i, j);
            }
        }
        swap(titles, i, right);
        return i;
    }

    private void swap(ArrayList<TitleBasic> titles, int i, int j) {
        TitleBasic temp = titles.get(i);
        titles.set(i, titles.get(j));
        titles.set(j, temp);
        movimentacoes++;
    }

    public int getMovimentacoes() {
        return movimentacoes;
    }

    public int getComparacoes() {
        return comparacoes;
    }
}
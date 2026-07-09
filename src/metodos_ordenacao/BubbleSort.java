package metodos_ordenacao;

import leitura_arquivo.TitleBasic;

import java.util.ArrayList;

public class BubbleSort {

    private int movimentacoes = 0;
    private int comparacoes = 0;

    public void bubbleSort(ArrayList<TitleBasic> titles) {
        movimentacoes = 0;
        comparacoes = 0;
        int out, in;

        for (out = titles.size() - 1; out >= 1; out--) {
            for (in = 0; in < out; in++) {

                Integer ano1 = titles.get(in).getStartYear();
                Integer ano2 = titles.get(in + 1).getStartYear();

                // Trata anos nulos como o maior valor(IMDB deixa registrar sem ano de lançamento)
                if (ano1 == null) ano1 = Integer.MAX_VALUE;
                if (ano2 == null) ano2 = Integer.MAX_VALUE;

                comparacoes++;
                if (ano1 > ano2) {
                    swap(titles, in, in + 1);
                }
            }
        }
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
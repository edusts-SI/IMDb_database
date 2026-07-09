package metodos_ordenacao;

import leitura_arquivo.TitleBasic;

import java.util.ArrayList;

public class SelectionSort {
	private int movimentacoes = 0;
	private int comparacoes = 0;

	public void selectionSort(ArrayList<TitleBasic> titles) {
		movimentacoes = 0;
		comparacoes = 0;
		int in, out, min;
		
		for(out = 0; out < titles.size() - 1; out++) {
			min = out;
			for(in = out+1; in < titles.size(); in++) {
				
                Integer ano1 = titles.get(in).getStartYear();
                Integer ano2 = titles.get(min).getStartYear();

                // Trata anos nulos como o maior valor(IMDB deixa registrar sem ano de lançamento)
                if (ano1 == null) ano1 = Integer.MAX_VALUE;
                if (ano2 == null) ano2 = Integer.MAX_VALUE;
                
                comparacoes++;
                if(ano1 < ano2) {
                	min = in;
                }
			}
			swap(titles, out, min);
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
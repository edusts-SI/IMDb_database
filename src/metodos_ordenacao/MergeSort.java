package metodos_ordenacao;

import leitura_arquivo.TitleBasic;

import java.util.ArrayList;

public class MergeSort {
	private int movimentacoes = 0;
	private int comparacoes = 0;

	public void mergeSort(ArrayList<TitleBasic> titles) {
		movimentacoes = 0;
		comparacoes = 0;
		ArrayList<TitleBasic> workSpace = new ArrayList<>();

	        for (int i = 0; i < titles.size(); i++) {
	            workSpace.add(null);
	        }

	        recMergeSort(workSpace, titles, 0, titles.size() - 1);
	    }

	private void recMergeSort(ArrayList<TitleBasic> workSpace, 
							  ArrayList<TitleBasic> titles, int lowerBound, 
							  int upperBound) {
		if(lowerBound >= upperBound) {
			return;
		} else {
			int mid = (lowerBound + upperBound) / 2;
			recMergeSort(workSpace, titles, lowerBound, mid);
			recMergeSort(workSpace, titles, mid + 1, upperBound);
			merge(workSpace, titles, lowerBound, mid+1, upperBound);
		}
	}
	
	private void merge(ArrayList<TitleBasic> workSpace,
			           ArrayList<TitleBasic> titles, int lowPtr, int highPtr,
			           int upperBound) {
		int j = 0;
		int lowerBound = lowPtr;
		int mid = highPtr - 1;
		int n = upperBound - lowerBound + 1;
		
		while(lowPtr <= mid && highPtr <= upperBound) {
			
		 //pega os dois anos para comparar depois	
			Integer ano1 = titles.get(lowPtr).getStartYear();
            Integer ano2 = titles.get(highPtr).getStartYear();
            
         // Trata anos nulos como o maior valor
            if (ano1 == null)
                ano1 = Integer.MAX_VALUE;

            if (ano2 == null)
                ano2 = Integer.MAX_VALUE;
            
         // começa a comparação
            comparacoes++;
            if (ano1 < ano2) {
                workSpace.set(j++, titles.get(lowPtr++));
                movimentacoes++;
            } else {
                workSpace.set(j++, titles.get(highPtr++));
                movimentacoes++;
            }
		}
		
		while(lowPtr <= mid) {
			workSpace.set(j++, titles.get(lowPtr++));
			movimentacoes++;
		}
		
		while(highPtr <= upperBound) {
			workSpace.set(j++, titles.get(highPtr++));
			movimentacoes++;
		}
		
		for(j = 0; j < n; j++) {
			titles.set(lowerBound + j,  workSpace.get(j));
			movimentacoes++;
		}
	}

	public void mergeSortPorTconstId(ArrayList<TitleBasic> titles) {
		movimentacoes = 0;
		comparacoes = 0;
		ArrayList<TitleBasic> workSpace = new ArrayList<>();
		for (int i = 0; i < titles.size(); i++) {
			workSpace.add(null);
		}
		recMergeSortPorTconstId(workSpace, titles, 0, titles.size() - 1);
	}

	private void recMergeSortPorTconstId(ArrayList<TitleBasic> workSpace,
										 ArrayList<TitleBasic> titles, int lowerBound,
										 int upperBound) {
		if (lowerBound >= upperBound) return;
		int mid = (lowerBound + upperBound) / 2;
		recMergeSortPorTconstId(workSpace, titles, lowerBound, mid);
		recMergeSortPorTconstId(workSpace, titles, mid + 1, upperBound);
		mergePorTconstId(workSpace, titles, lowerBound, mid + 1, upperBound);
	}

	private void mergePorTconstId(ArrayList<TitleBasic> workSpace,
								  ArrayList<TitleBasic> titles, int lowPtr, int highPtr,
								  int upperBound) {
		int j = 0;
		int lowerBound = lowPtr;
		int mid = highPtr - 1;
		int n = upperBound - lowerBound + 1;

		while (lowPtr <= mid && highPtr <= upperBound) {
			int id1 = titles.get(lowPtr).getTconstId();
			int id2 = titles.get(highPtr).getTconstId();
			comparacoes++;
			if (id1 < id2) {
				workSpace.set(j++, titles.get(lowPtr++));
				movimentacoes++;
			} else {
				workSpace.set(j++, titles.get(highPtr++));
				movimentacoes++;
			}
		}

		while (lowPtr <= mid) {
			workSpace.set(j++, titles.get(lowPtr++));
			movimentacoes++;
		}

		while (highPtr <= upperBound) {
			workSpace.set(j++, titles.get(highPtr++));
			movimentacoes++;
		}

		for (j = 0; j < n; j++) {
			titles.set(lowerBound + j, workSpace.get(j));
			movimentacoes++;
		}
	}

	public int getMovimentacoes() {
		return movimentacoes;
	}

	public int getComparacoes() {
		return comparacoes;
	}
}
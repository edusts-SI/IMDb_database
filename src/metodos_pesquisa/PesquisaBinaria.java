package metodos_pesquisa;

import leitura_arquivo.TitleBasic;
import java.util.ArrayList;

public class PesquisaBinaria {

    private int consultas;

    public int getConsultas() {
        return consultas;
    }

    public TitleBasic buscar(ArrayList<TitleBasic> lista, int tconstId) {
        consultas = 0;
        int esq = 0;
        int dir = lista.size() - 1;

        while (esq <= dir) {
            consultas++;
            int meio = (esq + dir) / 2;
            int idMeio = lista.get(meio).getTconstId();

            if (idMeio == tconstId) {
                return lista.get(meio);
            } else if (idMeio < tconstId) {
                esq = meio + 1;
            } else {
                dir = meio - 1;
            }
        }

        return null;
    }
}

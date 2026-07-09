package metodos_pesquisa;

import leitura_arquivo.TitleBasic;
import java.util.ArrayList;

public class PesquisaSequencial {

    private int consultas;

    public int getConsultas() {
        return consultas;
    }

    public TitleBasic buscar(ArrayList<TitleBasic> lista, int tconstId) {
        consultas = 0;
        for (TitleBasic t : lista) {
            consultas++;
            if (t.getTconstId() == tconstId) {
                return t;
            }
        }
        return null;
    }
}

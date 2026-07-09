package estrutura_arvore;

import leitura_arquivo.*;


public class TNodoAVL {

    public TitleBasic item;
    public TNodoAVL esq;
    public TNodoAVL dir;
    public TNodoAVL pai;
    public int altura;

    public TNodoAVL(TitleBasic item, TNodoAVL pai) {
        this.item = item;
        this.pai = pai;
        this.esq = null;
        this.dir = null;
        this.altura = 0;
    }

}

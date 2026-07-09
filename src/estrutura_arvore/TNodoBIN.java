package estrutura_arvore;

import leitura_arquivo.*;

public class TNodoBIN {
    TNodoBIN esq;
    TitleBasic item;
    TNodoBIN dir;
    TNodoBIN pai;

    public TNodoBIN(TitleBasic item, TNodoBIN pai) {
        this.item = item;
        this.esq = null;
        this.dir = null;
        this.pai = pai;
    }
}
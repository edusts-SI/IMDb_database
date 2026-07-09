package estrutura_arvore;

import java.util.ArrayList;
import leitura_arquivo.*;

public class ArvoreAVL {

    public TNodoAVL T;

    private int comparacoes = 0;

    public ArvoreAVL() {
        T = null;
    }

    private int comparaChave(TitleBasic a, TitleBasic b) {
        int anoA = (a.getStartYear() == null) ? Integer.MAX_VALUE : a.getStartYear();
        int anoB = (b.getStartYear() == null) ? Integer.MAX_VALUE : b.getStartYear();

        if (anoA != anoB) {
            return anoA - anoB;
        }

        return a.getTconst().compareTo(b.getTconst());
    }

    // ---------------- ALTURA / FATOR DE BALANCEAMENTO ----------------
    private int altura(TNodoAVL no) {
        return (no == null) ? -1 : no.altura;
    }

    private int fatorBalanceamento(TNodoAVL no) {
        return (no == null) ? 0 : altura(no.esq) - altura(no.dir);
    }

    private void atualizaAltura(TNodoAVL no) {
        no.altura = 1 + Math.max(altura(no.esq), altura(no.dir));
    }

    // ---------------- ROTACOES ----------------
    private TNodoAVL rotacaoDireita(TNodoAVL y) {
        TNodoAVL x = y.esq;
        TNodoAVL pai = y.pai;

        y.esq = x.dir;
        if (x.dir != null) x.dir.pai = y;

        x.dir = y;
        y.pai = x;

        x.pai = pai;
        if (pai != null) {
            if (pai.dir == y) pai.dir = x;
            else pai.esq = x;
        }

        atualizaAltura(y);
        atualizaAltura(x);

        return x;
    }

    private TNodoAVL rotacaoEsquerda(TNodoAVL x) {
        TNodoAVL y = x.dir;
        TNodoAVL pai = x.pai;

        x.dir = y.esq;
        if (y.esq != null) y.esq.pai = x;

        y.esq = x;
        x.pai = y;

        y.pai = pai;
        if (pai != null) {
            if (pai.dir == x) pai.dir = y;
            else pai.esq = y;
        }

        atualizaAltura(x);
        atualizaAltura(y);

        return y;
    }


    private TNodoAVL balancear(TNodoAVL no) {
        atualizaAltura(no);
        int fb = fatorBalanceamento(no);


        if (fb > 1) {
            if (fatorBalanceamento(no.esq) < 0) {
                no.esq = rotacaoEsquerda(no.esq);
            }
            no = rotacaoDireita(no);
        }

        else if (fb < -1) {
            if (fatorBalanceamento(no.dir) > 0) {
                no.dir = rotacaoDireita(no.dir);
            }
            no = rotacaoEsquerda(no);
        }

        if (no.pai == null) T = no;

        return no;
    }

    // ---------------- INSERE ----------------
    public void insere(TitleBasic item) {
        T = insere(T, item, null);
    }

    private TNodoAVL insere(TNodoAVL T, TitleBasic item, TNodoAVL pai) {
        if (T == null) {
            T = new TNodoAVL(item, pai);
        } else {
            int cmp = comparaChave(item, T.item);
            comparacoes++;
            if (cmp < 0)
                T.esq = insere(T.esq, item, T);
            else if (cmp > 0)
                T.dir = insere(T.dir, item, T);
            else
                return T; // chave ja existe, nao insere duplicado

            T = balancear(T);
        }
        return T;
    }

    public void inserirTodos(ArrayList<TitleBasic> titles) {
        comparacoes = 0;
        for (TitleBasic t : titles) {
            insere(t);
        }
    }

    // ---------------- PESQUISA ----------------
    public TNodoAVL pesquisa(TitleBasic item) {
        return pesquisa(T, item);
    }

    private TNodoAVL pesquisa(TNodoAVL T, TitleBasic item) {
        if (T == null) {
            return null;
        } else {
            int cmp = comparaChave(item, T.item);
            comparacoes++;
            if (cmp == 0)
                return T;
            else if (cmp < 0)
                return pesquisa(T.esq, item);
            else
                return pesquisa(T.dir, item);
        }
    }

    // ---------------- REMOVE ----------------
    public void remove(TitleBasic item) {
        T = remove(T, item);
    }

    private TNodoAVL remove(TNodoAVL T, TitleBasic X) {
        if (T == null) return null;

        comparacoes++;
        int cmp = comparaChave(X, T.item);
        if (cmp < 0) {
            T.esq = remove(T.esq, X);
            if (T.esq != null) T.esq.pai = T;
        } else if (cmp > 0) {
            T.dir = remove(T.dir, X);
            if (T.dir != null) T.dir.pai = T;
        } else {
            // no encontrado
            if (T.esq == null || T.dir == null) {
                TNodoAVL filho = (T.esq != null) ? T.esq : T.dir;
                if (filho != null) filho.pai = T.pai;
                T = filho;
            } else {

                TNodoAVL pred = getMax(T.esq);
                T.item = pred.item;
                T.esq = remove(T.esq, pred.item);
                if (T.esq != null) T.esq.pai = T;
            }
        }

        if (T == null) return null;

        return balancear(T);
    }

    // ---------------- GETMAX (usado na remocao) ----------------
    private TNodoAVL getMax(TNodoAVL T) {
        while (T.dir != null) {
            T = T.dir;
        }
        return T;
    }

    // ---------------- CAMINHAMENTOS ----------------
    public void emOrdem() {
        emOrdem(T);
        System.out.println();
    }

    private void emOrdem(TNodoAVL T) {
        if (T != null) {
            emOrdem(T.esq);
            System.out.print(T.item.getPrimaryTitle() + "(" + T.item.getStartYear() + ") ");
            emOrdem(T.dir);
        }
    }

    public void preOrdem() {
        preOrdem(T);
        System.out.println();
    }

    private void preOrdem(TNodoAVL T) {
        if (T != null) {
            System.out.print(T.item.getPrimaryTitle() + "(" + T.item.getStartYear() + ") ");
            preOrdem(T.esq);
            preOrdem(T.dir);
        }
    }

    public void posOrdem() {
        posOrdem(T);
        System.out.println();
    }

    private void posOrdem(TNodoAVL T) {
        if (T != null) {
            posOrdem(T.esq);
            posOrdem(T.dir);
            System.out.print(T.item.getPrimaryTitle() + "(" + T.item.getStartYear() + ") ");
        }
    }

    // ---------------- ALTURA (util para relatorios do trabalho) ----------------
    public int altura() {
        return altura(T);
    }

    // ---------------- FATOR DE BALANCEAMENTO DA RAIZ (util para relatorios/testes) ----------------
    public int fatorBalanceamento() {
        return fatorBalanceamento(T);
    }

    public int getComparacoes() {
        return comparacoes;
    }

    public void resetComparacoes() {
        comparacoes = 0;
    }
}

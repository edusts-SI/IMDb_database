package estrutura_arvore;

import java.util.ArrayList;
import leitura_arquivo.*;

public class ArvoreBinaria {

    public TNodoBIN T;

    private int comparacoes = 0;

    public ArvoreBinaria() {
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

    // ---------------- INSERE ----------------
    public void insere(TitleBasic item) {
        T = insere(T, item, null);
    }

    private TNodoBIN insere(TNodoBIN T, TitleBasic item, TNodoBIN pai) {
        if (T == null) {
            T = new TNodoBIN(item, pai);
        } else {
            pai = T;
            comparacoes++;
            int cmp = comparaChave(item, T.item);
            if (cmp < 0)
                T.esq = insere(T.esq, item, pai);
            else if (cmp > 0)
                T.dir = insere(T.dir, item, pai);

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
    public TNodoBIN pesquisa(TitleBasic item) {
        return pesquisa(T, item);
    }

    private TNodoBIN pesquisa(TNodoBIN T, TitleBasic item) {
        if (T == null) {
            return null;
        } else {
            comparacoes++;
            int cmp = comparaChave(item, T.item);
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
        TNodoBIN alvo = pesquisa(T, item);
        if (alvo != null) {
            remove(alvo, item);
        }
    }

    private TNodoBIN remove(TNodoBIN T, TitleBasic X) {
        if (T == null) return T;
        comparacoes++;
        int cmp = comparaChave(X, T.item);
        if (cmp == 0) {
            if ((T.esq == null) && (T.dir == null)) {
                if (T.pai == null) {
                    this.T = null;
                    return T;
                } else {
                    if (comparaChave(T.item, T.pai.item) > 0)
                        T.pai.dir = null;
                    else
                        T.pai.esq = null;
                }
            } else if (T.esq == null) {
                if (T.pai != null) {
                    if (T.dir != null) T.dir.pai = T.pai;
                    if (comparaChave(T.item, T.pai.item) > 0)
                        T.pai.dir = T.dir;
                    else
                        T.pai.esq = T.dir;
                } else {
                    this.T = T.dir;
                    this.T.pai = null;
                }
            } else if (T.dir == null) {
                if (T.pai != null) {
                    if (T.esq != null) T.esq.pai = T.pai;
                    if (comparaChave(T.item, T.pai.item) > 0)
                        T.pai.dir = T.esq;
                    else
                        T.pai.esq = T.esq;
                } else {
                    this.T = T.esq;
                    this.T.pai = null;
                }
            } else {

                TNodoBIN P = getMax(T.esq);
                T.item = P.item;
            }
        } else if (cmp < 0) {
            remove(T.esq, X);
        } else {
            remove(T.dir, X);
        }
        return T;
    }

    // ---------------- GETMAX (usado na remocao) ----------------
    private TNodoBIN getMax(TNodoBIN T) {
        if (T.dir == null) {
            if (T.esq != null) T.esq.pai = T.pai;
            if (comparaChave(T.item, T.pai.item) > 0)
                T.pai.dir = T.esq;
            else
                T.pai.esq = T.esq;
            return T;
        } else {
            return getMax(T.dir);
        }
    }

    // ---------------- CAMINHAMENTOS ----------------
    public void emOrdem() {
        emOrdem(T);
        System.out.println();
    }

    private void emOrdem(TNodoBIN T) {
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

    private void preOrdem(TNodoBIN T) {
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

    private void posOrdem(TNodoBIN T) {
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

    public int getComparacoes() {
        return comparacoes;
    }

    public void resetComparacoes() {
        comparacoes = 0;
    }

    private int altura(TNodoBIN T) {
        if (T == null) return -1;
        return 1 + Math.max(altura(T.esq), altura(T.dir));
    }
}
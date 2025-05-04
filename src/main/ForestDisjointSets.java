package it.unicam.cs.asdl2425.mp2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 * 
 * @author Luca Tesei (template)
 *         Riccardo Catervi - riccardo.catervi@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    /*
     * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;

    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;

        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;

        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;
        private int size;

        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         * 
         * @param item
         *                 l'elemento conservato in questo nodo
         * 
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
            this.size = 1;
        }
    }

    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets() { this.currentElements = new HashMap<>(INITIAL_CAPACITY, LOAD_FACTOR); }

    @Override
    // Verifica se l'elemento specificato è presente nella struttura degli insiemi disgiunti.
    // Ritorna true se l'elemento è non nullo e si trova nella mappa currentElements, false altrimenti.
    public boolean isPresent(E e) { return e != null && currentElements.containsKey(e); }

    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e) {
        Objects.requireNonNull(e, "L'elemento passato è nullo.");

        if (isPresent(e))
            throw new IllegalArgumentException("L'elemento" + e + " è gia presente in un insieme disgiunto.");

        // Inserisce l'elemento nella mappa come nodo radice di un nuovo albero.
        currentElements.put(e, new Node<>(e));
    }

    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e) {
        // Trova il rappresentante (radice) dell'insieme contenente l'elemento specificato.
        // Applica la compressione del cammino per ottimizzare la struttura degli alberi.
        Objects.requireNonNull(e, "L'elemento passato è nullo.");

        // Recupera il nodo associato all'elemento.
        Node<E> node = currentElements.get(e);
        if (node == null)
            return null;

        // Trova la radice e applica la compressione del cammino.
        return findRootAndCompress(node).item;
    }

    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2) {
        Objects.requireNonNull(e1, "Il primo elemento passato è nullo.");
        Objects.requireNonNull(e2, "Il secondo elemento passato è nullo.");

        Node<E> x = currentElements.get(e1); // Recupera il nodo associato al primo elemento.
        Node<E> y = currentElements.get(e2); // Recupera il nodo associato al secondo elemento.

        if (x == null || y == null)
            throw new IllegalArgumentException("Elementi non presenti nella struttura");

        // Trova le radici dei rispettivi insiemi e le unisce.
        collega(findRootAndCompress(x), findRootAndCompress(y));
    }

    @Override
    public Set<E> getCurrentRepresentatives() {
        Set<E> representatives = new HashSet<>(); // Crea un set vuoto per i rappresentanti.
        for (Node<E> node : currentElements.values()) {
            if (node == node.parent)
                // Aggiunge al set solo i nodi che sono radici (parent che punta a se stesso).
                representatives.add(node.item);
        }
        return representatives;
    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {
        Objects.requireNonNull(e);
        Node<E> node = currentElements.get(e);
        if (node == null)
            throw new IllegalArgumentException("Elemento non presente: " + e);

        // Calcola il set di elementi dinamicamente invece di mantenerlo nel nodo
        Node<E> radice = findRootAndCompress(node);
        Set<E> elements = new HashSet<>(radice.size);

        for (Map.Entry<E, Node<E>> entry : currentElements.entrySet()) {
            if (findRootAndCompress(entry.getValue()) == radice)
                elements.add(entry.getKey());
        }

        return elements;
    }

    @Override
    public void clear() { currentElements.clear(); }

    // METODI PRIVATI

    /**
     * Trova la radice dell'albero rappresentante l'insieme disgiunto
     * contenente il nodo specificato, applicando l'euristica
     * "compressione del cammino".
     *
     * La compressione del cammino ottimizza la struttura dell'albero
     * riducendo la profondità, collegando direttamente ogni nodo visitato
     * alla radice. Questo migliora l'efficienza delle successive operazioni
     * di ricerca, riducendo i tempi medi di accesso.
     *
     * @param node Il nodo di cui trovare la radice.
     * @return La radice dell'albero contenente il nodo specificato.
     *
     * @throws NullPointerException se il nodo passato è null.
     */
    private Node<E> findRootAndCompress(Node<E> node) {
        // Se il nodo è già la radice (il suo parent punta a sé stesso),
        // restituisci direttamente il nodo.
        if (node.parent == node)
            return node;

        // Altrimenti, trova ricorsivamente la radice.
        // Collega il nodo corrente direttamente alla radice trovata
        // per ridurre la profondità dell'albero.
        node.parent = findRootAndCompress(node.parent);

        // Restituisci la radice trovata.
        return node.parent;
    }

    /**
     * Collega i due alberi rappresentati dalle radici dei nodi specificati,
     * applicando l'euristica "unione per rango".
     * - La radice con rango maggiore diventa il genitore.
     * - Se i ranghi sono uguali, una radice è scelta arbitrariamente
     *   come nuova radice e il suo rango viene incrementato.
     *
     * @param x La radice del primo albero.
     * @param y La radice del secondo albero.
     */
    private void collega(Node<E> x, Node<E> y) {
        // Se i nodi appartengono già allo stesso insieme, non c'è nulla da fare.
        if (x == y)
            return;

        // Confronta i ranghi delle due radici per determinare quale albero sarà il nuovo genitore.
        if (x.rank > y.rank) {
            // Se il rango della radice x è maggiore, y viene collegato sotto x.
            y.parent = x;
            // Aggiorna la dimensione dell'albero di x sommando la dimensione dell'albero di y.
            x.size += y.size;
        }
        else {
            // Altrimenti, x viene collegato sotto y.
            x.parent = y;
            // Aggiorna la dimensione dell'albero di y sommando la dimensione dell'albero di x.
            y.size += x.size;

            // Se i ranghi delle due radici erano uguali, incrementa il rango di y,
            // poiché ora diventa la radice di un albero più profondo.
            if (x.rank == y.rank)
                y.rank++;
        }
    }
}

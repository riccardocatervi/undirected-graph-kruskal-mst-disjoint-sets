package it.unicam.cs.asdl2425.mp2;

import java.util.*;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author Luca Tesei (template)
 *         Riccardo Catervi - riccardo.catervi@studenti.unicam.it (implementazione)
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMSP<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;

    // Lista di archi da processare ordinata per peso crescente. Questa lista
    // segue un approccio goloso.
    private List<GraphEdge<L>> edgesToProcess;

    // Mappa che associa ogni arco al proprio peso, utilizzata per ottimizzare
    // il confronto tra archi durante l'ordinamento.
    private Map<GraphEdge<L>, Double> weightCache;


    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMSP() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
        this.edgesToProcess = null;
        this.weightCache = new HashMap<>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     *         copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        validateInputGraph(g); // Verifica che il grafo soddisfi i requisiti dell'algoritmo.
        initializeDataStructures(g); // Prepara le strutture dati necessarie.
        return constructMST(); // Esegue l'algoritmo e costruisce il MST.
    }

    /**
     * Verifica che il grafo fornito sia valido per l'algoritmo di Kruskal.
     * Un grafo valido deve essere:
     * - non nullo,
     * - non orientato,
     * - pesato con pesi non negativi.
     *
     * @param g il grafo da validare
     * @throws NullPointerException se il grafo è nullo
     * @throws IllegalArgumentException se il grafo non soddisfa i requisiti
     */
    private void validateInputGraph(Graph<L> g) {
        if (g == null)
            throw new NullPointerException("Grafo nullo non ammesso");
        if (g.isDirected())
            throw new IllegalArgumentException("Il grafo deve essere non orientato");
    }

    /**
     * Inizializza le strutture dati utilizzate dall'algoritmo di Kruskal.
     *
     * @param g il grafo da processare
     * @throws IllegalArgumentException se il grafo contiene archi con pesi non validi
     */
    private void initializeDataStructures(Graph<L> g) {
        // Inizializzazione ottimizzata delle strutture dati
        int edgeCount = g.edgeCount();
        edgesToProcess = new ArrayList<>(edgeCount);  // Pre-allocazione
        weightCache.clear(); // Cancella la mappa dei pesi per evitare dati residui da precedenti calcoli.

        // Raccolta degli archi del grafo e validazione dei pesi.
        for (GraphEdge<L> edge : g.getEdges()) {
            if (!edge.hasWeight() || edge.getWeight() < 0)
                throw new IllegalArgumentException("Archi con pesi non validi");

            edgesToProcess.add(edge); // Aggiunge l'arco alla lista.
            weightCache.put(edge, edge.getWeight()); // Memorizza il peso per confronto rapido.
        }

        // Ordina gli archi per peso crescente utilizzando la mappa weightCache.
        edgesToProcess.sort((e1, e2) -> Double.compare(weightCache.get(e1), weightCache.get(e2)));

        // Inizializza gli insiemi disgiunti con tutti i nodi del grafo.
        disjointSets.clear();
        for (GraphNode<L> node : g.getNodes())
            disjointSets.makeSet(node);
    }

    /**
     * Costruisce un albero di copertura minimo (MST) utilizzando l'algoritmo di Kruskal.
     *
     * @return un insieme di archi che rappresentano l'MST
     */
    private Set<GraphEdge<L>> constructMST() {
        // Utilizziamo LinkedHashSet per preservare l'ordine di inserimento degli archi.
        Set<GraphEdge<L>> mst = new LinkedHashSet<>();

        // Itera sugli archi ordinati per peso.
        for (GraphEdge<L> edge : edgesToProcess) {
            GraphNode<L> node1 = edge.getNode1();
            GraphNode<L> node2 = edge.getNode2();

            // Trova i rappresentanti degli insiemi a cui appartengono i nodi dell'arco.
            GraphNode<L> rep1 = disjointSets.findSet(node1);
            GraphNode<L> rep2 = disjointSets.findSet(node2);

            // Se i rappresentanti sono diversi, l'arco può essere aggiunto all'MST.
            if (!rep1.equals(rep2)) {
                mst.add(edge); // Aggiunge l'arco al risultato.
                disjointSets.union(node1, node2); // Unisce i due insiemi.
            }
        }

        return mst;
    }
}

package it.unicam.cs.asdl2425.mp2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe singoletto che realizza un calcolatore delle componenti connesse di un
 * grafo non orientato utilizzando una struttura dati efficiente (fornita dalla
 * classe {@ForestDisjointSets<GraphNode<L>>}) per gestire insiemi disgiunti di
 * nodi del grafo che sono, alla fine del calcolo, le componenti connesse.
 * 
 * @author Luca Tesei (template)
 *         Riccardo Catervi - riccardo.catervi@studenti.unicam.it (implementazione)
 *
 * @param <L>
 *                il tipo delle etichette dei nodi del grafo
 */
public class UndirectedGraphConnectedComponentsComputer<L> {

    /*
     * Struttura dati per gli insiemi disgiunti.
     */
    private ForestDisjointSets<GraphNode<L>> f;

    /**
     * Crea un calcolatore di componenti connesse.
     */
    public UndirectedGraphConnectedComponentsComputer() {
        this.f = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Calcola le componenti connesse di un grafo non orientato utilizzando una
     * collezione di insiemi disgiunti.
     * 
     * @param g
     *              un grafo non orientato
     * @return un insieme di componenti connesse, ognuna rappresentata da un
     *         insieme di nodi del grafo
     * @throws NullPointerException
     *                                      se il grafo passato è nullo
     * @throws IllegalArgumentException
     *                                      se il grafo passato è orientato
     */
    public Set<Set<GraphNode<L>>> computeConnectedComponents(Graph<L> g) {
        // Controllo parametri
        if (g == null)
            throw new NullPointerException("Il grafo passato è nullo");

        if (g.isDirected())
            throw new IllegalArgumentException("Il grafo passato è orientato");

        // Se il grafo è vuoto, ritorna un insieme vuoto
        if (g.nodeCount() == 0)
            return new HashSet<>();

        // Reinizializza la struttura dati
        f = new ForestDisjointSets<>();

        // Fase 1: crea un insieme per ogni nodo
        for (GraphNode<L> node : g.getNodes())
            f.makeSet(node);

        // Fase 2: unisce i nodi collegati da archi
        for (GraphEdge<L> edge : g.getEdges()) {
            GraphNode<L> node1 = edge.getNode1();
            GraphNode<L> node2 = edge.getNode2();
            // Se i nodi appartengono a insiemi diversi, li unisce.
            if (!f.findSet(node1).equals(f.findSet(node2)))
                f.union(node1, node2);
        }

        // Fase 3: costruisce l'insieme delle componenti connesse
        Map<GraphNode<L>, Set<GraphNode<L>>> components = new HashMap<>();

        // Per ogni nodo, aggiunge il nodo all'insieme della sua componente
        for (GraphNode<L> node : g.getNodes()) {
            // Trova il rappresentante della componente
            GraphNode<L> representative = f.findSet(node);

            // Se la componente non esiste, viene creata
            if (!components.containsKey(representative))
                components.put(representative, new HashSet<>());

            // Aggiunge il nodo alla sua componente
            components.get(representative).add(node);
        }

        return new HashSet<>(components.values());
    }
}

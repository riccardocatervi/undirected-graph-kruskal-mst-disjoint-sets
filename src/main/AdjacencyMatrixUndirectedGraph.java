/**
 * 
 */
package it.unicam.cs.asdl2425.mp2;

import java.util.*;

//TODO completare gli import necessari

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * 
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * 
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * 
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 * 
 * @author Luca Tesei (template)
 *         Riccardo Catervi - riccardo.catervi@studenti.unicam.it (implementazione)
 *
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    // Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
    // matrice di adiacenza
    protected Map<GraphNode<L>, Integer> nodesIndex;

    // Matrice di adiacenza, gli elementi sono null o oggetti della classe
    // GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
    // dimensione gradualmente ad ogni inserimento di un nuovo nodo.
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;
    private Map<L, GraphNode<L>> labelToNodeMap; // Mappa inversa che associa le etichette ai nodi (se presenti).
    private ArrayList<GraphNode<L>> indexToNode; // Lista che consente di recuperare il nodo direttamente dall'indice.
    private int edgesCount; // Contatore degli archi, per migliorare la complessità di edgeCount() a O(1).
    private static final GraphEdge<?> NO_EDGE = null; // Costante per indicare l'assenza di un arco tra due nodi nella matrice.

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
        this.labelToNodeMap = new HashMap<L, GraphNode<L>>();
        this.indexToNode = new ArrayList<GraphNode<L>>();
        this.edgesCount = 0; // Inizialmente, non esistono archi.
    }

    @Override
    public int nodeCount() { return this.nodesIndex.size(); } // O(1)

    @Override
    public int edgeCount() { return edgesCount; } // O(1)

    @Override
    public void clear() {
        // Svuotiamo tutte le strutture dati del grafo, riportandolo allo stato iniziale.
        this.nodesIndex.clear();
        this.matrix.clear();
        this.labelToNodeMap.clear();
        this.indexToNode.clear();
        this.edgesCount = 0;
    }

    @Override
    public boolean isDirected() { return false; } // Poiché questa classe implementa un grafo non orientato, il metodo deve restituire sempre false.


    @Override
    public Set<GraphNode<L>> getNodes() { return this.nodesIndex.keySet(); } // Usiamo il metodo keySet() della mappa che restituisce tutte le chiavi,
                                                                             // nel nostro caso definite con il tipo GraphNode<L>. O(1).


    @Override
    public boolean addNode(GraphNode<L> node) {
        Objects.requireNonNull(node, "Il nodo passato è nullo."); // Verifichiamo che il nodo passato non sia nullo.
        if (this.containsNode(node)) // Verifichiamo se il nodo passato è già presente nella mappa.
            return false;

        int index = this.nodesIndex.size(); // Aggiorniamo la dimensione della matrice.
        this.nodesIndex.put(node, index); // Inseriamo il nodo alla mappa con il metodo put().
        this.indexToNode.add(node); // Inseriamo il nodo alla lista con add().
        this.labelToNodeMap.put(node.getLabel(), node); // Aggiorniamo la mappa inversa.

        // Espansione ottimizzata della matrice
        ArrayList<GraphEdge<L>> newRow = new ArrayList<>(index + 1);
        for (int i = 0; i <= index; i++) {
            newRow.add((GraphEdge<L>) NO_EDGE);
        }
        this.matrix.add(newRow);

        // Aggiungiamo una colonna a tutte le righe esistenti
        for (int i = 0; i < index; i++) {
            this.matrix.get(i).add((GraphEdge<L>) NO_EDGE);
        }
        return true;
    }

    @Override
    public boolean removeNode(GraphNode<L> node) {
        Objects.requireNonNull(node, "Il nodo passato è nullo.");

        // Verifichiamo se il nodo esiste nel grafo
        Integer nodeIndex = this.nodesIndex.get(node);
        if (nodeIndex == null) {
            return false;  // Il nodo non esiste nel grafo
        }

        // Rimuoviamo prima tutti gli archi collegati al nodo
        // e aggiorniamo il contatore degli archi
        ArrayList<GraphEdge<L>> rigaNodo = this.matrix.get(nodeIndex);
        for (int i = 0; i < rigaNodo.size(); i++) {
            if (rigaNodo.get(i) != NO_EDGE) {
                edgesCount--;
            }
        }

        // Rimuoviamo la riga e la colonna dalla matrice
        this.matrix.remove((int)nodeIndex);
        for (ArrayList<GraphEdge<L>> riga : this.matrix) {
            riga.remove((int)nodeIndex);
        }

        // Aggiorniamo gli indici di tutti i nodi che seguono quello rimosso
        for (Map.Entry<GraphNode<L>, Integer> entry : this.nodesIndex.entrySet()) {
            if (entry.getValue() > nodeIndex) {
                entry.setValue(entry.getValue() - 1);
            }
        }

        // Rimuoviamo il nodo dalle altre strutture dati
        this.labelToNodeMap.remove(node.getLabel());
        this.indexToNode.remove((int)nodeIndex);
        this.nodesIndex.remove(node);

        return true;
    }

    @Override
    public boolean containsNode(GraphNode<L> node) {
        Objects.requireNonNull(node, "Il nodo passato è nullo."); // Verifichiamo come al solito che il nodo passato non sia nullo.
        return this.nodesIndex.containsKey(node); // Verifica se il nodo è presente nella mappa come key e restituisce il valore booleano.
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
        Objects.requireNonNull(label, "L'etichetta passata è nulla.");
        return this.labelToNodeMap.get(label); // Utilizzando la mappa inversa, se esiste, restituiamo il nodo in O(1),
        // altrimenti restituiamo null, e dunque l'etichetta non esiste nel grafo.
    }

    @Override
    public int getNodeIndexOf(L label) {
        GraphNode<L> graphNode = this.getNodeOf(label); // Ricaviamo il nodo con getNodeOf() che verifica che label non sia null.
        if (graphNode == null) // Se il nodo non esiste, lanciamo un'eccezione
            throw new IllegalArgumentException("Nessun nodo associato a questa etichetta presente nel grafo.");

        return this.nodesIndex.get(graphNode); // Se il nodo ricavato non è nullo, ovvero se esiste un'etichetta associata
        // a quel nodo, ricaviamo l'indice nella mappa associato a quel nodo e lo restituiamo.
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int i) {
        if (i < 0 || i >= nodeCount())
            throw new IndexOutOfBoundsException("L'indice inserito è fuori dal range.");

        return this.indexToNode.get(i); // Accediamo al nodo associato all'indice i attraverso la lista indexToNode in O(1).
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        int nodeIndex = checkAndGetIndex(node);

        Set<GraphNode<L>> adjacentNodes = new HashSet<>(matrix.size()); // Creiamo l'insieme dei nodi adiacenti che poi restituiremo.
        ArrayList<GraphEdge<L>> matrixRow = this.matrix.get(nodeIndex);

        for (int i = 0; i < matrixRow.size(); i++) {
            if (matrixRow.get(i) != NO_EDGE) // Verifichiamo se c'è un arco.
                adjacentNodes.add(this.indexToNode.get(i)); // Se c'è, aggiungiamo il nodo adiacente.
        }
        return adjacentNodes; // Restituiamo l'insieme dei nodi adiacenti.
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // Raccolta di tutti gli archi presenti nella matrice, scorrendo
        // soltanto la parte sopra la diagonale per evitare duplicati
        Set<GraphEdge<L>> allEdges = new HashSet<>((edgesCount * 4) / 3 + 1); // Creiamo un nuovo set dove inseriremo tutti gli archi.

        int size = matrix.size();
        for (int i = 0; i < size; i++) {
            ArrayList<GraphEdge<L>> row = matrix.get(i);
            // Ottimizzazione: iteriamo solo sulla parte triangolare superiore
            // Iteriamo su tutti gli elementi della riga. Ogni elemento può essere:
            // - null, se non c'è un arco
            // - un oggetto GraphEdge<L>, se esiste un arco tra i due nodi.
            for (int j = i + 1; j < size; j++) {
                GraphEdge<L> edge = row.get(j);
                if (edge != NO_EDGE)
                    // Se troviamo un arco, lo aggiungiamo all'insieme degli archi.
                    // Questo passo ci permette di raccogliere tutti gli archi del nodo in modo efficace.
                    allEdges.add(edge);
            }
        }
        return allEdges;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // Inversione dell'ordine di alcune istruzioni
        // (prima recuperiamo gli indici dei nodi, poi controlliamo se è orientato).
        GraphNode<L> firstNode = edge.getNode1();
        GraphNode<L> secondNode = edge.getNode2();

        int index1 = checkAndGetIndex(firstNode);
        int index2 = checkAndGetIndex(secondNode);

        if (edge.isDirected())
            throw new IllegalArgumentException("Gli archi orientati non sono supportati in questo grafo.");

        // Controlliamo se esiste già un arco tra i due nodi nella matrice.
        // Se la posizione non è null (quindi già occupata), significa che l'arco è già presente.
        if (this.matrix.get(index1).get(index2) != NO_EDGE)
            // C'è già un arco (o un duplicato).
            return false;

        // Aggiungiamo l'arco in entrambe le direzioni (matrice simmetrica)
        this.matrix.get(index1).set(index2, edge);
        this.matrix.get(index2).set(index1, edge);

        // Incrementiamo il contatore di archi
        this.edgesCount++;

        return true;
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        Objects.requireNonNull(edge, "L'arco passato è nullo.");

        if (edge.isDirected()) {
            throw new IllegalArgumentException("Gli archi orientati non sono supportati in questo grafo.");
        }

        // Otteniamo gli indici dei nodi dell'arco
        int index1 = checkAndGetIndex(edge.getNode1());
        int index2 = checkAndGetIndex(edge.getNode2());

        // Verifichiamo se l'arco esiste
        if (this.matrix.get(index1).get(index2) == NO_EDGE) {
            return false;  // L'arco non esiste
        }

        // Rimuoviamo l'arco in entrambe le direzioni (matrice simmetrica)
        this.matrix.get(index1).set(index2, (GraphEdge<L>) NO_EDGE);
        this.matrix.get(index2).set(index1, (GraphEdge<L>) NO_EDGE);

        // Decrementiamo il contatore degli archi
        this.edgesCount--;

        return true;
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
        // Controllo che l'arco non sia nullo
        Objects.requireNonNull(edge, "L'arco passato è nullo.");

        if (edge.isDirected())
            throw new IllegalArgumentException("Gli archi orientati non sono supportati in questo grafo.");

        // Recupero gli indici dei nodi (con i dovuti controlli)
        int index1 = checkAndGetIndex(edge.getNode1());
        int index2 = checkAndGetIndex(edge.getNode2());

        // Verifica immediata sulla matrice
        return this.matrix.get(index1).get(index2) != NO_EDGE;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        int nodeIndex = checkAndGetIndex(node);

        // Creiamo un insieme vuoto che conterrà tutti gli archi associati al nodo specificato.
        // L'utilizzo di un HashSet garantisce che gli archi non vengano duplicati.
        Set<GraphEdge<L>> edges = new HashSet<GraphEdge<L>>();
        // Recuperiamo la riga corrispondente al nodo nella matrice di adiacenza.
        // Questa riga rappresenta tutti i possibili archi che partono dal nodo dato.
        ArrayList<GraphEdge<L>> row = this.matrix.get(nodeIndex);
        for (GraphEdge<L> e : row) {
            if (e != NO_EDGE) {
                edges.add(e);
            }
        }
        return edges;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    private int checkAndGetIndex(GraphNode<L> node) {
        Objects.requireNonNull(node, "Il nodo passato è nullo.");

        // Recuperiamo l'indice associato al nodo dalla mappa nodesIndex.
        // Se il nodo non è presente, la mappa restituirà null.
        Integer index = this.nodesIndex.get(node);
        // Se l'indice è null, significa che il nodo non è presente nel grafo.
        // In questo caso, viene lanciata un'eccezione per segnalare il problema all'utente.
        if (index == null) {
            throw new IllegalArgumentException(
                    "Il nodo " + node + " non esiste nel grafo.");
        }
        return index;
    }

}

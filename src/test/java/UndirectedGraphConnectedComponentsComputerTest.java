package it.unicam.cs.asdl2425.mp2;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per la classe UndirectedGraphConnectedComponentsComputer.
 * 
 * @author Luca Tesei
 *
 */
class UndirectedGraphConnectedComponentsComputerTest {

    @Test
    final void testComputeConnectedComponentsExceptionsAndTrivialCases() {
        UndirectedGraphConnectedComponentsComputer<String> c = new UndirectedGraphConnectedComponentsComputer<String>();
        assertThrows(NullPointerException.class,
                () -> c.computeConnectedComponents(null));
        Graph<String> g = new AdjacencyMatrixUndirectedGraph<String>();
        Set<Set<GraphNode<String>>> result = new HashSet<>();
        assertTrue(c.computeConnectedComponents(g).equals(result));
        g.addNode(new GraphNode<String>("a"));
        Set<GraphNode<String>> comp1 = new HashSet<>();
        comp1.add(new GraphNode<String>("a"));
        result.add(comp1);
        assertTrue(c.computeConnectedComponents(g).equals(result));
        g.addNode(new GraphNode<String>("b"));
        Set<GraphNode<String>> comp2 = new HashSet<>();
        comp2.add(new GraphNode<String>("b"));
        result.add(comp2);
        assertTrue(c.computeConnectedComponents(g).equals(result));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("a"),
                new GraphNode<String>("b"), false));
        result.remove(comp2);
        comp1.add(new GraphNode<String>("b"));
        assertTrue(c.computeConnectedComponents(g).equals(result));
    }

    @Test
    final void testComputeConnectedComponents() {
        UndirectedGraphConnectedComponentsComputer<String> c = new UndirectedGraphConnectedComponentsComputer<String>();
        Graph<String> g = new AdjacencyMatrixUndirectedGraph<String>();
        g.addNode(new GraphNode<String>("a"));
        g.addNode(new GraphNode<String>("b"));
        g.addNode(new GraphNode<String>("c"));
        g.addNode(new GraphNode<String>("d"));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("a"),
                new GraphNode<String>("b"), false));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("d"),
                new GraphNode<String>("b"), false));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("c"),
                new GraphNode<String>("b"), false));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("c"),
                new GraphNode<String>("d"), false));
        Set<Set<GraphNode<String>>> result = new HashSet<>();
        Set<GraphNode<String>> comp1 = new HashSet<>();
        comp1.add(new GraphNode<String>("a"));
        comp1.add(new GraphNode<String>("b"));
        comp1.add(new GraphNode<String>("c"));
        comp1.add(new GraphNode<String>("d"));
        result.add(comp1);
        assertTrue(c.computeConnectedComponents(g).equals(result));

        g.addNode(new GraphNode<String>("e"));
        g.addNode(new GraphNode<String>("f"));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("f"),
                new GraphNode<String>("e"), false));
        Set<GraphNode<String>> comp2 = new HashSet<>();
        comp2.add(new GraphNode<String>("e"));
        comp2.add(new GraphNode<String>("f"));
        result.add(comp2);
        assertTrue(c.computeConnectedComponents(g).equals(result));

        g.addNode(new GraphNode<String>("g"));
        Set<GraphNode<String>> comp3 = new HashSet<>();
        comp3.add(new GraphNode<String>("g"));
        result.add(comp3);
        assertTrue(c.computeConnectedComponents(g).equals(result));

        g.addEdge(new GraphEdge<String>(new GraphNode<String>("g"),
                new GraphNode<String>("c"), false));
        result.remove(comp3);
        comp1.add(new GraphNode<String>("g"));
        assertTrue(c.computeConnectedComponents(g).equals(result));

        g.addEdge(new GraphEdge<String>(new GraphNode<String>("g"),
                new GraphNode<String>("g"), false));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("g"),
                new GraphNode<String>("f"), false));
        result.remove(comp2);
        comp1.add(new GraphNode<String>("f"));
        comp1.add(new GraphNode<String>("e"));
        assertTrue(c.computeConnectedComponents(g).equals(result));

        Graph<String> g1 = new AdjacencyMatrixUndirectedGraph<String>();
        result.clear();
        ;
        g1.addNode(new GraphNode<String>("a"));
        g1.addNode(new GraphNode<String>("b"));
        g1.addEdge(new GraphEdge<String>(new GraphNode<String>("a"),
                new GraphNode<String>("b"), false));
        comp1.clear();
        ;
        comp1.add(new GraphNode<String>("a"));
        comp1.add(new GraphNode<String>("b"));
        result.add(comp1);
        assertTrue(c.computeConnectedComponents(g1).equals(result));
    }
    
    @Test
    final void testGraphWithCycle() {
        UndirectedGraphConnectedComponentsComputer<String> c = new UndirectedGraphConnectedComponentsComputer<>();
        Graph<String> g = new AdjacencyMatrixUndirectedGraph<>();
        g.addNode(new GraphNode<>("a"));
        g.addNode(new GraphNode<>("b"));
        g.addNode(new GraphNode<>("c"));
        g.addEdge(new GraphEdge<>(new GraphNode<>("a"), new GraphNode<>("b"), false));
        g.addEdge(new GraphEdge<>(new GraphNode<>("b"), new GraphNode<>("c"), false));
        g.addEdge(new GraphEdge<>(new GraphNode<>("c"), new GraphNode<>("a"), false));
        Set<Set<GraphNode<String>>> result = new HashSet<>();
        Set<GraphNode<String>> comp = new HashSet<>();
        comp.add(new GraphNode<>("a"));
        comp.add(new GraphNode<>("b"));
        comp.add(new GraphNode<>("c"));
        result.add(comp);
        assertEquals(result, c.computeConnectedComponents(g), "Le componenti connesse del grafo ciclico non sono corrette.");
    }
    @Test
    final void testIsolatedNode() {
        UndirectedGraphConnectedComponentsComputer<String> c = new UndirectedGraphConnectedComponentsComputer<>();
        Graph<String> g = new AdjacencyMatrixUndirectedGraph<>();
        g.addNode(new GraphNode<>("a"));
        Set<Set<GraphNode<String>>> result = new HashSet<>();
        Set<GraphNode<String>> comp = new HashSet<>();
        comp.add(new GraphNode<>("a"));
        result.add(comp);
        assertEquals(result, c.computeConnectedComponents(g), "Il nodo isolato dovrebbe essere una componente connessa singola.");
    }
    
    @Test
    final void testDenseGraph() {
        // Crea un calcolatore per componenti connesse
        UndirectedGraphConnectedComponentsComputer<String> c = new UndirectedGraphConnectedComponentsComputer<>();
        Graph<String> g = new AdjacencyMatrixUndirectedGraph<>();

        // Aggiungi nodi
        g.addNode(new GraphNode<>("a"));
        g.addNode(new GraphNode<>("b"));
        g.addNode(new GraphNode<>("c"));
        g.addNode(new GraphNode<>("d"));

        // Aggiungi archi: grafo completamente connesso
        g.addEdge(new GraphEdge<>(new GraphNode<>("a"), new GraphNode<>("b"), false));
        g.addEdge(new GraphEdge<>(new GraphNode<>("a"), new GraphNode<>("c"), false));
        g.addEdge(new GraphEdge<>(new GraphNode<>("a"), new GraphNode<>("d"), false));
        g.addEdge(new GraphEdge<>(new GraphNode<>("b"), new GraphNode<>("c"), false));
        g.addEdge(new GraphEdge<>(new GraphNode<>("b"), new GraphNode<>("d"), false));
        g.addEdge(new GraphEdge<>(new GraphNode<>("c"), new GraphNode<>("d"), false));

        // Risultato atteso: tutti i nodi appartengono alla stessa componente
        Set<Set<GraphNode<String>>> result = new HashSet<>();
        Set<GraphNode<String>> comp = new HashSet<>();
        comp.add(new GraphNode<>("a"));
        comp.add(new GraphNode<>("b"));
        comp.add(new GraphNode<>("c"));
        comp.add(new GraphNode<>("d"));
        result.add(comp);

        // Verifica che tutti i nodi siano identificati nella stessa componente
        assertTrue(c.computeConnectedComponents(g).equals(result),
                "Un grafo completamente connesso dovrebbe essere una singola componente.");
    }
}

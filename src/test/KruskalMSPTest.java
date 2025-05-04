package it.unicam.cs.asdl2425.mp2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Classe di test per i metodi della classe KruskalMSP.
 * 
 * @author Luca Tesei
 *
 */
class KruskalMSPTest {

    @Test
    final void testComputeMSP1() {
        Graph<String> gr = new AdjacencyMatrixUndirectedGraph<String>();
        GraphNode<String> a = new GraphNode<String>("a");
        gr.addNode(a);
        GraphNode<String> b = new GraphNode<String>("b");
        gr.addNode(b);
        GraphNode<String> c = new GraphNode<String>("c");
        gr.addNode(c);
        GraphNode<String> d = new GraphNode<String>("d");
        gr.addNode(d);
        GraphNode<String> e = new GraphNode<String>("e");
        gr.addNode(e);
        GraphNode<String> f = new GraphNode<String>("f");
        gr.addNode(f);
        GraphNode<String> g = new GraphNode<String>("g");
        gr.addNode(g);
        GraphNode<String> h = new GraphNode<String>("h");
        gr.addNode(h);
        GraphNode<String> i = new GraphNode<String>("i");
        gr.addNode(i);
        gr.addEdge(new GraphEdge<String>(a, b, false, 4));
        gr.addEdge(new GraphEdge<String>(a, h, false, 8.5));
        gr.addEdge(new GraphEdge<String>(b, h, false, 11));
        gr.addEdge(new GraphEdge<String>(b, c, false, 8));
        gr.addEdge(new GraphEdge<String>(c, i, false, 2));
        gr.addEdge(new GraphEdge<String>(c, d, false, 7));
        gr.addEdge(new GraphEdge<String>(c, f, false, 4));
        gr.addEdge(new GraphEdge<String>(d, f, false, 14));
        gr.addEdge(new GraphEdge<String>(d, e, false, 9));
        gr.addEdge(new GraphEdge<String>(e, f, false, 10));
        gr.addEdge(new GraphEdge<String>(f, g, false, 2));
        gr.addEdge(new GraphEdge<String>(g, i, false, 6));
        gr.addEdge(new GraphEdge<String>(g, h, false, 1));
        gr.addEdge(new GraphEdge<String>(h, i, false, 7));
        KruskalMSP<String> alg = new KruskalMSP<String>();
        Set<GraphEdge<String>> result = new HashSet<GraphEdge<String>>();
        result.add(new GraphEdge<String>(a, b, false, 4));
        result.add(new GraphEdge<String>(b, c, false, 8));
        result.add(new GraphEdge<String>(c, i, false, 2));
        result.add(new GraphEdge<String>(c, d, false, 7));
        result.add(new GraphEdge<String>(c, f, false, 4));
        result.add(new GraphEdge<String>(d, e, false, 9));
        result.add(new GraphEdge<String>(f, g, false, 2));
        result.add(new GraphEdge<String>(g, h, false, 1));
        assertTrue(alg.computeMSP(gr).equals(result));
    }

    @Test
    final void testComputeMSP2() {
        Graph<String> gr = new AdjacencyMatrixUndirectedGraph<String>();
        GraphNode<String> a = new GraphNode<String>("a");
        gr.addNode(a);
        GraphNode<String> b = new GraphNode<String>("b");
        gr.addNode(b);
        GraphNode<String> c = new GraphNode<String>("c");
        gr.addNode(c);
        GraphNode<String> d = new GraphNode<String>("d");
        gr.addNode(d);
        GraphNode<String> e = new GraphNode<String>("e");
        gr.addNode(e);
        GraphNode<String> f = new GraphNode<String>("f");
        gr.addNode(f);
        GraphNode<String> g = new GraphNode<String>("g");
        gr.addNode(g);
        gr.addEdge(new GraphEdge<String>(a, b, false, 28));
        gr.addEdge(new GraphEdge<String>(b, c, false, 16));
        gr.addEdge(new GraphEdge<String>(c, d, false, 12));
        gr.addEdge(new GraphEdge<String>(d, e, false, 22));
        gr.addEdge(new GraphEdge<String>(e, f, false, 25));
        gr.addEdge(new GraphEdge<String>(f, a, false, 10));
        gr.addEdge(new GraphEdge<String>(g, b, false, 14));
        gr.addEdge(new GraphEdge<String>(g, d, false, 18));
        gr.addEdge(new GraphEdge<String>(g, e, false, 24));
        KruskalMSP<String> alg = new KruskalMSP<String>();
        Set<GraphEdge<String>> result = new HashSet<GraphEdge<String>>();
        result.add(new GraphEdge<String>(a, f, false, 10));
        result.add(new GraphEdge<String>(f, e, false, 25));
        result.add(new GraphEdge<String>(e, d, false, 22));
        result.add(new GraphEdge<String>(c, d, false, 12));
        result.add(new GraphEdge<String>(c, b, false, 16));
        result.add(new GraphEdge<String>(g, b, false, 14));
        assertTrue(alg.computeMSP(gr).equals(result));
    }

    @Test
    final void testComputeMSP3() {
        Graph<String> gr = new AdjacencyMatrixUndirectedGraph<String>();
        GraphNode<String> a = new GraphNode<String>("a");
        gr.addNode(a);
        GraphNode<String> b = new GraphNode<String>("b");
        gr.addNode(b);
        GraphNode<String> c = new GraphNode<String>("c");
        gr.addNode(c);
        GraphNode<String> d = new GraphNode<String>("d");
        gr.addNode(d);
        GraphNode<String> e = new GraphNode<String>("e");
        gr.addNode(e);
        gr.addEdge(new GraphEdge<String>(a, b, false, 1));
        gr.addEdge(new GraphEdge<String>(a, c, false, 7));
        gr.addEdge(new GraphEdge<String>(a, d, false, 10));
        gr.addEdge(new GraphEdge<String>(a, e, false, 5));
        gr.addEdge(new GraphEdge<String>(b, c, false, 3));
        gr.addEdge(new GraphEdge<String>(c, d, false, 4));
        gr.addEdge(new GraphEdge<String>(d, e, false, 2));
        KruskalMSP<String> alg = new KruskalMSP<String>();
        Set<GraphEdge<String>> result = new HashSet<GraphEdge<String>>();
        result.add(new GraphEdge<String>(a, b, false, 1));
        result.add(new GraphEdge<String>(d, e, false, 2));
        result.add(new GraphEdge<String>(b, c, false, 3));
        result.add(new GraphEdge<String>(c, d, false, 4));
        assertTrue(alg.computeMSP(gr).equals(result));
    }

    @Test
    final void testComputeMSP4() {
        Graph<String> gr = new AdjacencyMatrixUndirectedGraph<String>();
        KruskalMSP<String> alg = new KruskalMSP<String>();
        Set<GraphEdge<String>> result = new HashSet<GraphEdge<String>>();
        assertTrue(alg.computeMSP(gr).equals(result));
        GraphNode<String> a = new GraphNode<String>("a");
        gr.addNode(a);
        assertTrue(alg.computeMSP(gr).equals(result));
        GraphNode<String> b = new GraphNode<String>("b");
        gr.addNode(b);
        assertTrue(alg.computeMSP(gr).equals(result));
        gr.addEdge(new GraphEdge<String>(a, b, false, 1));
        result.add(new GraphEdge<String>(a, b, false, 1));
        assertTrue(alg.computeMSP(gr).equals(result));
        GraphNode<String> c = new GraphNode<String>("c");
        gr.addNode(c);
        GraphNode<String> d = new GraphNode<String>("d");
        gr.addNode(d);
        GraphNode<String> e = new GraphNode<String>("e");
        gr.addNode(e);
        assertTrue(alg.computeMSP(gr).equals(result));
    }

    @Test
    final void exceptionsTest() {
        Graph<String> gr = new AdjacencyMatrixUndirectedGraph<String>();
        GraphNode<String> a = new GraphNode<String>("a");
        gr.addNode(a);
        GraphNode<String> b = new GraphNode<String>("b");
        gr.addNode(b);
        GraphNode<String> c = new GraphNode<String>("c");
        gr.addNode(c);
        GraphNode<String> d = new GraphNode<String>("d");
        gr.addNode(d);
        GraphNode<String> e = new GraphNode<String>("e");
        gr.addNode(e);
        GraphNode<String> f = new GraphNode<String>("f");
        gr.addNode(f);
        GraphNode<String> g = new GraphNode<String>("g");
        gr.addNode(g);
        GraphNode<String> h = new GraphNode<String>("h");
        gr.addNode(h);
        GraphNode<String> i = new GraphNode<String>("i");
        gr.addNode(i);
        gr.addEdge(new GraphEdge<String>(a, b, false, 4));
        gr.addEdge(new GraphEdge<String>(a, h, false, 8.5));
        gr.addEdge(new GraphEdge<String>(b, h, false, 11));
        gr.addEdge(new GraphEdge<String>(b, c, false, 8));
        gr.addEdge(new GraphEdge<String>(c, i, false, 2));
        gr.addEdge(new GraphEdge<String>(c, d, false, 7));
        gr.addEdge(new GraphEdge<String>(c, f, false, 4));
        gr.addEdge(new GraphEdge<String>(d, f, false, 14));
        gr.addEdge(new GraphEdge<String>(d, e, false, 9));
        gr.addEdge(new GraphEdge<String>(e, f, false, 10));
        gr.addEdge(new GraphEdge<String>(f, g, false, 2));
        gr.addEdge(new GraphEdge<String>(g, i, false, 6));
        gr.addEdge(new GraphEdge<String>(g, h, false, 1));
        gr.addEdge(new GraphEdge<String>(h, i, false, 7));
        KruskalMSP<String> alg = new KruskalMSP<String>();
        assertThrows(NullPointerException.class, () -> {
            alg.computeMSP(null);
        });
        Graph<String> x = new AdjacencyMatrixUndirectedGraph<>();
        GraphNode<String> A = new GraphNode<>("A");
        x.addNode(A);
        GraphNode<String> B = new GraphNode<>("B");
        x.addNode(B);
        GraphNode<String> C = new GraphNode<>("C");
        x.addNode(C);
        x.addEdge(new GraphEdge<>(A, B, false, 0));
        x.addEdge(new GraphEdge<>(B, C, false, -2));
        // Archi non pesati o negativi
        assertThrows(IllegalArgumentException.class, () -> {
            alg.computeMSP(x);
        });
    }

    @Test
    final void testFullyConnectedGraph() {
        // Crea un grafo completamente connesso
        Graph<String> gr = new AdjacencyMatrixUndirectedGraph<>();
        GraphNode<String> a = new GraphNode<>("a");
        gr.addNode(a);
        GraphNode<String> b = new GraphNode<>("b");
        gr.addNode(b);
        GraphNode<String> c = new GraphNode<>("c");
        gr.addNode(c);

        // Aggiunta di archi tra tutti i nodi
        gr.addEdge(new GraphEdge<>(a, b, false, 1));
        gr.addEdge(new GraphEdge<>(a, c, false, 5));
        gr.addEdge(new GraphEdge<>(b, c, false, 2));

        // Algoritmo di Kruskal
        KruskalMSP<String> alg = new KruskalMSP<>();

        // Insieme atteso di archi
        Set<GraphEdge<String>> result = new HashSet<>();
        result.add(new GraphEdge<>(a, b, false, 1));
        result.add(new GraphEdge<>(b, c, false, 2));

        // Verifica
        assertEquals(result, alg.computeMSP(gr),
                "Il risultato non corrisponde all'MST del grafo completamente connesso.");
    }

    @Test
    final void testSparseGraph() {
        // Crea un grafo con pochi archi
        Graph<String> gr = new AdjacencyMatrixUndirectedGraph<>();
        GraphNode<String> a = new GraphNode<>("a");
        gr.addNode(a);
        GraphNode<String> b = new GraphNode<>("b");
        gr.addNode(b);
        GraphNode<String> c = new GraphNode<>("c");
        gr.addNode(c);
        GraphNode<String> d = new GraphNode<>("d");
        gr.addNode(d);

        // Aggiunta di pochi archi
        gr.addEdge(new GraphEdge<>(a, b, false, 3));
        gr.addEdge(new GraphEdge<>(b, c, false, 1));
        gr.addEdge(new GraphEdge<>(c, d, false, 4));

        // Algoritmo di Kruskal
        KruskalMSP<String> alg = new KruskalMSP<>();

        // Insieme atteso di archi
        Set<GraphEdge<String>> result = new HashSet<>();
        result.add(new GraphEdge<>(a, b, false, 3));
        result.add(new GraphEdge<>(b, c, false, 1));
        result.add(new GraphEdge<>(c, d, false, 4));

        // Verifica
        assertEquals(result, alg.computeMSP(gr),
                "Il risultato non corrisponde all'MST del grafo sparso.");
    }

    @Test
    final void testDisconnectedGraph() {
        // Crea un grafo disconnesso
        Graph<String> gr = new AdjacencyMatrixUndirectedGraph<>();
        GraphNode<String> a = new GraphNode<>("a");
        gr.addNode(a);
        GraphNode<String> b = new GraphNode<>("b");
        gr.addNode(b);
        GraphNode<String> c = new GraphNode<>("c");
        gr.addNode(c);

        // Aggiunta di archi solo tra alcuni nodi
        gr.addEdge(new GraphEdge<>(a, b, false, 3));

        // Algoritmo di Kruskal
        KruskalMSP<String> alg = new KruskalMSP<>();

        // Insieme atteso di archi
        Set<GraphEdge<String>> result = new HashSet<>();
        result.add(new GraphEdge<>(a, b, false, 3));

        // Verifica che l'MST contenga solo l'arco connesso
        assertEquals(result, alg.computeMSP(gr),
                "Il risultato non corrisponde all'MST del grafo disconnesso.");
    }
}

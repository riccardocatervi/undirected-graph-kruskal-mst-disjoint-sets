package it.unicam.cs.asdl2425.mp2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test per la classe ForestDisjointSets.
 * Questa classe verifica la correttezza delle funzionalità di ForestDisjointSets,
 * tra cui makeSet, union, findSet, e gestione delle eccezioni.
 * Include test per scenari normali, edge case, e performance.
 * 
 * 
 * @author Luca Tesei
 *
 */
class ForestDisjointSetsTest {

    /**
     * Verifica che il costruttore inizializzi una struttura vuota.
     */
    @Test
    final void testForestDisjointSets() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<>();
        // Controlla che non ci siano rappresentanti nella struttura vuota
        assertTrue(ds.getCurrentRepresentatives().isEmpty());
    }

    /**
     * Verifica il comportamento di isPresent per elementi presenti e non.
     */
    @Test
    final void testIsPresent() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<>();
        // Nessun elemento è presente all'inizio
        assertFalse(ds.isPresent(1));
        ds.makeSet(1);
        // Dopo makeSet, l'elemento è presente
        assertTrue(ds.isPresent(1));
        ds.makeSet(2);
        assertTrue(ds.isPresent(2));
        // Elementi non aggiunti non sono presenti
        assertFalse(ds.isPresent(3));
    }

    /**
     * NUOVO TEST: verifica isPresent dopo unioni multiple.
     */
    @Test
    final void testIsPresentAfterUnion() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<>();
        ds.makeSet(1);
        ds.makeSet(2);
        ds.union(1, 2); // Unisce 1 e 2 in un insieme
        assertTrue(ds.isPresent(1));
        assertTrue(ds.isPresent(2));
        assertFalse(ds.isPresent(3));
        ds.makeSet(3);
        assertTrue(ds.isPresent(3));
        ds.union(2, 3); // Unisce 1, 2 e 3
        assertTrue(ds.isPresent(1));
        assertTrue(ds.isPresent(2));
        assertTrue(ds.isPresent(3));
    }

    /**
     * Verifica che makeSet lanci eccezioni quando si tenta di aggiungere elementi null o duplicati.
     */
    @Test
    final void testMakeSetExceptions() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<>();
        assertThrows(NullPointerException.class, () -> ds.makeSet(null)); // Null non ammesso
        ds.makeSet(1);
        assertThrows(IllegalArgumentException.class, () -> ds.makeSet(1)); // Duplicato
    }

    /**
     * Verifica che makeSet gestisca tipi complessi correttamente.
     */
    @Test
    final void testMakeSetWithCustomObjects() {
        class Custom {
            String value;

            Custom(String value) {
                this.value = value;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Custom custom = (Custom) o;
                return value.equals(custom.value);
            }

            @Override
            public int hashCode() {
                return value.hashCode();
            }
        }

        ForestDisjointSets<Custom> ds = new ForestDisjointSets<>();
        Custom obj1 = new Custom("A");
        Custom obj2 = new Custom("B");
        Custom obj3 = new Custom("A");

        ds.makeSet(obj1);
        ds.makeSet(obj2);

        assertTrue(ds.isPresent(obj1));
        assertTrue(ds.isPresent(obj2));
        assertTrue(ds.isPresent(obj3)); // Deve essere true poiché obj1.equals(obj3)
    }
    
    /**
     * Stress test con molti elementi.
     */
    @Test
    final void testStressUnionFind() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<>();
        int numElements = 100000;

        // Creazione di un gran numero di insiemi singoli
        for (int i = 1; i <= numElements; i++) {
            ds.makeSet(i);
        }
        assertEquals(numElements, ds.getCurrentRepresentatives().size());

        // Unione di tutti gli elementi in un unico insieme
        for (int i = 2; i <= numElements; i++) {
            ds.union(1, i);
        }

        // Verifica che tutti abbiano lo stesso rappresentante
        for (int i = 1; i <= numElements; i++) {
            assertEquals(ds.findSet(1), ds.findSet(i));
        }

        assertEquals(1, ds.getCurrentRepresentatives().size());
    }

    /**
     * verifica la "compressione del cammino" nei findSet.
     */
    @Test
    final void testPathCompression() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<>();
        ds.makeSet(1); // Crea {1}
        ds.makeSet(2); // Crea {2}
        ds.makeSet(3); // Crea {3}

        // Unione di 1 e 2: 2 diventa il rappresentante perché è passato come secondo argomento
        ds.union(1, 2);

        // Unione di 2 e 3: 2 rimane rappresentante in quanto il rango di 2 è maggiore di 3
        ds.union(2, 3);

        // Prima chiamata a findSet: compressione del cammino
        assertEquals(2, ds.findSet(1), "Il rappresentante di 1 dovrebbe essere 2 dopo l'unione");
        assertEquals(2, ds.findSet(3), "Il rappresentante di 3 dovrebbe essere 2 dopo l'unione");

        // Verifica che il parent di 1 e 3 puntino direttamente al rappresentante (compressione del cammino)
        ForestDisjointSets.Node<Integer> node1 = ds.currentElements.get(1);
        ForestDisjointSets.Node<Integer> node3 = ds.currentElements.get(3);
        ForestDisjointSets.Node<Integer> node2 = ds.currentElements.get(2);

        assertEquals(node2, node1.parent, "Il parent di 1 dovrebbe puntare a 2 dopo la compressione del cammino");
        assertEquals(node2, node3.parent, "Il parent di 3 dovrebbe puntare a 2 dopo la compressione del cammino");
    }

    /**
     * verifica l'unione per rango.
     */
    @Test
    final void testUnionByRank() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<>();
        ds.makeSet(1);
        ds.makeSet(2);
        ds.makeSet(3);
        ds.union(1, 2); // Unione di due insiemi
        assertEquals(1, ds.currentElements.get(2).rank); // Rank aggiornato
        ds.union(2, 3); // Unione con rango maggiore
        assertEquals(1, ds.currentElements.get(2).rank);
        assertEquals(0, ds.currentElements.get(3).rank);
    }

    /**
     * verifica che clear ripulisca correttamente la struttura.
     */
    @Test
    final void testClear() {
        ForestDisjointSets<Integer> ds = new ForestDisjointSets<>();
        ds.makeSet(1);
        ds.makeSet(2);
        ds.union(1, 2);
        ds.clear();
        assertTrue(ds.getCurrentRepresentatives().isEmpty());
        assertTrue(ds.currentElements.isEmpty());
    }
}
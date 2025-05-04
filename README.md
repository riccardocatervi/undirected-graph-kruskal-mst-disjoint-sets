# Undirected Graph & Kruskal MST - Java Implementation

A self‑contained, pure‑Java 8 implementation of:
- **Adjacency‑matrix–based undirected graphs**
- **Forest Disjoint Sets (Union–Find)** with *union‑by‑rank* & *path‑compression*
- **Kruskal’s Minimum Spanning Tree (MST)** algorithm
- **Connected‑components** detection on undirected graphs
The project was produced for the *Algorithms and Data Structures laboratory*(A.S.D.L. 2, 2024/25) at the University of Camerino. All code purposely avoids Java 9+ features so that it can be compiled on legacy tool‑chains.

## Table of Contents
- [Project Structure](#project-structure)
- [Quick Start](#quick-start)
- [Key Classes](#key-classes)
- [Usage Examples](#usage-examples)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Project Structure
<pre><code>
├── src
│   ├── main
│   │   └── java
│   │       ├── AdjacencyMatrixUndirectedGraph.java
│   │       ├── DisjointSets.java          # Interface
│   │       ├── ForestDisjointSets.java    # Implementation using trees
│   │       ├── Graph.java                 # Abstract superclass
│   │       ├── GraphEdge.java             # Weighted/Un‑weighted edges
│   │       ├── GraphNode.java             # Generic labelled node
│   │       ├── KruskalMSP.java            # MST (or minimum spanning forest)
│   │       └── UndirectedGraphConnectedComponentsComputer.java
│   └── test
│       └── java
│           ├── AdjacencyMatrixUndirectedGraphTest.java
│           ├── ForestDisjointSetsTest.java
│           ├── KruskalMSPTest.java
│           └── UndirectedGraphConnectedComponentsComputerTest.java
└── lib
    └── junit-platform-console-standalone-1.12.0.jar
</code></pre>

## Quick Start
Make sure you have Java 8 installed.
```bash
# Clone the repository
git clone https://github.com/riccardocatervi/undirected-graph-kruskal-mst-disjoint-sets.git
cd undirected-graph-kruskal-mst-disjoint-sets

# Compile source & tests (plain javac)
mkdir -p out
javac -d out $(find src -name "*.java")

# Run JUnit 5 Test
java -jar lib/junit-platform-console-standalone.jar \
  --class-path out \
  --scan-class-path
```

**Tip** Import the project as a *Plain Java* module in IntelliJ IDEA / Eclipse.  The directory layout follows the standard Maven convention (`src/main/java`, `src/test/java`) so your IDE will recognise the sources automatically.

## Key Classes
The following table summarizes the main classes in the project, along with their responsibilities and notable features:

| Class                                               | Responsibility                          | Highlights                                                                                                                                     |
|-----------------------------------------------------|------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| `Graph<L>`                                          | Abstract skeleton for arbitrary graphs   | Defines the common API: `addNode`, `addEdge`, `containsNode`, `edgeCount`, `isDirected`, etc.                                                  |
| `AdjacencyMatrixUndirectedGraph<L>`                | Concrete **undirected** graph implementation | Backed by an `ArrayList<ArrayList<GraphEdge<L>>>`; auto-resizing square matrix; `O(1)` node/edge counts; no removals for predictable costs.   |
| `GraphNode<L>`<br/>`GraphEdge<L>`                  | Value objects                            | Equality is defined on node labels and the `(u, v, weight)` triple; immutable and hashable for use in sets and maps.                           |
| `DisjointSets<E>`                                   | Interface for disjoint-set operations    | Defines basic operations: `makeSet`, `findSet`, and `union`.                                                                                   |
| `ForestDisjointSets<E>`                             | Tree-based disjoint-set implementation   | Implements both **union-by-rank** and **path-compression**, achieving amortized `α(n)` time complexity.                                        |
| `UndirectedGraphConnectedComponentsComputer<L>`     | Computes connected components            | Performs a single pass over the edge set using `ForestDisjointSets`; efficiently identifies disjoint components.                               |
| `KruskalMSP<L>`                                     | Kruskal’s algorithm                      | Sorts edges by weight (`O(E log E)`); includes edges iff endpoints are in different sets; returns MST as a `Set<GraphEdge<L>>`.               |

All public methods are fully documented with **Javadoc**, including both Italian and English inline comments.

## Usage examples
Below is a self‑contained snippet that builds a graph, prints the MST weight and lists its edges.
```java
import it.unicam.cs.asdl2425.mp2.*;
import java.util.*;

public class Demo {
    public static void main(String[] args) {
        AdjacencyMatrixUndirectedGraph<String> g = new AdjacencyMatrixUndirectedGraph<>();
        GraphNode<String> a = new GraphNode<>("A");
        GraphNode<String> b = new GraphNode<>("B");
        GraphNode<String> c = new GraphNode<>("C");
        g.addNode(a); g.addNode(b); g.addNode(c);
        g.addEdge(new GraphEdge<>(a, b, false, 4.0));
        g.addEdge(new GraphEdge<>(b, c, false, 2.0));
        g.addEdge(new GraphEdge<>(a, c, false, 5.0));

        Set<GraphEdge<String>> mst = KruskalMSP.kruskalMst(g);
        double weight = mst.stream().mapToDouble(GraphEdge::getWeight).sum();
        System.out.println("MST weight = " + weight);
        mst.forEach(System.out::println);
    }
}
```
Expected output:
```zsh
MST weight = 6.0
(A,B,4.0)
(B,C,2.0)
```
## Algorithmic complexity
The table below outlines the theoretical time complexity of key operations in this project:

| Operation                           | Complexity                         |
|-------------------------------------|-------------------------------------|
| `addNode`                           | *O(1)*                            |
| `addEdge` (matrix update)           | *O(1)*                            |
| `findSet` in `ForestDisjointSets`   | *O(α(n))* amortised               |
| `kruskalMst`                        | *O(E log E)* (sorting dominates)  |

Kruskal’s correctness follows directly from the **cut property**;  
implementation details adhere to the canonical approach described in CLRS §23.2 ([algs4.cs.princeton.edu](https://algs4.cs.princeton.edu)).

## Testing
A comprehensive **JUnit 5** suite guards all public behaviours (matrix invariants, disjoint‑set properties, MST soundness, etc.). Run `mvn test` (after adding a pom) or execute the `*Test` classes directly as shown above.

## Contributing
Pull requests are welcome! Please open an issue first to discuss major changes.

## License
This project is distributed under the terms of the [MIT Licence](LICENSE).

The overall structure and project template were originally designed by **Professor Luca Tesei** from the **University of Camerino (UNICAM)**, as part of the course activities in Algorithms and Data Structures.

Special thanks to Professor Tesei for his excellent lectures and guidance, which laid the groundwork for the design and implementation of this project.

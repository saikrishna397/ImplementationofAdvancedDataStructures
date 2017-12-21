package cs6301.g1025;

import cs6301.g1025.Graph.Edge;
import cs6301.g1025.Graph.Vertex;
import cs6301.g1025.XGraph.XVertex;

import java.util.*;

import static cs6301.g1025.Flow.inResidualGraph;
import static cs6301.g1025.Flow.xgraph;
import static cs6301.g1025.XGraph.INFINITY;
import static java.lang.Integer.min;
import static java.lang.Math.abs;


public class RelabelToFront {

    Graph g;
    Vertex source;
    Vertex sink;
    HashMap<Edge, Integer> capacity;

    RelabelToFront(Graph g, Vertex source, Vertex sink, HashMap<Edge, Integer> capacity) {
        this.g = new XGraph(g, capacity);
        this.source = this.g.getVertex(source.getName());
        this.sink = this.g.getVertex(sink.getName());
        this.capacity = capacity;
    }

    RelabelToFront(Graph g, Vertex source, Vertex sink) {
        this.g = new XGraph(g);
        this.source = this.g.getVertex(source.getName());
        this.sink = this.g.getVertex(sink.getName());
    }

    /**
     * Initializing Max-Flow Relabel to Front
     */
    void initialize() {

        for (Vertex u : g) {
            xgraph(g).setHeight(u, 0);
            xgraph(g).setExcess(u, 0);
            for(Edge e : ((XVertex) u)){
                xgraph(g).setFlow(e, 0);
            }
        }

        xgraph(g).setHeight(source, g.size());

        for (Edge e : ((XVertex) source)) {
            Vertex u = e.otherEnd(source);
            xgraph(g).setFlow(e, xgraph(g).capacity(e));
            xgraph(g).setExcess(source, xgraph(g).excess(source) - xgraph(g).capacity(e));
            xgraph(g).setExcess(u, xgraph(g).excess(u) + xgraph(g).capacity(e));
        }

    }


    /**
     * Push flow out of u using e
     * edge (u, v) = e: is in Residual Graph (Gf)
     *
     * @param u From Vertex
     * @param v To Vertex
     * @param e Edge: (u, v)
     */
    void push(Vertex u, Vertex v, Edge e) {

        int delta = xgraph(g).excess(u);
        if(xgraph(g).cf(u, e) > 0)
            delta = Math.min(xgraph(g).excess(u), xgraph(g).cf(u, e));


        if (e.fromVertex().equals(u)) {
            xgraph(g).setFlow(e, xgraph(g).flow(e) + delta);
        } else {
            xgraph(g).setFlow(e, xgraph(g).flow(e) - delta);
        }

//        int excessu = xgraph(g).excess(u);
//        int excessv = xgraph(g).excess(v);

        xgraph(g).setExcess(u, xgraph(g).excess(u) - delta);
        xgraph(g).setExcess(v, xgraph(g).excess(v) + delta);

        if(xgraph(g).excess(u) == 0)
            xgraph(g).resetMoveUp(u);
//        if(xgraph(g).excess(v) > excessv)
//            xgraph(g).setMoveUp(v);
    }

    /**
     * increase the height of u, to allow u to get rid of its excess
     * Precondition: u.excess > 0, and for all ( u, v ) in Gf, u.height <= v.height
     *
     * @param u Vertex
     */
    void relabel(Vertex u) {

        int minHeight = INFINITY;

        XVertex xu = (XVertex) u;
        for (Edge e : xu) {
            Vertex v = e.otherEnd(u);
            if (xgraph(g).height(u) > xgraph(g).height(v)) continue;
            if (xgraph(g).height(v) < minHeight && inResidualGraph(g, u, e)) {
                minHeight = xgraph(g).height(v);
            }
        }

        xgraph(g).setHeight(u, 1 + minHeight);


    }

    /**
     * push all excess flow out of u, raising its height, as needed
     *
     * @param u Vertex
     */
    void discharge(Vertex u) {

        while (xgraph(g).excess(u) > 0) {
            XVertex xu = (XVertex) u;
            for (Edge e : xu) {
                Vertex v = e.otherEnd(u);
                if (inResidualGraph(g, u, e) && xgraph(g).height(u) == 1 + xgraph(g).height(v)) {
                    push(u, v, e);
                    if (xgraph(g).excess(u) == 0) return;
                }
            }
            relabel(u);
        }

    }

    /**
     * Algorithm to find max flow !
     */
    void relabelToFront() {

        initialize();

        List<Vertex> L = new LinkedList<>();
        for (Vertex u : g) {
            if (!u.equals(source) && !u.equals(sink)) {
                L.add(u);
            }
        }

        boolean done = false;

        while (!done) {

            Iterator<Vertex> it = L.iterator();
            done = true;
            Vertex u = next(it);
            while (u != null) {
                if (xgraph(g).excess(u) == 0) {
//                    it.remove();
                    xgraph(g).resetMoveUp(u);
                    u = next(it);
                    continue;
                }
                int oldHeight = xgraph(g).height(u);

                discharge(u);

                if (xgraph(g).height(u) != oldHeight) {
                    if(xgraph(g).height(u) < oldHeight)
                        xgraph(g).setMoveUp(u);
                    done = false;
                    break;
                }
                u = next(it);
            }

            if (!done) {
                it.remove();
                if(xgraph(g).moveUp(u))
                    L.add(0, u);
                else
                    L.add(u);
            }
        }

        for (Vertex u : g) {
            xgraph(g).resetSeen(u);
        }
    }

    /**
     * gives max flow value: excess value at sink node
     *
     * @return int
     */
    int maxFlow() {
        return abs(xgraph(g).excess(sink));
    }

    Vertex next(Iterator<Vertex> it) {
        return it.hasNext() ? it.next() : null;
    }

}

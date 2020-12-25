package ex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Node {
    private List<Node> children = new ArrayList<Node>();
    private NodeClass cl;
    private String value;

    Node(NodeClass cl) {
        this.cl = cl;
    }

    Node(NodeClass cl, String value) {
        this.cl = cl;
        this.value = value;
    }

    void prependNode(Node n) {
        children.add(0, n);
    }

    void appendNode(Node n) {
        children.add(n);
    }

    void mergeWithNode(Node other) {
        Iterator<Node> it = other.getChildren();
        while(it.hasNext()) {
            this.children.add(it.next());
        }
    }

    Iterator<Node> getChildren() {
        return this.children.iterator();
    }

    boolean hasChildren() {
        return !children.isEmpty();
    }

    public NodeClass getCl() {
        return cl;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        String s = "<";
        if (cl != null) s = s + cl.toString();
        if (value != null) s = s + ", " + value;
        return s + ">";
    }
}

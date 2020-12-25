package ex;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Interpreter {

    final double initDirection = 180;

    double x;
    double y;
    double direction;

    Graphics gc;

    HashMap<String, Node> procedures;

    Color colors[] = {
        Color.BLACK,
        Color.BLUE,
        Color.CYAN,
        Color.DARK_GRAY,
        Color.GRAY,
        Color.GREEN,
        Color.LIGHT_GRAY,
        Color.MAGENTA,
        Color.ORANGE,
        Color.PINK,
        Color.RED,
        Color.WHITE,
        Color.YELLOW
    };

    void interpreter(String s, double x, double y, Graphics gc) throws Exception {
        System.out.println();
        this.gc = gc;
        this.x = x;
        this.y = y;
        direction = initDirection;
        procedures = new HashMap<String, Node>();

        // récupère un exemple 'en dur' d'arbre syntaxique abstrait
        // A FAIRE : remplacer par l'implémentation d'une analyse syntaxique descendante
        /*
        Node root = example();

        System.out.println("Arbre syntaxique abstrait :");
        printAst(root, 0);
        evalRoot(root);*/
        

        
        Lexer l = new Lexer();
        Parser p = new Parser();
        evalRoot(p.parse(l.lexe(s)));

    }

    void evalRoot(Node root) {
        Iterator<Node> it = root.getChildren();
        while (it.hasNext()) {
            Node n = it.next();
            if (n.getCl() == NodeClass.nProc) {
                procedures.put(n.getValue(), n);
            } else {
                eval(n);
            }
        }
    }

    void eval(Node n) {
        Iterator<Node> it = n.getChildren();
        switch (n.getCl()) {
            case nBlock:
                while (it.hasNext()) {
                    eval(it.next());
                }
                break;
            case nForward:
                forward(Integer.valueOf(n.getValue()));
                break;
            case nLeft:
                direction = (direction + Integer.valueOf(n.getValue())) % 360;
                break;
            case nRight:
                direction = (direction - Integer.valueOf(n.getValue())) % 360;
                break;
            case nRepeat:
                int count = Integer.valueOf(n.getValue());
                Node nodeToRepeat = it.next();
                for (int i = 0; i < count; i++) {
                    eval(nodeToRepeat);
                }
                break;
            // A FAIRE : implémenter l'interprétation des noeuds nCall et nColor
                
                
            case nCall:
            	Node np = procedures.get(n.getValue()).getChildren().next();
            	eval(np);
            	break;
            	
            case nColor:
            	gc.setColor(colors[Integer.valueOf(n.getValue())]);
            	break;
            
            default:
            	System.out.println("eval : NodeClass non géré : "+ n.getCl() + " " + n.getValue());
            	break;
            
        }
    }

    void forward(double length) {
        double destX = x + Math.sin(direction*Math.PI*2/360) * length;
        double destY = y + Math.cos(direction*Math.PI*2/360) * length;
        gc.drawLine((int)x, (int)y, (int)destX, (int)destY);
        x = destX;
        y = destY;
    }

    static void printAst(Node n, int depth) {
        StringBuilder s = new StringBuilder();
        for(int i=0;i<depth;i++) s.append("  ");
        s.append(n.toString());
        System.out.println(s);
        Iterator<Node> children = n.getChildren();
        while(children.hasNext()) {
            printAst(children.next(), depth + 1);
        }
    }

    static Node exampleAst() {
        Node root = new Node(NodeClass.nBlock);

        root.appendNode(new Node(NodeClass.nRight, "90"));

        Node n1 = new Node(NodeClass.nBlock);
        n1.appendNode(new Node(NodeClass.nForward, "40"));
        n1.appendNode(new Node(NodeClass.nRight, "90"));
        Node n11 = new Node(NodeClass.nRepeat, "3");
        n11.appendNode(n1);
        root.appendNode(n11);

        root.appendNode(new Node(NodeClass.nForward, "50"));

        // root.appendNode(new Node(NodeClass.nRight, "90"));
        
        Node n2 = new Node(NodeClass.nBlock);
        n2.appendNode(new Node(NodeClass.nLeft, "90"));
        n2.appendNode(new Node(NodeClass.nForward, "20"));
        Node n21 = new Node(NodeClass.nRepeat, "3");
        n21.appendNode(n2);
        root.appendNode(n21);

        return root;
    }
    
    static Node example() {
        Node root = new Node(NodeClass.nBlock);

        Node p1 = new Node(NodeClass.nProc, "p1");
        Node bp1 = new Node(NodeClass.nBlock);
        bp1.appendNode(new Node(NodeClass.nForward, "10"));
        bp1.appendNode(new Node(NodeClass.nRight, "90"));
        p1.appendNode(bp1);
        root.appendNode(p1);
        
        Node p2 = new Node(NodeClass.nProc, "p2");
        Node bp2 = new Node(NodeClass.nBlock);
        bp2.appendNode(new Node(NodeClass.nColor, "11"));
        bp2.appendNode(new Node(NodeClass.nRight, "90"));
        bp2.appendNode(new Node(NodeClass.nForward, "15"));
        bp2.appendNode(new Node(NodeClass.nColor, "1"));
        Node r1 = new Node(NodeClass.nRepeat, "4");
        Node br1 = new Node(NodeClass.nBlock);
        br1.appendNode(new Node(NodeClass.nCall, "p1"));
        r1.appendNode(br1);
        bp2.appendNode(r1);
        bp2.appendNode(new Node(NodeClass.nRight, "180"));
        bp2.appendNode(new Node(NodeClass.nColor, "11"));
        bp2.appendNode(new Node(NodeClass.nForward, "15"));
        bp2.appendNode(new Node(NodeClass.nRight, "90"));
        p2.appendNode(bp2);
        root.appendNode(p2);
        
        root.appendNode(new Node(NodeClass.nColor, "11"));
        root.appendNode(new Node(NodeClass.nLeft, "90"));
        root.appendNode(new Node(NodeClass.nForward, "40"));
        root.appendNode(new Node(NodeClass.nRight, "90"));
        Node r2 = new Node(NodeClass.nRepeat, "10");
        Node br2 = new Node(NodeClass.nBlock);
        br2.appendNode(new Node(NodeClass.nColor, "3"));
        br2.appendNode(new Node(NodeClass.nForward, "40"));
        br2.appendNode(new Node(NodeClass.nRight, "36"));
        br2.appendNode(new Node(NodeClass.nCall, "p2"));
        r2.appendNode(br2);
        root.appendNode(r2);
        
        

        return root;
    }
}

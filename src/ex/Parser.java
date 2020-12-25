package ex;

import java.util.ArrayList;
import java.util.List;

/**
 * Transforms a list of tokens into an ast (tree of nodes)
 */
public class Parser {
	
	int pos = 0;
	List<Token> tokens;
	
	public Parser() {	}
	
	
	public Node parse(List<Token> tokens) throws Exception{
		this.tokens = tokens;
		pos = 0;
		Node root = new Node(NodeClass.nBlock);
			
		while(!isEOF()) {
			Token t = getNextToken();
			switch(t.getCl()) {

			case kProc:
				root.appendNode(kProc(t));
				break;
			case kCall:
				root.appendNode(kCall(t));
				break;
			case kColor:
				root.appendNode(kColor(t));
				break;
			case kForward:
				root.appendNode(kForward(t));
				break;
			case kLeft:
				root.appendNode(kLeft(t));
				break;
			case kRight:
				root.appendNode(kRight(t));
				break;
			case kRepeat:
				root.appendNode(kRepeat(t));
				break;
			default: throw UnexpextedToken();

			}

		}
		
		
		return root;
	}
	
	private Node blockStart(Token t) throws Exception {
		Node n = new Node(NodeClass.nBlock);
		while(!isEOF()) {
			Token to = getNextToken();
			switch(to.getCl()) {
			
			
			case kCall:
				n.appendNode(kCall(to));
				break;
			case kColor:
				n.appendNode(kColor(to));
				break;
			case kForward:
				n.appendNode(kForward(to));
				break;
			case kLeft:
				n.appendNode(kLeft(to));
				break;
			case kRight:
				n.appendNode(kRight(to));
				break;
			
			case kRepeat:
				n.appendNode(kRepeat(t));
				break;
				
			case blockEnd: return n;
			
			default: throw UnexpextedToken();
			
			}
			
		}
		throw new Exception("Missing block end!");
	}
	
	
	
	private Node kProc(Token t) throws Exception {
		if(viewNextToken() == TokenClass.ident) {
			Node n = new Node(NodeClass.nProc, ident(getNextToken()));
			if(viewNextToken() == TokenClass.blockStart) {
				n.appendNode(blockStart(getNextToken()));
			}
			return n;
		}
		else {
			throw UnexpextedToken();
		}
	}
	
	private Node kRepeat(Token t) throws Exception {
		if(viewNextToken() == TokenClass.intVal) {
			Node n = new Node(NodeClass.nRepeat, intVal(getNextToken()));
			if(viewNextToken() == TokenClass.blockStart)
				n.appendNode(blockStart(getNextToken()));
			return n;
		}
		else {
			throw UnexpextedToken();
		}
	}
	
	private String ident(Token t) {
		return t.getValue();
	}
	
		
	private Node kCall(Token t) throws Exception {
		if(viewNextToken() == TokenClass.ident) {
			return new Node(NodeClass.nCall, ident(getNextToken()));
		}
		else {
			throw UnexpextedToken();
		}
	}
	
	
	private String intVal(Token t) {
		return t.getValue();
	}
	
	private Node kColor(Token t) throws Exception {
		if(viewNextToken() == TokenClass.intVal) {
			return new Node(NodeClass.nColor, intVal(getNextToken()));
		}
		else {
			throw UnexpextedToken();
		}
	}
	
	private Node kForward(Token t) throws Exception {
		if(viewNextToken() == TokenClass.intVal) {
			return new Node(NodeClass.nForward, intVal(getNextToken()));
		}
		else {
			throw UnexpextedToken();
		}
	}
	
	private Node kLeft(Token t) throws Exception {
		if(viewNextToken() == TokenClass.intVal) {
			return new Node(NodeClass.nLeft, intVal(getNextToken()));
		}
		else {
			throw UnexpextedToken();
		}
	}
	
	private Node kRight(Token t) throws Exception {
		if(viewNextToken() == TokenClass.intVal) {
			return new Node(NodeClass.nRight, intVal(getNextToken()));
		}
		else {
			throw UnexpextedToken();
		}
	}
	
	
	
	
	/*Other Methods*/
	private boolean isEOF() {
		return pos >= tokens.size();
	}
	
	private Token getNextToken() {
		return (!isEOF()) ? tokens.get(pos++) : null;
	}
	
	private TokenClass viewNextToken() {
		return (!isEOF()) ? tokens.get(pos).getCl() : null;
	}
	
	private Exception UnexpextedToken(){
		return new Exception("Unexpected Token: "+viewNextToken()+ " at " + pos );
	}
	
	
	
	
	
	public static List<Token> exemple(){
		List<Token> tokens = new ArrayList<Token>();
		
		tokens.add(new Token(TokenClass.kProc));
		tokens.add(new Token(TokenClass.ident, "p1"));
		tokens.add(new Token(TokenClass.blockStart));
			tokens.add(new Token(TokenClass.kForward));
			tokens.add(new Token(TokenClass.intVal, "10"));
			tokens.add(new Token(TokenClass.kRight));
			tokens.add(new Token(TokenClass.intVal, "90"));
		tokens.add(new Token(TokenClass.blockEnd));
		
		
		tokens.add(new Token(TokenClass.kProc));
		tokens.add(new Token(TokenClass.ident, "p2"));
		tokens.add(new Token(TokenClass.blockStart));		
			tokens.add(new Token(TokenClass.kColor));
			tokens.add(new Token(TokenClass.intVal, "11"));
			tokens.add(new Token(TokenClass.kRight));
			tokens.add(new Token(TokenClass.intVal, "90"));
			tokens.add(new Token(TokenClass.kForward));
			tokens.add(new Token(TokenClass.intVal, "15"));
			tokens.add(new Token(TokenClass.kColor));
			tokens.add(new Token(TokenClass.intVal, "1"));
		
			tokens.add(new Token(TokenClass.kRepeat));
			tokens.add(new Token(TokenClass.intVal, "4"));
			tokens.add(new Token(TokenClass.blockStart));
				tokens.add(new Token(TokenClass.kCall));
				tokens.add(new Token(TokenClass.ident, "p1"));			
			tokens.add(new Token(TokenClass.blockEnd));
		
			tokens.add(new Token(TokenClass.kRight));
			tokens.add(new Token(TokenClass.intVal, "180"));
			tokens.add(new Token(TokenClass.kColor));
			tokens.add(new Token(TokenClass.intVal, "11"));
			tokens.add(new Token(TokenClass.kForward));
			tokens.add(new Token(TokenClass.intVal, "15"));
			tokens.add(new Token(TokenClass.kRight));
			tokens.add(new Token(TokenClass.intVal, "90"));
		
		tokens.add(new Token(TokenClass.blockEnd));
		
		tokens.add(new Token(TokenClass.kColor));
		tokens.add(new Token(TokenClass.intVal, "11"));
		tokens.add(new Token(TokenClass.kLeft));
		tokens.add(new Token(TokenClass.intVal, "90"));
		tokens.add(new Token(TokenClass.kForward));
		tokens.add(new Token(TokenClass.intVal, "40"));
		tokens.add(new Token(TokenClass.kRight));
		tokens.add(new Token(TokenClass.intVal, "90"));
		
		
		tokens.add(new Token(TokenClass.kRepeat));
		tokens.add(new Token(TokenClass.intVal, "10"));
		tokens.add(new Token(TokenClass.blockStart));
			tokens.add(new Token(TokenClass.kColor));
			tokens.add(new Token(TokenClass.intVal, "3"));
			tokens.add(new Token(TokenClass.kForward));
			tokens.add(new Token(TokenClass.intVal, "40"));
			tokens.add(new Token(TokenClass.kRight));
			tokens.add(new Token(TokenClass.intVal, "36"));
			tokens.add(new Token(TokenClass.kCall));
			tokens.add(new Token(TokenClass.ident, "p2"));			
		tokens.add(new Token(TokenClass.blockEnd));
		
		return tokens;
	}
	
	
}

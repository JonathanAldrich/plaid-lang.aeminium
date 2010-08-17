package tests;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Variable {
	public enum Permission {
		UNIQUE,
		IMMUTABLE;
		
		static Permission fromString(String s) {
			if (s.equals("U"))
				return UNIQUE;
			else
				return IMMUTABLE; 
		}
	}
	
	private Permission permission;
	private String name;
	
	public Variable(Permission perm, String name) {
		this.permission = perm;
		this.name = name;
	}
	
	public String toString() {
		return (this.permission == Permission.UNIQUE ? "U" : "I") + "." + this.name;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

class Node {
	private List<Variable> vars = new ArrayList<Variable>();
	private int num;
	
	public Node(String s, int num) {
		System.out.println("Node " + num + ": " + s);
		
		for (String v : s.split(",")) {
			String[] x = v.split("\\.");
			vars.add(new Variable(Variable.Permission.fromString(x[0]), x[1]));
		}
		
		this.num = num;
	}

	public List<Variable> getVars() {
		return vars;
	}

	public void setVars(List<Variable> vars) {
		this.vars = vars;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}

public class DependencyTest {
	public static List<Node> buildExample() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		String example = "U.x,U.y;U.x;I.x;I.x;I.x,I.y;U.x;I.y;U.x,U.y";
//		String example = "U.x;I.x;I.x;U.x";
		
		int num = 1;
		for (String n : example.split(";")) {
			nodes.add(new Node(n, num++));
		}
		
		return nodes;
	}
	
	public static void searchDependencies(List<Node> nodes) {
		Map<String, List<Node>> readLookup = new HashMap<String, List<Node>>();
		Map<String, Node> writeLookup = new HashMap<String, Node>();
		
		StringBuilder s = new StringBuilder();
		
		s.append("digraph G {\n\trankdir=RL;\n\n");
		
		for (Node node : nodes) {
			List<Node> myDeps = new ArrayList<Node>();
			
			for (Variable v : node.getVars()) {
				if (v.getPermission() == Variable.Permission.IMMUTABLE) {
					Node n = writeLookup.get(v.getName());
					myDeps.add(n);
					
					if (readLookup.get(v.getName()) == null)
						readLookup.put(v.getName(), new ArrayList<Node>());
					List<Node> l = readLookup.get(v.getName());
					l.add(node);
				}
				else { /* UNIQUE */
					List<Node> l = readLookup.get(v.getName());
					if (l != null)
						myDeps.addAll(l);
					else {
						Node n = writeLookup.get(v.getName());
						if (n != null)
							myDeps.add(n);
					}
					
					writeLookup.put(v.getName(), node);					
					readLookup.put(v.getName(), null);
				}
			}
			
			for (Node n : myDeps) {
				s.append("\t" + node.getNum() + " -> " + n.getNum() + ";");
//				s.append("[label=\"" + ";\n");
			}
		}
		
		s.append("}\n");
		
		try {
			FileWriter f = new FileWriter("dep.dot");
			f.write(s.toString());
			f.close();
			Runtime.getRuntime().exec("dot -Tpng -o dep.png dep.dot");
		}
		catch (IOException x) {
			System.err.println(x.getMessage());
		}
	}
	
	public static void main(String[] args) {
		List<Node> example = buildExample();
		System.out.println();
		
		searchDependencies(example);
	}
}

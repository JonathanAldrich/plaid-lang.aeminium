package tests;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	private final Permission permission;
	private final String name;
	
	public Variable(Permission perm, String name) {
		this.permission = perm;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return (this.permission == Permission.UNIQUE ? "U" : "I") + "." + this.name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Variable))
			return false;
		
		Variable ov = (Variable) o;
		return this.name.equals(ov.getName()) && this.permission == ov.getPermission();
	}

	public Permission getPermission() {
		return permission;
	}

	public String getName() {
		return name;
	}
}

class Node {
	private final List<Variable> vars = new ArrayList<Variable>();
	private final int num;
	
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

	public int getNum() {
		return num;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Node))
			return false;
		
		Node on = (Node) o;
		return this.num == on.num;
	}
}

class Dependency {
	private final Node from;
	private final Node to;
	private final Variable variable;
	
	public Dependency(Node from, Node to, Variable variable) {
		this.from = from;
		this.to = to;
		this.variable = variable;
	}

	public Node getFrom() {
		return from;
	}

	public Node getTo() {
		return to;
	}

	public Variable getVariable() {
		return variable;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Dependency))
			return false;
		
		Dependency od = (Dependency) o;
		return this.from.equals(od.getFrom())
		       && this.to.equals(od.getTo())
		       && this.variable.equals(od.getVariable());
	}
}

public class DependencyTest {
	public static List<Node> buildExample() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		
//		String example = "U.x,U.y;U.x;I.x;I.x;I.x,I.y;U.x;I.y;U.x,U.y";
//		String example = "U.x;I.x;I.x;U.x";
		String example = "U.x,I.y;U.a;I.b;U.x,I.a;I.a,I.b;U.a,U.b";
//		String example = "U.to,U.from,I.amount;U.to,I.amount;U.from,I.amount;U.to,U.from,I.amount";
		
		int num = 1;
		for (String n : example.split(";")) {
			nodes.add(new Node(n, num++));
		}
		
		return nodes;
	}
	
	
	public static void searchDependencies(List<Node> nodes) {
		Map<String, Set<Node>> readers = new HashMap<String, Set<Node>>();
		Map<String, Node> writer = new HashMap<String, Node>();
		StringBuilder s = new StringBuilder("digraph G {\n\trankdir=RL;\n\n");
		
		for (Node node : nodes) {
			Set<Dependency> myDeps = new HashSet<Dependency>();
			
			for (Variable v : node.getVars()) {
				if (v.getPermission() == Variable.Permission.IMMUTABLE) {
					// Check if someone had write access to this variable before
					Node w = writer.get(v.getName());
					if (w != null) {
						// Yes, so we have to depend on that node
						myDeps.add(new Dependency(node, w, v));
					}
					
					// Get the set of reader nodes of that variable
					Set<Node> r = readers.get(v.getName());
					
					// If r is null, we are the first node referencing this variable at all
					// or the first node after a write access.
					if (r == null)
						r = new HashSet<Node>();
					r.add(node);
					readers.put(v.getName(), r);
				}
				else if (v.getPermission() == Variable.Permission.UNIQUE) {
					Set<Node> r = readers.get(v.getName());
					// We depend on all previous readers of that variable (if there are any)
					if (r != null) {
						for (Node n : r)
							myDeps.add(new Dependency(node, n, v));
					}
					else {
						// If there are no previous reads, there might still be a write.
						// We only want to depend on the write if there haven't been any reads in the meantime.
						Node w = writer.get(v.getName());
						if (w != null)
							myDeps.add(new Dependency(node, w, v));
					}
					
					// In any case, we are now the last write to that variable.
					writer.put(v.getName(), node);
					// Also, we clear the list of readers to that variable.
					readers.put(v.getName(), null);
				}
			}
			
			// We've collected all our dependencies now (can be empty)
			s.append("\t" + node.getNum() + " [ label=\"" + node.getNum() + ": ");
			for (Variable var : node.getVars())
				s.append(var + " ");
			s.append("\" ];\n");
			for (Dependency dep : myDeps)
				s.append("\t" + node.getNum() + " -> " + dep.getTo().getNum() + " [ label=\"" + dep.getVariable().getName() + "\" ];\n");
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

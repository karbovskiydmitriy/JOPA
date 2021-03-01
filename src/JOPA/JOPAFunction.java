package JOPA;

import java.util.ArrayList;

public class JOPAFunction {

	public String name;
	public ArrayList<JOPANode> nodes;

	public JOPAFunction(String name) {
		this.name = name;
		this.nodes = new ArrayList<JOPANode>();
	}

}
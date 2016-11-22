package CallGraph;

import java.util.ArrayList;

public class CallGraphNode {

	private int id;
	private String name;
	private boolean deadNode = true;
	private ArrayList<CallGraphNode> children = new ArrayList<CallGraphNode>();
	private ArrayList<CallGraphNode> parents = new ArrayList<CallGraphNode>();
	
	public CallGraphNode(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public void addChild(CallGraphNode child)
	{
		children.add(child);
		//System.out.println(child.getName());
		child.addParent(this);
	}
	
	public void addParent(CallGraphNode child)
	{
		parents.add(child);
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}
	
	public ArrayList<CallGraphNode> getChildren()
	{
		return children;
	}

	public boolean isDeadNode() {
		return deadNode;
	}

	public void setDeadNode(boolean deadNode) {
		this.deadNode = deadNode;
	}
}

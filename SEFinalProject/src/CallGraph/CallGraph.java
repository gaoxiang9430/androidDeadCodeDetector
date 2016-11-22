package CallGraph;

import java.util.ArrayList;

public class CallGraph {
	
	final private String [] essentialNode={"ChessClock","CTextView","DialogFactory","Prefs"};
	ArrayList<CallGraphNode> nodes;
	
	
	public CallGraph()
	{
		nodes = new ArrayList<CallGraphNode>();
	}
	
	public void addNode(CallGraphNode node)
	{
		if(node != null && !filterNode(node) && getNodeByName(node.getName())==null)
			nodes.add(node);
	}
	
	public void matchId(int id, String name)
	{
		int length = nodes.size();
		for(int i=0; i<length; i++)
		{
			CallGraphNode temp = nodes.get(i);
			if(temp.getName().equals(name))
				temp.setId(id);
		}
	}
	
	public void addEdge(int start, int end)
	{
		CallGraphNode startNode = getNodeById(start);
		CallGraphNode endNode = getNodeById(end);
		if(startNode != null && endNode != null)
			startNode.addChild(endNode);
	}
	
	public void addEdge(String start, String end)
	{
		CallGraphNode startNode = getNodeByName(start);
		CallGraphNode endNode = getNodeByName(end);
		if(startNode != null && endNode != null)
		{
			startNode.addChild(endNode);
		}
	}
	
	public CallGraphNode getNodeById(int id)
	{
		int length = nodes.size();
		for(int i=0; i<length; i++)
		{
			CallGraphNode temp = nodes.get(i);
			if(temp.getId() == id)
				return temp;
		}
		return null;
	}
	
	public CallGraphNode getNodeByName(String name)
	{
		int length = nodes.size();
		for(int i=0; i<length; i++)
		{
			CallGraphNode temp = nodes.get(i);
			
			if(temp.getName().equals(name))
			{
				return temp;
			}
		}
		return null;
	}
	
	
	public String toString()
	{
		String ret = "";
		int length = nodes.size();
		for(int i=0; i<length; i++)
		{
			CallGraphNode temp = nodes.get(i);
			if(temp.isDeadNode())
			{
				String classMethod = temp.getName();
				String []tempString = classMethod.split("@");
				
				ret += "ClassName: " + tempString[0];
				ret += "\tMethodName: " + tempString[1];
				ret += "\tisDead: "+temp.isDeadNode();
				ret += "\n";
			}
		}
		return ret;
	}
	
	/*
	 * This function is used to filter the android system API node
	 */
	boolean filterNode(CallGraphNode node)
	{
		for(int i=0;i<essentialNode.length;i++)
			if(node.getName().contains(essentialNode[i]))
			{
				return false;
			}
		return true;
	}
}

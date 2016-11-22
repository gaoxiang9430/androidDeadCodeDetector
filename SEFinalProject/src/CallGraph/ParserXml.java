package CallGraph;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParserXml {
	
	private CallGraph callGraph;
	
	public ParserXml(CallGraph callGraph)
	{
		this.callGraph = callGraph;
	}
	
	public void read(String fileName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        NodeList nodeList = null, edgeList = null;
        
        try{
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        
	        Document document = builder.parse(this.getClass().getResourceAsStream(fileName));
	        // get root node
	        Element root = document.getDocumentElement();
	        //System.out.println(root.getNodeName());
	 
	        //get the nodeList based on the tag name
	        nodeList = root.getElementsByTagName("node");
	        edgeList = root.getElementsByTagName("edge");
        }
        catch(Exception e)
        {
        	System.out.println(e.getMessage());
        	System.exit(0);
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            // get one node
            Node node = nodeList.item(i);
            // get all the attribute(id,label)
            NamedNodeMap attributes = node.getAttributes();
            
            String nodeId = attributes.item(0).getNodeValue();
            String nodeName = attributes.item(1).getNodeValue();
            if(nodeId != null && nodeName != null)// && nodeName.contains("V") && !nodeName.contains("$"))
            {
            	
            	addNodeToCG(nodeId, nodeName);
            }
        }
        
        for (int i = 0; i < edgeList.getLength(); i++) {
            // get one node
            Node node = edgeList.item(i);
            // get all the attribute(id,source,target)
            NamedNodeMap attributes = node.getAttributes();
            
            String sourceId = attributes.item(1).getNodeValue();
            String targetId = attributes.item(2).getNodeValue();
            if(sourceId != null && targetId != null)
            	addEdgeToCG(sourceId, targetId);
        }
    }
	
	public void addNodeToCG(String nodeId, String nodeName)
	{
        //parse nodeName
		String []tempNodeName = nodeName.split(" ");
		String className = tempNodeName[0];
		int start = className.lastIndexOf("/")+1;
		if(start>0)
		{
			className =	className.substring(start, className.length()-1);
			String methodName = className + ".java@" + tempNodeName[1];
	        callGraph.matchId(Integer.parseInt(nodeId), methodName);
		}
	}
	
	public void addEdgeToCG(String sourceId, String targetId)
	{
		int source = Integer.parseInt(sourceId);
		int target = Integer.parseInt(targetId);
        callGraph.addEdge(source, target);
	}
	
	public ArrayList<String> readManifest(String fileName) {
		ArrayList<String> mainActivity = new ArrayList<String>();
		
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        NodeList nodeList = null;
        
        try{
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        
	        Document document = builder.parse(this.getClass().getResourceAsStream(fileName));
	        // get root node
	        Element root = document.getDocumentElement();
	        nodeList = root.getElementsByTagName("activity");
        }
        catch(Exception e)
        {
        	System.out.println(e.getMessage());
        	System.exit(0);
        }
        
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NodeList secondNodeList = node.getChildNodes();
            
            for(int j=0; j<secondNodeList.getLength(); j++)
            {
            	if("intent-filter".equals(secondNodeList.item(j).getNodeName()))
            	{
            		NamedNodeMap attributes = nodeList.item(i).getAttributes();
            		mainActivity.add(attributes.getNamedItem("android:name").getNodeValue().substring(1));
            		break;
            	}	
            }
            
        }
        return mainActivity;
	}
	
	public ArrayList<String> readLayout(String fileName)
	{
ArrayList<String> mainActivity = new ArrayList<String>();
		
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        NodeList nodeList = null;
        
        try{
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        
	        Document document = builder.parse(this.getClass().getResourceAsStream(fileName));
	        // get root node
	        Element root = document.getDocumentElement();
	        NodeList secondNodeList = root.getChildNodes();
	        for(int j=0; j<secondNodeList.getLength(); j++)
            {
            	if(secondNodeList.item(j).getNodeName().contains("TextView"))
            	{
            		mainActivity.add(secondNodeList.item(j).getNodeName());
            		break;
            	}	
            }
        }
        catch(Exception e)
        {
        	System.out.println(e.getMessage());
        	System.exit(0);
        }
        return mainActivity;
	}
	
}


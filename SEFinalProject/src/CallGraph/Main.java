package CallGraph;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import ASTree.MyAST;


public class Main {
	
	static private CallGraph callGraph = null;
	static private MyAST myAST = null;
	
	ArrayList<String> classList = new ArrayList<String>();
	ArrayList<String> indirectEdge = new ArrayList<String>(); 
	ArrayList<String> mainActivity = null;
	ArrayList<String> layoutActivity = null;
	private String layout = "main.xml";
	private String manifest = "androidManifest.xml";
	
	String[] entryPoint = {"onCreateOptionsMenu", "onOptionsItemSelected", "onCreate", "onPause", "onResume", "onDestroy", "onCreateDialog"
			,"onWindowFocusChanged", "onDraw", "onPrepareOptionsMenu"};
	
	public Main()
	{
        callGraph = new CallGraph();
        myAST = new MyAST(callGraph);
	}

	//build a call graph based on the information from androidguard
	private void buildCallGraph(String path)
	{        
		ParserXml parse = new ParserXml(callGraph);
        parse.read(path);
        
    	mainActivity= parse.readManifest("/"+manifest);
    	layoutActivity = parse.readLayout("/"+layout);
	}
	
	//build an abstract syntax tree for each java file
	private void buildAST(String filePath)
	{
		myAST.build(filePath);
		ArrayList<String> temp = myAST.getaddedEdge();//this is all methods in one java file, instead of all project

		indirectEdge.addAll(temp);
	}
	
	// loop directory to get file list
	public void ParseFilesInDir(String path) {
		File fpath = new File(path);
		if (fpath.isDirectory()) {
			File[] files = fpath.listFiles();
			for (File f : files) {
				ParseFilesInDir(f.getAbsolutePath());
			}
		} else if (fpath.isFile() && fpath.getName().endsWith(".java")) {
			classList.add(fpath.getName());
			buildAST(fpath.getAbsolutePath());
		}
	}
	
	public void addIndirectEdge()
	{
		for(int i=0; i<indirectEdge.size(); i++)
		{
			String []addedEdge = indirectEdge.get(i).split("%");
			callGraph.addEdge(addedEdge[0], addedEdge[1]);
		}
	}
	
	public String getDeadClass()
	{
		
		//waiting to be implement
		return "";
	}
	
	public String getDeadMethod()
	{
		Stack<CallGraphNode> stack = new Stack<CallGraphNode>();
		
		for(int i=0; i<layoutActivity.size(); i++)
		{
			if(layoutActivity.get(i).contains("TextView"))
			{
				String temp = layoutActivity.get(i);
				temp = temp.replace(".", "%");
				String[] parseName = temp.split("%");
				String className = parseName[parseName.length-1];
				
				CallGraphNode tempNode = callGraph.getNodeByName(className+".java@"+className);
				if(tempNode!=null)
				{
					tempNode.setDeadNode(false);
					stack.add(tempNode);
				}
				
				tempNode = callGraph.getNodeByName(className+".java@onDraw");
				if(tempNode != null)
				{
					tempNode.setDeadNode(false);
					stack.add(tempNode);
				}
			}
		}
			
		for(int i=0; i<mainActivity.size(); i++)
		{
			CallGraphNode tempNode = callGraph.getNodeByName(mainActivity.get(i)+".java@"+mainActivity.get(i));
			if(tempNode!=null)
			{
				tempNode.setDeadNode(false);
				stack.add(tempNode);
			}
			for(int ii=0; ii<entryPoint.length; ii++)
			{
				//callGraph.markNode(mainActivity.get(i)+".java@"+entryPoint[ii]);
				tempNode = callGraph.getNodeByName(mainActivity.get(i)+".java@"+entryPoint[ii]);
				if(tempNode!=null)
				{
					tempNode.setDeadNode(false);
					stack.add(tempNode);
				}
			}
		}
		
		while(!stack.isEmpty())
		{
			CallGraphNode curNode = stack.pop();
			ArrayList<CallGraphNode> kids = curNode.getChildren();
			for(int i=0; i<kids.size(); i++)
			{
				CallGraphNode curKid = kids.get(i);
				if(!stack.contains(curKid) && curKid.isDeadNode())
				{
					curKid.setDeadNode(false);
					stack.add(curKid);
				}
			}
		}
		System.out.println(callGraph.toString());
		return "";
	}
	
    public static void main(String[] args) throws Exception {
    	
    	Main cgmain = new Main();
        
    	//the path of the code
    	String javaPath = "ChessClock\\src\\com\\chessclock\\android";
    	cgmain.ParseFilesInDir(args[0] + File.separator + javaPath);
    	cgmain.addIndirectEdge();
    	
    	String xmlFile = "ChessClock.xml";
    	cgmain.buildCallGraph("/"+xmlFile);
    	
    	cgmain.getDeadMethod();
    }
}

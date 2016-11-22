package ASTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;

import CallGraph.CallGraph;

public class MyAST {
	
	private MyASTVisitor myASTvisitor;
	private ASTParser parser;
	private CallGraph callGraph = null;
	
	public MyAST(CallGraph callGraph)
	{
		this.callGraph = callGraph;		
		
		//create a AST parser
		parser = ASTParser.newParser(AST.JLS8);
	}
	
	public void build(String inputFile)
	{
		File file = new File(inputFile);
		if (!file.isFile()) {
			System.err.println("The input directory is not exist!, Path: "  + inputFile);
			System.exit(0);
		}
		
		//get the absolute path of the file waiting to be parsed
		String inputFilePath = file.getAbsolutePath();
		String fileContent = readFileToString(inputFilePath);
		
		//get the content of the file
		Document document = new Document(fileContent);
		//System.out.println(document.get().toCharArray());

		parser.setSource(document.get().toCharArray());

		myASTvisitor = new MyASTVisitor(inputFile, callGraph);
		
		//this is the root node
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		//start visit all nodes
		cu.accept(myASTvisitor);
	}
	
	public ArrayList<String> getaddedEdge()
	{
		return myASTvisitor.getaddedEdge();
	}
	
	// read file content into a string
	private static String readFileToString(String filePath) {
		//System.out.println("FilePath:" + filePath);
		StringBuilder fileData = null;
		char[] buf = new char[10];
		int numRead = 0;
		try{
			fileData = new StringBuilder();
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			reader.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return fileData.toString();
	}
}

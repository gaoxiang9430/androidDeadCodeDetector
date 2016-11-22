package ASTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.eclipse.jdt.core.dom.*;

import CallGraph.CallGraph;
import CallGraph.CallGraphNode;


public class MyASTVisitor extends ASTVisitor {
	
	private class IntentItem
	{
		private String intentName = null;
		private String from = null;
		private String to = null;
		public String getIntentName() {
			return intentName;
		}
		public void setIntentName(String intentName) {
			this.intentName = intentName;
		}
		public String getFrom() {
			return from;
		}
		public void setFrom(String from) {
			this.from = from;
		}
		public String getTo() {
			return to;
		}
		public void setTo(String to) {
			this.to = to;
		}
		public String toString()
		{
			return intentName+" "+from +" "+to;
		}
	}
	
	private class DeclaredObject
	{
		private String variable;
		private String className;
		
		public DeclaredObject(String variable, String className)
		{
			this.variable = variable;
			this.className = className;
		}
		
		public String getVariable() {
			return variable;
		}
		public void setVariable(String variable) {
			this.variable = variable;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
	}
	
	private ArrayList<String> addedEdge = new ArrayList<String>();
	private String file;
	private CallGraph callGraph;
	private ArrayList<IntentItem> intent = new ArrayList<IntentItem>();
	private ArrayList<DeclaredObject> declaredObject = new ArrayList<DeclaredObject>();
	private IntentItem curIntent = null;
	private String curMethod = null;
	private DeclaredObject curDeclaredObject = null;
	private String curInvocedMethon = null;
	
	private Stack<String> invocationStack = new Stack<String>();
	
	
	public MyASTVisitor(String file, CallGraph callGraph) {
		super();
		file = file.replace("\\", " ");
		String[] pathName = file.split(" ");
		this.file = pathName[pathName.length-1];
		this.callGraph = callGraph;
	}

	public ArrayList<String> getaddedEdge()
	{
		return addedEdge;
	}
	
	public boolean visit(AnonymousClassDeclaration node) {
		return true;
	}

	public boolean visit(ClassInstanceCreation node) {
		if(curInvocedMethon != null && ( curInvocedMethon.equals("postDelayed") || curInvocedMethon.equals("removeCallbacks") || curInvocedMethon.equals("setOnClickListener")|| curInvocedMethon.equals("setPositiveButton") || curInvocedMethon.equals("setNegativeButton")))
		{
			addIndirectEdge(curMethod, node.getType().toString()+".java@onClick");
		}
		else if(curInvocedMethon != null && ( curInvocedMethon.equals("postDelayed") || curInvocedMethon.equals("removeCallbacks")))
		{
			addIndirectEdge(curMethod, node.getType().toString()+".java@run");
			//System.out.println(curMethod+"   "+ node.getType().toString()+".java@onClick");
		}
		else if(curDeclaredObject != null)
		{
			curDeclaredObject.setClassName(node.getType().toString());
		}
		
		curInvocedMethon = null;
		
		if(node.getType().toString().equals("Intent") && curIntent!=null)
		{
			List args = node.arguments();
			String from = args.get(0).toString();
			curIntent.setFrom(from.substring(0, from.indexOf(".")));
			String to = args.get(1).toString();
			curIntent.setTo(to.substring(0, to.indexOf(".")));
		}
		
		addIndirectEdge(curMethod, node.getType().toString()+".java@"+node.getType().toString());
		return true;
	}

	public boolean visit(SuperMethodInvocation node) {
		return false;
	}

	public boolean visit(ThisExpression node) {
		return false;
	}

	public boolean visit(MethodDeclaration node) {
		//List stms = node.getBody().statements();

		String methodName = file+"@"+node.getName().toString();		
		CallGraphNode tempNode = new CallGraphNode(0, methodName);
		callGraph.addNode(tempNode);
		
		//System.out.println(methodName);
		curMethod = methodName;
		return true;
	}

	public void endVisit(MethodDeclaration node) {
		curMethod = null;
	}

	public boolean visit(FieldDeclaration node) {
		//int line = cu.getLineNumber(node.getStartPosition());
		String varType = node.getType().toString();
		for (Object o : node.fragments()) {
			VariableDeclarationFragment vd = (VariableDeclarationFragment) o;
			DeclaredObject temp = new DeclaredObject(vd.getName().toString(), varType);
			declaredObject.add(temp);
			curDeclaredObject = temp;
		}
		return true;
	}
	
	public void endVisit(FieldDeclaration node)
	{
		curDeclaredObject = null;
	}

	public boolean visit(ConditionalExpression node) {
		return true;
	}

	// Expression
	public boolean visit(Assignment node) {
		Expression left = node.getLeftHandSide();
		for(int i=0;i<intent.size(); i++)
		{
			if(left.toString().equals(intent.get(i).getIntentName()))
			{
				curIntent = intent.get(i);
				break;
			}
		}
		for(int i=0;i<declaredObject.size(); i++)
		{

			if(left.toString().equals(declaredObject.get(i).getVariable()))
			{
				curDeclaredObject = declaredObject.get(i);
				break;
			}
		}
		
		return true;
	}

	public void endVisit(Assignment node)
	{
		curDeclaredObject = null;
		curIntent = null;
	}
	
	public boolean visit(InfixExpression node) {
		// Expression right = node.getRightOperand();
		// Expression left = node.getLeftOperand();
		return true;
	}

	public boolean visit(PostfixExpression node) {
		// Expression exp = node.getOperand();
		return true;
	}

	public boolean visit(PrefixExpression node) {
		// Expression exp = node.getOperand();
		return true;
	}

	public boolean visit(MethodInvocation node) {
		List<SimpleName> args = node.arguments();
		String methodName = node.getName().toString();
		if(methodName.equals("startActivity") || methodName.equals("startService"))
		{
			String intentInvoked = args.get(0).toString();
			for(int i=0; i<intent.size(); i++)
			{
				if(intent.get(i).getIntentName().equals(intentInvoked))
				{
					addIntentEdge(intent.get(i), methodName);
				}
			}
		}		
		else if(methodName.equals("setOnClickListener"))
		{
			String listener = args.get(0).toString();
			for(int i=0; i<declaredObject.size(); i++)
			{
				if(listener.equals(declaredObject.get(i).getVariable()))
				{
					addIndirectEdge(curMethod, declaredObject.get(i).getClassName()+".java@onClick");
				}
			}
		}
		else if(methodName.equals("postDelayed") || methodName.equals("removeCallbacks"))
		{
			try{
				String handler = args.get(0).toString();
				for(int i=0; i<declaredObject.size(); i++)
				{
					if(handler.equals(declaredObject.get(i).getVariable()))
					{
						addIndirectEdge(curMethod, declaredObject.get(i).getClassName()+".java@run");
					}
				}
			}
			catch(ClassCastException e)
			{}
		}
		else if(methodName.equals("setPositiveButton") || methodName.equals("setNegativeButton"))
		{
			try{
				String listener = args.get(1).toString();
				for(int i=0; i<declaredObject.size(); i++)
				{
					if(listener.equals(declaredObject.get(i).getVariable()))
					{
						addIndirectEdge(curMethod, declaredObject.get(i).getClassName()+".java@onClick");
					}
				}
			}
			catch(ClassCastException e)
			{}
		}
		curInvocedMethon = methodName;
		invocationStack.add(methodName);
		return true;
	}
	
	public void endVisit(MethodInvocation node) {
		invocationStack.pop();
		if(invocationStack.isEmpty())
			curInvocedMethon = null;
		else
			curInvocedMethon = invocationStack.firstElement();
	}

	/* Declaration in for statements */
	public boolean visit(VariableDeclarationExpression node) {
		return false;
	}

	// Expression ends

	// Statements
	public boolean visit(EnhancedForStatement node) {
		return true;
	}

	public void endVisit(EnhancedForStatement node) {
	}

	public boolean visit(ExpressionStatement node) {
		return true;
	}

	public void endVisit(ExpressionStatement node) {
	}

	public boolean visit(ForStatement node) {
		return true;
	}

	public void endVisit(ForStatement node) {
	}

	public boolean visit(IfStatement node) {
		return true;
	}

	public void endVisit(IfStatement node) {
	}

	public boolean visit(SwitchStatement node) {
		return true;
	}

	public void endVisit(SwitchStatement node) {
	}

	public boolean visit(TryStatement node) {
		return true;
	}

	public boolean visit(WhileStatement node) {
		return true;
	}

	public void endVisit(WhileStatement node) {
	}


	public boolean visit(ReturnStatement node) {
		return true;
	}
	public void endVisit(ReturnStatement node) {
	}
	public boolean Visit(ThrowStatement node) {
		return true;
	}
	public void endVisit(ThrowStatement node) {
	}
	public boolean visit(DoStatement node) {
		return true;
	}
	public void endVisit(DoStatement node) {
	}
	
	
	// Statements end
	public boolean visit(VariableDeclarationStatement node) {
		
		String varType = node.getType().toString();
		for (Object o : node.fragments()) {
			VariableDeclarationFragment vd = (VariableDeclarationFragment) o;
			DeclaredObject temp = new DeclaredObject(vd.getName().toString(), varType);
			declaredObject.add(temp);
			curDeclaredObject = temp;
		}
		
		if(node.getType().toString().equals("Intent"))
		{
			@SuppressWarnings("unchecked")
			List<VariableDeclarationFragment> fragments = node.fragments();
			for(int i=0; i<fragments.size(); i++)
			{
				VariableDeclarationFragment temp = fragments.get(i);
				IntentItem tempItem = new IntentItem();
				tempItem.setIntentName(temp.getName().toString());
				intent.add(tempItem);
				curIntent = tempItem;
			}
		}
		return true;
	}

	public void endVisit(VariableDeclarationStatement node) {
		curDeclaredObject = null;
		curIntent = null;
	}
	// VariableDeclaration
	public boolean visit(SingleVariableDeclaration node) {
		String type = node.getType().toString();
		String name = node.getName().toString();
		
		return true;
	}

	public boolean visit(VariableDeclarationFragment node) {
		String variableName = node.getName().toString();
		for(int i=0;i<intent.size(); i++)
		{
			if(variableName.equals(intent.get(i).getIntentName()))
			{
				curIntent = intent.get(i);
				break;
			}
		}
		return true;
	}
	
	public void endVisit(VariableDeclarationFragment node) {
		curIntent = null;
	}
	
	public void addIntentEdge(IntentItem intentItem, String methodName)
	{
		if(curMethod != null)
		{
			if(methodName.equals("startActivity"))
			{
				addedEdge.add(curMethod+"%"+intentItem.getTo()+".java@onCreate");
				addedEdge.add(curMethod+"%"+ intentItem.getTo()+".java@onPause");
				addedEdge.add(curMethod+"%"+ intentItem.getTo()+".java@onResume");
				addedEdge.add(curMethod+"%"+ intentItem.getTo()+".java@onDestroy");
			}
			else if(methodName.equals("startService"))
			{
				addedEdge.add(curMethod+"%"+ intentItem.getTo()+".java@onCreate");
				addedEdge.add(curMethod+"%"+ intentItem.getTo()+".java@onStartCommand");
				addedEdge.add(curMethod+"%"+ intentItem.getTo()+".java@onBind");
				addedEdge.add(curMethod+"%"+ intentItem.getTo()+".java@onDestroy");
			}
		}
	}
	
	public void addIndirectEdge(String source, String target)
	{
		//System.out.println(source+" "+target);
		addedEdge.add(source+ "%" +target);
	}

	// VariableDeclaration end
	public void postVisit(ASTNode node) {

	}
}

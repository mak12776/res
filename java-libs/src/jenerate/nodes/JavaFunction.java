package jenerate.nodes;

import java.util.List;

import jenerate.interfaces.JavaStatement;
import jenerate.modifiers.JavaAccessModifier;

public class JavaFunction
{
	public JavaAccessModifier accessModifier;
	
	public boolean isStatic;
	public boolean isFinal;
	
	public String name;
	
	public JavaType returnType;
	
	
	public List<JavaStatement> statements;
}
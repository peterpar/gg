package gg;

public class Value {
	
	int value ;
	
	Value left=null, right=null, same=null ;
	
	public Value( int v )
	{
		value = v ;
		
	}
	
	public void setLeft( Value v)
	{
		left = v ;
	}
	
	public void setRight( Value v )
	{
		right = v ;
	}
	
	public void setSame( Value v )
	{
		same = v ;
	}

}


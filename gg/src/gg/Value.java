package gg;

public class Value implements Comparable<Value> {
	
	int value ;// number on the die
	int row ; // nth dice
	
	Value left=null, right=null, same=null ;
	
	public Object clone() 
	{
		try {
			Value v = new Value();
 			v.value = value ;
			v.row = row ;
			return v ;
		}
		catch( Exception  x)
		{
			x.printStackTrace();
			return null ;
		}
	}
	
	// mark as unused
	public Value Clear()
	{
		value = -1 ;
		row = -1;
		return this;
	}
	
	public Value( int v , int row)
	{
		value = v ;
		this.row = row ;
 	}
	
	public Value() {
		value = -1 ;
		row = -1;
  	}

	public void setValue( int v, int row )
	{
		this.value = v ;
		this.row = row ;
	}
	
	/*
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
	}*/

	@Override
	public int compareTo(Value o) {
		return value - o.value ;
 	}
	
	public String toString() {
		return "[" + value + "," + row + "] ";
	}

}


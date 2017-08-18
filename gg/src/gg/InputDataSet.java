package gg;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

// holds one set of dice
public class InputDataSet {
	
	Die [] dice ;
	
	class Die {
		
		HashSet<Integer> sideValues = null ;
		
		public Die( Scanner is )
		{
			sideValues = new HashSet<Integer>(6);
			
			for( int ix = 0 ; ix < 6 ; ix++ )
			{
				sideValues.add( new Integer( is.nextInt()));
			}
			
		}
		
		public String toString()
		{
			return sideValues.toString();
		}
		
	}
	
	public InputDataSet( Scanner is )
	{
		int rows = is.nextInt() ;
		dice = new Die[ rows ];
		
		for( int row = 0 ; row < rows ; row++ )
		{
			dice[row] = new Die( is );
		}
	}

	private Value[] toValueRowArray() {

		Value [] res = new Value[ dice.length * 6 ];
		int ix = 0 ;
		LinkedList<Value> ll = new LinkedList<Value>();
 
		for( int row = 0 ; row < dice.length ; row++ )
		{
			final int frow = row ;
 			Die d = dice[row];
 			Integer [] svs = d.sideValues.toArray( new Integer[0] ) ;
 					
 			for( int ds = 0 ; ds < 6 ; ds++ )
			{
		 		ll.add( new Value( svs[ds], row ));
 			}
 		}
		return ll.toArray( res );
 	}

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "no of dice:" + dice.length + "\n" );
		for( int ix = 0 ; ix < dice.length ; ix++ )
		{
			sb.append("\n");
			sb.append( dice[ix].toString() ) ;
		}
		return sb.toString() ;
	}
	
	public static void selfTest() {
		
		String test = "3 "+
"4 "+
"4 8 15 16 23 42 "+
"8 6 7 5 30 9 "+
"1 2 3 4 55 6 "+
"2 10 18 36 54 86 "+
"2 "+
"1 2 3 4 5 6 "+
"60 50 40 30 20 10 "+
"3 "+
"1 2 3 4 5 6 "+
"1 2 3 4 5 6 "+
"1 4 2 6 5 3 " ;
		
		Scanner is = new Scanner( test );
		int sets = is.nextInt() ;
		InputDataSet [] data_sets ;
		data_sets = new InputDataSet[ sets ];
		
		LinkedList<Value> longest = new LinkedList<Value>();
		
		for( int ix = 0 ; ix < sets ; ix++ )
		{
			data_sets[ ix ] = new InputDataSet( is );
			System.out.println( data_sets[ ix ].toString() );
			Value [] all = data_sets[ ix ].toValueRowArray();
			
			LinkedList<Value> fl = findLongest( all );
			if( fl.size() > longest.size() )
				longest = fl;
			
		}
		
		
		
	}
	
	 

	private static LinkedList<Value> findLongest(Value[] all) {
		Arrays.sort(all);
		System.out.println( Arrays.toString( all ));
		// TODO Auto-generated method stub
		return new LinkedList<Value>();
	}

	public static void main( String args[] )
	{
		InputDataSet.selfTest();
	}
}

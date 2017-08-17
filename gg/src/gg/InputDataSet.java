package gg;

import java.io.InputStream;
import java.util.HashSet;
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
		
		for( int ix = 0 ; ix < sets ; ix++ )
		{
			data_sets[ ix ] = new InputDataSet( is );
			System.out.println( data_sets[ ix ].toString() );
		}
		
	}
	
	
	public static void main( String args[] )
	{
		InputDataSet.selfTest();
	}
}

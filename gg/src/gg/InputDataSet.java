package gg;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

// holds one set of dice
public class InputDataSet {
	
	HashMap<Integer,HashSet<Integer>> map_value_to_rows = null;
	boolean [] rows_used ;
	Value [] current_path;
	
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

	/**
	 * returns array of Values that contain (value,row) information
	 * @return
	 */
	private Value[] toValueRowArray() {

		Value [] res = new Value[ dice.length * 6 ];
 		LinkedList<Value> ll = new LinkedList<Value>();
 
		for( int row = 0 ; row < dice.length ; row++ )
		{
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
			
			LinkedList<Value> fl = data_sets[ ix ].findLongest( all );
			if( fl.size() > longest.size() )
				longest = fl;
 		}
 	}
	
	
	private List<Value> findLongest( Integer start_at, Integer max_value, int ix )
	{
		HashSet<Integer> rows = map_value_to_rows.get( start_at );
		Iterator<Integer> it = rows.iterator();
	
		current_path[ ix ].value = -1 ; // mark unused
		
		if( start_at > max_value )
			return null ; // we are past the possible values
		
		if( !it.hasNext() )
		{
			// no rows exist with value start_at so sequence is broken
			return null;
		}
		
		// try using each of the available rows for that number
		while( it.hasNext() )
		{ 
			Integer row = it.next();
			if( rows_used[ row ] )
				continue;
			// found row with value so use it at our position ix in sequence
			current_path[ ix ].value = start_at;
			current_path[ ix ].row = row ;
			
			rows_used[ row ] = true ;
			
			// now try to extend the list to the next value
			
			List<Value> list = findLongest( start_at+1, max_value, ix+1 );
			if( list == null )
			{
				// cannot find another node so return as we are
				List<Value> lv =  new LinkedList<Value>();
				Arrays.stream( current_path ).forEach( v -> { 
					if( v!= null && v.value != -1 ) 
						lv.add( (Value)v.clone() ); } );
				return lv ;
 			}
			else
			{
				
			}
		}
		
	}
 
	private  LinkedList<Value> findLongest(Value[] all) {
		Arrays.sort( all );
		// build index of value to rows
		map_value_to_rows = new HashMap<Integer,HashSet<Integer>>(100);
		 
		AtomicInteger num_rows = new AtomicInteger(0);
		
		Arrays.stream( all ).forEach( v -> {
			HashSet<Integer> hs = map_value_to_rows.get( v.value );
			if( hs == null )
			{
				map_value_to_rows.put( v.value, hs = new HashSet<Integer>() );
			}
			hs.add( v.row );
			if( v.row > num_rows.get() )
				num_rows.set( v.row );
		});
		
		Integer first = all[0].value ;
		Integer last = all[ all.length-1 ].value ;
		
		rows_used = new boolean[ num_rows.get() ];
		current_path = new Value[ num_rows.get() ];
		
		Arrays.setAll( current_path, ix -> current_path[ix] = new Value(0, 0) );
		
		LinkedList<Integer> res = findLongest( first, last, 0 );
		
		
		System.out.println( Arrays.toString( all ));
		// TODO Auto-generated method stub
		return new LinkedList<Value>();
	}

	public static void main( String args[] )
	{
		InputDataSet.selfTest();
	}
}

package gg;

import java.io.File;
import java.io.FileNotFoundException;
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
	Boolean [] rows_used ;
	Value [] current_path;
	ResultsManager rm = null ;
	int set_num;
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
	
	public InputDataSet( Scanner is, int set_num )
	{
		this.set_num = set_num;
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
	
	public static void selfTest( File inputFile ) {
		String test;
		String testStr = "3 "+
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
		
		Scanner is=null;
		
		if( inputFile == null )
		{
			test = testStr;
			is = new Scanner( test );
		}
		else
		{
			try {
				is = new Scanner( inputFile );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 
		int sets = is.nextInt() ;
		InputDataSet [] data_sets ;
		data_sets = new InputDataSet[ sets ];
		
		List<Value> longest = new LinkedList<Value>();
		
		for( int ix = 0 ; ix < sets ; ix++ )
		{
			data_sets[ ix ] = new InputDataSet( is, ix );
			//System.out.println( data_sets[ ix ].toString() );
			Value [] all = data_sets[ ix ].toValueRowArray();
			int num_dice = data_sets[ix].dice.length;
			
			List<Value> fl = data_sets[ ix ].findLongest( all, num_dice );
			if( fl.size() > longest.size() )
				longest = fl;
 		}
 	}
	  
	/**
	 * main entry
	 * @param all - array of all values sorted by value
	 * @param no_of_dice - rows
	 * @return the longest strait
	 */
	private  List<Value> findLongest( Value[] all, int no_of_dice ) {
		
		Arrays.sort( all );
		// build index of value to rows
		map_value_to_rows = new HashMap<Integer,HashSet<Integer>>(1000);
		  
		// prepare the value to rows mapping index
		// it tells which dice (rows) contain the indicated value
		Arrays.stream( all ).forEach( v -> {
			HashSet<Integer> hs = map_value_to_rows.get( v.value );
			if( hs == null )
			{
				map_value_to_rows.put( v.value, hs = new HashSet<Integer>(1000) );
			}
			hs.add( v.row );
 		});
		
		// figure out the range of values 
		Integer first = all[0].value ;
		Integer last = all[ all.length-1 ].value ;
		
		// array of flags to indicate a used die
		rows_used = new Boolean[ no_of_dice ];
		
		// maintain the current path for copying into result lists
		current_path = new Value[ no_of_dice ];
		Arrays.setAll( current_path, i -> current_path[i] = new Value() ) ;
		rm = new ResultsManager( set_num + 1 );
		
		for( int start_value = first ; start_value <= last ; start_value++  )
		{
			// reset used dice flags
			Arrays.setAll( rows_used, i -> new Boolean(false));
		 	Arrays.stream( current_path ).forEach( v -> v.Clear() );
			scanAllPathsStartingAt( start_value, 0, last, no_of_dice );
			System.out.println("Done:" + start_value  + " up to " + last ) ;
	 	}
 		
		//System.out.println( Arrays.toString( all ));
		
		System.out.println( rm.toString());
		 
		// TODO Auto-generated method stub
		return new LinkedList<Value>();
	}

	private int scanAllPathsStartingAt( int start_value, int current_die_ix,  int last_value, int num_of_dice )
	{
		HashSet<Integer> rows = map_value_to_rows.get( new Integer( start_value ));
		final int under ;
		
		
		if( (current_die_ix >= num_of_dice) ||
				(start_value > last_value) ||
				(rows==null) )
		{
			// terminal cases, we are at longest path so register it
			int v = current_path[0].value ;
			if( v != -1 )
			{
				rm.addResultForStartValue( v, current_path, current_die_ix );
			}
			return 0;
		}
	 	
		AtomicInteger ai = new AtomicInteger();
		ai.set(0);
		rows.forEach( d -> {
			int cnt = 0 ;
			if( !rows_used[d] )
			{
				// found an unused dice for start_value
				// accept it into path
				current_path[ current_die_ix ] = new Value( start_value, d );
				rows_used[ d ] = true ;

				// here it does not matter if we failed to go deeper or not
				ai.addAndGet(  scanAllPathsStartingAt( start_value+1, current_die_ix+1, last_value, num_of_dice ) ) ;
				rows_used[ d ] = false ; // moving on to a different die
				current_path[ current_die_ix ].Clear();
  			}
		});
		
		return 1 + ai.get();
 	}
	
	public static void main( String args[] )
	{
		File f;
		if( args.length == 0 )
			f = null;
		else
			f = new File( args[0] );
		InputDataSet.selfTest( f );
	}
}

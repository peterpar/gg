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
import java.util.concurrent.atomic.AtomicLong;

import gg.ResultsManager.ResultList;

// holds one set of dice
public class InputDataSet {
	
	HashMap<Integer,HashSet<Integer>> map_value_to_rows = null;
	Boolean [] rows_used ;
	Value [] current_path;
	ResultsManager rm = null ;
	int set_num;
	Die [] dice ;
	public static long calls = 0;
	
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
	  
	// all has 
	// values min ... max in a straight
	// index tells the dice that can produce that value
	// v0 ->    d2,      d5
	// v1 ->    d2,          d7
	// v2 ->       d3
	// v3 ->    d2 d3
 	// v4 -> d1          d5
	//
	
 
	 	

	private boolean  verifyPath( Value [] all, int start, int end,  int [] path,   HashMap<Integer,HashSet<Integer>> index_value_to_dice_nums ) 
	{
		return true ;
	}
	
	private List<Value> approachB( Value [] all,  HashMap<Integer,HashSet<Integer>> index_value_to_dice_nums )
	{
		// incrementally seek the longest straight and verify each candidate
		
		int ix = 0 ;
		int start = 0 ;
		int end = 0 ;
		int maxlen = 0 ;
		int maxix = all.length-1;
		
		// sliding detection of longest straight
		while( true )
		{
			if( end+1 < maxix )
			{
				if( all[end].value + 1 == all[end+1].value )
				{
					end++;
				}
				else
				{
					end++;
					start = end;
				}
			}
			else
			{
				start++;
				if( start == end )
					break;
			}
			
			int len = end - start +1 ;
			if( len > maxlen )
			{
				int [] path = new int[ len ];
				// verify it
				if( verifyPath( all, start, end, path ) )
				{
					maxlen = len ;
					// register this path
				}
			}
			 			
		}
		
		
	}
	
	/**
	 * main entry
	 * @param all - array of all values sorted by value
	 * @param no_of_dice - rows
	 * @return the longest strait
	 */
	private  List<Value> findLongest( Value[] all, int no_of_dice ) {
		ResultsManager.trace = true ;
		
		if( ResultsManager.trace )
			System.out.println("start sort of:" + all.length );
		
		Arrays.sort( all );
		if( ResultsManager.trace )
			System.out.println("Sorted");
		
		// build index of value to rows
		map_value_to_rows = new HashMap<Integer,HashSet<Integer>>(1000);
		  
		// prepare the value to rows mapping index
		// it tells which dice (rows) contain the indicated value
		Arrays.stream( all ).forEach( v -> {
			HashSet<Integer> hs = map_value_to_rows.get( v.value );
			if( hs == null )
			{
				map_value_to_rows.put( new Integer( v.value ), hs = new HashSet<Integer>(1000) );
			}
			hs.add( new Integer( v.row ) );
 		});

		if( ResultsManager.trace )
			System.out.println("Prepared index");
		// figure out the range of values 
		Integer first = all[0].value ;
		Integer last = all[ all.length-1 ].value ;
	
		
		return approachB( all, map_value_to_rows );
		
		
		
		// array of flags to indicate a used die
		rows_used = new Boolean[ no_of_dice ];
		
		// maintain the current path for copying into result lists
		current_path = new Value[ no_of_dice ];
		Arrays.setAll( current_path, i -> current_path[i] = new Value() ) ;
		rm = new ResultsManager( set_num + 1 );

		ResultsManager.trace = false ;
  		
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
		 
 		return new LinkedList<Value>();
	}
	
	public static long  getCheckSum( Value [] array_path ) {
		 
		AtomicLong al = new AtomicLong(0);
		AtomicInteger item = new AtomicInteger(1);
		Arrays.stream( array_path ).forEach( ix -> {
			if( ix.value != -1 )
			{
				al.addAndGet( ix.row * item.incrementAndGet() );
	 			al.addAndGet( ix.value * item.incrementAndGet() );
			}
		});
	
		return al.get();
  	}

	private int scanAllPathsStartingAt( int start_value, int current_die_ix,  int last_value, int num_of_dice )
	{
		calls++;
 		HashSet<Integer> base_set = map_value_to_rows.get( new Integer( start_value )) ;
 		
		
		if( (current_die_ix >= num_of_dice) ||
				(start_value > last_value) ||
				(base_set==null) )
		{
			// terminal cases, we are at longest path so register it
			int v = current_path[0].value ;
			if( v != -1 )
			{
				ResultsManager.trace = true ;
 				rm.addResultForStartValue( v, current_path, current_die_ix );
			}
			return 0;
		}
		
		HashSet<Integer> rows = (HashSet<Integer>)base_set.clone();
		 
		if( ResultsManager.trace && (calls % 10000 == 0) )
		{
			System.out.println("loop over:" + rows.size() + " items for start_value:" + start_value + " at depth:" + current_die_ix + " calls=" + calls );
 			
			System.out.println("Current path:" + Arrays.toString( current_path ) ) ;
 		}
		
		Iterator itr = rows.iterator();
		boolean found = false;
		while( itr.hasNext() )
		{
 			Integer iv = (Integer) itr.next();
  			int d = iv.intValue();
 			if( !rows_used[d] )
			{
 				found = true;
  				// found an unused dice for start_value
				// accept it into path
				current_path[ current_die_ix ].setValue( start_value, d );
				rows_used[ d ] = true ;

				// here it does not matter if we failed to go deeper or not
				scanAllPathsStartingAt( start_value+1, current_die_ix+1, last_value, num_of_dice ) ;
				
				rows_used[ d ] = false ; // moving on to a different die
				current_path[ current_die_ix ].Clear();
  			}
		};
		
		if(!found)
		{
			ResultsManager.trace = true ;

	 		rm.addResultForStartValue( current_path[0].value, current_path, current_die_ix );
		}	
		
		return 0;
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

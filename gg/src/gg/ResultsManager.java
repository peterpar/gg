package gg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class ResultsManager {
	
	/**
	 * holds one list of values (value.row)
	 * @author peterpar
	 *
	 */
	class ResultList {
		LinkedList<Value> result ;
		
		public ResultList ( Value [] src, int len ) {
			result = new LinkedList<Value>();
			
			Arrays.stream(src).forEach( x -> result.add( (Value)(x.clone()) ));
		}
 	}
	

	HashMap<Integer,LinkedList<ResultList>> results = null ;
	
	public ResultsManager( )
	{
		results = new HashMap<Integer,LinkedList<ResultList>>();
 	}
	
	private LinkedList<ResultList> insureExists( Integer v )
	{
		LinkedList<ResultList> llv = results.get( v );
		if( llv == null )
		{
			llv = new LinkedList<ResultList>();
			results.put( v,  llv );
		}
		return llv ;
	}
	
		 
	public void addResultForStartValue( Integer start_value, Value[] path, int len  )
	{
		LinkedList<ResultList> llv = insureExists( start_value );
		llv.add( new ResultList( path, len ));
 	}
	
}

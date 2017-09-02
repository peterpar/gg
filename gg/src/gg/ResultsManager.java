package gg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class ResultsManager {
	
	int case_num;
	/**
	 * holds one list of values (value.row)
	 * @author peterpar
	 *
	 */
	class ResultList {
		LinkedList<Value> result ;
		int len ;
		
		public ResultList ( Value [] src, int len ) {
			this.len = len ; 
			result = new LinkedList<Value>();
			
			Arrays.stream(src).forEach( x -> { 
				if( x.value != -1 )  result.add( (Value)(x.clone()) );
			});
		}
		
		public int getLen() {
			return len ;
		}
		
		public String toString()
		{
 			return "Result List:" + result.toString();
 		}
 	}
	

	HashMap<Integer,LinkedList<ResultList>> results = null ;
	
	public ResultsManager( int case_num )
	{
		this.case_num = case_num;
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
	
	public int getLongestResult()
	{
		 
		AtomicInteger max = new AtomicInteger(0);
		results.values().stream().forEach( x -> {
			// x - LinkedList<ResultList>
			x.stream().forEach( y -> {
				// y -> ResultList
				int ylen = y.getLen() ;
				if( max.get() < ylen )
					max.set( ylen );
			});
 		});
		return max.get();
	}

	public String toString()
	{
		return "Case #" + case_num + ": " + getLongestResult() ;
  	}
}

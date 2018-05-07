
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.IntStream;

import negotiator.Bid;

public class Learner {
	private Bid bestBid;
	private int dim;
	private int maxE = 1000; //TODO
	
	public double dist(double[] v, double[] v2)
	{
		for(int i = 0; i < v.length; ++i)
		{
			double d = v[i] - v2[i]; 

		}
	}
		
	public void init(Bid _bestBid, int _dim)
	{
		dim = _dim;
		bestBid = _bestBid;
		// TODO
	}
	
	public Bid run(Bid counterOffer)
	{
		List<Bid> offers;
		offers.add(bestBid);
		
		// TODO: write algorithm
		for (int i = 0; i < maxE; i++)
		{
			
		}
		return counterOffer;
	}
}

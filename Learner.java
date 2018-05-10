
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.IntStream;

import negotiator.Bid;
import negotiator.parties.NegotiationInfo;
import negotiator.parties.NegotiationParty;

public class Learner {
	private Bid bestBid;
	private int dim;
	private int maxE = 3; //TODO
	private int populationSize = 200;
	private List<Individual> population;
	
//	public double dist(double[] v, double[] v2)
//	{
//		for(int i = 0; i < v.length; ++i)
//		{
//			double d = v[i] - v2[i]; 
//
//		}
//	}
		
	public Learner(List<Bid> randomBids, Bid _bestBid, int _dim, NegotiationInfo info)
	{
		dim = _dim;
		bestBid = _bestBid;
		
		population = new Vector<Individual>();
		
		// generate initial population
		for (Bid randomBid : randomBids)
		{
			population.add(new Individual(randomBid, info));
		}
	}
	
	public Bid run(Bid counterOffer)
	{
		List<Bid> offers;
//		offers.add(bestBid);
		
		// TODO: write algorithm
		for (int i = 0; i < maxE; i++)
		{
			
		}
		return counterOffer;
	}
}

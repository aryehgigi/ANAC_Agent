
import java.util.HashMap;
import java.util.List;

import negotiator.Bid;
import negotiator.issue.Issue;
import negotiator.issue.IssueDiscrete;
import negotiator.issue.Value;
import negotiator.issue.ValueDiscrete;
import negotiator.utility.AdditiveUtilitySpace;
import negotiator.utility.EvaluatorDiscrete;
import negotiator.parties.NegotiationInfo;
import java.util.Random;

public class Individual {
	private HashMap<Integer, Value> m_gene;
	private Double m_util;
	private NegotiationInfo m_info;
	private Random m_rand;
	
	public Individual(Bid b, NegotiationInfo info)
	{
		m_gene = b.getValues();
		m_info = info;
		m_util = CalcUtility();
		m_rand = new Random();
	}
	
	public Boolean equals(Individual other)
	{
		for(int i = 0; i < m_gene.size(); ++i)
		{
			if (!m_gene.get(i).equals(other.GetValue(i)))
			{
				return false;
			}			
		}
		return true;
	}
	
	public Value GetValue(Integer key)
	{
		return m_gene.get(key);
	}
	
	public void SetValue(Integer key, Value value)
	{
		m_gene.put(key, value);
	}
	
	public Double GetUtility()
	{
		return m_util;
	}
	
	private Double CalcUtility()
	{
		// TODO maybe simply call utilitySpace.getUtility(randomBid); see https://github.com/tdgunes/ExampleAgent/wiki/Generating-a-random-bid-with-a-utility-threshold
		Double util = 0.0;

		AdditiveUtilitySpace additiveUtilitySpace = (AdditiveUtilitySpace) m_info.getUtilitySpace();
		List<Issue> issues = additiveUtilitySpace.getDomain().getIssues();

		for (Issue issue : issues)
		{
		    int issueNumber = issue.getNumber();
		    Double weight = additiveUtilitySpace.getWeight(issueNumber);

		    // Assuming that issues are discrete only
		    IssueDiscrete issueDiscrete = (IssueDiscrete) issue;
		    EvaluatorDiscrete evaluatorDiscrete = (EvaluatorDiscrete) additiveUtilitySpace.getEvaluator(issueNumber);
		    Double evaluation = 0.0;
		    
		    for (ValueDiscrete valueDiscrete : issueDiscrete.getValues())
		    {
		        if (valueDiscrete.getValue().equals(m_gene.get(issueNumber).toString()))
		        {
		        	try
		        	{
						evaluation = evaluatorDiscrete.getEvaluation(valueDiscrete);
					}
		        	catch (Exception e)
		        	{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    }
		    
		    util += (weight * evaluation);
		}
		return util;
	}
	
	public Individual Clone(Bid randomBid) // getting a random bid because only an agent can generate one
	{
		for (Integer key : m_gene.keySet())
		{
			randomBid.putValue(key, m_gene.get(key));
		}
		
		return new Individual(randomBid, m_info);
	}
	
	public void Mutate()
	{
		AdditiveUtilitySpace additiveUtilitySpace = (AdditiveUtilitySpace) m_info.getUtilitySpace();
		List<Issue> issues = additiveUtilitySpace.getDomain().getIssues();
		
		int randomNum = m_rand.nextInt(issues.size()) + 1;
		Issue issue = issues.get(randomNum);
		
		IssueDiscrete issueDiscrete = (IssueDiscrete) issue;
		int issueNumber = issue.getNumber();
		randomNum = issueNumber;
		while (randomNum == issueNumber)
		{
			randomNum = m_rand.nextInt(issueDiscrete.getValues().size()) + 1;
		}
		
		Value newValue = issueDiscrete.getValues().get(randomNum);
		m_gene.put(issueNumber, newValue);
	}
	
	public void Crossover(Individual other)
	{
		AdditiveUtilitySpace additiveUtilitySpace = (AdditiveUtilitySpace) m_info.getUtilitySpace();
		List<Issue> issues = additiveUtilitySpace.getDomain().getIssues();
		
		int randomNum = m_rand.nextInt(issues.size() - 1) + 1; // minus one since crossover location is in gap between alleles
		
		for (Integer key : m_gene.keySet())
		{
			if (key > randomNum)
			{	
				break;
			}
			
			Value temp = m_gene.get(key);
			m_gene.put(key, other.GetValue(key));
			other.SetValue(key, temp);
		}
	}
}

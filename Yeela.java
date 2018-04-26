

import negotiator.AgentID;
import negotiator.Bid;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.Offer;
import negotiator.parties.AbstractNegotiationParty;
import negotiator.parties.NegotiationInfo;

import java.util.List;

/**
 * group1.Agent1 returns the bid that maximizes its own utility for half of the negotiation session.
 * In the second half, it offers a random bid. It only accepts the bid on the table in this phase,
 * if the utility of the bid is higher than Example Agent's last bid.
 */
public class Yeela extends AbstractNegotiationParty {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2676016971703971492L;

	private final String description = "Example Agent";

    private Bid lastReceivedOffer; // offer on the table
    private Learner curLearner;
    private boolean firstToAct;
    private double timeToGiveUp = 0.95;
    
    @Override
    public void init(NegotiationInfo info) {
        super.init(info);

        System.out.println("Discount Factor is " + info.getUtilitySpace().getDiscountFactor());
        System.out.println("Reservation Value is " + info.getUtilitySpace().getReservationValueUndiscounted());
        firstToAct = true;
        curLearner.init(getMaxUtilityBid(), info.getUtilitySpace().getDomain().getIssues().size());
    }

    /**
     * When this function is called, it is expected that the Party chooses one of the actions from the possible
     * action list and returns an instance of the chosen action.
     *
     * @param list
     * @return
     */
    public Action chooseAction(List<Class<? extends Action>> list) {
        // According to Stacked Alternating Offers Protocol list includes
        // Accept, Offer and EndNegotiation actions only.
        double time = getTimeLine().getTime(); // Gets the time, running from t = 0 (start) to t = 1 (deadline).
                                               // The time is normalized, so agents need not be
                                               // concerned with the actual internal clock.

        System.out.println(time);

        // if we are first
        if (firstToAct)
        {
        	return new Offer(this.getPartyId(), this.getMaxUtilityBid());
        }
        
        // create new offer
        Bid bid = curLearner.run(lastReceivedOffer);
        
        // decide whether to offer it or accept counter offer
        if ((bid == lastReceivedOffer) ||
        	(this.utilitySpace.getUtility(lastReceivedOffer) >= this.utilitySpace.getUtility(bid)) ||
        	(timeToGiveUp < time))
        {
        	return new Accept(this.getPartyId(), lastReceivedOffer);
        }
        else
        {
        	return new Offer(this.getPartyId(), bid);
        }
    }

    /**
     * This method is called to inform the party that another NegotiationParty chose an Action.
     * @param sender
     * @param act
     */
    @Override
    public void receiveMessage(AgentID sender, Action act) {
        super.receiveMessage(sender, act);

        if (act instanceof Offer) { // sender is making an offer
            Offer offer = (Offer) act;

            // storing last received offer
            lastReceivedOffer = offer.getBid();
            firstToAct = false;
        }
    }

    /**
     * A human-readable description for this party.
     * @return
     */

    public String getDescription() {
        return description;
    }

    private Bid getMaxUtilityBid() {
        try {
            return this.utilitySpace.getMaxUtilityBid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
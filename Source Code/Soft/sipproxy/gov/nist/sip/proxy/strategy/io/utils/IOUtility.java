package gov.nist.sip.proxy.strategy.io.utils;

import java.util.HashMap;
import java.util.Map;

import gov.nist.sip.proxy.states.Pair;
import gov.nist.sip.proxy.states.UserState;
import gov.nist.sip.proxy.strategy.billing.BillingStrategy;
import gov.nist.sip.proxy.strategy.io.IOStrategy;

public class IOUtility {
	Map<Pair, UserState> blocking;
	Map<String, BillingStrategy> billing;
	IOStrategy ioStrategy;
	
	//Singleton instance of the IOUtility
	private static IOUtility instance = null;
	
	// the constructor should not be visible so we are making it private
	private IOUtility(){
		blocking = new HashMap<Pair, UserState>();
		billing = new HashMap<String, BillingStrategy>();
	}
	
	//lazy initialization of the instance
	public static IOUtility getInstance(){
		if (instance == null) {
			instance = new IOUtility();
		}
		return instance;
	}

	public Map<Pair, UserState> getBlocking() {
		return blocking;
	}

	public void setBlocking(Map<Pair, UserState> blocking) {
		this.blocking = blocking;
	}

	public IOStrategy getIOStrategy() {
		return ioStrategy;
	}

	public void setIOStrategy(IOStrategy ioStrategy) {
		this.ioStrategy = ioStrategy;
	}

	public Map<String, BillingStrategy> getBilling() {
		return billing;
	}

	public void setBilling(Map<String, BillingStrategy> billing) {
		this.billing = billing;
	}

	public void performIOOperation() {
		ioStrategy.performIOOperation();
	}
	
}
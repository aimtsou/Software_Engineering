package gov.nist.sip.proxy;

import gov.nist.sip.proxy.strategy.io.BillingReadingStrategy;
import gov.nist.sip.proxy.strategy.io.BillingWritingStrategy;
import gov.nist.sip.proxy.strategy.io.utils.IOUtility;

import java.io.File;

// The Class that implements Billing
public class Billing {
	public static double findAccountType(String caller) {
		IOUtility io = IOUtility.getInstance();
		// if this entry is already in the map, return the value
		if (io.getBilling().containsKey(caller)) {
			return io.getBilling().get(caller).getCost();
		}
		BillingReadingStrategy billingStr = new BillingReadingStrategy(caller);
		io.setIOStrategy(billingStr);
		io.performIOOperation();

		return billingStr.getCurrentBillingState().getCost();
	}

	public static void updateAccount(File xmlFile, String caller, double charge) {
		IOUtility io = IOUtility.getInstance();
		BillingWritingStrategy billingStr = new BillingWritingStrategy(caller, xmlFile, charge);
		io.setIOStrategy(billingStr);
		io.performIOOperation();
	}

}

package gov.nist.sip.proxy.strategy.billing;

public class BillingStrategy {
	public static String FREE = "free";
	public static String BASIC = "basic";
	public static String PREMIUMM = "premium";
	
	protected double cost;
	
	public double getCost(){
		return cost;
	}
}

package gov.nist.sip.proxy.strategy.billing;


public class FreeBillingStrategy extends BillingStrategy{
	public FreeBillingStrategy() {
		this.cost = -1.0;
	}
}
package gov.nist.sip.proxy.strategy.io;

import gov.nist.sip.proxy.strategy.billing.BillingStrategy;
import gov.nist.sip.proxy.strategy.billing.FreeBillingStrategy;
import gov.nist.sip.proxy.strategy.billing.PremiumBillingStrategy;
import gov.nist.sip.proxy.strategy.billing.StandardBillingStrategy;
import gov.nist.sip.proxy.strategy.io.utils.IOUtility;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BillingReadingStrategy implements IOStrategy {
	public static final String BILLING_XML = "./gov/nist/sip/proxy/Billing.xml";
	
	String caller;
	
	BillingStrategy billingState;
	
	BillingStrategy freeBillingState;
	BillingStrategy standardBillingState;
	BillingStrategy premiumBillingState;
	
	public void setCurrentBillingState(String stateName){
		if (stateName.equals(BillingStrategy.FREE)){
			setFreeBillingState();
		} else if (stateName.equals(BillingStrategy.BASIC)){
			setStandardBillingStrategy();
		} else if (stateName.equals(BillingStrategy.PREMIUMM)){
			setPremiumBillingState();
		}
	}
	
	public void setFreeBillingState(){
		if (freeBillingState == null)
			freeBillingState = new FreeBillingStrategy();
		billingState = freeBillingState;
	}
	public void setStandardBillingStrategy(){
		if (standardBillingState == null)
			standardBillingState = new StandardBillingStrategy();
		billingState = standardBillingState;
	}
	public void setPremiumBillingState(){
		if (premiumBillingState == null)
			premiumBillingState = new PremiumBillingStrategy();
		billingState = premiumBillingState;
	}
	public BillingStrategy getCurrentBillingState(){
		return billingState;
	}
	public BillingReadingStrategy(String caller) {
		this.caller = caller;
	}
	
	@Override
	public void performIOOperation() {
		setFreeBillingState();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			// Read and parse the Billing.xml file
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(BILLING_XML);

			// Get all the user nodes
			NodeList user = document.getElementsByTagName("user");
			
			for (int i = 0; i < user.getLength(); i ++){
				Element el = (Element) user.item(i);
				String txt = el.getFirstChild().getNodeValue();

				// When you find the Caller, save his index
				if (txt.trim().equalsIgnoreCase(caller)) {
					// Read the value of the node account
					NodeList account = el.getElementsByTagName("account");
					txt = account.item(0).getFirstChild().getNodeValue();
					setCurrentBillingState(txt.trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			setFreeBillingState();
		}
		IOUtility.getInstance().getBilling().put(caller, getCurrentBillingState());
	}
	
}

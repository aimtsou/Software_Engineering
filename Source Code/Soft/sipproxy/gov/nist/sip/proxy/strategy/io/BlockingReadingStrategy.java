package gov.nist.sip.proxy.strategy.io;

import gov.nist.sip.proxy.states.BlockedState;
import gov.nist.sip.proxy.states.Pair;
import gov.nist.sip.proxy.states.UnblockedState;
import gov.nist.sip.proxy.states.UserState;
import gov.nist.sip.proxy.strategy.io.utils.IOUtility;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BlockingReadingStrategy implements IOStrategy {
	public static final String BLOCKED_USERS_XML = "./gov/nist/sip/proxy/BlockedUsers.xml";
	String caller;
	String callee;
	
	UserState currentState;

	UserState blockedState;
	UserState unblockedState;
	
	public BlockingReadingStrategy(String caller, String callee) {
		this.caller = caller;
		this.callee = callee;
	}
	
	public void setBlockedState() {
		if (blockedState == null)
			blockedState = new BlockedState();
		currentState = blockedState;
	}

	public void setUnblockedState() {
		if (unblockedState == null)
			unblockedState = new UnblockedState();
		currentState = unblockedState;
	}

	public UserState getCurrentState() {
		return currentState;
	}
	
	@Override
	public void performIOOperation() {
		//by default the state of a pair caller , callee is unblocked
		setUnblockedState();
		
		// create a pair of the Caller and Callee
		Pair p = new Pair(caller, callee);
		// if this entry is already in the map, return the value
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {

				// Read and parse the BlockedUsers.xml file
				DocumentBuilder builder = factory.newDocumentBuilder();
				//./gov/nist/sip/proxy/
				Document document = builder.parse(BLOCKED_USERS_XML);
				
				// Get all the user nodes
				NodeList user = document.getElementsByTagName("user");
				
				for(int i = 0; i < user.getLength(); i ++){
					Element el = (Element) user.item(i);
					Node node = el.getFirstChild();
					String userName = node.getNodeValue().trim();
					if (userName.equals(callee)){
						// this is the callee user node
						NodeList children = el.getChildNodes();
						for (int j = 0; j < children.getLength(); j++) {
							if (children.item(j).getNodeName().equals("blocked")) {
								// we have found a block node under the callee user
								Node blockedItem = children.item(j).getChildNodes().item(0);
								if (blockedItem.getNodeValue().trim().equals(caller)) {
									setBlockedState(); // the caller is blocked by the callee
									break;
								}
							}
						}
						break;
					}
				}
			} catch (Exception e){
				e.printStackTrace();
				setUnblockedState();
			}

			IOUtility.getInstance().getBlocking().put(p, getCurrentState());
	}

}

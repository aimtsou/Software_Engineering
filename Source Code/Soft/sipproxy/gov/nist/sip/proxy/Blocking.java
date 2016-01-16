package gov.nist.sip.proxy;

import gov.nist.sip.proxy.states.Pair;
import gov.nist.sip.proxy.strategy.io.BlockingReadingStrategy;
import gov.nist.sip.proxy.strategy.io.utils.IOUtility;

// The Class that implements Blocking
public class Blocking {
	Blocking() {
	}
	
	public static boolean checkForBlocks(String caller, String callee) {
		IOUtility io = IOUtility.getInstance();
		// create a pair of the Caller and Callee
		Pair p = new Pair(caller, callee);
		// if this entry is already in the map, return the value
		if (io.getBlocking().containsKey(p)) {
			return io.getBlocking().get(p).isBlocked();
		}
		BlockingReadingStrategy blockingStr = new BlockingReadingStrategy(caller, callee);
		io.setIOStrategy(blockingStr);
		io.performIOOperation();

		return blockingStr.getCurrentState().isBlocked();
	}
}
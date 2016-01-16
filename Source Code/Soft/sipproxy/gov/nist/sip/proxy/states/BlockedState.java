package gov.nist.sip.proxy.states;


public class BlockedState extends UserState {

	@Override
	public boolean isBlocked() {
		return true;
	}

}

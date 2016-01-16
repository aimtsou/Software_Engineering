package gov.nist.sip.proxy.states;


public class UnblockedState extends UserState {
	@Override
	public boolean isBlocked() {
		return false;
	}

}

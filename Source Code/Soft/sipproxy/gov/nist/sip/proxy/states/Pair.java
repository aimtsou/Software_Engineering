package gov.nist.sip.proxy.states;

public class Pair{
	String caller;
	String callee;
	
	public Pair(String caller, String callee) {
		this.caller = caller;
		this.callee = callee;
	}
	@Override
	public boolean equals(Object obj) {
		Pair p = (Pair)obj;
		return caller.equals(p.caller) && callee.equals(p.callee);
	}
}
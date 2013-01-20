package eu.q_b.asn007.lambda;

public class Session {

	private String user;
	private String session;
	
	public Session(String user, String session) {
		this.user = user;
		this.session = session;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
}

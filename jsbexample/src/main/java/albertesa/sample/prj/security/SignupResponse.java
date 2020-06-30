package albertesa.sample.prj.security;

public class SignupResponse {
	private String status;
	private String email;
	
	private SignupResponse(String status, String email) {
		super();
		this.status = status;
		this.email = email;
	}

	public static SignupResponse ok(String email) {
		return new SignupResponse("success", email);
	}

	public static SignupResponse error(String email, String errMsg) {
		return new SignupResponse(errMsg, email);
	}

	public String getStatus() {
		return status;
	}

	public String getEmail() {
		return email;
	}

}

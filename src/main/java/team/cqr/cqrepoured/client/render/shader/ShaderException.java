package team.cqr.cqrepoured.client.render.shader;

@SuppressWarnings("serial")
public class ShaderException extends RuntimeException {

	public ShaderException() {

	}

	public ShaderException(String message) {
		super(message);
	}

	public ShaderException(Throwable cause) {
		super(cause);
	}

	public ShaderException(String message, Throwable cause) {
		super(message, cause);
	}

}

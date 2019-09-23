package tk.pratanumandal.fts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
	
	private static final long serialVersionUID = -1833573021639160895L;

	public ForbiddenException() {
		super();
	}

	public ForbiddenException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public ForbiddenException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ForbiddenException(String arg0) {
		super(arg0);
	}

	public ForbiddenException(Throwable arg0) {
		super(arg0);
	}
	
}
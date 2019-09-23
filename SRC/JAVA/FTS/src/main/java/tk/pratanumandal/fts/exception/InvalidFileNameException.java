package tk.pratanumandal.fts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFileNameException extends RuntimeException {
	
	private static final long serialVersionUID = -1833573021639160895L;

	public InvalidFileNameException() {
		super();
	}

	public InvalidFileNameException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public InvalidFileNameException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidFileNameException(String arg0) {
		super(arg0);
	}

	public InvalidFileNameException(Throwable arg0) {
		super(arg0);
	}
	
}
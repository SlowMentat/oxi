package oxi.errors;



/*
* Custome Error thrown during account registration.
* Used in UserAccountService and RetailerAccountService classes.
*/
public final class UserAlreadyExistException extends RuntimeException{
	
	private static final long serialVersionUID = 5861310530339287163L;

	public UserAlreadyExistException(){
		super();
	}

	public UserAlreadyExistException(final String message, final Throwable cause){
		super(message, cause);
	}

	public UserAlreadyExistException(final String message){
		super(message);
	}

	public UserAlreadyExistException(final Throwable throwable){
		super(throwable);
	}
}

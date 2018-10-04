package dk.gundmann.budget;

public class BudgetException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BudgetException(String message, Throwable error) {
		super(message, error);
	}

}

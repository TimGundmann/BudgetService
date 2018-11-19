package dk.gundmann.budget;

import dk.gundmann.budget.Expense.ExpenseBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Income {
	
	private double value;
	
	public boolean isIncome() {
		return true;
	}

}

package dk.gundmann.budget;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class BudgetServiceTest {

	private BudgetService budgetService = new BudgetService();
	
	@Test
	public void givenAAccountStatementFileWillCreateABudget() {
		// given when then
		assertThat(budgetService.makeBudget(null)).isNotNull();
	}
	
}

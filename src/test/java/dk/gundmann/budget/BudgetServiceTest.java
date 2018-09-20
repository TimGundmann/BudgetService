package dk.gundmann.budget;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;

public class BudgetServiceTest {

	private BudgetService budgetService = new BudgetService();
	
	@Test
	public void givenAAccountStatementFileWillCreateABudget() {
		// given when then
		assertThat(budgetService.makeBudget(new File(""))).isNotNull();
	}
	
}

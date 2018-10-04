package dk.gundmann.budget;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class BudgetServiceTest {

	private static final String CSV_HEADER_LINE = "\nBogført;Tekst;Rentedato;Beløb;Saldo";
	
	private BudgetService budgetService = new BudgetService();
	
	@Test
	public void givenAAccountStatementFileWillCreateABudget() {
		// given
		InputStream stream = makeCSVFileWith("26-09-2018;;;-214,90");
		
		// when then
		assertThat(budgetService.makeBudget(stream)).isNotNull();
	}
	
	@Test
	public void verifyThatExpenseIsAdded() {
		// given 
		InputStream stream = makeCSVFileWith("26-09-2018;;;-214,90");
		
		// when then
		assertThat(budgetService.makeBudget(stream)).isEqualTo(Budget.builder()
				.months(newArrayList(Month.builder()
						.month(9)
						.expenses(newArrayList(Expense.builder()
						.value(-214.90)
						.build()))
					.build()))
				.build());
	}
	
	@Test
	public void givenTwoLinesWithSameMonthsWillCollectOnlyOneMonth() {
		// given 
		InputStream stream = makeCSVFileWith(
				"26-09-2018;;;-214,90",
				"26-09-2018;;;-15,90");
		
		// when then
		assertThat(budgetService.makeBudget(stream).getMonths()).describedAs("only one month expected").hasSize(1);
	}
	
	@Test
	public void givenTwoLinesWithDiffrentMonthsWillCollectBothMonth() {
		// given 
		InputStream stream = makeCSVFileWith(
				"26-10-2018;;;-214,90",
				"26-09-2018;;;-15,90");
		
		// when then
		assertThat(budgetService.makeBudget(stream).getMonths()).describedAs("two month expected").hasSize(2);
	}

	@Test
	public void givenTwoLinesWithOneExpenseAndOnIncomeInSameMonthWillCollectThem() {
		// given 
		InputStream stream = makeCSVFileWith(
				"26-10-2018;;;-214,90;",
				"26-10-2018;;;15,90;");
		
		// when then
		Month month = budgetService.makeBudget(stream).getMonths().get(0);
		
		// then
		assertThat(month.getExpenses()).describedAs("one expense expected").hasSize(1);
		assertThat(month.getIncomes()).describedAs("one income expected").hasSize(1);
	}

	@Test
	public void givenExpensesWillCalculateMonthTotal() {
		// given 
		InputStream stream = makeCSVFileWith(
				"26-10-2018;;;-214,90;",
				"26-10-2018;;;-15,10;");
		
		// when
		Month month = budgetService.makeBudget(stream).getMonths().get(0);

		// then
		assertThat(month.getTotalExpenses()).isEqualTo(-230.00);
	}

	@Test
	public void givenIncomesWillCalculateMonthTotal() {
		// given 
		InputStream stream = makeCSVFileWith(
				"26-10-2018;;;214,90;",
				"26-10-2018;;;15,10;");
		
		// when
		Month month = budgetService.makeBudget(stream).getMonths().get(0);
		
		// then
		assertThat(month.getTotalIncomes()).isEqualTo(230.00);
	}

	@Test
	public void givenIncomesWillCalculateBudgetTotal() {
		// given 
		InputStream stream = makeCSVFileWith(
				"26-01-2018;;;200,00;",
				"26-02-2018;;;200,00;",
				"26-03-2018;;;200,00;",
				"26-04-2018;;;200,00;");
		
		// when then
		assertThat(budgetService.makeBudget(stream).getTotalIncomes()).isEqualTo(800.00);
	}

	@Test
	public void givenExpensesWillCalculateBudgetTotal() {
		// given 
		InputStream stream = makeCSVFileWith(
				"26-01-2018;;;-100,00;",
				"26-02-2018;;;-100,00;",
				"26-03-2018;;;-100,00;",
				"26-04-2018;;;-100,00;");
		
		// when then
		assertThat(budgetService.makeBudget(stream).getTotalExpenses()).isEqualTo(-400.00);
	}

	private ByteArrayInputStream makeCSVFileWith(String... lines) {
		return new ByteArrayInputStream(
				(CSV_HEADER_LINE + Arrays.stream(lines)
					.map(s -> "\n" + s)
					.collect(Collectors.joining()))
				.getBytes());
	}
	
}

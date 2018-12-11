package dk.gundmann.budget;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class BudgetServiceMakeTest {

	private static final String CSV_HEADER_LINE = "\nBogført;Tekst;Rentedato;Beløb;Saldo";
	
	private BudgetService budgetService = new BudgetService(null);
	
	@Test
	public void givenAAccountStatementFileWillCreateABudget() {
		// given
		InputStream stream = makeCSVFileWith("26-09-2018;;;-214,90;0");
		
		// when then
		assertThat(budgetService.makeBudget(stream)).isNotNull();
	}

	@Test
	public void verifyThatExpenseIsAdded() {
		// given 
		InputStream stream = makeCSVFileWith("26-09-2018;test;;-214,90;0");
		
		// when then
		assertThat(budgetService.makeBudget(stream)).isEqualTo(Budget.builder()
				.categories(newArrayList(Category.builder()
					.name("test")
					.months(newArrayList(Month.builder()
							.month(9)
							.expenses(newArrayList(Expense.builder()
							.value(214.90)
							.build()))
						.build()))
					.build()))
				.build());
	}
	
	@Test
	public void givenTwoLinesWithSameMonthsWillCollectOnlyOneMonth() {
		// given 
		InputStream stream = makeCSVFileWith(
				"26-09-2018;;;-139,00",
				"26-09-2018;;;-139,00",
				"26-09-2018;;;-60,00",
				"26-09-2018;;;-69,41",
				"26-09-2018;;;-51,5",
				"26-09-2018;;;-61,5",
				"26-09-2018;;;-55,66");
		
		// when 
		List<Month> months = budgetService.makeBudget(stream).getCategories().get(0).getMonths();
		
		// then
		assertThat(months).describedAs("only one month expected").hasSize(1);
		assertThat(months.get(0).getTotalExpenses()).describedAs("total is wrong").isEqualTo(576.07);
		
	}
	
	@Test
	public void givenTwoLinesWithDiffrentMonthsWillCollectBothMonth() {
		// given 
		InputStream stream = makeCSVFileWith(
				"26-10-2018;;;-214,90;0",
				"26-09-2018;;;-15,90;0");
		
		// when then
		assertThat(budgetService.makeBudget(stream).getCategories().get(0).getMonths())
			.describedAs("two month expected")
			.hasSize(2);
	}

	@Test
	public void givenTwoLinesWithOneExpenseAndOnIncomeInSameMonthWillCollectThem() {
		// given 
		InputStream stream = makeCSVFileWith(
				"26-10-2018;category 1;;-214,90;0",
				"26-10-2018;category 2;;15,90;0");
		
		// when then
		List<Category> categories = budgetService.makeBudget(stream).getCategories();
		Month monthExpenses = categories.get(categories.indexOf(Category.builder()
					.name("category")
					.build()))
				.getMonths().get(0);
		Month monthIncomes = categories.get(categories.indexOf(Category.builder()
				.name(Income.INDCOME)
				.build()))
			.getMonths().get(0);
		
		// then
		assertThat(monthExpenses.getExpenses()).describedAs("one expense expected").hasSize(1);
		assertThat(monthIncomes.getIncomes()).describedAs("one income expected").hasSize(1);
		assertThat(monthIncomes.getMonth()).describedAs("bot same month").isEqualTo(monthExpenses.getMonth());
	}

	@Test
	public void givenMoreLinesWithDiffrentCategoriesWillCollectThem() {
		// given 
		InputStream stream = makeCSVFileWith(
				"26-09-2018;Type One;;-200,00;0",
				"26-09-2018;Type Two;;-100,00;0",
				"26-10-2018;Type One;;-20,00;0",
				"26-10-2018;Type Two;;-10,00;0",
				"26-09-2018;Income One;;220,00;0",
				"26-10-2018;Income Two;;110,00;0");
		
		// then
		List<Category> categories = budgetService.makeBudget(stream).getCategories();
		
		// when 
		assertThat(categories).describedAs("to many categories").hasSize(3);
		assertThat(categories).describedAs("wrong category name").contains(Category.builder()
				.name("Type One")
				.build());
		assertThat(categories).describedAs("wrong category name").contains(Category.builder()
				.name("Type Two")
				.build());
		assertThat(categories).describedAs("wrong category name").contains(Category.builder()
				.name(Income.INDCOME)
				.build());
	}

	private ByteArrayInputStream makeCSVFileWith(String... lines) {
		return new ByteArrayInputStream(
				(CSV_HEADER_LINE + Arrays.stream(lines)
					.map(s -> "\n" + s)
					.collect(Collectors.joining()))
				.getBytes());
	}
	
}

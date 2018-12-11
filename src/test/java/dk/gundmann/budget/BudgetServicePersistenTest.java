package dk.gundmann.budget;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class BudgetServicePersistenTest {

    @Autowired
    private TestEntityManager entityManager;
	
    @Autowired
    private BudgetRepository budgetRepository;
    
	private BudgetService budgetService;
	
	
	@Before
	public void setUp() {
		budgetService = new BudgetService(budgetRepository);
	}

	@Test
	public void verifyThatABudgetIsSaved() {
		// given
		Budget budget = Budget.builder().name("test").build();
		
		// when 
		budgetService.save(budget);
		
		// then
		assertThat(entityManager.find(Budget.class, "test")).isNotNull();
	}
	
	@Test
	public void verifyThatABudgetAndCategoriesIsSaved() {
		// given
		Budget budget = Budget.builder().name("test")
				.categories(newArrayList(Category.builder().build()))
				.build();
		
		// when 
		budgetService.save(budget);
		
		// then
		Budget actual = entityManager.find(Budget.class, "test");
		assertThat(actual.getCategories()).hasSize(1);
	}

	@Test
	public void verifyThatABudgetAndMonthIsSaved() {
		// given
		Budget budget = Budget.builder().name("test")
				.categories(newArrayList(Category.builder()
						.months(newArrayList(Month.builder().build()))
						.build()))
				.build();
		
		// when 
		budgetService.save(budget);
		
		// then
		Category actual = entityManager.find(Budget.class, "test").getCategories().get(0);
		assertThat(actual.getMonths()).hasSize(1);
	}

	@Test
	public void verifyThatABudgetAndExpenceAndIncomeIsSaved() {
		// given
		Budget budget = Budget.builder().name("test")
				.categories(newArrayList(Category.builder()
						.months(newArrayList(Month.builder()
								.expenses(newArrayList(Expense.builder().build()))
								.incomes(newArrayList(Income.builder().build()))
								.build()))
						.build()))
				.build();
		
		// when 
		budgetService.save(budget);
		
		// then
		Month actual = entityManager.find(Budget.class, "test").getCategories().get(0).getMonths().get(0);
		assertThat(actual.getExpenses()).hasSize(1);
		assertThat(actual.getIncomes()).hasSize(1);
	}
	
}

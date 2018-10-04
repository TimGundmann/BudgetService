package dk.gundmann.budget;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode(of = "month")
@ToString
public class Month {

	private int month;
	
	@Builder.Default
	private List<Expense> expenses = newArrayList();
	
	@Builder.Default
	private List<Income> incomes = newArrayList();

	public double getTotalExpenses() {
		return expenses.stream().mapToDouble(e -> e.getValue()).sum();
	}

	public double getTotalIncomes() {
		return incomes.stream().mapToDouble(m -> m.getValue()).sum();
	}
	
}

package dk.gundmann.budget;

import static com.google.common.collect.Lists.newArrayList;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
	private double balance;
	
	@Builder.Default
	private List<Expense> expenses = newArrayList();
	
	@Builder.Default
	private List<Income> incomes = newArrayList();

	public double getTotalExpenses() {
	    return new BigDecimal(this.expenses.stream().mapToDouble(e -> e.getValue()).sum())
	    		.setScale(2, RoundingMode.HALF_UP)
	    		.doubleValue();
	}

	public double getTotalIncome() {
		return new BigDecimal(this.incomes.stream().mapToDouble(e -> e.getValue()).sum())
				.setScale(2, RoundingMode.HALF_UP)
				.doubleValue();
	}
	
}

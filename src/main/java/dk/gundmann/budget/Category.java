package dk.gundmann.budget;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode(of = "name")
@ToString
public class Category {

	private String name;
	
	@Builder.Default
	private List<Month> months = newArrayList();

	public double getTotalExpenses() {
		return 0;
	}
	
	public boolean isIncome() {
		return Income.INDCOME.equals(getName());
	}
	
}

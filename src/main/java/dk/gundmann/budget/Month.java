package dk.gundmann.budget;

import static com.google.common.collect.Lists.newArrayList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode(of = "month")
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Month {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private int month;
	private double balance;
	
	@Builder.Default
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private List<Expense> expenses = newArrayList();
	
	@Builder.Default
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Income> incomes = newArrayList();

	@Transient
	public double getTotalExpenses() {
	    return new BigDecimal(this.expenses.stream().mapToDouble(e -> e.getValue()).sum())
	    		.setScale(2, RoundingMode.HALF_UP)
	    		.doubleValue();
	}

	@Transient
	public double getTotalIncome() {
		return new BigDecimal(this.incomes.stream().mapToDouble(e -> e.getValue()).sum())
				.setScale(2, RoundingMode.HALF_UP)
				.doubleValue();
	}
	
}

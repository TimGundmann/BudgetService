package dk.gundmann.budget;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Budget {

	private String name;
	@Builder.Default
	private List<Month> months = newArrayList();
	
	public double getTotalIncomes() {
		return months.stream().mapToDouble(m -> m.getTotalIncomes()).sum();
	}
	
	public double getTotalExpenses() {
		return months.stream().mapToDouble(m -> m.getTotalExpenses()).sum();
	}
	
}

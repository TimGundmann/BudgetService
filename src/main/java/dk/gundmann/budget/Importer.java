package dk.gundmann.budget;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class Importer {

	public static Budget importExpenses(InputStream inputStream) {
		try {
			return new Importer().importFrom(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BudgetException("Error importing expenses", e);
		}
	}

	public Budget importFrom(InputStream inputStream) throws IOException {
		return Budget.builder()
				.months(makeMonths(inputStream))
				.build();
	}

	private List<Month> makeMonths(InputStream inputStream) throws IOException {
		return createCsvReader(inputStream).readAll().stream()
				.collect(Collectors.groupingBy(s -> importMonth(s)))
				.entrySet()
				.stream()
				.map(importMonth())
				.collect(Collectors.toList());
	}

	private CSVReader createCsvReader(InputStream inputStream) {
		CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream))
				.withCSVParser(new CSVParserBuilder()
						.withSeparator(';')
						.withIgnoreLeadingWhiteSpace(true)
						.build())
				.withSkipLines(2)
				.build();
		return csvReader;
	}

	private Function<? super Entry<Month, List<String[]>>, ? extends Month> importMonth() {
		return e -> { 
			Month month = e.getKey();
			month.getExpenses().addAll(importExpenses(month, e.getValue())); 
			month.getIncomes().addAll(importIncome(month, e.getValue())); 
			return month; 
		};
	}

	private Month importMonth(String[] lines) {
		return Month.builder()
				.month(extractMonth(lines[0]))
				.build();
	}
	
	private List<Expense> importExpenses(Month month, List<String[]> lines) {
		return lines.stream()
				.filter(s -> isSameMonth(month, s) && isValueExpenses(s))
				.map(e -> Expense.builder()
							.value(extractValue(e))
							.build())
				.collect(Collectors.toList());
	}

	private Collection<? extends Income> importIncome(Month month, List<String[]> lines) {
		return lines.stream()
				.filter(s -> isSameMonth(month, s) && !isValueExpenses(s))
				.map(e -> Income.builder()
							.value(extractValue(e))
							.build())
				.collect(Collectors.toList());
	}

	private boolean isValueExpenses(String[] s) {
		return extractValue(s) < 0;
	}

	private double extractValue(String[] line) {
		try {
			return Double.parseDouble(line[3].replace(',', '.'));
		} catch (Exception e) {
			throw new BudgetException("error parsing line: " + line, e);
		}
	}

	private boolean isSameMonth(Month month, String[] s) {
		return extractMonth(s[0]) == month.getMonth();
	}

	private int extractMonth(String date) {
		try {
			return Integer.parseInt(date.substring(3, 5));
		} catch (Exception e) {
			throw new BudgetException("error parsing date: " + date, e);
		}
	}

}

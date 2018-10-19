package dk.gundmann.budget;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class Importer {

	public static final String INDCOME = "Indcome";

	private List<Category> categories = newArrayList();

	private CategoryFilter filter;

	public Importer(CategoryFilter filter) {
		this.filter = filter;
	}

	public static Budget importExpenses(InputStream inputStream, CategoryFilter filter) {
		try {
			return new Importer(filter).importFrom(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BudgetException("Error importing expenses", e);
		}
	}

	public Budget importFrom(InputStream inputStream) throws IOException {
		return Budget.builder().categories(newArrayList(makeCategories(inputStream))).build();
	}

	private Set<Category> makeCategories(InputStream inputStream) throws IOException {
		return createCsvReader(inputStream).readAll().stream().map(e -> createOrGetCategory(e))
				.collect(Collectors.toSet());
	}

	private CSVReader createCsvReader(InputStream inputStream) throws UnsupportedEncodingException {
		CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream, filter.encoding()))
				.withCSVParser(new CSVParserBuilder()
						.withSeparator(';')
						.withIgnoreLeadingWhiteSpace(true)
						.build())
				.withSkipLines(2)
				.build();
		return csvReader;
	}

	private Category createOrGetCategory(String[] line) {
		Category category = createCategory(line);
		if (categories.contains(category)) {
			category = categories.get(categories.indexOf(category));
		} else {
			categories.add(category);
		}
		return addMonth(category, line);
	}

	private Category createCategory(String[] line) {
		String name = INDCOME;
		if (isValueExpenses(line)) {
			name = this.filter.filter(line[1]);
		}
		return Category.builder()
				.name(name)
				.build();
	}

	private Category addMonth(Category category, String[] line) {
		Month month = Month.builder().month(extractMonth(line[0])).build();
		if (category.getMonths().contains(month)) {
			month = category.getMonths().get(category.getMonths().indexOf(month));
		} else {
			category.getMonths().add(month);
		}
		if (isValueExpenses(line)) {
			month.getExpenses().add(importExpenses(line));
		} else {
			month.getIncomes().add(importIncome(line));
		}
		return category;
	}

	private Expense importExpenses(String[] line) {
		return Expense.builder().value(extractValue(line)).build();
	}

	private Income importIncome(String[] line) {
		return Income.builder().value(extractValue(line)).build();
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

	private int extractMonth(String date) {
		try {
			return Integer.parseInt(date.substring(3, 5));
		} catch (Exception e) {
			throw new BudgetException("error parsing date: " + date, e);
		}
	}

}

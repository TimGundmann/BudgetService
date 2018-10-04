package dk.gundmann.budget;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

@Service
public class BudgetService {
	
	public Budget makeBudget(InputStream inputStream) {
		return Importer.importExpenses(inputStream);
	}

}

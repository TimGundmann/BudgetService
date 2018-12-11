package dk.gundmann.budget;

import java.io.InputStream;

import org.springframework.stereotype.Service;

@Service
public class BudgetService {
	
	private BudgetRepository repository;

	public BudgetService(BudgetRepository repository) {
		this.repository = repository;
		
	}
	
	public Budget makeBudget(InputStream inputStream) {
		return Importer.importExpenses(inputStream, new NorderCategoryFilter());
	}

	public void save(Budget budget) {
		repository.save(budget);
	}

	public void delete(Budget budget) {
		repository.delete(budget);		
	}

}

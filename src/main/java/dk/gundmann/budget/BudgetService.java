package dk.gundmann.budget;

import java.io.InputStream;

import org.springframework.stereotype.Service;

@Service
public class BudgetService {

	public Budget makeBudget(InputStream inputStream) {
		return Budget.builder().build();
	}

}

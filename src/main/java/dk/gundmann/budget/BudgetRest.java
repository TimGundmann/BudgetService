package dk.gundmann.budget;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/budget")
public class BudgetRest {
	
	private BudgetService budgetService;

	public BudgetRest(BudgetService budgetService) {
		this.budgetService = budgetService;
		
	}

	@PostMapping(path = "/make", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Budget make(@RequestParam("file") MultipartFile file) throws IOException {
		return budgetService.makeBudget(file.getInputStream());
	}
	
	
	
}

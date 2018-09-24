package dk.gundmann.budget;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BudgetRest {

	@RequestMapping("/budget")
	public String get() {
		return "Test";
	}
}

package dk.gundmann.budget;

import java.nio.charset.Charset;

public interface CategoryFilter {

	Charset encoding();
	
	String filter(String categoryName);
	
}

package dk.gundmann.budget;

import java.nio.charset.Charset;
import java.util.Arrays;

public class NorderCategoryFilter implements CategoryFilter {

	@Override
	public Charset encoding() {
		return Charset.forName("windows-1258");
	}

	@Override
	public String filter(String categoryName) {
		return removeTailingNumbers(categoryName);
	}

	private String removeTailingNumbers(String categoryName) {
		char[] chars = categoryName.toCharArray();
		if (chars.length > 0 && Character.isDigit(chars[chars.length - 1])) {
			for (int i = chars.length - 2; i > -1; i--) {
				if (!Character.isDigit(chars[i])) {
					return String.copyValueOf(Arrays.copyOfRange(chars, 0, i));
				}
			}
		}
		return categoryName;
	}

}

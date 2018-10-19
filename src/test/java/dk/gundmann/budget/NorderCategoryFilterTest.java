package dk.gundmann.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.Test;

public class NorderCategoryFilterTest {

	private NorderCategoryFilter filter = new NorderCategoryFilter();
	
	@Test
	public void verifyThatTheLastDigitesAreRemoved() throws Exception {
		// given when then
		assertThat(filter.filter("test 0234")).isEqualTo("test");
	}
	
	@Test
	public void givenNoDigitsWillNotRemoveAny() throws Exception {
		// given when then
		assertThat(filter.filter("test")).isEqualTo("test");
	}

}

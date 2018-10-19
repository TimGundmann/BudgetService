package dk.gundmann.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.Test;

public class NorderCategoryFilterTest {

	private NorderCategoryFilter filter = new NorderCategoryFilter();
	
	@Test
	public void verifyThatTheLastFDigitesAreRemoved() throws Exception {
		// given when then
		assertThat(filter.filter("test 0234")).isEqualTo("test");
	}
}

package dk.gundmann.budget;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Budget {

	private String name;
	@Builder.Default
	private List<Category> categories = newArrayList();
	
}

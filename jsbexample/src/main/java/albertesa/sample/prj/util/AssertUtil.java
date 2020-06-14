package albertesa.sample.prj.util;

import org.springframework.util.Assert;

public class AssertUtil {

	public static void assertValidRequestId(String id) throws IllegalArgumentException {
		Assert.notNull(id, "ID must be a valid not null string");
		Assert.hasLength(id, "ID must not be empty");
		Assert.isTrue(!id.equalsIgnoreCase("null"), "ID must be a valid not null string");
	}
}

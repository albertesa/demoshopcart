package albertesa.sample.prj.util;

import org.owasp.html.Sanitizers;

public class SanitizerUtil {
	public static String sanitize(String input) {
		return Sanitizers.FORMATTING.sanitize(input);
	}
}

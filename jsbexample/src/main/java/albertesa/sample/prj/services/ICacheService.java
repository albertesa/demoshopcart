package albertesa.sample.prj.services;

import java.util.Optional;

public interface ICacheService {

	Optional<String> getValue(String key);

	void setValue(String key, String value);

}
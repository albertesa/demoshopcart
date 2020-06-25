package albertesa.sample.prj.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheService implements ICacheService {
	
	private StringRedisTemplate redisTemplate;

	@Autowired
	public CacheService(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@Override
	public Optional<String> getValue(String key) {
		String cachedVal = redisTemplate.opsForValue().get(key);
		return Optional.ofNullable(cachedVal);
	}
	
	@Override
	public void setValue(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

}

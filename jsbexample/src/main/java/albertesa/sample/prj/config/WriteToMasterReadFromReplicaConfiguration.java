package albertesa.sample.prj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import io.lettuce.core.ReadFrom;

@Configuration
class WriteToMasterReadFromReplicaConfiguration {

	private String redisHost;
	private int redisPort;
	private String redisPasswd;

	@Autowired
	public WriteToMasterReadFromReplicaConfiguration(
			@Value("#{environment.REDIS_HOST}") String redisHost,
			@Value("#{environment.REDIS_PORT}") int redisPort,
			@Value("#{environment.REDIS_PASSWD}") String redisPasswd) {
		super();
		this.redisHost = redisHost;
		this.redisPort = redisPort;
		this.redisPasswd = redisPasswd;
	}

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {

		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
				.readFrom(ReadFrom.REPLICA_PREFERRED)
				.build();

		RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(
				redisHost, redisPort);
		serverConfig.setPassword(redisPasswd);
		return new LettuceConnectionFactory(serverConfig, clientConfig);
	}
}

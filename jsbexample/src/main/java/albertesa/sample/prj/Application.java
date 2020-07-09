package albertesa.sample.prj;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import ch.qos.logback.core.util.StatusPrinter;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);
		LoggerContext loggerContext = ((ch.qos.logback.classic.Logger)logger).getLoggerContext();
        URL mainURL = ConfigurationWatchListUtil.getMainWatchURL(loggerContext);
        System.out.println(mainURL);
        // or even
        logger.info("Logback used '{}' as the configuration file.", mainURL);
        
		/*
		 * LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		 * 
		 * try { JoranConfigurator configurator = new JoranConfigurator();
		 * configurator.setContext(context); // Call context.reset() to clear any
		 * previous configuration, e.g. default // configuration. For multi-step
		 * configuration, omit calling context.reset(). context.reset();
		 * configurator.doConfigure(args[0]); } catch (JoranException je) { //
		 * StatusPrinter will handle this }
		 * StatusPrinter.printInCaseOfErrorsOrWarnings(context);
		 */
	}

}
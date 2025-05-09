package cz.vojtechsika.wiki_transformer;

import cz.vojtechsika.wiki_transformer.cli.WikiTransformerCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import picocli.CommandLine;

@SpringBootApplication
public class WikiTransformerApplication {


	public static void main(String[] args) {
		SpringApplication.run(WikiTransformerApplication.class, args);
	}


	@Bean
	public CommandLineRunner commandLineRunner(WikiTransformerCommand wikiTransformerCommand) {

		return args -> {
			CommandLine commandLine = new CommandLine(wikiTransformerCommand);
			int exitCode = commandLine.execute(args);
			System.exit(exitCode);

		};
	}

}

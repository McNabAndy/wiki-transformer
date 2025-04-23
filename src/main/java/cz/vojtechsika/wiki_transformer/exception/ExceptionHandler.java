package cz.vojtechsika.wiki_transformer.exception;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandler {

    public void exitWithError(String userMessage, Exception e) {
        System.out.println("Error: " + userMessage);
        System.out.println("Detail: " + e.getMessage());
        System.exit(1);
    }

    // přetížená metoda abych mohl použít k ukončení když například mi chyba programu nevyvovolá vyjímku například pokud bude prázdné URL
    public void exitWithError(String userMessage) {
        System.out.println("Error: " + userMessage);
        System.exit(1);
    }



}

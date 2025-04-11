package karate;
public class KarateTests {
    
    @Karate.Test
    Karate testAll() {
        return Karate.run("classpath:karate/card-controller.feature");
    }
}

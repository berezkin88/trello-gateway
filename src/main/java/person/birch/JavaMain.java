package person.birch;

import io.quarkus.runtime.Quarkus;

//@QuarkusMain
public class JavaMain {
    public static void main(String... args) {
        Quarkus.run(App.class, args);
    }
}

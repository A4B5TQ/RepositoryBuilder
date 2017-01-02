package repository.builder.lib;

import org.springframework.boot.SpringApplication;
import repository.builder.lib.builderFactory.BuilderFactory;
import repository.builder.lib.builderFactory.BuilderFactoryImpl;
import repository.builder.lib.builders.interfaces.Builder;
import repository.builder.lib.enums.Strategy;

public class ANSRBuilder {

    private static BuilderFactory builderFactory;
    private static Builder builder;

    static {
        builderFactory = new BuilderFactoryImpl();
    }

    public static void run(Strategy strategy, Class clazz, String... args) {
        builder = createBuilder (null,strategy);
        build();
        springRun(clazz, args);
    }

    public static void run(String repositoryPostfix, Strategy strategy, Class clazz, String... args) {
        builder = createBuilder(repositoryPostfix, strategy);
        build();
        springRun(clazz, args);
    }

    private static void springRun(Class clazz, String... args) {
        SpringApplication.run(clazz, args);
    }

    private static Builder createBuilder(String repositoryPostfix, Strategy strategy) {
        return builderFactory.createBuilder(repositoryPostfix,strategy);
    }

    private static void build() {
        if (builder != null) {
            builder.build();
        }
    }
}

package repository.builder.lib;

import repository.builder.lib.builderFactory.BuilderFactory;
import repository.builder.lib.builderFactory.BuilderFactoryImpl;
import repository.builder.lib.builders.interfaces.Builder;
import repository.builder.lib.enums.BuilderStrategy;

public class ANSRBuilder {

    private static BuilderFactory builderFactory;
    private static Builder builder;

    static {
        builderFactory = new BuilderFactoryImpl();
    }

    public static void build(BuilderStrategy strategy) {
        builder = builderFactory.createBuilder(strategy);
        builder.build();
    }

    public static void build(String repositoryPostfix, BuilderStrategy strategy) {
        builder = builderFactory.createBuilder(repositoryPostfix,strategy);
        builder.build();
    }
}

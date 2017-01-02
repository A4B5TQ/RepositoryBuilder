package repository.builder.lib.builderFactory;

import repository.builder.lib.builders.implementations.BuilderImpl;
import repository.builder.lib.builders.implementations.RepositoryBuilder;
import repository.builder.lib.builders.implementations.ServiceBuilderImpl;
import repository.builder.lib.builders.interfaces.Builder;
import repository.builder.lib.enums.interfaces.Strategy;

public class BuilderFactoryImpl implements BuilderFactory {

    private static final String EMPTY_POSTFIX = "";

    private RepositoryBuilder repositoryBuilder;
    private ServiceBuilderImpl serviceBuilder;

    @Override
    public Builder createBuilder(String repositoryPostfix, Strategy strategy) {

        Builder builder = this.getBuilder(strategy, repositoryPostfix);
        if (builder != null) {
            return builder;
        }
        return null;
    }

    private Builder getBuilder(Strategy currentStrategy, String postfix) {

        String strategy = currentStrategy.getStrategy();

        switch (strategy) {
            case "REPOSITORIES":

                this.repositoryBuilder = new RepositoryBuilder(postfix,false);
                return new BuilderImpl(repositoryBuilder, null);

            case "REPOSITORIES_AND_SERVICES":

                this.repositoryBuilder = new RepositoryBuilder(postfix,false);
                this.serviceBuilder = new ServiceBuilderImpl();

                return new BuilderImpl(repositoryBuilder, serviceBuilder);
            case "REPOSITORIES_WITH_METHODS":
                this.repositoryBuilder = new RepositoryBuilder(postfix,true);
                return new BuilderImpl(repositoryBuilder,null);
            case "REPOSITORIES_WITH_METHODS_AND_SERVICES":
                this.repositoryBuilder = new RepositoryBuilder(postfix,true);
                this.serviceBuilder = new ServiceBuilderImpl();

                return new BuilderImpl(repositoryBuilder, serviceBuilder);
            case "NONE":
                return new BuilderImpl(null, null);
        }
        return null;
    }
}

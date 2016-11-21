package repository.builder.lib.builderFactory;

import repository.builder.lib.builders.implementations.BuilderImpl;
import repository.builder.lib.builders.implementations.RepositoryBuilder;
import repository.builder.lib.builders.implementations.ServiceBuilder;
import repository.builder.lib.builders.interfaces.Builder;
import repository.builder.lib.enums.BuilderStrategy;

public class BuilderFactoryImpl implements BuilderFactory {

    private RepositoryBuilder repositoryBuilder;
    private ServiceBuilder serviceBuilder;

    @Override
    public Builder createBuilder(BuilderStrategy strategy) {

        switch (strategy) {
            case REPOSITORY:
                this.repositoryBuilder = new RepositoryBuilder("");
                return new BuilderImpl(repositoryBuilder,null);
            case REPOSITORY_AND_SERVICES:
                this.repositoryBuilder = new RepositoryBuilder("");
                this.serviceBuilder = new ServiceBuilder();
                return new BuilderImpl(repositoryBuilder,serviceBuilder);
        }
        return null;
    }

    @Override
    public Builder createBuilder(String repositoryPostfix, BuilderStrategy strategy) {

        switch (strategy) {
            case REPOSITORY:
                this.repositoryBuilder = new RepositoryBuilder(repositoryPostfix);
                return new BuilderImpl(repositoryBuilder,null);
            case REPOSITORY_AND_SERVICES:
                this.repositoryBuilder = new RepositoryBuilder(repositoryPostfix);
                this.serviceBuilder = new ServiceBuilder();
                return new BuilderImpl(repositoryBuilder,serviceBuilder);
        }
        return null;
    }
}

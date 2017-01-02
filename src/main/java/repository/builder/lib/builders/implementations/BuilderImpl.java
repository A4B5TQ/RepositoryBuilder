package repository.builder.lib.builders.implementations;

import repository.builder.lib.builders.interfaces.Builder;

public class BuilderImpl implements Builder {

    private RepositoryBuilder repositoryBuilder;
    private ServiceBuilderImpl serviceBuilder;

    public BuilderImpl(RepositoryBuilder repositoryBuilder, ServiceBuilderImpl serviceBuilder) {
        this.repositoryBuilder = repositoryBuilder;
        this.serviceBuilder = serviceBuilder;
    }

    @Override
    public void build() {
        if (repositoryBuilder != null && serviceBuilder != null) {
            this.repositoryBuilder.build();
            this.serviceBuilder.build();
        } else if (repositoryBuilder != null){
            this.repositoryBuilder.build();
        }
    }
}

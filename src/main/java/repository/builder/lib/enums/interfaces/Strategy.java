package repository.builder.lib.enums.interfaces;

import repository.builder.lib.enums.BuilderStrategy;

public interface Strategy {

    String getStrategy();

    static Strategy NONE() {
        return BuilderStrategy.NONE;
    }

    static Strategy REPOSITORIES() {
        return BuilderStrategy.REPOSITORIES;
    }

    static Strategy REPOSITORIES_AND_SERVICES() {
        return BuilderStrategy.REPOSITORIES_AND_SERVICES;
    }

    static Strategy REPOSITORIES_WITH_METHODS_AND_SERVICES() {
        return BuilderStrategy.REPOSITORIES_WITH_METHODS_AND_SERVICES;
    }

    static Strategy REPOSITORIES_WITH_METHODS() {
        return BuilderStrategy.REPOSITORIES_WITH_METHODS;
    }
}

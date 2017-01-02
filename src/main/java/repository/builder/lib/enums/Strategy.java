package repository.builder.lib.enums;

public interface Strategy {

    String getStrategy();

    static Strategy NONE() {
        return BuilderStrategy.NONE;
    }

    static Strategy REPOSITORY() {
        return BuilderStrategy.REPOSITORY;
    }

    static Strategy REPOSITORY_AND_SERVICES() {
        return BuilderStrategy.REPOSITORY_AND_SERVICES;
    }
}

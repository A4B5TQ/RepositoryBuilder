package repository.builder.lib.enums;

public enum BuilderStrategy implements Strategy {

    REPOSITORY {
        @Override
        public String getStrategy() {
            return "REPOSITORY";
        }
    },
    REPOSITORY_AND_SERVICES {
        @Override
        public String getStrategy() {
            return "REPOSITORY_AND_SERVICES";
        }
    },
    NONE {
        @Override
        public String getStrategy() {
            return "NONE";
        }
    },
}

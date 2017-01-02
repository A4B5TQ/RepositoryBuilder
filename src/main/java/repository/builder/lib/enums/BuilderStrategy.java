package repository.builder.lib.enums;

import repository.builder.lib.enums.interfaces.Strategy;

public enum BuilderStrategy implements Strategy {

    REPOSITORIES {
        @Override
        public String getStrategy() {
            return "REPOSITORIES";
        }
    },
    REPOSITORIES_WITH_METHODS {
        @Override
        public String getStrategy() {
            return "REPOSITORIES_WITH_METHODS";
        }
    },
    REPOSITORIES_AND_SERVICES {
        @Override
        public String getStrategy() {
            return "REPOSITORIES_AND_SERVICES";
        }
    },
    REPOSITORIES_WITH_METHODS_AND_SERVICES {
        @Override
        public String getStrategy() {
            return "REPOSITORIES_WITH_METHODS_AND_SERVICES";
        }
    },
    NONE {
        @Override
        public String getStrategy() {
            return "NONE";
        }
    },
}

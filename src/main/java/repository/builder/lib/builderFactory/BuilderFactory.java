package repository.builder.lib.builderFactory;

import repository.builder.lib.builders.interfaces.Builder;
import repository.builder.lib.enums.interfaces.Strategy;

public interface BuilderFactory {

   Builder createBuilder(String repositoryPostfix, Strategy strategy);

}

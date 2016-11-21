package repository.builder.lib.builderFactory;

import repository.builder.lib.builders.interfaces.Builder;
import repository.builder.lib.enums.BuilderStrategy;

public interface BuilderFactory {

   Builder createBuilder(BuilderStrategy strategy);

   Builder createBuilder(String repositoryPostfix, BuilderStrategy strategy);

}

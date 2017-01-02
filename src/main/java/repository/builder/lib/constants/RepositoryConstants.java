package repository.builder.lib.constants;

public class RepositoryConstants {

    public final static String REPOSITORY_DIRECTORY_NAME = "repositories";
    public final static String REPOSITORY_IMPORT = "import org.springframework.stereotype.Repository;";
    public final static String JPA_IMPORT = "import org.springframework.data.jpa.repository.JpaRepository;";
    public final static String REPOSITORY_ANNOTATION = "@Repository";
    public final static String REPOSITORY_INTERFACE_NAME = "public interface %s extends JpaRepository<%s,%s> {";
    public final static String REPOSITORY_NAME = "Repository";
    public final static String COLLECTION_RETURN_TYPE = "\tIterable<%s>";
}

package repository.builder.lib.constants;

public class ServiceConstants {

    public final static String SERVICE_DIRECTORY_NAME = "services";
    public final static String SERVICE_ANNOTATION = "@Service";
    public final static String SERVICE_INTERFACE_NAME = "public interface %s {";
    public final static String SERVICE_CLASS_NAME = "public class %s implements %s {";
    public final static String SERVICE_NAME = "Service";
    public final static String SERVICE_ANNOTATIONS_IMPORT = "import org.springframework.beans.factory.annotation.Autowired;\n" +
            "import org.springframework.stereotype.Service;\n" +
            "import org.springframework.transaction.annotation.Transactional;";
    public final static String SERVICE_NAME_IMPL = "Impl";
    public final static String SERVICE_REPOSITORY_FINAL = "\tprivate final %s %s;";
    public final static String SERVICE_CONSTRUCTOR = "\tpublic %s(%s %s) { %n" +
            "\t\t%s = %s;%n" +
            "\t}";
}

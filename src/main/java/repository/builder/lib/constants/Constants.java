package repository.builder.lib.constants;

public class Constants {
    public final static String SOURCE_CODE_PATH = "/src/main/java";
    public final static String PROJECT_PATH = System.getProperty("user.dir");
    public final static String SOURCE_PATH = PROJECT_PATH + SOURCE_CODE_PATH;
    public final static String CLOSE_BRACKET = "}";
    public final static String OPEN_BRACKET = "{";
    public final static String PACKAGE = "package ";
    public final static String TRANSACTIONAL_ANNOTATION = "@Transactional";
    public final static String AUTOWIRED_ANNOTATION = "\t@Autowired";
    public final static String METHOD_SUFFIX_COLLECTION_PARAM = "(Collection<%s> %s);";
    public final static String METHOD_SUFFIX_TYPE_PARAM = "(%s %s);";
    public final static String METHOD_SUFFIX_WITHOUT_PARAM = "();";
    public static String MAIN_PATH = "";
}

package repository.builder.lib.constants;

/**
 * Keywords inside method names
 * for creating a query using the JPA criteria API.
 */
public class MethodConstants {

    public final static String FIND_BY = "findBy";

    public final static String AND = "And";
    public final static String OR = "Or";
    public final static String IS = "Is";
    public final static String EQUALS = "Equals";
    public final static String BETWEEN = "Between";
    public final static String LESS_THAN = "LessThan";
    public final static String LESS_THAN_EQUAL = "LessThanEqual";
    public final static String GREATER_THAN = "GreaterThan";
    public final static String GREATER_THAN_EQUAL = "GreaterThanEqual";
    public final static String AFTER = "After";
    public final static String BEFORE = "Before";
    public final static String IS_NULL = "IsNull";
    public final static String IS_NOT_NULL = "IsNotNull";
    public final static String NOT_NULL = "NotNull";
    public final static String LIKE = "Like";
    public final static String NOT_LIKE = "NotLike";
    public final static String STARTING_WITH = "StartingWith";
    public final static String ENDING_WITH = "EndingWith";
    public final static String CONTAINING = "Containing";
    public final static String ORDER_BY = "OrderBy";
    public final static String NOT = "Not";
    public final static String IN = "In";
    public final static String NOT_IN = "NotIn";
    public final static String TRUE = "True";
    public final static String FALSE = "False";
    public final static String IGNORE_CASE = "IgnoreCase";

    public final static String METHOD_SUFFIX_COLLECTION_PARAM = "(Collection<%s> %s);";
    public final static String METHOD_SUFFIX_TYPE_PARAM = "(%s %s);";
    public final static String METHOD_SUFFIX_WITHOUT_PARAM = "();";
}

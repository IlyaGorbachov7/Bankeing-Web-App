package by.epam.baranovsky.banking.entity.criteria;

import by.epam.baranovsky.banking.dao.query.Query;
import lombok.Data;
import lombok.Getter;

import java.util.*;

/**
 * Criteria class that is used to retrieve DB entities by parameters.
 * <p>
 *     Generates JDBC SQL prepared statement (query) based on parameters
 *     and on string constant that defines the algorithm of generation.
 *     Criteria can generate query where a retrieved object must
 *     satisfy all the parameters listed or one of parameters listed.
 * </p>
 * <p>
 *     However, if criteria has to accept multiple values for same DB column,
 *     an entity will have to satisfy only one of such values.
 * </p>
 *
 * @param <T> the type of enum that serves as a source of entity's parameters,
 *          so criteria won't include parameters for different entities.
 * @see EntityEnum
 * @author Baranovsky E. K.
 * @version 1.0.0
 */
public class Criteria<T extends EntityEnum> {

    /**
     * Constant to enable 'satisfy all' algorithm.
     */
    public static final String SQL_AND = "AND";
    /**
     * Constant to enable 'satisfy one' algorithm.
     */
    public static final String SQL_OR = "OR";
    /**
     * List of parameters of criteria.
     * @see Parameter
     */
    private final List<Parameter> parameters = new ArrayList<>();
    /**
     * A particle that will be included in a query
     * between parameters.
     */
    private final String particle;

    public Criteria(){
        this(SQL_AND);
    }

    public Criteria(String sqlParticle){
        if(sqlParticle.equals(SQL_AND) || sqlParticle.equals(SQL_OR)){
            this.particle=sqlParticle;
        }
        else this.particle=SQL_AND;
    }

    /**
     * Adds a parameter to a criteria.
     * Creates an instance of Parameter with given name and value.
     *
     * @param name Name of parameter retrieved from corresponding EntityEnum.
     * @param value Value of a parameter.
     * @return {@code false} if a parameter with same name and value already
     * exists within criteria, {@code true} otherwise.
     * @see CriteriaValue
     */
    public boolean add(T name, CriteriaValue<?> value){
        Parameter parameterToAdd = new Parameter(name, value);

        if(parameters.contains(parameterToAdd)){
            return false;
        }

        parameters.add(parameterToAdd);

        return true;
    }

    /**
     * Generates SQL query for a criteria.
     * <p>
     *     In fact,appends generated conditions to a SQL query.
     *     Trying to retrieve anything using a criteria
     *     without parameters will give no results.
     * </p>
     * <p>
     *     Generation algorithm:
     *     <ul>
     *         <li>Divides list of parameters into groups of eponymous parameters.</li>
     *         <li>For each parameter in each group calls for generateSqlCondition() method
     *         to generate single condition string.</li>
     *         <li>Puts values of parameters into a list.</li>
     *         <li>Puts 'OR' between parameters in group of there are more than one.</li>
     *         <li>Unites all groups into a single condition string, putting @{code particle}
     *         between groups.</li>
     *         <li>Appends this string to a starting query.</li>
     *         <li>Creates an instance of Query using generated query string and list of parameters.</li>
     *     </ul>
     * </p>
     * @see Parameter#generateSqlCondition()
     * @param sqlQueryStart A query to append generated parameters to.
     * @return Instance of Query with a prepared statement
     * and an array of parameters for that statement in correct order.
     */
    public Query generateQuery(String sqlQueryStart){

        StringBuilder builder = new StringBuilder(sqlQueryStart);
        builder.append(" WHERE ");

        if(parameters.isEmpty()){
            return new Query(builder.append("FALSE").toString());
        }

        Map<T, List<Parameter>> eponymousGroups = new HashMap<>();
        for(Parameter parameter : parameters){
            eponymousGroups.computeIfAbsent(parameter.name, k -> new ArrayList<>());
            eponymousGroups.get(parameter.name).add(parameter);
        }

        List<List<Parameter>> groups = eponymousGroups.values().stream().toList();
        List<Object> queryParams = new ArrayList<>();

        for(List<Parameter> group : groups){
            builder.append("(").append(group.get(0).generateSqlCondition());
            queryParams.addAll(group.get(0).getValue().getAllValues());

            for(int i = 1; i<group.size(); i++){
                builder.append(" OR ").append(group.get(i).generateSqlCondition());
                queryParams.addAll(group.get(i).getValue().getAllValues());
            }

            builder.append(" ) ").append(particle).append(" ");
        }

        if (SQL_OR.equals(particle)) {
            builder.append("FALSE");
        } else {
            builder.append("TRUE");
        }
        return new Query(builder.toString(), queryParams.toArray());

    }

    /**
     * Inner class that represents a parameter of a criteria.
     * @author Baranovsky E. K.
     * @version 1.0.0
     */
    @Data
    public class Parameter{
        /**
         * Name of this parameter: instance of EntityEnum.
         */
        private T name;
        /**
         * Value of this parameter: instance of CriteriaValue.
         * @see CriteriaValue
         */
        private CriteriaValue<?> value;

        public Parameter(T name, CriteriaValue<?> value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Generates single SQL condition depending on what kind of value is passed.
         * @return sql-style condition with given column name
         * and values replaced by question marks.
         */
        public String generateSqlCondition(){
            return value.generateSqlCondition(name);
        }
    }
}

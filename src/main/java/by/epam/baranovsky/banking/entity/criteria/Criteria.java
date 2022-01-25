package by.epam.baranovsky.banking.entity.criteria;

import by.epam.baranovsky.banking.dao.query.Query;
import lombok.Data;

import java.util.*;

public class Criteria<T extends EntityEnum> {

    public static final String SQL_AND = "AND";
    public static final String SQL_OR = "OR";
    private final List<Parameter> parameters = new ArrayList<>();
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

    public boolean add(T name, CriteriaValue<?> value){
        Parameter parameterToAdd = new Parameter(name, value);

        if(parameters.contains(parameterToAdd)){
            return false;
        }

        parameters.add(parameterToAdd);

        return true;
    }

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
            return new Query(builder.append("FALSE").toString(), queryParams.toArray());
        }
        return new Query(builder.append("TRUE").toString(), queryParams.toArray());

    }

    @Data
    private class Parameter{
        private T name;
        private CriteriaValue<?> value;

        public Parameter(T name, CriteriaValue<?> value) {
            this.name = name;
            this.value = value;
        }

        public String generateSqlCondition(){
            StringBuilder builder = new StringBuilder(" (");

            if(value instanceof Range){
                builder.append(name.getColumn())
                        .append(">=?");
                builder.append(" AND ");
                builder.append(name.getColumn())
                        .append("<=?");
            } else if (value instanceof SingularValue){
                builder.append(name.getColumn())
                        .append("=?");
            }

            builder.append(")");

            return builder.toString();
        }
    }
}

package by.epam.baranovsky.banking.entity.criteria;

import by.epam.baranovsky.banking.dao.query.Query;
import lombok.Data;

import java.util.*;

public class Criteria<T extends EntityEnum> {

    private final List<Parameter> parameters = new ArrayList<>();

    public Criteria() {}

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
            return new Query(builder.append("TRUE").toString());
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

            builder.append(" ) AND ");
        }

        return new Query(builder.append("TRUE").toString(), queryParams.toArray());
    }

    /**
     * Type is reflected by letters: E - eponymous to current, N - not such
     * (current elem is in center)
     * Scheme: | prev | curr | next |
     * 0 - |E|C|E| or |N|C|N|
     * 1 - |E|C|N|
     * 2 - |N|E|E|
     * @param prev
     * @param currentPos
     * @param next
     * @return
     */
    private int checkPosition(int prev, int currentPos, int next){

        Parameter prevParam = parameters.get(prev);
        Parameter currParam = parameters.get(currentPos);
        Parameter nextParam = parameters.get(next);

        if((!areEponymous(currParam, nextParam) && !areEponymous(currParam, prevParam))
                || (areEponymous(currParam,nextParam) && areEponymous(currParam, prevParam))){
            return 0;
        } else if(areEponymous(currParam, prevParam)){
            return 1;
        } else return 2;

    }

    private boolean areEponymous(Parameter p1, Parameter p2){
        return p1.name.equals(p2.name);
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

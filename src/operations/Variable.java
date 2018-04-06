package operations;

import java.util.Objects;

public class Variable extends Expression {
    private String name;
    private Integer hash = null;

    public Variable(String na) {
        name = na;
    }

    @Override
    public String toTree() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected void addDescription(StringBuilder builder) {
        builder.append(name);
    }

    @Override
    public int getChildrenCount() {
        return 0;
    }

    @Override
    public String getSymbol() {
        return "Variable";
    }

    @Override
    public Expression[] getChildren() {
        return new Expression[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || o.hashCode() != hashCode()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(name, variable.name);
    }

    @Override
    public int hashCode() {
        if (hash != null) {
            return hash;
        }
        return hash = Objects.hash(getSymbol(), name);
    }
}

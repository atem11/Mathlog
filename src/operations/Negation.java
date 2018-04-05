package operations;

public class Negation extends Expression {
    private Expression negated;

    public Negation(Expression ex) {
        negated = ex;
    }

    public Expression get() {
        return negated;
    }

    @Override
    public String toTree() {
        return "(" + getSymbol() + negated.toTree() + ")";
    }

    @Override
    protected void addDescription(StringBuilder builder) {
        builder.append("(");
        builder.append(getSymbol());
        negated.addDescription(builder);
        builder.append(")");
    }

    @Override
    public int getChildrenCount() {
        return 1;
    }

    @Override
    public String getSymbol() {
        return "!";
    }

    @Override
    public Expression[] getChildren() {
        return new Expression[]{negated};
    }
}

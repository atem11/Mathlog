package operations;

public class Variable extends Expression {
    private String name;

    public Variable(String na) {
        name = na;
    }

    @Override
    public String toTree() {
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
}

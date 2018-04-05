package operations;

import java.util.Objects;

public abstract class BinOperator extends Expression {
    private Expression left;
    private Expression right;

    public BinOperator(Expression l, Expression r) {
        left = l;
        right = r;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public String toTree() {
        return "(" + getSymbol() + "," + left.toTree() + "," + right.toTree() +")";
    }

    @Override
    public int getChildrenCount() {
        return 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || o.hashCode() != hashCode()) return false;
        BinOperator that = (BinOperator) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(right, that.right);
    }

    @Override
    public Expression[] getChildren() {
        return new Expression[]{left, right};
    }

    @Override
    protected void addDescription(StringBuilder builder) {
        builder.append("(");
        left.addDescription(builder);
        builder.append(getSymbol());
        right.addDescription(builder);
        builder.append(")");
    }
}

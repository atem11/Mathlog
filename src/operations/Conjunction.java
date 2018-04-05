package operations;

public class Conjunction extends BinOperator {

    public Conjunction(Expression l, Expression r) {
        super(l, r);
    }

    @Override
    public String getSymbol() {
        return "&";
    }
}

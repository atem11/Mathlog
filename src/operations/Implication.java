package operations;

public class Implication extends BinOperator {

    public Implication(Expression l, Expression r) {
        super(l, r);
    }

    @Override
    public String getSymbol() {
        return "->";
    }
}

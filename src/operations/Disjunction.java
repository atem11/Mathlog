package operations;

public class Disjunction extends BinOperator {

    public Disjunction(Expression l, Expression r) {
        super(l, r);
    }

    @Override
    public String getSymbol() {
        return "|";
    }
}

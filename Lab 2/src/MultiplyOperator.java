

/**
 * Created by b on 20-Oct-16.
 */
public class MultiplyOperator extends Operator{
    @Override
    public int apply(int x, int y) {
        return x*y;
    }

    @Override
    public double apply(double x, double y) {
        return x*y;
    }
}



/**
 * Created by b on 20-Oct-16.
 */
public class AdditionOperator extends Operator {
    @Override
    public int apply(int x, int y) {
        return x+y;
    }

    @Override
    public double apply(double x, double y) {
        return x+y;
    }

    public ComplexNumber apply(ComplexNumber x,ComplexNumber y){
        double real = x.getReal() + y.getReal(), imaginary = x.getImaginary() + y.getImaginary();
        return new ComplexNumber(real,imaginary);
    }
}

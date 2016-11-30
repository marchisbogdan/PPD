/**
 * Created by b on 20-Oct-16.
 */
public class GenericAdditionOperator<T> extends GenericOperator<T>{

    @Override
    public <T extends Number> double apply(T x, T y) {
        return x.doubleValue() + y.doubleValue();
    }
}

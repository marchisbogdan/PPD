/**
 * Created by b on 20-Oct-16.
 */
public abstract class GenericOperator <T> {
    public abstract <T extends Number> double apply (T x, T y);
}

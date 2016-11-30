import java.util.Random;

/**
 * Created by b on 20-Oct-16.
 */
public class ComplexNumberMatrix extends Thread {
    private ComplexNumber[][] matrix;
    private int rows;
    private int columns;
    private Operator operator;

    ComplexNumberMatrix(int n,int m){
        this.matrix = new ComplexNumber[n][m];
        this.rows = n;
        this.columns = m;
        this.operator = new AdditionOperator();
    }

    ComplexNumberMatrix(ComplexNumber[][] c,int n,int m){
        this.matrix = c;
        this.rows = n;
        this.columns = m;
        this.operator = new AdditionOperator();
    }

    public void addition(ComplexNumberMatrix m,int i,int j){
        this.matrix[i][j].setReal(operator.apply(this.matrix[i][j].getReal(),m.matrix[i][j].getReal()));
        this.matrix[i][j].setImaginary(operator.apply(this.matrix[i][j].getImaginary(), m.matrix[i][j].getImaginary()));
    }

    public void parallel_addition(ComplexNumberMatrix m,int start_row,int end_row){
        for(int i = start_row; i<end_row; i++){
            for(int j = 0;j < this.columns;j++){
                this.matrix[i][j].setReal(operator.apply(this.matrix[i][j].getReal(),m.matrix[i][j].getReal()));
                this.matrix[i][j].setImaginary(operator.apply(this.matrix[i][j].getImaginary() , m.matrix[i][j].getImaginary()));
            }
        }
    }

    public static ComplexNumber[][] generateRandomMatrix(int n, int m){
        Random rand = new Random();
        ComplexNumber[][] gen_matrix = new ComplexNumber[n][m];
        double val = 0,rez1=0,rez2=0;
        for (int i=0;i<n;i++){
            for (int j=0; j<m ;j++){
                val = rand.nextDouble(); // Numbers from -100 to 99
                rez1 = -100 + (val *(100 - (-100)));

                val = rand.nextDouble(); // Numbers from -100 to 99
                rez2 = -100 + (val *(100 - (-100)));

                gen_matrix[i][j] = new ComplexNumber(rez1,rez2);
            }
        }
        return gen_matrix;
    }

    public void printMatrix(){
        for(int i=0;i<this.rows;i++){
            for(int j=0;j<this.columns;j++){
                if (this.matrix[i][j].getImaginary() < 0){
                    System.out.printf("(%.2f%.2fi)", this.matrix[i][j].getReal(),this.matrix[i][j].getImaginary());
                }else{
                    System.out.printf("(%.2f+%.2fi)", this.matrix[i][j].getReal(),this.matrix[i][j].getImaginary());
                }
            }
            System.out.println();
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void run(){

    }
}

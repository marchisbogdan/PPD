

import java.io.*;
import java.util.Random;

/**
 * Created by b on 08-Oct-16.
 */
public class Matrix extends Thread {
    private int[][] matrix;
    private int rows;
    private int columns;

    public Matrix (int n,int m){
        this.matrix = new int[n][m];
        this.rows = n;
        this.columns = m;
    }

    public static Matrix addition(Matrix m1, Matrix m2) {
        Matrix m = new Matrix(m1.rows, m1.columns);

        if (m1.rows != m2.rows && m1.columns != m2.columns) {
            System.out.println("Matricile au dimensiuni diferite!");
        } else {
            for (int i = 0; i < m1.rows; i++) {
                for (int j = 0; j < m1.columns; j++) {
                    m.matrix[i][j] = m1.matrix[i][j] + m2.matrix[i][j];
                }
            }
        }
        return m;
    }

    public void addition(Matrix m,int i,int j){
        //this.matrix[i][j] += m.matrix[i][j];
        Operator operator = new AdditionOperator();
        this.matrix[i][j] = operator.apply(this.matrix[i][j],m.matrix[i][j]);
    }

    public void parallel_addition(Matrix m1,int start_row,int end_row){
        Operator operator = new AdditionOperator();
        for(int i = start_row; i<end_row; i++){
            for(int j = 0;j < this.columns;j++){
                //this.matrix[i][j] += m1.matrix[i][j];
                this.matrix[i][j] = operator.apply(this.matrix[i][j],m1.matrix[i][j]);
            }
        }
    }

    public static int[][] generateRandomMatrix(int n, int m){
        Random rand = new Random();
        int[][] gen_matrix = new int[n][m];
        int val = 0;
        for (int i=0;i<n;i++){
            for (int j=0; j<m ;j++){
                val = rand.nextInt(100); // Numbers from 0 to 99
                gen_matrix[i][j] = val;
            }
        }
        return gen_matrix;
    }

    public static void writeGeneratedMatrixToFile(String path,int n,int m) throws IOException {
        int[][] gen_matrix = generateRandomMatrix(n,m);

        FileWriter writer = new FileWriter(path,false);
        PrintWriter print_line = new PrintWriter(writer);

        for (int i=0;i<n;i++){
            for (int j=0; j <m ;j++){
                print_line.println(gen_matrix[i][j]);
            }
        }
        print_line.close();
    }

    public void readMatrixFromFile(String path) throws IOException {
        FileReader reader = new FileReader(path);
        BufferedReader br = new BufferedReader(reader);
        String line;
        int value;
        for(int i=0;i<this.rows;i++){
            for(int j=0;j<this.columns;j++){
                line = br.readLine();
                value = Integer.parseInt(line);
                this.matrix[i][j] = value;
            }
        }
        System.out.println("matrix size:"+this.matrix.length);
        System.out.println("matrix size:"+this.matrix[0].length);
        br.close();
    }

    public void printMatrix(){
        for(int i=0;i<this.rows;i++){
            for(int j=0;j<this.columns;j++){
                System.out.printf("%d ",this.matrix[i][j]);
            }
            System.out.println();
        }
    }

    public static Matrix multiply(Matrix m1, Matrix m2) {
        Matrix m = new Matrix(m1.rows, m2.columns);

        for (int i = 0; i < m1.rows; i++) {
            for(int k = 0;k < m2.columns; k++){
                m.matrix[i][k]=0;
                for (int j = 0; j < m2.rows; j++) {
                    m.matrix[i][k] = m1.matrix[i][j] * m2.matrix[j][k] + m.matrix[i][k];
                }
            }
        }
        return m;
    }

    public void parallel_multiply(Matrix m1, Matrix m2,int start_row, int end_row){
        for (int i = start_row; i < end_row; i++) {
            for(int k = 0;k < m2.columns; k++){
                this.matrix[i][k]=0;
                for (int j = 0; j < m2.rows; j++) {
                    this.matrix[i][k] = m1.matrix[i][j] * m2.matrix[j][k] + this.matrix[i][k];
                }
            }
        }
        //return dest;
    }

    public void multiply(Matrix m1, Matrix m2, int row, int col){
        int commun_size = m1.getColumns();
        Operator operator = new MultiplyOperator();
        this.matrix[row][col] = 0;
        for(int i=0;i<commun_size;i++){
            this.matrix[row][col] = m1.matrix[row][i] * m2.matrix[i][col] + this.matrix[row][col];
        }
    }
    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}

package com.company;

/**
 * Created by b on 20-Oct-16.
 */
public class MatrixMultiplicationThread extends Thread {
    private Matrix matrix1;
    private Matrix matrix2;
    private Matrix matrix3;
    private int start_row;
    private int end_row;
    private int start_column;
    private int end_column;

    MatrixMultiplicationThread (Matrix matrix1, Matrix matrix2, Matrix matrix3, int start_row, int end_row, int start_column, int end_column){
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.matrix3 = matrix3;
        this.start_row =  start_row;
        this.end_row = end_row;
        this.start_column = start_column;
        this.end_column = end_column;
    }

    public void run(){
        int row_poz = start_row,col_poz = start_column, row_end = end_row,col_end= end_column;
        int nr_of_columns = matrix2.getColumns();
        while(row_poz != row_end && col_poz != col_end){
            matrix3.multiply(this.matrix1,this.matrix2,row_poz,col_poz);

            if(col_poz<nr_of_columns-1){
                col_poz++;
            }else{
                col_poz=0;
                row_poz++;
            }

        }
    }
}


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {
    static int nr_of_threads = 10;
    static ExecutorService fixedPool = Executors.newFixedThreadPool(nr_of_threads);

    public static void adunareaADouaMatrici() {
        Scanner scanner = new Scanner(System.in);
        String path = "fisier1.txt", path2 = "fisier2.txt";
        int n = 0, m = 0, nr_of_elements = 0;
        System.out.println("Dati parametrii generarea matricilor...");
        System.out.println("Dati n=");
        n = Integer.parseInt(scanner.nextLine());
        System.out.println("Dati m=");
        m = Integer.parseInt(scanner.nextLine());
        nr_of_elements = n * m;

        try {
            Matrix.writeGeneratedMatrixToFile(path, n, m);
            Matrix.writeGeneratedMatrixToFile(path2, n, m);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Matrix matrix1 = new Matrix(n, m);
        Matrix matrix2 = new Matrix(n, m);
        try {
            matrix1.readMatrixFromFile(path);
            System.out.println("matrix1:");
            matrix1.printMatrix();
            matrix2.readMatrixFromFile(path2);
            System.out.println("matrix2:");
            matrix2.printMatrix();

            long start = System.nanoTime();

//            List<Callable<Integer>> callableList = new LinkedList<>();
//            int blocks = matrix1.getRows() / nr_of_threads, start_row = 0, end_row = 0;
//            for (int i = 1; i <= nr_of_threads; i++) {
//                if (i != nr_of_threads) {
//                    start_row = end_row;
//                    end_row = blocks * i;
//                    final int s = start_row, f = end_row;
//                    Callable<Integer> callable1 = new Callable<Integer>() {
//                        @Override
//                        public Integer call() throws Exception {
//                            matrix1.parallel_addition(matrix2, s, f);
//                            return 1;
//                        }
//                    };
//                    callableList.add(callable1);
//                    //matrix1.parallel_addition(matrix2,start_row,end_row);
//                } else {
//                    start_row = end_row;
//                    end_row = matrix1.getRows();
//                    final int s = start_row, f = end_row;
//                    Callable<Integer> callable1 = new Callable<Integer>() {
//                        @Override
//                        public Integer call() throws Exception {
//                            matrix1.parallel_addition(matrix2, s, f);
//                            return 1;
//                        }
//                    };
//                    callableList.add(callable1);
//                }
//            }
//            List<Future<Integer>> futureList = fixedPool.invokeAll(callableList);


            //Future<Matrix> callableFuture = fixedPool.submit(callable);
            //long start = System.nanoTime();

            //Matrix m3 = Matrix.addition(matrix1, matrix2);
            //Matrix m3 = callableFuture.get();
            int block = nr_of_elements / nr_of_threads, start_row = 0, end_row = 0, start_column = 0, end_column = 0;
            int nr_of_columns = matrix2.getColumns();
            for (int i = 1; i <= nr_of_threads; i++) {
                int segment = block * i;

                if (i != nr_of_threads) {
                    if (segment % nr_of_columns == 0) {
                        end_row = segment / nr_of_columns;
                        end_column = nr_of_columns - 1;
                    } else {
                        end_row = segment / nr_of_columns;
                        end_column = segment % nr_of_columns;
                    }
                } else {
                    end_row = matrix1.getRows()-1;
                    end_column = nr_of_columns - 1;
                }
                MatrixAdditionThread m_add = new MatrixAdditionThread(matrix1,matrix2,start_row,end_row,start_column,end_column);
                m_add.start();

                start_row = end_row;
                start_column = end_column;
            }

            long end = System.nanoTime();
            long elapsedTime = end - start;
            System.out.println("Time addition (ms s):" + TimeUnit.MICROSECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));

            System.out.println("Resulted matrix:");
            matrix1.printMatrix();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void adunareaADouaMatriciComplexe() {
        Scanner scanner = new Scanner(System.in);
        String path = "fisier1.txt", path2 = "fisier2.txt";
        int n = 0, m = 0, k = 0, nr_of_elements = 0;
        System.out.println("Dati parametrii generarea matricilor...");
        System.out.println("Dati n=");
        n = Integer.parseInt(scanner.nextLine());
        System.out.println("Dati m=");
        m = Integer.parseInt(scanner.nextLine());

        ComplexNumber[][] c1 = ComplexNumberMatrix.generateRandomMatrix(n, m);
        ComplexNumber[][] c2 = ComplexNumberMatrix.generateRandomMatrix(n, m);
        ComplexNumberMatrix matrix1 = new ComplexNumberMatrix(c1, n, m);
        ComplexNumberMatrix matrix2 = new ComplexNumberMatrix(c2, n, m);
        ComplexNumberMatrix matrix3 = new ComplexNumberMatrix(c2, n, m);

        try {
            System.out.println("matrix1:");
            matrix1.printMatrix();
            System.out.println("matrix2:");
            matrix2.printMatrix();
            long startTime = System.nanoTime();

            List<Callable<ComplexNumberMatrix>> callableList = new LinkedList<>();
            int blocks = matrix1.getRows() / nr_of_threads, start_row = 0, end_row = 0;

            ComplexNumberMatrix rez = new ComplexNumberMatrix(n,m);

            for (int i = 1; i <= nr_of_threads; i++) {
                if (i != nr_of_threads) {
                    start_row = end_row;
                    end_row = blocks * i;
                    final int s = start_row, f = end_row;
                    Callable<ComplexNumberMatrix> callable1 = new Callable<ComplexNumberMatrix>() {
                        @Override
                        public ComplexNumberMatrix call() throws Exception {
                            matrix1.parallel_addition(matrix2,s,f);
                            return matrix1;
                        }
                    };
                    callableList.add(callable1);
                    //matrix1.parallel_addition(matrix2,start_row,end_row);
                } else {
                    start_row = end_row;
                    end_row = matrix1.getRows();
                    final int s = start_row, f = end_row;
                    Callable<ComplexNumberMatrix> callable1 = new Callable<ComplexNumberMatrix>() {
                        @Override
                        public ComplexNumberMatrix call() throws Exception {
                            matrix1.parallel_addition(matrix2, s, f);
                            return matrix1;
                        }
                    };
                    callableList.add(callable1);
                }
            }
            List<Future<ComplexNumberMatrix>> futureList = fixedPool.invokeAll(callableList);

            //Matrix m3 = Matrix.multiply(matrix1, matrix2);
            //Future<Matrix> futureCallable = fixedPool.submit(callable);
            //Matrix m3 = futureCallable.get();

//            int block = nr_of_elements / nr_of_threads, start_row = 0, end_row = 0, start_column = 0, end_column = 0;
//            int nr_of_columns = matrix2.getColumns();
//            for (int i = 1; i <= nr_of_threads; i++) {
//                int segment = block * i;
//
//                if (i != nr_of_threads) {
//                    if (segment % nr_of_columns == 0) {
//                        end_row = segment / nr_of_columns;
//                        end_column = nr_of_columns - 1;
//                    } else {
//                        end_row = segment / nr_of_columns;
//                        end_column = segment % nr_of_columns;
//                    }
//                } else {
//                    end_row = matrix1.getRows() - 1;
//                    end_column = nr_of_columns - 1;
//                }
//                MatrixMultiplicationThread m_multi = new MatrixMultiplicationThread(matrix1, matrix2, matrix3, start_row, end_row, start_column, end_column);
//                m_multi.start();
//
//                start_row = end_row;
//                start_column = end_column;
//            }

            long stopTime = System.nanoTime();
            long elapsedTime = stopTime - startTime;
            System.out.println("Time multiplication (mc s)" + TimeUnit.MICROSECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));

            System.out.println("Resulted matrix:");
            //matrix1.printMatrix();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String path = "fisier1.txt", path2 = "fisier2.txt";
        while (true) {
            System.out.println("Alegeti operatia dintre matrici....");
            System.out.println("0 - exit");
            System.out.println("1 - adunarea");
            System.out.println("2 - adunare matrice nr complexe");
            System.out.println("3 - empty files");
            String answer = scanner.nextLine();

            if ("1".equals(answer)) {
                adunareaADouaMatrici();
            } else if ("2".equals(answer)) {
                adunareaADouaMatriciComplexe();
            } else if ("0".equals(answer)) {
                break;
            } else if ("3".equals(answer)) {
                try {
                    PrintWriter pw = new PrintWriter(path);
                    pw.write("");
                    PrintWriter pw2 = new PrintWriter(path2);
                    pw2.write("");
                    pw.close();
                    pw2.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Comanda invalida!");
            }
        }
    }
}

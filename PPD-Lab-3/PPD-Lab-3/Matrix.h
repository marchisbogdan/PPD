#pragma once

const int ARR_SIZE = 1000;

class Matrix {
private:
	int **matrix;
	int rows;
	int columns;
public:
	Matrix(int, int);

	~Matrix();

	int** generateRandomMatrix(int, int);
	void writeGeneratedMatrixToFile(char*, int, int);
	void readFromFile(char*);
	void printMatrix();

	int getRows() const;

	int getColumns() const;

	void addition(Matrix, int, int);

	void multiply(Matrix, Matrix, int, int);

	void thread_addition(Matrix, int, int, int, int);

	void thread_multiply(Matrix, Matrix, int, int, int, int);
};

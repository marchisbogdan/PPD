#include "stdafx.h"
#include "Matrix.h"

//
// Created by b on 25-Oct-16.
//

#include <time.h>
#include <iostream>
#include <fstream>
#include <random>
#include <algorithm>
#include <stdlib.h>
#include <stdio.h>
#include <chrono>

using namespace std;

void Matrix::printMatrix() {
	for (int i = 0; i < rows; i++) {
		for (int j = 0; j < columns; j++) {
			cout << matrix[i][j] << " ";
		}
		cout << endl;
	}
}

void Matrix::readFromFile(char *path) {
	int a;
	ifstream infile(path);
	int current_row = 0, current_col = 0;
	while (infile >> a || current_row < this->getRows()) {
		matrix[current_row][current_col] = a;
		if (current_col < columns - 1) {
			current_col++;
		}
		else {
			current_col = 0;
			current_row++;
		}
	}
}

int Matrix::getRows() const {
	return rows;
}

int Matrix::getColumns() const {
	return columns;
}

void Matrix::writeGeneratedMatrixToFile(char *path, int rows, int columns) {
	//int **gen_matrix = generateRandomMatrix(rows,columns);
	///
	int number = 0;
	int **gen_matrix;
	gen_matrix = new int *[rows];
	for (int k = 0; k < rows; k++) {
		gen_matrix[k] = new int[columns];
	}
	unsigned seed = std::chrono::system_clock::now().time_since_epoch().count();
	struct timespec ts;
	std::chrono::system_clock::now();
	auto now = std::chrono::system_clock::now();

	//srand(std::chrono::duration_cast<std::chrono::nanoseconds>(now.time_since_epoch().count()));
	std::random_device rd;
	uniform_int_distribution<int> ud(0, 99);
	mt19937 mt(rd());
	for (int i = 0; i < rows; i++) {
		for (int j = 0; j < columns; j++) {
			//number = rand() % 100; // from 0 to 99
			number = ud(mt);
			gen_matrix[i][j] = number;
			// << number << " ";
		}
		//cout << endl;
	}
	cout << endl;
	///
	ofstream outputFile;
	outputFile.open(path);
	for (int i = 0; i < rows; i++) {
		for (int j = 0; j < columns; j++) {
			outputFile << gen_matrix[i][j] << endl;
		}
	}
	// clear the allocated space
	for (int k = 0; k < rows; k++) {
		delete[]gen_matrix[k];
	}
	delete[]gen_matrix;

	outputFile.close();
}

int **Matrix::generateRandomMatrix(int rows, int columns) {
	int number = 0;
	int **gen_matrix;
	gen_matrix = new int *[rows];
	for (int k = 0; k < rows; k++) {
		gen_matrix[k] = new int[columns];
	}

	srand(time(NULL));
	for (int i = 0; i < rows; i++) {
		for (int j = 0; j < columns; j++) {
			number = rand() % 100; // from 0 to 99
			gen_matrix[i][j] = number;
		}
	}

	return gen_matrix;
}

Matrix::Matrix(int rows, int columns) : rows(rows), columns(columns) {
	matrix = new int *[rows];
	for (int i = 0; i < columns; i++) {
		matrix[i] = new int[columns];
	}
}

void Matrix::multiply(Matrix m1, Matrix m2, int row, int col) {
	int commun_size = m1.getColumns(), rows = m2.getRows();
	this->matrix[row][col] = 0;
	for (int i = 0; i < rows; i++) {
		this->matrix[row][col] = (m1.matrix[row][i] * m2.matrix[i][col]) + this->matrix[row][col];
	}
}

void Matrix::addition(Matrix matrix1, int row, int column) {
	this->matrix[row][column] += matrix1.matrix[row][column];
}

Matrix::~Matrix() {
	//for (int i = 0; i < rows; i++) {
	//	delete[]matrix[i];
	//}
	//delete[]matrix;
}

void Matrix::thread_addition(Matrix m2, int start_row, int end_row, int start_column, int end_column) {
	int nr_of_columns = this->getColumns(), s_r = start_row, e_r = end_row, s_c = start_column, e_c = end_column;
	while (s_r != e_r && s_c != e_c) {
		this->addition(m2, s_r, s_c);

		if (s_c < nr_of_columns - 1) {
			s_c++;
		}
		else {
			s_c = 0;
			s_r++;
		}
	}
}

void Matrix::thread_multiply(Matrix m1, Matrix m2, int start_row, int end_row, int start_column, int end_column) {
	int nr_of_columns = this->getColumns(), s_r = start_row, e_r = end_row, s_c = start_column, e_c = end_column;
	while (s_r != e_r && s_c != e_c) {
		this->multiply(m1, m2, s_r, s_c);

		if (s_c < nr_of_columns - 1) {
			s_c++;
		}
		else {
			s_c = 0;
			s_r++;
		}
	}
}

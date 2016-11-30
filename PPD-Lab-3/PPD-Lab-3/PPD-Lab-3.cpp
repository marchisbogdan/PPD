// PPD-Lab-3.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>
#include "Matrix.h"
#include <chrono>
#include <thread>

using namespace std;

int nr_of_threads = 4;
std::chrono::high_resolution_clock Clock;

void adunareaADouaMatrici() {
	int n, m, nr_of_elements, nr_of_columns, block, start_row = 0, end_row = 0, start_column = 0, end_column = 0;
	char path1[] = "C:\\Users\\b\\documents\\visual studio 2015\\Projects\\PPD-Lab-3\\fisierul1.txt",
		path2[] = "C:\\Users\\b\\documents\\visual studio 2015\\Projects\\PPD-Lab-3\\fisierul2.txt";
	
	cout << "dati n:";
	cin >> n;
	cout << "dati m:";
	cin >> m;
	Matrix* matrix1 = new Matrix(n, m);
	Matrix* matrix2 = new Matrix(n, m);

	matrix1->writeGeneratedMatrixToFile(path1, n, m);
	matrix2->writeGeneratedMatrixToFile(path2, n, m);

	matrix1->readFromFile(path1);
	matrix2->readFromFile(path2);

	nr_of_elements = n*m;
	block = nr_of_elements / nr_of_threads;
	nr_of_columns = matrix2->getColumns();

	matrix1->printMatrix();
	cout << endl;
	matrix2->printMatrix();
	cout << endl;

	// starting clock
	auto t1 = Clock.now();

		for (int i = 1; i <= nr_of_threads; i++) {
			int segment = block * i;
			if (i != nr_of_threads) {
				if (segment % nr_of_columns == 0) {
					end_row = segment / nr_of_columns;
					end_column = nr_of_columns + 1;
				}
				else {
					end_row = segment / nr_of_columns;
					end_column = segment % nr_of_columns;
				}
			}
			else {
				end_row = matrix1->getRows();
				end_column = nr_of_columns + 1;
			}
			// create thread
			thread t1([=] { matrix1->thread_addition(*matrix2, start_row, end_row, start_column, end_column); });
			t1.join();
			start_row = end_row;
			start_column = end_column;
		}
	//stoping clock
	auto t2 = Clock.now();
	cout << "Estimated time:" << std::chrono::duration_cast<std::chrono::nanoseconds>(t2 - t1).count() << " nanosecounds" << endl;

	matrix1->printMatrix();
	cout << endl;
	//matrix2->printMatrix();

	delete matrix1;
	delete matrix2;
}

void inmultireaADouaMatrici() {
	int n, m,k, nr_of_elements, nr_of_columns, block, start_row = 0, end_row = 0, start_column = 0, end_column = 0;
	char path1[] = "C:\\Users\\b\\documents\\visual studio 2015\\Projects\\PPD-Lab-3\\fisierul1.txt",
		path2[] = "C:\\Users\\b\\documents\\visual studio 2015\\Projects\\PPD-Lab-3\\fisierul2.txt";

	cout << "dati n:";
	cin >> n;
	cout << "dati k:";
	cin >> k;
	cout << "dati m:";
	cin >> m;
	Matrix* matrix1 = new Matrix(n, k);
	Matrix* matrix2 = new Matrix(k, m);
	Matrix* matrix3 = new Matrix(n, m);

	matrix1->writeGeneratedMatrixToFile(path1, n, k);
	matrix2->writeGeneratedMatrixToFile(path2, k, m);

	matrix1->readFromFile(path1);
	matrix2->readFromFile(path2);

	nr_of_elements = n*m;
	block = nr_of_elements / nr_of_threads;
	nr_of_columns = matrix2->getColumns();

	//matrix1->printMatrix();
	//cout << endl;
	//matrix2->printMatrix();
	//cout << endl;

	// starting clock
	auto t1 = Clock.now();

	for (int i = 1; i <= nr_of_threads; i++) {
		int segment = block * i;
		if (i != nr_of_threads) {
			if (segment % nr_of_columns == 0) {
				end_row = segment / nr_of_columns;
				end_column = nr_of_columns + 1;
			}
			else {
				end_row = segment / nr_of_columns;
				end_column = segment % nr_of_columns;
			}
		}
		else {
			end_row = matrix1->getRows();
			end_column = nr_of_columns + 1;
		}
		// create thread
		thread t1([=] { matrix3->thread_multiply(*matrix1,*matrix2, start_row, end_row, start_column, end_column); });
		t1.join();
		start_row = end_row;
		start_column = end_column;
	}
	//stopping clock
	auto t2 = Clock.now();
	cout << "Estimated time:" << std::chrono::duration_cast<std::chrono::nanoseconds>(t2 - t1).count() << " nanosecounds" << endl;

	cout << "Result:"<<endl;
	//matrix3->printMatrix();

	delete matrix1;
	delete matrix2;
}


int main() {
	int n;
	while (true) {
		cout << "Alegeti operatia dintre matrici..." << endl;
		cout << "1 - adunarea" << endl;
		cout << "2 - inmultirea" << endl;
		cout << "3 - exit" << endl;
		cin >> n;
		if (n == 1) {
			cout << "adunareee";
			adunareaADouaMatrici();
		}
		else if (n == 2) {
			inmultireaADouaMatrici();
		}
		else if (n == 3) {
			cout << "Exit!" << endl;
			break;
		}
		else {
			cout << "Comanda invalida!" << endl;
		}
	}
	return 0;
}
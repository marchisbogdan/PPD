// PPD-Lab-4.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

#include "SortedDoublyLinkedList.h"
#include "Element.h"

#include <thread>

using namespace std;

const int nr_of_threads = 10;

int main()
{
	SortedDoublyLinkedList *list = new SortedDoublyLinkedList();

	for (int i = 1; i <= nr_of_threads; i++) {
		thread t1([=] { list->runnable(); });
		t1.join();
	}
	list->printList();
	delete list;
    return 0;
}


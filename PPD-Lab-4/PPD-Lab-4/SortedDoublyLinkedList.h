#pragma once
#include "Element.h"
class SortedDoublyLinkedList
{
private:
	Element* start;
	int size;
public:
	SortedDoublyLinkedList();
	~SortedDoublyLinkedList();

	void insert(int);
	void delete_pos(int);
	bool isEmpty();
	void printList();
	int get(int);
	int getSize();
	void runnable();

};


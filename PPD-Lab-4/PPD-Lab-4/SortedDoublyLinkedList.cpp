#include "stdafx.h"
#include "SortedDoublyLinkedList.h"
#include <mutex>
#include <random>
#include <algorithm>
#include <stdlib.h>
#include <stdio.h>
#include <chrono>

using namespace std;

mutex the_mutex;

SortedDoublyLinkedList::SortedDoublyLinkedList()
{
	this->start = nullptr;
	this->size = 0;
}


SortedDoublyLinkedList::~SortedDoublyLinkedList()
{
}

void SortedDoublyLinkedList::insert(int val)
{
	the_mutex.lock();
	
	Element *new_elem = new Element(val, nullptr, nullptr);
	Element *first_ptr, *second_ptr;
	bool insert = false;

	if (start == nullptr) {
		start = new_elem;
	}
	else if (val <= start->getValue()) {
		new_elem->setNextElement(start);
		start->setPrevElement(new_elem);
		start = new_elem;
	}
	else {
		first_ptr = start;
		second_ptr = start->getNextElement();
		while (second_ptr != nullptr)
		{
			if (val >= first_ptr->getValue() && val <= second_ptr->getValue()) {
				first_ptr->setNextElement(new_elem);
				new_elem->setPrevElement(first_ptr);
				new_elem->setNextElement(second_ptr);
				second_ptr->setPrevElement(new_elem);
				insert = true;
				break;
			}
			else {
				first_ptr = second_ptr;
				second_ptr = second_ptr->getNextElement();
			}
		}
		if (!insert) {
			first_ptr->setNextElement(new_elem);
			new_elem->setPrevElement(first_ptr);
		}
	}
	size++;

	the_mutex.unlock();
}

void SortedDoublyLinkedList::delete_pos(int pos)
{
	the_mutex.lock();

	if (pos == 1) {
		if (size == 1) {
			start = nullptr;
			size = 0;
			return;
		}
		start = start->getNextElement();
		start->setPrevElement(nullptr);
		size--;
		return;
	}
	else if (pos == size) {
		Element *ptr = start;
		for (int i = 1; i < size - 1; i++) {
			ptr = ptr->getNextElement();
		}
		ptr->setNextElement(nullptr);
		size--;
		return;
	}
	Element *ptr = start->getNextElement();
	for (int i = 2; i <= size; i++) {
		if (i == pos) {
			Element *prev = ptr->getPrevElement();
			Element *next = ptr->getNextElement();

			prev->setPrevElement(next);
			next->setNextElement(prev);
			size--;
			return;
		}
		ptr = ptr->getNextElement();
	}

	the_mutex.unlock();
}

bool SortedDoublyLinkedList::isEmpty()
{
	return start == nullptr;
}

void SortedDoublyLinkedList::printList()
{
	if (size == 0) {
		cout << "List is empty!" << endl;
		return;
	}
	else if (start->getNextElement() == nullptr) {
		cout << start->getValue() << endl;
		return;
	}
	else {
		Element *pointer = start;
		cout << pointer->getValue() << " - ";
		pointer = start->getNextElement();
		while (pointer->getNextElement() != nullptr) {
			cout << pointer->getValue() << " - ";
			pointer = pointer->getNextElement();
		}
		cout << pointer->getValue() << endl;
	}

}

int SortedDoublyLinkedList::get(int pos)
{
	if (pos > this->size || pos <= 0) {
		return -1;
	}
	else if (pos == 1) {
		return this->start->getValue();
	}
	else {
		Element *pointer = start;
		for (int i = 2; i <= pos; i++) {
			pointer = pointer->getNextElement();
		}
		return pointer->getValue();
	}
}

int SortedDoublyLinkedList::getSize()
{
	return this->size;
}

void SortedDoublyLinkedList::runnable()
{
	the_mutex.lock();

	int rand_choice = 0, rand_nr = 0, rand_pos = 0;
	random_device rd;
	uniform_int_distribution<int> ud1(0, 99);
	mt19937 mt(rd());

	if (size > 0) {
		uniform_int_distribution<int> ud2(1, size);
		rand_pos = ud2(mt);
	}
	rand_choice = ud1(mt);
	if (rand_choice % 2 == 1) {
		//insert
		rand_nr = ud1(mt);
		this->insert(rand_nr);
		cout << "Inserted value " << rand_nr << endl;
	}
	else {
		//delete
		if (rand_pos != 0) {
			this->delete_pos(rand_pos);
			cout << "Delete value from pos " << rand_pos << endl;
		}
	}

	the_mutex.unlock();
}

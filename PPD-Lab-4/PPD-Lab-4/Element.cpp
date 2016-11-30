#include "stdafx.h"
#include "Element.h"
#include <iostream>>

using namespace std;

Element::Element(int value,Element *prevElement, Element *nextElement): value(value), prevElement(prevElement), nextElement(nextElement) {

}

Element * Element::getPrevElement()
{
	return this->prevElement;
}

Element * Element::getNextElement()
{
	return this->nextElement;
}

void Element::setPrevElement(Element *prevElement)
{
	this->prevElement = prevElement;
}

void Element::setNextElement(Element *nextElement)
{
	this->nextElement = nextElement;
}

int Element::getValue()
{
	return this->value;
}

void Element::setValue(int value)
{
	this->value = value;
}
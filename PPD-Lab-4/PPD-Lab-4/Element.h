#pragma once

class Element {
private:
	Element *prevElement;
	int value;
	Element *nextElement;
public:
	Element(int, Element*, Element*);
	
	Element* getPrevElement();
	Element* getNextElement();

	void setPrevElement(Element*);
	void setNextElement(Element*);

	int getValue();
	void setValue(int);

};
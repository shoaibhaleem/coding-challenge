import java.util.ArrayList;

class LinkS
{
	public ArrayList<Object> dData; // data item
	public LinkS next; // next link in list
	// -------------------------------------------------------------

	public LinkS(ArrayList<Object> dd) // constructor
	{
		dData = dd;
	}

	// -------------------------------------------------------------
	public void displayLink() // display ourself
	{
		System.out.print(dData + " ");
	}
} // end class Link
	////////////////////////////////////////////////////////////////

class LinkList
{
	private LinkS first; // ref to first item on list
	// -------------------------------------------------------------

	public LinkList() // constructor
	{
		first = null;
	} // no items on list yet
		// -------------------------------------------------------------

	public boolean isEmpty() // true if list is empty
	{
		return (first == null);
	}

	// -------------------------------------------------------------
	public void insertFirst(ArrayList<Object> dd) // insert at start of list
	{ // make new link
		LinkS newLink = new LinkS(dd);
		newLink.next = first; // newLink --> old first
		first = newLink; // first --> newLink
	}

	// -------------------------------------------------------------
	public ArrayList<Object> deleteFirst() // delete first item
	{ // (assumes list not empty)
		LinkS temp = first; // save reference to link
		first = first.next; // delete it: first-->old next
		return temp.dData; // return deleted link
	}

	// -------------------------------------------------------------
	public void displayList()
	{
		LinkS current = first; // start at beginning of list
		while (current != null) // until end of list,
		{
			current.displayLink(); // print data
			current = current.next; // move to next link
		}
		System.out.println("");
	}
	// -------------------------------------------------------------
} // end class LinkList
	////////////////////////////////////////////////////////////////

public class Stack
{
	private LinkList theList;
	private int itsSize;

	// --------------------------------------------------------------
	public Stack() // constructor
	{
		theList = new LinkList();
		itsSize = 0;
	}

	// --------------------------------------------------------------
	public void push(ArrayList<Object> j) // put item on top of stack
	{
		theList.insertFirst(j);
		itsSize++;
	}

	// --------------------------------------------------------------
	public ArrayList<Object> pop() // take item from top of stack
	{
		if (!isEmpty())
			itsSize--;
		return theList.deleteFirst();
	}

	public int size()
	{
		return itsSize;
	}

	// --------------------------------------------------------------
	public boolean isEmpty() // true if stack is empty
	{
		return (theList.isEmpty());
	}

	// --------------------------------------------------------------
	public void displayStack()
	{
		System.out.print("Stack (top-->bottom): ");
		theList.displayList();
	}
	// --------------------------------------------------------------
} // end class LinkStack
	////////////////////////////////////////////////////////////////

class EmptyStackException extends Exception
{
	public EmptyStackException()
	{
		super("\nDuplication failed. The stack is currently empty!\n");
	}

	public EmptyStackException(String message)
	{
		super(message);
	}
}
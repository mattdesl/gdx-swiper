package mdesl.swipe;

import com.badlogic.gdx.utils.Array;

/**
 * A simple extension of Array that allows inserting an element 
 * at the head (index 0) without ever growing the backing array.
 * Elements are shifted right and eventually discarded to make 
 * way for new additions.
 * 
 * @author mattdesl
 * @param <T> generic type
 */
public class FixedList<T> extends Array<T> {

	/**
	 * Safely creates a list that is backed by an array which
	 * can be directly accessed. 
	 * 
	 * @param capacity the fixed-size capacity of this list
	 * @param type the class type of the elements in this list
	 */
	public FixedList(int capacity, Class<T> type) {
		super(false, capacity, type);
	}

	/**
	 * Inserts the item into index zero, shifting all items to the right,
	 * but without increasing the list's size past its array capacity.
	 * @param t the element to insert
	 */
	public void insert(T t) {
		T[] items = this.items;

		// increase size if we have a new point
		size = Math.min(size + 1, items.length);
		
		// shift elements right
		for (int i = size - 1; i > 0; i--) {
			items[i] = items[i - 1];
		}

		// insert new item at first index
		items[0] = t;
	}
	
	
}

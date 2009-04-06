package quizfun.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * M. Isuru Tharanga Chrishantha Perera
 */
public class Utils {

	/**
	 * <p>
	 * Update a detached list with a new list. Purpose of this method is to
	 * update a child collection of an entity and get the list of removed
	 * children.
	 * </p>
	 * 
	 * <p>
	 * This method depends on {@code equals()} method implemented for child
	 * collection objects.
	 * </p>
	 * 
	 * @param <T>
	 *            The type of child collection elements
	 * @param detachedList
	 *            The detached list
	 * @param newList
	 *            New List
	 * @return The removed list from the detached list.
	 */
	public static <T> List<T> updateDetachedList(Collection<T> detachedList, Collection<T> newList, MergeCallback<T> mergeCallback) {
		List<T> newObjectList = new ArrayList<T>(newList.size());
		//  To use iterator.remove()
		newObjectList.addAll(newList);

		boolean foundDetachedObject;

		Iterator<T> iterator = newObjectList.iterator();
		while (iterator.hasNext()) {
			foundDetachedObject = false;
			T newObject = iterator.next();
			for (T detachedObject : detachedList) {
				if (detachedObject.equals(newObject)) {
					iterator.remove();
					mergeCallback.updateEntity(detachedObject, newObject);
					foundDetachedObject = true;
					break;
				}
			}
			if (!foundDetachedObject) {
				detachedList.add(newObject);
			}
		}

		List<T> list = new ArrayList<T>(detachedList);
		list.retainAll(newList);

		List<T> removeList = new ArrayList<T>(detachedList);
		removeList.removeAll(list);

		for (T object : removeList) {
			detachedList.remove(object); // Remove from the list
		}

		return removeList;
	}
}

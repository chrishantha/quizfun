package quizfun.model.util;

/**
 * Callback interface for merging.
 * 
 * @author M. Isuru Tharanga Chrishantha Perera
 * @see Utils#updateDetachedList(java.util.List, java.util.List, MergeCallback)
 */
public interface MergeCallback<T> {

	/**
	 * Gets called by {@code Utils.updateDetachedList} with detached object
	 * and new object.
	 * 
	 * @param detachedObject
	 *            Detached object
	 * @param newObject
	 *            New object
	 */
	void updateEntity(T detachedObject, T newObject);

}

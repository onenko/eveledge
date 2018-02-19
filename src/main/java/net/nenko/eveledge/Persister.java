package net.nenko.eveledge;

import java.util.List;

/**
 * Persister interface defines API to store (persist) events, injected in and used by Engine
 *
 * @author conenko
 */
public interface Persister {

	/**
	 * save saves event in persistent storage
	 *
	 * @param event
	 * @return null on success, or custom string describing the error
	 */
	String save(Event event);

	List<Event> find(String app, String obj);
}

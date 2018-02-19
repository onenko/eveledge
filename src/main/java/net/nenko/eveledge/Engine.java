package net.nenko.eveledge;

import java.util.List;

/**
 * Engine core class of EVEnt LEDGEr package
 *
 * @author conenko
 */
public class Engine {

	private Persister persister;

	public Engine(Persister persister) {
		this.persister = persister;
	};

	/**
	 * save() processes event, persist it to storage
	 *
	 * @param event event to store
	 * @return null on success or some text explaining error
	 */
	public String save(Event event) {
		return persister.save(event);
	}

	/**
	 * find() finds events
	 */
	public List<Event> find(String obj, String app) {
		return persister.find(obj, app);
	}

	public void setPersister(Persister persister) {
		this.persister = persister;
	}

	public Persister getPersister() {
		return this.persister;
	}
}

package net.nenko.eveledge;

import java.util.HashMap;
import java.util.Map;

/**
 * Event encapsulates all data about processed event
 *
 * NOTE1: if a client wants to add some timestamp, this should be additional property of event, not 'tstamp'
 *
 * NOTE2: no multithreading protection provided, in properties
 *
 * @author conenko
 */
public class Event {
	private final String app;		// application name (some rather short string
	private final String obj;		// object id, several events may be related to the same object
	private final long tstamp;	// ms from epoch; timed here in engine, not by client
	private final long event;		// event id
	private Map<String, String> props;	// properties, lazy initialized map, not final

	public Event(String app, String obj, long event) {
		this.app = app;
		this.obj = obj;
		this.tstamp = System.currentTimeMillis();
		this.event = event;
		this.props = new HashMap<>();
	}

	public void addProp(String name, String value) {
		props.put(name, value);
	}

	public String getProp(String name) {
		return props.get(name);
	}

	public Map<String, String> getProps() { return props; }
	
	public String getApp() { return app; }

	public String getObj() { return obj; }

	public long getTstamp() { return tstamp; }

	public long getEvent() { return event; }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Event{app=").append(app).append(", obj=").append(obj).append(", tstamp=").append(tstamp)
    	  .append(", event=").append(event).append(", props='").append(props).append("'}");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		int hc = (app + obj).hashCode();
		hc ^= (int) tstamp;
		hc ^= (int) event;
		return hc;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(o instanceof Event) {
			Event e = (Event) o;
			return e.tstamp == this.tstamp && e.event == this.event && strequals(app, e.app) && strequals(obj, e.obj);   
		}
		return false;
	}

	private static boolean strequals(String s1, String s2) {
		return (s1 == null) ? s2 == null : s1.equals(s2);
	}

}

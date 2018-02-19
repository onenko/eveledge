package net.nenko.eveledge;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersisterInMemory extends PersisterBase {

	private Map<Event, Event> events = new ConcurrentHashMap<>();

	public PersisterInMemory(PropStringifier propStringifier) {
		super(propStringifier);
	}

	@Override
	public String save(Event event) {
		log.info("save({}) executed.", event);
		log.info("Stringified properties:" + prop2str(event.getProps()));
		events.put(event, event);
		return null;
	}

	@Override
	public List<Event> find(String app, String obj) {
		log.info("find({},{}) executed.", app, obj);
		List<Event> result = events.values().stream().filter(e -> e.getObj().equals(obj) && e.getApp().equals(app)).collect(Collectors.toList());
		log.info("find(): stored {} events, found {} events", events.size(), result.size());
		log.info("find(): storage: {}", events.values());
		return result;
	}

}

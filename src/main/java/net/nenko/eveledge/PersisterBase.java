package net.nenko.eveledge;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class PersisterBase implements Persister {

	private PropStringifier propStringifier;

	@Autowired
	public PersisterBase(PropStringifier propStringifier) {
		this.propStringifier = propStringifier;
	}

	protected String prop2str(Map<String, String> props) {
		return propStringifier.encode(props);
	}

	protected String str2prop(Map<String, String> props, String str) {
		return propStringifier.decode(props, str);
	}

}

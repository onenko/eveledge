package net.nenko.eveledge;

import java.util.Map;

/**
 * PropStringifierUnknown - this decoder is called last in chain - always reports error
 *
 * @author conenko
 */

public class PropStringifierUnknown extends PropStringifier {

	public PropStringifierUnknown() {
		super(null);
	}

	@Override
	public String decode(Map<String, String> map, String str) {
		if(str == null) {
			return "Error: input encoded string is null";
		}
		if(str.isEmpty()) {
			return "Error: input encoded string is empty";
		}
		String prefix = str.length() > 5 ? str.substring(0, 5) + "..." : str.substring(0);
		return "Error: input encoded string format not recognized: " + prefix;
	}

}

package net.nenko.eveledge;

import java.util.Map;

/**
 * PropStringifier defines API to stringify and destringify properties
 *
 * NOTE1: Having several implementations of PropStringifier allows to store properties in different formats, to be extensible
 *
 * NOTE2: This class is ACTIVE version of stringifier, exclusively used for stringified, and tried first in chain of
 * destringifiers.
 * Steps to implement another active stringifier:
 *  - copy this PropStringifier class into another PropStrigifierPropValList class;
 *  - implement new functionality in this class, keep the name PropStringifier;
 *  - update AppConfig.java to create @Bean PropStrigifierPropValList and insert in chain between PropStringifier and PropStringifierUnknown.
 *
 * @author conenko
 */

public class PropStringifier {

	private static final String MAGIC = "p=v`";
	private static final char PROP_VAL_SEPAR = '=';	// expect that property names have no this char inside
	private static final char PV_PAIR_SEPAR = '`';	// never used, small chance to escape

	private PropStringifier chained; 

	public PropStringifier(PropStringifier chained) {
		this.chained = chained;
	}

	/**
	 * encode - converts map of properties into string, which may be saved to storage and decoded back
	 *
	 * @param properties properties to be encoded
	 * @return stringified version of properties
	 */
	public String encode(Map<String, String> properties) {
		if(properties == null) {
			return "null";
		}
		StringBuilder sb = new StringBuilder(MAGIC);
		for(Map.Entry<String, String> entry: properties.entrySet()) {
			appendEscaped(sb, entry.getKey(), PROP_VAL_SEPAR);
			appendEscaped(sb, entry.getValue(), PV_PAIR_SEPAR);
		}
		if(sb.length() > MAGIC.length()) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * decode properties from string into original map
	 *
	 * @param toBeFilled in-out parameter to be filled with properties
	 * @param encoded stringified data to be decoded
	 * @return null on success or some text explaining error
	 */
	public String decode(Map<String, String> toBeFilled, String encoded) {
		if(encoded != null && encoded.startsWith(MAGIC)) {
			return decodeEscaped(toBeFilled, encoded);
		} else {
			return chained.decode(toBeFilled, encoded);
		}
	}

	/**
	 * recognize() check if the encoded string can be decoded with this stringer
	 *
	 * NOTE: this method expected to be fast and check just header,
	 *  so it does not guarantie that the whole string will be decoded without errors
	 *
	 * @param encoded stringified data to be decoded
	 * @return boolean if stringified format recognized
	 */
//	protected boolean recognize(String encoded);

	/**
	 * appendEscaped - copies input string to string builder, escaping 'vipChar' terminating result with 'vipChar'
	 *
	 * @param sb in-out buffer to append result
	 * @param in string to process
	 * @param vipChar char that terminates the result and thus must be escaped inside
	 */
	protected void appendEscaped(StringBuilder sb, String in, char vipChar) {
		if(in != null) {
			for(int i = 0; i < in.length(); i++) {
				char c = in.charAt(i);
				if(c == vipChar || c == '\\') {
					sb.append('\\');
				}
				sb.append(c);
			}
			sb.append(vipChar);
		}
	}

	
	protected String decodeEscaped(Map<String, String> toBeFilled, String in) {
		boolean inName = true;
		String name = null;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			if(c == '\\') {
				if( ++i >= in.length()) {
					return "Error: unexpected end of string on \\ char";
				}
				sb.append(in.charAt(i));
			} else {
				if(inName) {
					if(c == PROP_VAL_SEPAR) {
						inName = false;
						name = sb.toString();
						sb.setLength(0);
					} else {
						sb.append(c);
					}
				} else {
					if(c == PV_PAIR_SEPAR) {
						inName = true;
						toBeFilled.put(name, sb.toString());
						sb.setLength(0);
						name = null;
					} else {
						sb.append(c);
					}
				}
			}
		}
		if(name == null || sb.length() == 0) {
			return "Error: unexpected end of string";
		}
		toBeFilled.put(name, sb.toString());
		return null;
	}
	
}

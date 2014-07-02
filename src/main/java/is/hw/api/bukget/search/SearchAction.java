package is.hw.api.bukget.search;

public enum SearchAction {
	EQUALS("equals"),
	NOT_EQUALS("not-equals"),
	LESS("less"),
	LESS_EQUAL("less-equal"),
	MORE("more"),
	MORE_EQUAL("more-equal"),
	
	LIKE("like"),
	IN("in"),
	NOT_IN("not in"),
	
	ALL("all"),
	EXISTS("exists"),
	
	AND("and"),
	OR("or"),
	LIKEOR("likeor"),
	NOR("nor"),
	NOT("not")
	;
	
	private final String action;
	
	private SearchAction(String action) {
		this.action = action;
	}
	
	public String getAction() {
		return this.action;
	}
}

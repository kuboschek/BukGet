package is.hw.api.bukget;

import is.hw.api.JsonWebContent;

public class GenerationInfo extends BukGetData {
	public GenerationInfo() {
		super("", true);
	}

	@JsonWebContent
	public Generation[] content;
	//
	public class Generation {
		public long timestamp;
		public String parser;
		public String type;
		public int duration;
		public Change changes[];
		public String id;
		
		public class Change {
			public String version;
			public String plugin;
		}
	}
}

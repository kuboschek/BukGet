package is.hw.get.chat;


import is.hw.get.settings.GetConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents one section of a structured chat output. ChatSections can contain subsections
 * @author Leonhard
 *
 */
public class ChatSection {
	//Holds this sections' prefix
	protected String contentPrefix;
	
	//Holds this sections' suffix
	protected String contentSuffix;
	
	//Holds this sections' line prefix. Used to indent subsections.
	protected String linePrefix = "  ";
	
	//A list of lines of text that are part of this ChatSection
	protected List<String> content;
	
	//A list of subsections of this ChatSection
	protected List<ChatSection> subSections;
	
	/**
	 * Default constructor. Needs to be overwritten by subclasses
	 */
	protected ChatSection() {
		content = new ArrayList<>();
		subSections = new ArrayList<>();
	}
	
	/**
	 * Adds one line of content to this chat section.
	 * @param line	The content to be added.
	 */
	protected void addContent(String line) {
		addContent(line, (Object[])null);
	}
	
	/**
	 * Adds one line of content to this chat section. Formats line using objects given
	 * @param line	The format of this line
	 * @param obj	The objects to insert into the format
	 */
	protected void addContent(String line, Object... obj) {
		if(line != null) content.add(String.format(line, obj));
	}
	
	/**
	 * Returns the content of this ChatSection, including the content of all subsections.
	 * @return	List of strings with content.
	 */
	protected List<String> getContent() {
		List<String> fullContent = new ArrayList<>();
		
		//Only add the content prefix if one is set
		if(contentPrefix != null) fullContent.add(contentPrefix);
		
		//Add the contents of this ChatSection
		fullContent.addAll(content);
		
		for(ChatSection s : subSections) {
			for(String line : s.getContent()) {
				//Add each line of each ChatSection, including the subsections line prefix
				fullContent.add(s.linePrefix + line);
			}
		}
		
		//Only add the content suffix if one is set
		if(contentSuffix != null) fullContent.add(contentSuffix);
		
		return fullContent;
	}
	
	/**
	 * Add a subsection to this ChatSection
	 * @param subSection	The ChatSection to add
	 */
	protected void addSubsection(ChatSection subSection) {
		//Do not allow ChatSection to be added to itself.
		if(subSection.equals(this)) return;
		
		subSections.add(subSection);
	}
	
	/**
	 * Adds an array of strings to the chat section, including singular and plural names
	 * @param array	The array of strings to add.
	 * @param singularName	The singular name of the values that are added
	 * @param pluralName	The plural name of the values that are added
	 */
	protected void addArrayIfNotNull(String[] array, String singularName, String pluralName) {
		//Sanity checks. No need to add values if array is null or empty.
		if(array==null) return;
		if(array.length == 0) return;
		
		//Use the singular name if only one value is present
		if(array.length == 1) {
			addContent(singularName + GetConfig.General.nvDelim + array[0]); 
			return;
		}
		
		String line = pluralName + GetConfig.General.nvDelim;
		
		for(String entry : array) {
			line += entry + GetConfig.General.vvDelim;
		}
		
		//Remove the last value-value delimiter. Avoids spare commata
		line = line.substring(0, line.lastIndexOf(GetConfig.General.vvDelim));
		
		addContent(line);
	}
	
	/**
	 * Adds an integer with name
	 * @param name	The name of value
	 * @param value	The integer value to add
	 */
	protected void addIfNotNull(String name, int value) {
		addIfNotNull(name, new Integer(value).toString());
	}
	
	/**
	 * Adds a string value with name
	 * @param name	The name of the value
	 * @param value	The integer to add
	 */
	protected void addIfNotNull(String name, String value) {
		if(value == null) return;
		
		addContent(name + GetConfig.General.nvDelim + value);
	}
	
	/**
	 * Adds a date, with name, from a UNIX timestamp
	 * @param name	Name of the date
	 * @param timestamp	Timestamp to add
	 */
	protected void addDateFromLongIfNotNull(String name, long timestamp) {
		if(timestamp==0) return;
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		addIfNotNull(name, df.format(new Date((timestamp * 1000L))));
	}
	
}

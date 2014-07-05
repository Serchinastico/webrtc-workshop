package org.etaoin.webrtcdemo.signaling;

/**
 * @author Sergio Gutiérrez
 */
public class Message {

	private String from;
	private String type;
	private String content;

	public Message(String from, String type, String content) {
		this.from = from;
		this.type = type;
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public String getType() {
		return type;
	}

	public String getContent() {
		return content;
	}

}

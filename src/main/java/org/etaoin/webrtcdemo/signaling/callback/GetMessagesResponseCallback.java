package org.etaoin.webrtcdemo.signaling.callback;

import org.etaoin.webrtcdemo.signaling.Message;

import java.util.List;

/**
 * @author Sergio Gutiérrez
 */
public interface GetMessagesResponseCallback {

	public void onNewMessages(List<Message> messages);

}

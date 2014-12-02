package org.etaoin.webrtcdemo.communication.observer;

import org.etaoin.webrtcdemo.communication.CallState;

/**
 * @author Sergio Gutiérrez
 */
public interface CallModelObserver {

	public void onStateChange(CallState state);

	public void onSignalingLogUpdate(String log);

}

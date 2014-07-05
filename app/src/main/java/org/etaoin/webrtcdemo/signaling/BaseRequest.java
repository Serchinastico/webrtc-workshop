package org.etaoin.webrtcdemo.signaling;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author Sergio Gutiérrez
 */
public abstract class BaseRequest<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	protected String executeGet(String nickname) {
		String response = null;

		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(new HttpGet(buildUrl(getMethodName(), nickname)));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			httpResponse.getEntity().writeTo(out);
			out.close();
			response = out.toString().split("\r\n")[0];
		} catch (IOException e) {
			// Empty
		}

		return response;
	}

	protected void executePost(List<NameValuePair> params) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(buildUrl(getMethodName(), null));
			post.setEntity(new UrlEncodedFormEntity(params));
			httpClient.execute(post);
		} catch (IOException e) {
			// Empty
		}
	}

	private String buildUrl(String method, String nickname) {
		String url = "http://37.134.46.166:5002/?method=" + method;
		if (nickname != null) {
			url += "&name=" + nickname;
		}
		return url;
	}

	protected abstract String getMethodName();
}

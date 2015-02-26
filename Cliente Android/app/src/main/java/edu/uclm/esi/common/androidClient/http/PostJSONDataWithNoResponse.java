package edu.uclm.esi.common.androidClient.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class PostJSONDataWithNoResponse extends AsyncTask<JSONObject, Void, Void> {

	private String resourceURL;

	public PostJSONDataWithNoResponse(String resourceURL) {
		this.resourceURL=resourceURL;
	}

	@Override
	protected Void doInBackground(JSONObject... jso) {
        InputStream inputStream = null;
        JSONObject result=new JSONObject();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            URI uri=new URI("http", 
            		Proxy.get().getUrlServer(),
            		"/rsp/" + this.resourceURL, 
            		(jso.length>0 ? "command=" + jso[0].toString() : ""),
            		null);
            String sURI=uri.toASCIIString();

            HttpPost httpPost = new HttpPost(sURI);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null)
                result = getJSONObject(inputStream);
            else
                result = new JSONObject();
 
        } catch (Exception e) {
        	Log.e("MACOMACO", e.toString());
            result=new JSONObject();
            try {
				result.put("result", e.getMessage());
			} catch (JSONException e1) {
				Log.e("MACOMACO", e1.toString());
			}
        }
        return null;
	}

	private JSONObject getJSONObject(InputStream inputStream) throws IOException, JSONException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String sResult = "";
		while((line = bufferedReader.readLine()) != null)
			sResult += line;

		inputStream.close();
		JSONObject result=new JSONObject(sResult);
		return result;
	}
}
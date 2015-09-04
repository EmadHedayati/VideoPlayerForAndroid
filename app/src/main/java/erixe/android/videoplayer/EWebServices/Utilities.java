package erixe.android.videoplayer.EWebServices;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by emad on 9/3/2015.
 */
public class Utilities {
    
    public static String encodeStringWithUTF8(String string) throws UnsupportedEncodingException {
        byte[] tmp = string.getBytes(Charset.forName("UTF-8"));
        String tmp2 = new String(tmp, Charset.forName("UTF-8"));
        String result = new String(tmp2.getBytes(), "ISO-8859-1");
        return result;
    }

    public static String getQueryInString(Map<String, String> paramsMap) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> pair : paramsMap.entrySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public static void setQueryToHttpURLConnection(HttpURLConnection connection, Map<String, String> paramsMap) throws IOException {
        String query = getQueryInString(paramsMap);
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(query);
        writer.flush();
        writer.close();
        os.close();
    }

    public static HttpURLConnection makeHttpURLConnection(String methodType, String username, String password, String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();

        final String basicAuth = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
        connection.setRequestProperty ("Authorization", basicAuth);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestMethod(methodType.toUpperCase());
        if(methodType.toUpperCase().equals(EWebServiceStrings.METHOD_TYPE_POST))
            connection.setDoOutput(true);

        return connection;
    }

    public static InputStream getWebServiceInputStream(String methodType, String username, String password,
                                                       String url, Map<String, String> paramsMap) throws IOException {
        HttpURLConnection connection = makeHttpURLConnection(methodType, username, password, url);
        if(paramsMap != null)
            setQueryToHttpURLConnection(connection, paramsMap);
        InputStream inputStream = connection.getInputStream();
        return inputStream;
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        String line = "", result = "";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public static WebServiceRespond getPreWebServiceRespond(String methodType, String username, String password,
                                                            String url, Map<String, String> paramsMap) {
        WebServiceRespond webServiceRespond;
        try {
            InputStream inputStream = Utilities.getWebServiceInputStream(methodType, username, password, url, paramsMap);
            if (inputStream != null)
                webServiceRespond = new WebServiceRespond(true, null, new JSONObject(Utilities.convertInputStreamToString(inputStream)));
            else
                webServiceRespond = new WebServiceRespond(false, EWebServiceStrings.TASK_FAILED_TO_CONNECT, null);
        } catch (Exception e) {
            Log.d(url, e.getLocalizedMessage());
            webServiceRespond = new WebServiceRespond(false, EWebServiceStrings.TASK_AN_EXCEPTION_ACURED + ":" + e.getLocalizedMessage(), null);
        }
        return webServiceRespond;
    }

    public static WebServiceRespond initializeWebServiceRespond(WebServiceRespond webServiceRespond) {
        try
        {
            String result = webServiceRespond.result.getString("ok");
            String message = webServiceRespond.result.getString("description");
            JSONObject content = webServiceRespond.result.getJSONObject("result");

            if(result.equals(EWebServiceStrings.TASK_SUCCEEDED))
                return new WebServiceRespond(true, message, content);
            if(result.equals(EWebServiceStrings.TASK_FAILED))
                return new WebServiceRespond(false, message, content);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return new WebServiceRespond(false, e.getMessage(), null);
        }


        String shomare = "";
        return new WebServiceRespond(false, "", null);
    }

}

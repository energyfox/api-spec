import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SolarfoxAPIRequest {

  private final String ENDPOINT = "http://api.solar-fox.com/import";

  public static void main(String[] args) throws Exception {
    SolarfoxAPIRequest http = new SolarfoxAPIRequest();
    http.sendPost();
  }

  private void sendPost() throws Exception {
    URL obj = new URL(ENDPOINT);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    // better use simple-json or gson
    String json = "{"
                  + "\"Headquarters\": { "
                  + "   \"WR-118412E\": { "
                  + "    \"type\": \"ELECTRICITY_CONSUMPTION_ENERGY_ABSOLUTE\","
                  + "     \"readings\": [ "
                  + "       {"
                  + "        \"timestamp\": " + System.currentTimeMillis()/1000 + ","
                  + "         \"value\": 128013.0 "
                  + "        }"
                  + "      ]"
                  + "    }"
                  + "  }"
                  + "}";

    String postData = json.toString();

    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Authorization","test");
    con.setDoOutput(true);
    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    wr.writeBytes(postData);
    wr.flush();
    wr.close();

    int responseCode = con.getResponseCode();
    System.out.println("Status: " + responseCode);

    if (responseCode == HttpURLConnection.HTTP_OK) {
      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
      }
      in.close();
      System.out.println("Body: " + response.toString());
    } else {
      System.out.println("POST request failed");
    }

  }
}

package demo;


import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Trisna Wanto
 *
 */
public class SMSTester {


    public final static String smsHost = "10.70.29.101";
    public final static int smsPort = 9003;
    public final static String smsPath = "/PUSH";
    public final static String smsUsername = "3Karnava";
    public final static String smsPassword = "arnava00";
    public final static String smsShortCode = "3Karnaval";

    public static void main(String[] args) {
        sendSMSToServer("89000000001", "Test");
    }

    private static void sendSMSToServer(String phoneNumber, String message) {

        String udh = "1";

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setSocketTimeout(3000).build();
        HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager).build();

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(smsHost)
                .setPort(smsPort)
                .setPath(smsPath)
                .setParameter("USERNAME", smsUsername)
                .setParameter("PASSWORD", smsPassword)
                .setParameter("REG_DELIVERY", "0")
                .setParameter("ORIGIN_ADDR", smsShortCode)
                .setParameter("MOBILENO", "62" + phoneNumber)
                .setParameter("TYPE", "0")
                .setParameter("MESSAGE", message)
                .setParameter("UDH", udh);

        HttpGet get = null;
        try {
            get = new HttpGet(builder.build());
        } catch (URISyntaxException ex) {
            System.out.println("Error Sending SMS. " + ex);
        }

        CloseableHttpResponse response = null;
        try {

            response = client.execute(get);
            HttpEntity responseEntity = response.getEntity();
            String bodyAsString = EntityUtils.toString(responseEntity);
            int responseCode = response.getStatusLine().getStatusCode();
            String responseMessage = response.getStatusLine().getReasonPhrase();
            EntityUtils.consume(responseEntity);

            System.out.println("Response Code :  " + responseCode);
            System.out.println("Response Message :  " + responseMessage);
            System.out.println("Response Body :  " + bodyAsString);

        } catch (IOException | ParseException ex) {
            System.out.println("Error Sending SMS. " + ex);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException ex) {
                System.out.println("Error Sending SMS. " + ex);
            }
        }
    }

}
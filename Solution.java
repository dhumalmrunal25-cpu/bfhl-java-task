import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) throws Exception {
        // Step 1: Generate Webhook
        URL url = new URL("https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Update these placeholders with your actual info
        String jsonInput = "{\"name\": \"Mrunal Dhumal\", \"regNo\": \"2027XXXX\", \"email\": \"your@email.com\"}";
        
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes());
        }

        Scanner sc = new Scanner(conn.getInputStream());
        String response = sc.useDelimiter("\\A").next();
        String token = response.split("\"accessToken\":\"")[1].split("\"")[0];

        // Step 2: Submit SQL Query (Question 2 Logic)
        URL submitUrl = new URL("https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA");
        HttpURLConnection submitConn = (HttpURLConnection) submitUrl.openConnection();
        submitConn.setRequestMethod("POST");
        submitConn.setRequestProperty("Content-Type", "application/json");
        submitConn.setRequestProperty("Authorization", token);
        submitConn.setDoOutput(true);

        String sqlQuery = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, " +
                          "(SELECT COUNT(*) FROM EMPLOYEE e2 WHERE e2.DEPARTMENT = e1.DEPARTMENT " +
                          "AND e2.DOB > e1.DOB) AS YOUNGER_EMPLOYEES_COUNT " +
                          "FROM EMPLOYEE e1 JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID " +
                          "ORDER BY e1.EMP_ID DESC;";

        String submitJson = "{\"finalQuery\": \"" + sqlQuery + "\"}";
        try (OutputStream os = submitConn.getOutputStream()) {
            os.write(submitJson.getBytes());
        }

        System.out.println("Status Code: " + submitConn.getResponseCode());
        System.out.println("Submission Successful!");
    }
}

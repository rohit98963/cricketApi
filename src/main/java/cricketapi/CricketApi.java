
package cricketapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class CricketApi {
    
	
    private static int getScores(String score) {
        // If the score string is null or empty, return 0 else return parsed score
        if (score == null ) {
            return 0;
        }
        
           else if(score.isEmpty()){
        	return 0;
               }
        
        	else {
            String[] parts = score.split("/");
            // Parse the first part of the score string into an integer
            int a = Integer.parseInt(parts[0]);

            return a;
        }
    }

    // The printResultMethod is used to print the result
private static void   printResultMethod(int countMatches,int highScore,String highScoringTeam) {
	  System.out.println("Highest Score : " + highScore + "and Team Name is :" + highScoringTeam);
	  System.out.println("Number Of Matches with total 300 Plus Score : " + countMatches);
	
}

    public static void main(String[] args) {
        // This is apiKey and apiURl
        String apiUrl = "https://api.cuvora.com/car/partner/cricket-data";
        String apiKey = "test-creds@2320";

        // Initialize the high scoring team , score and  count matches variable
        String highScoringTeam = "";
        int highScore = 0;
        int countMatches = 0;

        try {
            // Getting  API response as  JSON string and then parse into object
            String jsonResponse = getApiRes(apiUrl, apiKey);
            JSONObject responseObj = new JSONObject(jsonResponse);
            JSONArray matches = responseObj.getJSONArray("data");

            // Iterate through each match in the array
            int n = matches.length();
            for (int x = 0; x < n; x++) {
                JSONObject match = matches.getJSONObject(x);

                // Get scores for team 1 and team 2 by calling getScores method
                int t1Score = getScores(match.getString("t1s"));
                int t2Score = getScores(match.getString("t2s"));

                // update the  high scoring team 
                   if (t1Score > highScore) {
                   highScore = t1Score;
                   highScoringTeam = match.getString("t1");
                         }

                   if (t2Score > highScore) {
                    highScore = t2Score;
                    highScoringTeam = match.getString("t2");
                          }

                // Counting the matches in which total score is  > 300
                if (t1Score + t2Score > 300) {
                    countMatches++;
                           }
           }

            // Print results by printResultMethod
          printResultMethod(countMatches,highScore,highScoringTeam);

        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private static String getApiRes(String apiUrl, String apiKey) throws Exception {
        // API url created by URL object
          URL url = new URL(apiUrl);
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("GET");
        connection.setRequestProperty("apiKey", apiKey);
          BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        // Read the response line by line and build a string
         String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Return the response as a string
        String res = response.toString();
        return res;
    }


}
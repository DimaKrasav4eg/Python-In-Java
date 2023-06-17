package starter;

import com.google.common.io.ByteStreams;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PyStart {
    private static final String START_JSON = "start json";
    private static final String END_JSON = "end json";

    public static void main(String[] args) throws IOException, InterruptedException {
        String pathSavingJSON = "src/main/results/resultJava.json";
        // Create a ProcessBuilder instance for the Python interpreter
        ProcessBuilder pb = new ProcessBuilder(
                "python3", "src/main/python/nn-v2.py"); //nn-v2.py
        pb.redirectErrorStream(true);
        // Start the Python process
        Process process = pb.start();

        // Get the output stream of the Python process
        OutputStream stdin = process.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        // Data preparation
        InputStream inputStream =
                new BufferedInputStream(
                        Files.newInputStream(
                                Paths.get("src/main/resources/man-and-table.jpeg")));

        ByteStreams.copy(inputStream, stdin);


        // Write data to the Python process
//        writer.write("Hello from Java\n");
//        writer.flush();

        // Close the input stream to signal end of input to the Python process
        writer.close();

        // Get the input stream of the Python process
        InputStream stdout = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));

        // Read the output of the Python process
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        //receipt JSON
        JSONObject json = parseJson(result.toString(), START_JSON, END_JSON);
        try (PrintWriter out = new PrintWriter(new FileWriter(pathSavingJSON))) {
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Wait for the Python process to exit
        int exitCode = process.waitFor();
        System.out.println("Python process exited with code " + exitCode);
    }

    public static JSONObject parseJson(String s,
                                       String startSymbol,
                                       String endSymbol){
        String result = "";
        String regex = startSymbol + ".+" + endSymbol;
        Matcher matcher = Pattern.compile(regex).matcher(s);
        if (matcher.find()){
            result = matcher.group();
        }
        int startJSONIndex = START_JSON.length()+1;
        int endJsonIndex = result.length() - END_JSON.length()-1;
        return new JSONObject(
                result.substring(startJSONIndex, endJsonIndex));
    }
}
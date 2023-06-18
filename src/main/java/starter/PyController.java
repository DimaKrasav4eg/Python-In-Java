package starter;

import com.google.common.io.ByteStreams;
import org.json.JSONObject;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PyController {
    private static final String START_JSON = "start json";
    private static final String END_JSON = "end json";
    private final ProcessBuilder processBuilder;
    private final Process process;
    private OutputStream pyStdin;
    private InputStream pyStdout;
    private JSONObject json;

    public PyController(String command, String nnPath) throws IOException {
        processBuilder = new ProcessBuilder(command, nnPath);
        processBuilder.redirectErrorStream(true);
        this.process = processBuilder.start();
        this.pyStdout = this.process.getInputStream();
    }

    public void sendImage(InputStream image) throws IOException, InterruptedException {
        this.pyStdin = this.process.getOutputStream();
        ByteStreams.copy(image, this.pyStdin);
        pyStdin.close();
    }
    public String getOutput() throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.pyStdout));

        // Read the output of the Python process
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
            result.append("\n").append(line);
        }
        this.json = parseJson(result.toString(), START_JSON, END_JSON);
        int exitCode = this.process.waitFor();
        result.append("\nPython process exited with code ").append(exitCode);
        return result.toString();
    }
    private JSONObject parseJson(String s,
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
    public void save(String s, String filePath) throws IOException {
        PrintWriter file = new PrintWriter(new FileWriter(filePath));
        file.write(json.toString());
    }
    public JSONObject getJson(){
        return json;
    }
}

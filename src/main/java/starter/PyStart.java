package starter;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PyStart {
    public static void main(String[] args) throws IOException, InterruptedException {
        String imagePath = "src/main/resources/man-and-table.jpeg";
        String resultPath = "src/main/results/resultJava.json";
        if (args.length == 2){
            imagePath = args[0];
            resultPath = args[1];
        }

        PyController pyController = new PyController("python3",
                "src/main/python/nn-v2.py");
        InputStream image =
                new BufferedInputStream(Files.newInputStream(
                                Paths.get(imagePath)));
        pyController.sendImage(image);
        JSONObject result = pyController.getJson();
        pyController.save(result, resultPath);
    }
}
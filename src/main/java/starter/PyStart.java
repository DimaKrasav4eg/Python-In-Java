package starter;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


// java -jar PyStart imagePath resultPath
public class PyStart {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 2){
            throw new IllegalArgumentException("Exactly 2 parameters required !");
        }
        String imagePath = args[0];
        String resultPath = args[1];

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
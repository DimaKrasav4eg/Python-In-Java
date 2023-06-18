package starter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PyStart {
    public static void main(String[] args) throws IOException, InterruptedException {
        final String pathImage = "src/main/resources/man-and-table.jpeg";
        PyController pyController = new PyController("python3",
                "src/main/python/nn-v2.py");
        InputStream image =
                new BufferedInputStream(Files.newInputStream(
                                Paths.get(pathImage)));
        pyController.sendImage(image);
        System.out.println(pyController.getOutput());
    }
}
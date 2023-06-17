package starter;

import com.google.common.io.ByteStreams;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PyStart {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Create a ProcessBuilder instance for the Python interpreter
        ProcessBuilder pb = new ProcessBuilder("python3", "src/main/python/nn-v2.py"); //nn-v2.py
        pb.redirectErrorStream(true);
        // Start the Python process
        Process process = pb.start();

        // Get the output stream of the Python process
        OutputStream stdin = process.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));

        // Data preparation
        InputStream inputStream =
                new BufferedInputStream(Files.newInputStream(Paths.get("src/main/resources/man-and-table.jpeg")));

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
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // Wait for the Python process to exit
        int exitCode = process.waitFor();
        System.out.println("Python process exited with code " + exitCode);
    }
}
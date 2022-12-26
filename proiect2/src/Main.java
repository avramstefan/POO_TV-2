import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import input.Input;
import platform.Platform;

import java.io.File;
import java.io.IOException;

public final class Main {

    private Main() {

    }

    /**
     * Main function to filter the I/O and triggering the platform to run.
     * @param args keeping input path and output path
     * @throws IOException for failed I/O operations
     */
    public static void main(final String[] args) throws IOException {

        String inputPath = args[0];
        String outputDirPath = args[1];

        ObjectMapper objMapper = new ObjectMapper();
        Input inputData = objMapper.readValue(new File(inputPath), Input.class);

        ArrayNode output = objMapper.createArrayNode();

        Platform platform = Platform.getInstance(inputData);
        platform.runActions(output);

        Platform.setPlatform(null);

        String[] firstSplitArray = inputPath.split("_");
        String firstSplit = firstSplitArray[firstSplitArray.length - 1];
        String outputPath = "basic_" + firstSplit;

        ObjectWriter objectWriter = objMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(outputDirPath), output);
        objectWriter.writeValue(new File(inputPath.replace("in", "out")), output);
    }
}

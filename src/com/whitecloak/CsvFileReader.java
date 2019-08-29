package com.whitecloak;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class CsvFileReader {

    public static Stream<String> readAllFilesInDirectory(Path directoryPath) throws IOException {
        return Files.walk(directoryPath)
                .filter(Files::isRegularFile)
                .flatMap(CsvFileReader::read);
    }

    public static Stream<String> read(Path filePath) {
        try {
            return Files.lines(filePath);
        } catch(IOException e) {
            e.printStackTrace(System.err);
            return Stream.empty();
        }
    }

}

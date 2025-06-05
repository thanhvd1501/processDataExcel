package org.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ReaderAndWriterByBuffer {
    private static void writeFile() {
        Path path = Paths.get("D:/test.tsv");
        Path outputPath = Paths.get("D:/test_v3.tsv");

        try(Stream<String> lines = Files.lines(path);
            BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            AtomicInteger i = new AtomicInteger();
            lines.skip(1)
                    .forEach(l -> {
                        var column = l.split("\t");
                        try {
                            if (column.length > 2) {
                                writer.write(String.format("%s\t%s\t%s\n",
                                        column[0].replace("vd9", "vd9_" + i),
                                        column[1] + "_" + i,
                                        column[2].replace("1", "_" + i)));
                                i.getAndIncrement();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        writeFile();
    }
}

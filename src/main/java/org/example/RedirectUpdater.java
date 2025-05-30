package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.data.RedirectEntry;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RedirectUpdater {

    private static Map<String, RedirectEntry> readExcel(String filePath) {
        Map<String, RedirectEntry> map = new HashMap<>();
        try (var reader = new FileInputStream(filePath)) {
            var workBook = new XSSFWorkbook(reader);
            var sheet = workBook.getSheetAt(0);
            sheet.forEach(row -> {
                if (row.getRowNum() != 0) {
                    Cell cell1 = row.getCell(0);
                    Cell cell2 = row.getCell(1);
                    Cell cell3 = row.getCell(2);
                    if (cell1 != null && cell2 != null && cell3 != null) {
                        String urlSource = cell1.toString();
                        String urlDestination = cell2.toString();
                        String status = cell3.toString();
                        if (status.equals("modify")) {
                            map.put(urlSource, new RedirectEntry(urlSource, urlDestination));
                        }
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    private static String cleanPath(String path) {
        return path.trim().replaceAll("/+$", "");
    }

    public static void main(String[] args) throws IOException {

        String excelPath = "C:/tmp/excelRedirect.xlsx";
        String redirectPath = "C:/tmp/redirect.map";

        Map<String, RedirectEntry> entries = readExcel(excelPath);
        Map<String, RedirectEntry> entriesContain = new HashMap<>();

        File inputFile = new File(redirectPath);
        File tempFile = new File("redirect_temp.map");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            boolean insideBlock = false;
            while ((line = reader.readLine()) != null) {
                String urlComplex = line.trim();
//                System.out.println(urlComplex.equals("location: {"));
//                System.out.println(urlComplex);
                if (urlComplex.equals("location {")) {
                    insideBlock = true;
                }
                String[] parts = urlComplex.replace(";", "").split("\\s+");
                if (parts.length >= 2) {
                    String urlSource = cleanPath(parts[0]);
                    String urlDestination = parts[1];
                    String keyTmp = urlSource + "/";
                    if (entries.containsKey(keyTmp)) {
                        var correct = entries.get(keyTmp);
                        entriesContain.put(keyTmp, new RedirectEntry(keyTmp, urlDestination));
                        if (!urlDestination.equals(correct.getUrlDestination())) {
                            String updatedLine = "  " + parts[0] + " " + correct.getUrlDestination() + ";";
                            writer.write(updatedLine);
                            writer.newLine();
                            System.out.println("Updated: " + updatedLine);
                            continue;
                        }
//                        System.out.println("Not update: " + urlComplex);
                    }
                }

                if (urlComplex.equals("}") && insideBlock) {
                    entries.forEach((k, v) -> {
                        String keyTmp = k.trim();
                        if (Objects.isNull(entriesContain.get(k))) {
                            System.out.println("Exist in excel file but not exist in redirect.map file: " + k);
                            try {
                                if (k.contains(".*")) {
                                    keyTmp = ".~" + k.substring(0, k.indexOf(".*")).trim() + "(.*)";
                                    writer.write("  " + keyTmp + " " + v.getUrlDestination() + ";");
                                    writer.newLine();
                                    return;
                                }
                                writer.write("  " + keyTmp + " " + v.getUrlDestination() + ";");
                                writer.newLine();
                                writer.write("  " + cleanPath(keyTmp) + " " + v.getUrlDestination() + ";");
                                writer.newLine();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    insideBlock = false;
                }


                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Files.move(tempFile.toPath(), inputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}

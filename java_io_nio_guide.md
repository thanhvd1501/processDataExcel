
# 📚 Hướng dẫn đầy đủ về Java IO và Java NIO

## 📌 I. Tổng quan: Java IO vs Java NIO

| Java IO (java.io) | Java NIO (java.nio) |
|-------------------|---------------------|
| Là API cũ, dựa trên luồng (Stream) | Là API mới, dựa trên Channel và Buffer |
| Blocking (đợi cho tới khi xong) | Non-blocking (không cần chờ hoàn tất) |
| Đơn giản, quen thuộc | Hiệu suất cao hơn, đặc biệt với file lớn |

## 🛠️ II. Java IO (Truyền thống)

### 🎯 Các class chính:
- **FileInputStream/FileOutputStream:** Đọc/ghi dữ liệu dạng byte.
- **FileReader/FileWriter:** Đọc/ghi dữ liệu dạng ký tự (text).
- **BufferedReader/BufferedWriter:** Dùng buffer giúp đọc ghi nhanh hơn.

### Ví dụ đọc ghi file dạng byte:
```java
try (FileInputStream fis = new FileInputStream("anh.jpg");
     FileOutputStream fos = new FileOutputStream("copy_anh.jpg")) {
    byte[] buffer = new byte[4096];
    int bytesRead;
    while ((bytesRead = fis.read(buffer)) != -1) {
        fos.write(buffer, 0, bytesRead);
    }
}
```

### Ví dụ đọc ghi file dạng text:
```java
try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
     BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        writer.write(line.toUpperCase());
        writer.newLine();
    }
}
```

## 🚀 III. Java NIO (Hiện đại, hiệu suất cao)

### Ví dụ dùng Files:
```java
Path inputPath = Paths.get("input.txt");
Path outputPath = Paths.get("output.txt");
List<String> lines = Files.readAllLines(inputPath);
Files.write(outputPath, lines);
```

### Ví dụ xử lý file lớn với Stream:
```java
Path inputPath = Paths.get("big_file.txt");
Path outputPath = Paths.get("output_big_file.txt");
try (Stream<String> stream = Files.lines(inputPath);
     BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
    stream.forEach(line -> {
        try {
            writer.write(line.toLowerCase());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });
}
```

### Ví dụ Channel và Buffer:
```java
Path source = Paths.get("video.mp4");
Path target = Paths.get("copy_video.mp4");
try (FileChannel sourceChannel = FileChannel.open(source, StandardOpenOption.READ);
     FileChannel targetChannel = FileChannel.open(target, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
    ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
    while (sourceChannel.read(buffer) > 0) {
        buffer.flip();
        targetChannel.write(buffer);
        buffer.clear();
    }
}
```

## 🔖 IV. Các thao tác thường gặp:
- Tạo thư mục: `Files.createDirectories(dir);`
- Copy/Move/Delete file: `Files.copy(...)`, `Files.move(...)`, `Files.deleteIfExists(...)`

## 📈 V. So sánh IO vs NIO:
- IO: Dễ hiểu, nhưng hiệu suất thấp với file lớn.
- NIO: Phức tạp hơn nhưng hiệu quả cao hơn với dữ liệu lớn.

## 🧠 VI. Mẹo hữu ích:
- Luôn dùng `try-with-resources`.
- Dùng bộ đệm Buffer.
- Buffer size tối ưu từ 4KB - 8KB (IO), 1MB - 4MB (NIO).

## 🎓 VII. Bài tập thực hành:
- Copy thư mục lớn dùng NIO.
- Xử lý file CSV.
- Đếm số từ trong file văn bản lớn.

## 📖 VIII. Tài liệu tham khảo:
- [Oracle Java Tutorials – IO](https://docs.oracle.com/javase/tutorial/essential/io/)
- [Oracle Java Tutorials – NIO](https://docs.oracle.com/javase/tutorial/essential/io/fileio.html)
- [Baeldung Java IO vs NIO](https://www.baeldung.com/java-io-vs-nio)

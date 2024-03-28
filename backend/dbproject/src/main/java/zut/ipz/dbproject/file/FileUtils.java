package zut.ipz.dbproject.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class FileUtils {
    public static List<String> getLines(MultipartFile sqlFile) {
        List<String> lines;

        try (var br = new BufferedReader(new InputStreamReader(sqlFile.getInputStream()))) {
            lines = br.lines().toList();
        } catch (IOException e) {
            throw new IllegalFormatCodePointException(2);
        }

        return lines;
    }
}

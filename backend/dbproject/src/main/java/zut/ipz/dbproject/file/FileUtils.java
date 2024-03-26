package zut.ipz.dbproject.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class FileUtils {
    /**
     * This method reads a file and returns a list of strings.
     *
     * @param sqlFile is a file that will be read
     * @return a list of strings
     */
    public static List<String> getLines(MultipartFile sqlFile) {
        List<String> lines;
        try (var br = new BufferedReader(new InputStreamReader(sqlFile.getInputStream()))) {
            lines = br.lines().toList();

        } catch (IOException e) {
            throw new IllegalFormatCodePointException(2);
        }

//        List<String> lines = null;
//        try (Stream<String> streamLines = Files.lines(Path.of(sqlFile.getName()))) {
//            lines = streamLines.toList();
//        } catch (IOException e) {
//            System.err.println(e.getMessage());
//        }

        return lines;
    }
}

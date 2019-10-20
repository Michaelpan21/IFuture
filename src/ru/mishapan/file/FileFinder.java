package ru.mishapan.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*TODO
1) Написать класс, перебирающий файлы в папке
2) Добавить возможно задавать расширение файлов
 */
public class FileFinder {

    public List<Path> findFiles(Path path, String glob) {

        List<Path> pathList = new ArrayList<>();

        try {
            Iterable<Path> stream = Files.newDirectoryStream(path, glob);
            stream.forEach(pathList::add);

        } catch (IOException | DirectoryIteratorException ex) {
            ex.printStackTrace();
        }

        return pathList;
    }

    /*
    Прочитать строку, если найдено частичное совпадение, проверить продолжение в другой строке
     */
    public List<Path> findTextInFiles(List<Path> pathList, Pattern pattern) {

        if (pathList.size() == 0) throw new IllegalArgumentException("No files to search");

        List<Path> filesWithMatches = new ArrayList<>();

        pathList.forEach(path -> {
            try (BufferedReader bf = new BufferedReader(new FileReader(path.toString()))) {

                String fileLine;
                while ((fileLine = bf.readLine()) != null) {

                    Matcher matcher = pattern.matcher(fileLine);

                    if (matcher.find()) {
                        filesWithMatches.add(path);
                        System.out.println(fileLine);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return filesWithMatches;
    }

    public static void main(String[] args) {

        FileFinder files = new FileFinder();
        Path path = FileSystems.getDefault().getPath("C:/Users/Михаил/Desktop/findMe");

        Pattern pattern = Pattern.compile("Error 404: Server not found");
        files.findTextInFiles(files.findFiles(path, "*.log"), pattern);

    }


}

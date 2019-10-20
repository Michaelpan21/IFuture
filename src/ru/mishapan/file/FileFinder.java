package ru.mishapan.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
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
    public List<Path> findTextInFiles(List<Path> pathList, String text) {

        if (pathList.size() == 0) throw new IllegalArgumentException("No files to search");

        String[] textWords = text.split(" ");

        List<Path> filesWithMatches = new ArrayList<>();

        pathList.forEach(path -> {
            try (BufferedReader bf = new BufferedReader(new FileReader(path.toString()))) {

                int matchCounter = 0;
                boolean isMatch = false;
                int itr = 0;
                String fileLine;

                while ((fileLine = bf.readLine()) != null) {

                    String[] fileWords = fileLine.split(" ");

                    for (int i = 0; i < fileWords.length; i++) {

                        if (fileWords[i].equals(textWords[itr])) {

                            matchCounter++;

                            for (int j = i + 1; j < fileWords.length; j++) {
                                if (fileWords[j].equals(textWords[itr])) {
                                    matchCounter++;
                                }
                                else{
                                    matchCounter = 0;
                                    itr = 0;
                                    break;
                                }
                                /*if (j == fileWords.length - 1 && j != textWords.length - 1 && matchCounter != 0) {
                                    isMatch = true;
                                }*/
                                if (matchCounter == textWords.length) {
                                    isMatch = true; System.out.println(fileLine);
                                    break;
                                }

                                itr++;
                            }
                        }
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

        Pattern pattern = Pattern.compile("Error[\\s\n]404:[\\s\n]Server[\\s\n]not[\\s\n]found");
        //Pattern pattern1 = Pattern.compile("人去买酒");
        String text = "Error 404: Server not found";
        files.findTextInFiles(files.findFiles(path, "*.log"), text);

    }


}

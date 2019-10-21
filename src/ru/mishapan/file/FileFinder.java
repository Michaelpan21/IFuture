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

/*TODO
1) кодировка
 */
public class FileFinder {

    public List<Path> findFiles(Path path, String glob) {

        List<Path> filesPathList = new ArrayList<>();

        try {
            Iterable<Path> stream = Files.newDirectoryStream(path, glob);
            stream.forEach(filesPathList::add);

        } catch (IOException | DirectoryIteratorException ex) {
            ex.printStackTrace();
        }

        return filesPathList;
    }

    public List<Path> findTextInFiles(List<Path> pathList, String text) {

        if (pathList.size() == 0) throw new IllegalArgumentException("No files to search");

        char[] textChars = text.toCharArray();

        List<Path> filesWithMatches = new ArrayList<>();

        pathList.forEach(path -> {
            try (BufferedReader bf = new BufferedReader(new FileReader(path.toString()))) {

                String fileLine;
                int itr = 0;
                boolean toAdd = false;

                while ((fileLine = bf.readLine()) != null) {

                    char[] fileChars = fileLine.toCharArray();
                    for (int i = 0; i < fileChars.length; i++) {

                        if (fileChars[i] == textChars[itr]) {

                            itr++;
                            for (int j = i + 1; j < fileChars.length; j++) {

                                if (fileChars[j] != textChars[itr]) {
                                    itr = 0;
                                    break;
                                }

                                if (itr == textChars.length - 1) {
                                    i = fileChars.length - 1;
                                    itr = 0;
                                    toAdd = true;
                                    break;
                                }

                                if (j == fileChars.length - 1) {
                                    i = fileChars.length - 1;
                                    itr++;
                                    break;
                                }
                                itr++;
                            }
                        }
                    }
                }

                if (toAdd){
                    filesWithMatches.add(path);
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

        String text = "Error 404: Server not found";
        List<Path> st = files.findTextInFiles(files.findFiles(path, "*.*"), text);

        st.forEach(System.out::println);

    }


}

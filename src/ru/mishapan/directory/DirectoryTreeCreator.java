package ru.mishapan.directory;

import ru.mishapan.file.FileFinder;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/*TODO
 Задание: найти папку, заданную пользователем, и построить дерево каталогов
 1) Разобраться с символьными ссылками
 2) подумать над возвращаемым значением createTree
 */
public class DirectoryTreeCreator extends SimpleFileVisitor<Path> {

    private List<Path> pathList;
    private String folderName;

    public DirectoryTreeCreator() {
        pathList = new ArrayList<>();
    }

    public List<Path> getPathList() {
        return pathList;
    }

    private void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public String[] createTree(Path path) {

        if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
            throw new InvalidPathException(path.toString(), "Can not find folder by this path");
        }
        String[] folders = new String[path.getNameCount() + 1];

        folders[0] = path.getRoot().toString();

        for (int i = 0, j = 1; i < path.getNameCount(); i++, j++) {
            folders[j] = path.getName(i).toString();
        }

        return folders;
    }

    public List<Path> findFolders(String folderName, Path startDir, FileVisitor<Path> visitor) {

        setFolderName(folderName);

        try {
            Files.walkFileTree(startDir, visitor);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return getPathList();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {

        if (attrs.isDirectory() && dir.getFileName().toString().equals(getFolderName())) {
            pathList.add(dir);
        }
        return FileVisitResult.CONTINUE;
    }

    public static void main(String[] args) {

        System.out.println("\n\n");

        DirectoryTreeCreator dc = new DirectoryTreeCreator();

        Path startDir = Paths.get("C:/Users/Михаил/Desktop/");

        FileFinder fileFinder = new FileFinder();

        String text = "Error 404:";

        dc.findFolders("findMe", startDir, dc).forEach(path -> {

            List<Path> list = fileFinder.findFiles(path, "*.*");
            if (list.size() > 0) {
                fileFinder.findTextInFiles(list, text).forEach(System.out::println);
            }
        });

    }
}

package ru.mishapan.directory;

import java.nio.file.*;

/*TODO
 Задание: найти папку, заданную пользователем, и построить дерево каталогов
 1) Написать метод для поиска папки по абслолютному пути
    Метод должен возращать дерево папок
 2) Написать метод для поиска папок по названию
    Метод должен возращать дерево папок
 */
public class DirectoryTreeCreator {

    public String[] createTree(Path path) {

        if (Files.notExists(path, LinkOption.NOFOLLOW_LINKS)) {
            throw new InvalidPathException(path.toString(), "Can not find folder by this path");
        }
        String[] folders = new String[path.getNameCount() + 1];

        folders[0] = path.getRoot().toString();

        for (int i = 0, j = 1 ; i < path.getNameCount(); i++, j++ ){
            folders[j] = path.getName(i).toString();
        }

        return folders;
    }

    public static void main(String[] args) {
        DirectoryTreeCreator tree = new DirectoryTreeCreator();
        Path path = FileSystems.getDefault().getPath("C:/Users/Михаил/Desktop/findMe");
        String[] folders = tree.createTree(path);

        for (String str : folders) {
            System.out.println(str);
        }
    }
}

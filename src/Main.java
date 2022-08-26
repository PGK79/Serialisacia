import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(90, 20, 10, 250.32);
        GameProgress gameProgress2 = new GameProgress(80, 15, 7, 244.32);
        GameProgress gameProgress3 = new GameProgress(70, 10, 5, 234.32);

        String path1 = "C://Projects/Serialisacia/savegames/save0.data";
        String path2 = "C://Projects/Serialisacia/savegames/save1.data";
        String path3 = "C://Projects/Serialisacia/savegames/save2.data";

        List<String> list = Arrays.asList(path1, path2, path3);

        String zipPath = "C://Projects/Serialisacia/savegames/zipSaveGames.zip";
        String anZipPath = "C://Projects/Serialisacia/savegames/";

        File dir = new File("C://Projects/Serialisacia/savegames");
        if (dir.mkdir()) {
            System.out.println("Каталог создан");
        } else {
            System.out.println("Каталог не создан");
        }

        saveGame(path1, gameProgress1);
        saveGame(path2, gameProgress2);
        saveGame(path3, gameProgress3);

        zipFiles(zipPath, list);

        deleteFiles(path1); // можно и одним вызовом, если в метод передать list
        deleteFiles(path2); // пройтись foreach и  убрать все что было в списке на
        deleteFiles(path3); // архивирование

        openZip(zipPath, anZipPath);

        openProgress(path1);
    }

    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void zipFiles(String zipPath, List<String> list) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (int i = 0; i < list.size(); i++) {
                FileInputStream fis = new FileInputStream(list.get(i));
                ZipEntry entry = new ZipEntry("save" + i + ".data");
                byte[] bytes = new byte[fis.available()];
                zos.putNextEntry(entry);
                fis.read(bytes);
                zos.write(bytes);
                zos.closeEntry();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openZip(String zipPath, String anZipPath) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(anZipPath + name);

                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }

                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteFiles(String path) {
        File file = new File(path);
        file.delete();
    }

    public static void openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(gameProgress);
    }
}
package se.fidde.thepenguinstory.util.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;

import se.fidde.thepenguinstory.domain.penguin.Penguin;
import android.util.Log;

import com.google.gson.Gson;

public class PenguinFactory {

    private static String SAVE_FILE = "/penguin.json";
    private static final String FACTORY = "penguinFactory";

    private PenguinFactory() {
    }

    public static boolean savePenguin(Penguin penguin, String folder) {
        Log.d(FACTORY, "saving penguin");
        try {
            saveToFile(penguin, folder);
            Log.d(FACTORY, "penguin saved");

        } catch (Exception e) {
            Log.e(FACTORY, "error saving penguin");
            return false;
        }
        return true;
    }

    private static void saveToFile(Penguin penguin, String folder)
            throws JSONException, IOException {
        String penguinAsJson = getPenguinAsJson(penguin);

        Log.d(FACTORY, "saving to " + folder + SAVE_FILE);
        File file = new File(folder + SAVE_FILE);
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        try {
            fileOutputStream.write(penguinAsJson.getBytes());
            Log.d(FACTORY, "penguin save complete");

        } finally {
            fileOutputStream.close();
        }
    }

    private static String getPenguinAsJson(Penguin penguin)
            throws JSONException {

        Gson gson = new Gson();
        return gson.toJson(penguin);
    }

    public static Penguin loadPenguin(String folder) {
        try {
            Log.d(FACTORY, "loading penguin");
            Penguin penguin = parseJson(folder);

            if (penguin.isDead())
                return new Penguin();

            return penguin;

        } catch (FileNotFoundException e) {
            Log.e(FACTORY, "no file found, creating new penguin");
            return new Penguin();
        }
    }

    private static Penguin parseJson(String folder)
            throws FileNotFoundException {
        File file = new File(folder + SAVE_FILE);
        FileReader fileReader = new FileReader(file);

        Gson gson = new Gson();
        Penguin penguin = gson.fromJson(fileReader, Penguin.class);
        return penguin;
    }

}

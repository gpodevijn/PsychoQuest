package be.ac.ulb.iridia.psychoquest;

import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;

/**
 * Created by gaetan on 09.11.15.
 */
public class Utils {

    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile)  {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            try {
                fromChannel.transferTo(0, fromChannel.size(), toChannel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (fromChannel != null) {
                    try {
                        fromChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                if (toChannel != null) {
                    try {
                        toChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void saveOnDropbox(DropboxAPI<AndroidAuthSession> dbAPI, File fileToSave,
                                     String destination) {
        InputStream is = null;
        try {
            is = new FileInputStream(fileToSave);
            putToDropbox(dbAPI, is,fileToSave.length(), destination);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveOnDropbox(DropboxAPI<AndroidAuthSession> dbAPI, String contentFile,
                                     String destination) {
        InputStream is = new ByteArrayInputStream(contentFile.getBytes());
        putToDropbox(dbAPI, is, contentFile.length(), destination);
    }

    private static void putToDropbox(DropboxAPI<AndroidAuthSession> dbAPI, InputStream is,
                                     long length, String destination) {
        DropboxAPI.Entry response = null;
        try {
            if (dbAPI == null) {
                Log.d("Utils", "dbapi is null");
            }
            response = dbAPI.putFile(destination, is,
                    length, null, null);
            Log.d("Utils", "response put file " + response);
        } catch (DropboxException e) {
            e.printStackTrace();
        }
    }

    public static void saveOnSD(String filepath, String content) {
        Log.d("Utils", "save on sd");
        String resultFilePath = filepath;
        File file = new File(resultFilePath);
        file.getParentFile().mkdirs();

        PrintWriter out = null;
        try {
            out = new PrintWriter(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (out != null) {
            out.println(content);
            out.close();
        }

    }

    public static void saveOnSD(String dir, File from, File to) {

        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        if (!to.exists()) {
            try {
                Utils.copyFile(new FileInputStream(from), new FileOutputStream(to));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}

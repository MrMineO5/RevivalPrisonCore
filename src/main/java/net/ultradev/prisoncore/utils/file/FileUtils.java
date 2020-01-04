/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.utils.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class FileUtils {
    @Nullable
    public static String loadFile(@NotNull File f) {
        if (!f.exists()) {
            try {
                if (f.getParentFile().mkdirs()) {
                    if (f.createNewFile()) {
                        return "";
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            StringBuilder str = new StringBuilder();
            while (stream.available() > 0) {
                int i = stream.read();
                str.append((char) i);
            }
            return str.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

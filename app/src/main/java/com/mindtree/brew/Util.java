package com.mindtree.brew;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by M1036144 on 18-Apr-16.
 */

public class Util {
    public static String getProperty(String key,Context context) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(Constants.EMAIL_INFO_PROPERTIES);
        properties.load(inputStream);
        return properties.getProperty(key);

    }
}

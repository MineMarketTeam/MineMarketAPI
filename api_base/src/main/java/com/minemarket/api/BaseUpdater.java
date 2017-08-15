package com.minemarket.api;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public interface BaseUpdater {

	public void update();
	
    public default void downloadFile(String downloadURL, String savePath) throws IOException{
        URL download = new URL(downloadURL);
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
          
            in = new BufferedInputStream(download.openStream());
            fout = new FileOutputStream(savePath);

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }
	
}

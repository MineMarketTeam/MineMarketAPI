package com.minemarket.api;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Esta interface deve ser implementada para atualiza��es autom�ticas da aplica��o, qunado houver uma nova vers�o dispon�vel.
 */
public interface BaseUpdater {

    /**
     * Este m�todo ser� chamado toda vez que houver a necessidade de realizar uma atualiza��o.
     * Deve conter todo o procedimento necess�rio para que o sistema seja atualizado para a nova vers�o.
     */
    void update();

    /**
     * Fun��o que auxilia no donwload de arquivos via HTTP.
     *
     * @param downloadURL a URL de origem.
     * @param savePath    o local de arquivo destino.
     * @throws IOException Disparada caso aconte�a alguma falha durante a opera��o.
     */
    default void downloadFile(String downloadURL, String savePath) throws IOException {
        URL download = new URL(downloadURL);
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {

            in = new BufferedInputStream(download.openStream());
            fout = new FileOutputStream(savePath);

            final byte[] data = new byte[1024];
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

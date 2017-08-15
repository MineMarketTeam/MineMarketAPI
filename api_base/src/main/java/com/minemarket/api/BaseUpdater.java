package com.minemarket.api;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Esta interface deve ser implementada para atualizações automáticas da aplicação, qunado houver uma nova versão disponível.
 *
 */
public interface BaseUpdater {

	/**
	 * Este método será chamado toda vez que houver a necessidade de realizar uma atualização.
	 * Deve conter todo o procedimento necessário para que o sistema seja atualizado para a nova versão.
	 */
	public void update();
	
	/**
	 * Função que auxilia no donwload de arquivos via HTTP.
	 * @param downloadURL a URL de origem.
	 * @param savePath o local de arquivo destino.
	 * @throws IOException Disparada caso aconteça alguma falha durante a operação.
	 */
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

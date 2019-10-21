package com.swein.exceptionreport.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileIOUtil {

    public static void copyFileUsingFileChannels( File source, File dest )
            throws IOException {
        FileChannel inputChannel  = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream( source ).getChannel();
            outputChannel = new FileOutputStream( dest ).getChannel();
            outputChannel.transferFrom( inputChannel, 0, inputChannel.size() );
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
        finally {
            if(inputChannel != null) {
                inputChannel.close();
            }
            if(outputChannel != null) {
                outputChannel.close();
            }
        }
    }

}

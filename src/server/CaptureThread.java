package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * CaptureThread
 */
public class CaptureThread extends Thread {
    protected boolean running;
    ByteArrayOutputStream out;

    ObjectOutputStream output;

    public CaptureThread(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void run() {
        try {
            while (true) {
                final AudioFormat format = getFormat();
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            
                int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                line.open(format);
                line.start();

                line.read(buffer, 0, buffer.length);
                
                output.writeObject( buffer );
                
                line.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AudioFormat getFormat() {
        float sampleRate = 8000;
        int sampleSizeInBits = 8;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}
package client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class PlayerThread extends Thread {
    ObjectInputStream entrada;

    public PlayerThread(ObjectInputStream entrada) {
        this.entrada = entrada;
    }

    @Override
    public void run() {
        try {

            AudioFormat format = getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            while (true) {
                byte[] audio = (byte[]) entrada.readObject();
                InputStream input = new ByteArrayInputStream(audio);
                AudioInputStream ais = new AudioInputStream(input, format, audio.length / format.getFrameSize());

                line.open(format);
                line.start();
                int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];
                int count;
                while ((count = ais.read(buffer, 0, buffer.length)) != -1) {
                    if (count > 0) {
                        line.write(buffer, 0, count);
                    }
                }
                line.drain();
                line.close();
            }
        }

        catch (Exception e) {
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
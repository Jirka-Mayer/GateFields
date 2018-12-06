package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.scheme.Scheme;

import java.io.*;
import java.util.Arrays;

public class Saver {
    private static final String MAGIC = "GateFields scheme file";
    private static final int FILE_VERSION = 1;

    private Scheme scheme;
    private Camera camera;

    public Saver(Scheme scheme, Camera camera) {
        this.scheme = scheme;
        this.camera = camera;
    }

    public boolean saveTo(File file) {
        try (FileOutputStream stream = new FileOutputStream(file)) {
            this.saveTo(stream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveTo(OutputStream stream) throws IOException {
        DataOutputStream dataStream = new DataOutputStream(stream);

        dataStream.write(MAGIC.getBytes());
        dataStream.writeInt(FILE_VERSION);

        camera.writeTo(dataStream);
        scheme.writeTo(dataStream);

        dataStream.flush();
    }

    public boolean loadFrom(File file) {
        try (FileInputStream stream = new FileInputStream(file)) {
            this.loadFrom(stream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void loadFrom(InputStream stream) throws IOException {
        DataInputStream dataStream = new DataInputStream(stream);

        byte[] expectedMagic = MAGIC.getBytes();
        byte[] magicBuffer = new byte[expectedMagic.length];
        dataStream.read(magicBuffer);

        if (!Arrays.equals(expectedMagic, magicBuffer))
            throw new IOException("Magic does not match.");

        if (dataStream.readInt() != FILE_VERSION)
            throw new IOException("File version not supported.");

        camera.readFrom(dataStream);
        scheme.readFrom(dataStream);
    }
}

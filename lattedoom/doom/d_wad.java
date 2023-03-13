package lattedoom.doom;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class d_wad {

    private static final int HEADER_SIZE = 12;
    private static final int DIRECTORY_ENTRY_SIZE = 16;
    private static final String WAD_SIGNATURE = "IWAD";

    private byte[] header;
    private int numLumps;
    private int directoryOffset;
    private Lump[] lumps;
    String wadFilePath;
    public d_wad(String wadFilePath) throws IOException {
    	this.wadFilePath = wadFilePath;
        File wadFile = new File(wadFilePath);
        if (!wadFile.exists() || !wadFile.isFile()) {
            throw new IllegalArgumentException("Invalid WAD file path");
        }

        FileInputStream fis = new FileInputStream(wadFile);
        header = new byte[HEADER_SIZE];
        fis.read(header);

        String signature = new String(header, 0, 4);
        if (!signature.equals(WAD_SIGNATURE) && !signature.equals("PWAD")) {
            throw new IllegalArgumentException("Invalid WAD file signature");
        }

        numLumps = (getInt(header, 4));
        directoryOffset = (getInt(header, 8));
        // 파싱된 Lump 배열 초기화
        lumps = new Lump[numLumps];
        fis.skip(directoryOffset-HEADER_SIZE);
        for (int i = 0; i < numLumps; i++) {
            byte[] directoryEntry = new byte[DIRECTORY_ENTRY_SIZE];
            fis.read(directoryEntry);
            int lumpOffset = (getInt(directoryEntry, 0));
            int lumpSize = (getInt(directoryEntry, 4));
            String lumpName = new String(directoryEntry, 8, 8).replaceAll("[^\\x20-\\x7e]", "");
            lumps[i] = new Lump(lumpOffset, lumpSize, lumpName);
        }
        fis.close();
    }
    public byte[] getData(Lump lump) {
    	FileInputStream fis;
		try {
			fis = new FileInputStream(new File(wadFilePath));
		
	        byte[] lumpData = new byte[lump.getSize()];
	        fis.skip(lump.getOffset());
	        fis.read(lumpData);
	        fis.close();
	        return lumpData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return null;
    }

    public byte[] getLumpData(int lumpNumber) throws IOException {
        Lump lump = getLump(lumpNumber);
        if (lump != null) {
            FileInputStream fis = new FileInputStream(new File(wadFilePath));
            byte[] lumpData = new byte[lump.getSize()];
            fis.skip(lump.getOffset());
            fis.read(lumpData);
            fis.close();
            return lumpData;
        }
        return null;
    }

    public Lump[] getLumps() {
        return lumps;
    }

    public int getNumLumps() {
        return numLumps;
    }

    private Lump getLump(int lumpNumber) {
        if (lumpNumber >= 0 && lumpNumber < numLumps) {
            return lumps[lumpNumber];
        }
        return null;
    }

    private int getInt(byte[] bytes, int offset) {
        return (bytes[offset + 3] & 0xFF) << 24 |
                (bytes[offset + 2] & 0xFF) << 16 |
                (bytes[offset + 1] & 0xFF) << 8 |
                (bytes[offset] & 0xFF);
    }
    public short getShort(byte[] bytes, int offset) {
    	return (short)((bytes[offset + 1] & 0xFF) << 8 | (bytes[offset] & 0xFF));
    }

    public static class Lump {
        private int offset;
        private int size;
        private String name;

        public Lump(int offset, int size, String name) {
            this.offset = offset;
            this.size = size;
            this.name = name;
        }

        public int getOffset() {
            return offset;
        }

        public int getSize() {
            return size;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "L";
        }
    }
}
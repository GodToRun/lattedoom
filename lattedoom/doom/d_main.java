package lattedoom.doom;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import lattedoom.engine.*;
import lattedoom.engine.bufutils.*;
import lattedoom.doom.*;
import lattedoom.doom.d_wad.Lump;

public class d_main {
	public d_engine engine;
	public r_bsp bsp;
	public d_wad wad = null;
	public String map = "E1M6";
	HashMap<String, texture> textureHash = new HashMap<String, texture>(); 
	IntBuffer loadTex(int texNum, String pictureName, int sx, int sy, int x1, int y1, int texWidth, int texHeight) {
		try {
			BufferedImage img = ImageIO.read(new File(pictureName));
			IntBuffer b = IntBuffer.alloc(texWidth * texHeight);
			for (int x = sx; x < x1+sx; x++) {
				for (int y = sy; y < y1+sy; y++) {
					int col = img.getRGB(x, y);
					Color color = new Color((col>>16)&0xFF, (col>>8)&0xFF, (col)&0xFF);
					b.seek((x-sx) + (y-sy) * (texWidth));
					b.put(color.getRGB());
				}
			}
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public d_main() {
		engine = new d_engine(640, 400);
		bsp = new r_bsp(this);
		engine.bsp = bsp;
		engine.player.a = 270;
		try {
			wad = new d_wad("resdoom\\DOOM.WAD");
			engine.setFrameBufferSize(320, 200);
			engine.setBufferStretch(2, 2);
			boolean foundit = false;
			for (Lump lump : wad.getLumps()) {
				//System.out.println(lump.getName());
				if (lump.getName().equals(map)) {
					foundit = true;
				}
				else if (lump.getName().equals("LINEDEFS") && foundit) {
					int lsize = line_t.class.getDeclaredAnnotation(size_t.class).size();
					int SIZE = lump.getSize() / lsize;
					byte[] data = wad.getData(lump);
					bsp.linedef = new line_t[SIZE];
					for (int i = 0; i < lump.getSize(); i += lsize) {
						bsp.linedef[i/lsize] = new line_t(wad.getShort(data, i), wad.getShort(data, i+2), wad.getShort(data, i+4), wad.getShort(data, i+6),
								wad.getShort(data, i+8), wad.getShort(data, i+10), wad.getShort(data, i+12));
					}
				}
				else if (lump.getName().equals("SIDEDEFS") && foundit) {
					int ssize = side_t.class.getDeclaredAnnotation(size_t.class).size();
					int SIZE = lump.getSize() / ssize;
					byte[] data = wad.getData(lump);
					bsp.sidedef = new side_t[SIZE];
					for (int i = 0; i < lump.getSize(); i += ssize) {
						bsp.sidedef[i/ssize] = new side_t(wad.getShort(data, i), wad.getShort(data, i+2),
								new String(data, i+4, 8).trim(), new String(data, i+12, 8).trim(), new String(data, i+20, 8).trim(), wad.getShort(data, i+28));
					}
				}
				else if (lump.getName().equals("SECTORS") && foundit) {
					int ssize = sector_t.class.getDeclaredAnnotation(size_t.class).size();
					int SIZE = lump.getSize() / ssize;
					byte[] data = wad.getData(lump);
					bsp.sector = new sector_t[SIZE];
					for (int i = 0; i < lump.getSize(); i += ssize) {
						bsp.sector[i/ssize] = new sector_t(wad.getShort(data, i), wad.getShort(data, i+2), new String(data, i+4, 8)
								, new String(data, i+12, 8), wad.getShort(data, i+20), wad.getShort(data, i+22), wad.getShort(data, i+24));
					}
				}
				else if (lump.getName().equals("SSECTORS") && foundit) {
					int ssize = ssector_t.class.getDeclaredAnnotation(size_t.class).size();
					int SIZE = lump.getSize() / ssize;
					byte[] data = wad.getData(lump);
					bsp.ssector = new ssector_t[SIZE];
					for (int i = 0; i < lump.getSize(); i += ssize) {
						bsp.ssector[i/ssize] = new ssector_t(0,0);
						ssector_t ssector = bsp.ssector[i/ssize];
						ssector.segcount = wad.getShort(data, i);
						ssector.firstseg = wad.getShort(data, i+2);
						ssector.top = new int[engine.window.width];
						ssector.under = new int[engine.window.width];
						for(int j=0;j<ssector.under.length;j++) {
							ssector.under[j] = 2147483647;
						}
					}
				}
				else if (lump.getName().equals("SEGS") && foundit) {
					int ssize = seg_t.class.getDeclaredAnnotation(size_t.class).size();
					int SIZE = lump.getSize() / ssize;
					byte[] data = wad.getData(lump);
					bsp.seg = new seg_t[SIZE];
					for (int i = 0; i < lump.getSize(); i += ssize) {
						bsp.seg[i/ssize] = new seg_t();
						seg_t seg = bsp.seg[i/ssize];
						seg.sv = wad.getShort(data, i);
						seg.ev = wad.getShort(data, i+2);
						seg.angle = wad.getShort(data, i+4);
						seg.linedef = wad.getShort(data, i+6);
						seg.direction = wad.getShort(data, i+8);
						seg.offset = wad.getShort(data, i+10);
					}
				}
				else if (lump.getName().equals("VERTEXES") && foundit) {
					int vsize = vertex.class.getDeclaredAnnotation(size_t.class).size();
					int SIZE = lump.getSize() / vsize;
					byte[] data = wad.getData(lump);
					bsp.vertexes = new vertex[SIZE];
					short scale = 2;
					for (int i = 0; i < lump.getSize(); i += vsize) {
						bsp.vertexes[i/vsize] = new vertex(wad.getShort(data, i) / scale, wad.getShort(data, i+2) / scale);
					}
				}
				else if (lump.getName().equals("BLOCKMAP") && foundit) {
					foundit = false;
					//break;
				}
				else if (lump.getName().startsWith("FLAT") || lump.getName().startsWith("CEIL")) {
					byte[] data = wad.getData(lump);
					IntBuffer buf = IntBuffer.alloc(64 * 64);
					for (int i = 0; i < data.length; i++) {
						byte color = data[i];
						buf.put(color);
					}
					textureHash.put(lump.getName(), new texture(buf, 64, 64));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
		    for (final File fileEntry : new File("resdoom\\wall").listFiles()) {
		    	BufferedImage img = ImageIO.read(fileEntry);
		    	IntBuffer tex = loadTex(0, fileEntry.getAbsolutePath(), 0, 0, img.getWidth(), img.getHeight(), img.getWidth(), img.getHeight());
		    	String s = fileEntry.getName().replace(".png", "");
		        textureHash.put(s, new texture(tex, img.getWidth(), img.getHeight()));
		    }
		} catch (Exception e) {e.printStackTrace();}
		
		while(true) {
			engine.update();
			update();
			engine.window.update();
		}
	}
	public texture tex(String name) {
		if (textureHash.get(name) != null)
			return textureHash.get(name);
		else return new texture(null, 0, 0);
	}
	void update() {
		if (engine.key[KeyEvent.VK_RIGHT] == 1) {
			engine.player.rotate((float)(80D * engine.dt()));
		}
		if (engine.key[KeyEvent.VK_W] == 1) {
			
			engine.player.l -= ((float)(80D * engine.dt()));
		}
		if (engine.key[KeyEvent.VK_S] == 1) {
			
			engine.player.l += ((float)(80D * engine.dt()));
		}
		if (engine.key[KeyEvent.VK_LEFT] == 1) {
			engine.player.rotate((float)(-80D * engine.dt()));
		}
		if (engine.key[KeyEvent.VK_SPACE] == 1) {
			//zcoll = false;
			engine.player.z -= engine.dt() * 80;
		}
		if (engine.key[KeyEvent.VK_SHIFT] == 1) {
			//zcoll = false;
			engine.player.z += engine.dt() * 80;
		}
		float dx=(float)Math.sin(engine.player.a * (Math.PI/180));
		float dy=(float)Math.cos(engine.player.a * (Math.PI/180));
		float speed = 120;
		if (engine.key[KeyEvent.VK_UP] == 1) {
			//bobbing();
			engine.player.x += dx*speed*engine.dt();
			engine.player.y += dy*speed*engine.dt();
		}
		else if (engine.key[KeyEvent.VK_DOWN] == 1) {
			//bobbing();
			engine.player.x += -dx*speed*engine.dt();
			engine.player.y += -dy*speed*engine.dt();
		}
	}
	public static void main(String[] args) {
		new d_main();
	}

}

package lattedoom.engine;

import java.awt.Color;
import java.util.ArrayList;

public class Texture {
	public int width, height;
	public ArrayList<Color> texture = new ArrayList<Color>();
	public Texture(int width, int height) {
		this.width = width;
		this.height = height;
	}
}

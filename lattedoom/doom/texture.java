package lattedoom.doom;

import lattedoom.engine.bufutils.*;

public class texture {
	public IntBuffer tex;
	public int width, height;
	public texture(IntBuffer tex, int width, int height) {
		this.width = width;
		this.height = height;
		this.tex = tex;
	}
}

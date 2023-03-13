package lattedoom.engine;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class Renderer {
	public d_engine engine;
	float XStretch = 1, YStretch = 1;
	public Renderer(d_engine engine) {
		this.engine = engine;
	}
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if (engine != null && engine.screenBuffer != null)
			g2d.drawImage(engine.screenBuffer, 0, 0, (int)(engine.screenBuffer.getWidth() * XStretch), (int)(engine.screenBuffer.getHeight() * YStretch), null);
	}
}

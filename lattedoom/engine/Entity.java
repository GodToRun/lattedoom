package lattedoom.engine;

public class Entity {
	public float x = 0, y = 0, z = 0, a = 0, l = 0;
	public void rotate(float a) {
		this.a += a;
		if (this.a < 0) this.a += 360;
		this.a %= (90 * 4);
	}
}

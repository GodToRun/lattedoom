package lattedoom.doom;
@size_t(size=4)
public class vertex {
	public short x, y;
	public vertex(short x, short y) {
		this.x = x;
		this.y = y;
	}
	public vertex(int x, int y) {
		this.x = (short)x;
		this.y = (short)y;
	}
}

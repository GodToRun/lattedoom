package lattedoom.doom;
@size_t(size=30)
public class side_t {
	public short xoff, yoff, snum;
	public String lower, middle, upper;
	public side_t(int xoff, int yoff, String upper, String lower, String middle, int snum) {
		this.xoff = (short)xoff;
		this.yoff = (short)yoff;
		this.snum = (short)snum;
		this.lower = lower;
		this.middle = middle;
		this.upper = upper;
	}
}

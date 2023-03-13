package lattedoom.doom;
@size_t(size=14)
public class line_t {
	public short sv, ev, rightface, leftface, spectype, flags, sectag;
	public line_t(int sv, int ev, int flags, int spectype, int sectag, int rightface, int leftface) {
		this.sv = (short)sv;
		this.ev = (short)ev;
		this.sectag = (short)sectag;
		this.leftface = (short)leftface;
		this.rightface = (short)rightface;
		this.spectype = (short)spectype;
		this.flags = (short)flags;
	}
}

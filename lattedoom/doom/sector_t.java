package lattedoom.doom;
@size_t(size=26)
public class sector_t {
	public short flheight;
	public short clheight, lightlevel, spectype, sectag;
	public String fltexture, cltexture;
	// for rendering, original doom haven't this field.
	public int surface = 0, c1 = 0xFFFFFF, c2 = 0xFF00FF;
	public sector_t(int flheight, int clheight, String fltexture, String cltexture, int lightlevel, int spectype, int sectag) {
		this.flheight = (short)flheight;
		this.sectag = (short)sectag;
		this.clheight = (short)clheight;
		this.fltexture = fltexture;
		this.cltexture = cltexture;
		this.spectype = (short)spectype;
		this.lightlevel = (short)lightlevel;
	}
}

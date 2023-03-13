package lattedoom.doom;
@size_t(size=4)
public class ssector_t {
	public short segcount;
	public short firstseg;
	
	public int d = 0;
	
	public int[] top, under;
	public int mtop, munder = 2147483647;
	public ssector_t(int firstseg, int segcount) {
		this.segcount = (short)segcount;
		this.firstseg = (short)firstseg;
	}
}

package lattedoom.engine.bufutils;

public class IntBuffer extends Buffer {
	private int[] value;
	private int size;
	private IntBuffer(int size) {
		value = new int[size];
	}
	public int size() {
		return size;
	}
	public static IntBuffer alloc(int size) {
		return new IntBuffer(size);
	}
	@Override
	public void flip() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void seek(int index) {
		indicator = index;
	}
	public IntBuffer put(int i) {
		value[indicator] = i;
		indicator++;
		return this;
	}
	public int get(int index) {
		return value[index];
	}
}

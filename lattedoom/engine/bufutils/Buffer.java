package lattedoom.engine.bufutils;

public abstract class Buffer {
	protected int indicator = 0;
	public abstract void flip();
	public abstract void seek(int index);
}

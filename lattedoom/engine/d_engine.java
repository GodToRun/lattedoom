package lattedoom.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import lattedoom.doom.line_t;
import lattedoom.doom.sector_t;
import lattedoom.doom.seg_t;
import lattedoom.doom.side_t;
import lattedoom.doom.ssector_t;
import lattedoom.doom.texture;
import lattedoom.engine.*;
import lattedoom.engine.bufutils.*;

public class d_engine {
	public int[] key = new int[500];
	public Renderer renderer;
	public BufferedImage screenBuffer;
    public int[] screenPixels;
    public Player player;
	public Window window;
	private long curTime = System.currentTimeMillis();
    private long lastTime = curTime;
    private long totalTime;
    private double frames;
    private double fps = 100;
    public int clearColor = 0x0088FF;
    private int frameBufferWidth, frameBufferHeight;
    public r_bsp bsp;
    
	public d_engine(int xsiz, int ysiz) {
		player = new Player();
		window = new Window(this, xsiz, ysiz);
		renderer = new Renderer(this);
		screenBuffer = new BufferedImage(xsiz, ysiz, BufferedImage.TYPE_INT_RGB);
		frameBufferWidth = xsiz;
		frameBufferHeight = ysiz;
		screenPixels = ((DataBufferInt)screenBuffer.getRaster().getDataBuffer()).getData();
	}
	public void setBufferStretch(float Xs, float Ys) {
		this.renderer.XStretch = Xs;
		this.renderer.YStretch = Ys;
	}
	public void setFrameBufferSize(int width, int height) {
		screenBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.frameBufferWidth = width;
		this.frameBufferHeight = height;
		
		screenPixels = ((DataBufferInt)screenBuffer.getRaster().getDataBuffer()).getData();
	}
	private int []clipBehindPlayer(int x1, int y1, int z1, int x2, int y2, int z2) {
		int[] arr = new int[3];
		float da=y1;
		float db=y2;
		float d=da-db; if (d<=0) {d=1;}
		float s = da/(da-db);
		arr[0] = (int)(x1 + s*(x2-x1));
		arr[1] = (int)(y1 + s*(y2-y1)); if (arr[1]<=0) arr[1]=1;
		arr[2] = (int)(z1 + s*(z2-z1));
		return arr;
	}
	public float dist(float x1, float y1, float x2, float y2) {
		float distance = (float)Math.sqrt((double)((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)));
		return distance;
	}
	public float dist(float x1, float y1, float z1, float x2, float y2, float z2) {
		float distance = (float)Math.sqrt((double)((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)) + (z2-z1)*(z2-z1));
		return distance;
	}
	void drawWall(int x1, int x2, int b1, int b2, int t1, int t2, int s, int l, int f) {
		int x, y;
		
		int dyb = b2 - b1;
		int dyt = t2-t1;
		int dx = x2-x1; if (dx==0) dx=1;
		int xs=x1;
		
		int tx1 = x1;
		int tx2 = x2;
		
		if(x1<0) {x1=0;}
		if(x2<0) {x2=0;}
		if(x1>=frameBufferWidth) {x1=frameBufferWidth-1;}
		if(x2>=frameBufferWidth) {x2=frameBufferWidth-1;}
		
		side_t face = bsp.sidedef[bsp.linedef[bsp.seg[l].linedef].rightface];
		float sheight = Math.abs(bsp.sector[face.snum].clheight - bsp.sector[face.snum].flheight);
		texture tex = bsp.main.tex(face.lower);
		if (f == 1) tex = bsp.main.tex(face.middle);
		else if (f == 2) tex = bsp.main.tex(face.upper);
		for (x=/*Math.min(*/x1/*, x2)*/;x</*Math.max(x1, */x2/*)*/;x++) {
			int y1 = (int)(dyb*(x-xs+0.5)/dx+b1);
			int y2 = (int)(dyt*(x-xs+0.5)/dx+t1);
			int ty1= y1;
			int ty2= y2;
			if(y1<0) {y1=0;}
			if(y2<0) {y2=0;}
			if(y1>=frameBufferHeight) {y1=frameBufferHeight-1;}
			if(y2>=frameBufferHeight) {y2=frameBufferHeight-1;}
			/*if (bsp.sector[s].surface==1) {bsp.sector[s].surf[x]=y1; continue;}
			else if (bsp.sector[s].surface==2) {bsp.sector[s].surf[x]=y2; continue;}
			else if (bsp.sector[s].surface==-1) {
				for(y=bsp.sector[s].surf[x];y<y1;y++) {
					int tx = (int)(((float)(x-tx1)/(float)(tx2-tx1)) * (float)tex.width);
					int ty = (int)(((float)y*5/(float)y1) * (float)tex.height) + (int)player.y;
					
					tx %= tex.width;
					ty %= tex.height;
					pixel(x,y, bsp.sector[s].c1);
				}
			}
			else if (bsp.sector[s].surface==-2) {for(y=y2;y<bsp.sector[s].surf[x];y++) {pixel(x,y,bsp.sector[s].c2);}}*/
			if (f != 1) {
				int surf = Math.min(y1, y2);
				if (f==2) surf=Math.max(y1, y2);
				bsp.ssector[s].top[x] = Math.max(bsp.ssector[s].top[x], surf);
				bsp.ssector[s].under[x] = Math.min(bsp.ssector[s].under[x], surf);
				bsp.ssector[s].mtop = Math.max(bsp.ssector[s].mtop, surf);
				bsp.ssector[s].munder = Math.min(bsp.ssector[s].munder, surf);
			}
			if (tex!=null&&tex.tex!=null) {
				for (y=/*Math.min(*/y1/*, y2)*/;y</*Math.max(y1, */y2/*)*/;y++) {
					int len = x2-x1;
					float u = 1, v = 4;
					int tx = (int)(((float)(x-x1)/(float)(len)) * (float)tex.width*u);
					int ty = (int)(((float)(y-ty1)/(float)(ty2-ty1)) * (float)tex.height*v);
					if(tex.width != 0 && tex.height != 0) {
						tx %= tex.width;
						ty %= tex.height;
					}
					if (tx < 0) tx = 0;
					if (ty < 0) ty = 0;
					pixel(x, y, tex.tex.get(tx + ty * tex.width));
					//pixel(x, y, 0xFFFFFF);
				}
			}
		}
		if (bsp.ssector[s].segcount+bsp.ssector[s].firstseg == l+1 && f != 1) {
			int fx1=0, fx2=0;
			for(int i=0;i<bsp.ssector[s].under.length;i++) {
				if (fx1 == 0 && bsp.ssector[s].under[i] != 2147483647) {
					fx1 = i;
				}
				if (fx1 != 0 && bsp.ssector[s].under[i] == 2147483647) {
					fx2 = i;
					break;
				}
			}
			for (int fx = fx1; fx < fx2; fx++) {
				for (int fy = bsp.ssector[s].under[fx]; fy < bsp.ssector[s].top[fx]; fy++) {
					
					float scale = 1;
					x = fx - fx1;
					y = fy - bsp.ssector[s].munder;
					if (x == 0 || y == 0 || player.z == 0) continue;
					int tx = (int)((float)x);
					int ty = (int)(200.0f);
					int rx = (int)(((double)tx)*Math.sin(player.a * Math.PI/180)*180/Math.PI-((double)ty)*Math.cos(player.a * Math.PI/180)*180/Math.PI);
					int ry = (int)(((double)tx)*Math.cos(player.a * Math.PI/180)*180/Math.PI+((double)ty)*Math.sin(player.a * Math.PI/180)*180/Math.PI);
					if (rx<0)rx=-rx+1;
					if (ry<0)ry=-ry+1;
					if (rx % 2 == ry % 2) pixel(fx, fy, 0xFF0000);
					else pixel(fx, fy, 0xFFFF00);
					/*if (tex!=null&&tex.tex!=null) {
						pixel(fx, fy, tex.tex.get(tx + ty * tex.width));
					}*/
					
				}
				bsp.ssector[s].under[fx]=2147483647;
				bsp.ssector[s].top[fx]=0;
			}
		}
		bsp.ssector[s].mtop = 0;
		bsp.ssector[s].munder = 2147483647;
	}
	public int[] calcOnPoint(float playerx, float playery, float playerz, float yaw, float pitch, float sx, float sy, float sz, float CS, float SN) {
		float x1=sx-playerx;
		float y1=sy-playery;
		
		int[] wx = new int[4];
		int[] wy = new int[4];
		int[] wz = new int[4];
		
		wx[0]=(int)(x1*CS-y1*SN);
		wx[2]=wx[0];
		
		wy[0]=(int)(y1*CS+x1*SN);
		wy[2]=wy[0];
		
		wz[0]=(int)(sz-playerz+((pitch*wy[0])/32.0));
		
		if (wy[0]<=0) {
			int[] arr = clipBehindPlayer(wx[0], wy[0], wz[0], wx[1], wy[1], wz[1]);
			wx[0]=arr[0];
			wy[0]=arr[1];
			wz[0]=arr[2];
		}
		
		
		int res=2;
		int div = wy[0];
		if(div==0)div=1;
		wx[0]=wx[0]*200/div+frameBufferWidth/(res); wy[0]=wz[0]*200/div+frameBufferHeight/(res);
		
		return new int[] { wx[0], wy[0] };
	}
	public double dt() {
		return 1D / fps;
	}
	public double fps() {
		return fps;
	}
	void draw3D() {
		//System.out.println(player.x + ", " + player.y);
		float CS = (float)Math.cos(player.a * (Math.PI/180));
		float SN = (float)Math.sin(player.a * (Math.PI/180));
		for(int s=0;s<bsp.ssector.length-1;s++) {
			for(int w=0;w<bsp.ssector.length-s-1;w++) {
				if (bsp.ssector[w].d<bsp.ssector[w+1].d) {
					//ssector_t st=new ssector_t(bsp.ssector[s].firstseg, bsp.ssector[s].segcount);
					ssector_t st=bsp.ssector[w];
					bsp.ssector[w]=bsp.ssector[w+1];bsp.ssector[w+1]=st;
				}
			}
		}
		for(int s=0;s<bsp.ssector.length;s++) {
			ssector_t ssector = bsp.ssector[s];
			bsp.ssector[s].d = 0;
			for(int loop=0;loop<2;loop++) {
			for (int f=0;f<3;f++) {
				for (int l=bsp.ssector[s].firstseg;l<bsp.ssector[s].firstseg+bsp.ssector[s].segcount;l++) {
					seg_t seg = bsp.seg[l];
					int rface = bsp.linedef[seg.linedef].rightface;
					int lface = bsp.linedef[seg.linedef].leftface;
					if (rface ==-1) continue;
					side_t rightside = bsp.sidedef[rface];
					//side_t leftside = bsp.sidedef[lface];
					sector_t sector = bsp.sector[rightside.snum];
					line_t line = bsp.linedef[seg.linedef];
					float sx = bsp.vertexes[seg.sv].x;
					float sy = bsp.vertexes[seg.sv].y;
					float sz = 0;
					float sz2 = 500;
					//else if (face.lower == "-") continue;
					float sx2 = bsp.vertexes[seg.ev].x;
					float sy2 = bsp.vertexes[seg.ev].y;
					float x1=sx-player.x;
					float y1=sy-player.y;
					float x2=sx2-player.x;
					float y2=sy2-player.y;
					if (loop==1) {
					float swp=x1; x1=x2; x2=swp;
					swp=y1; y1=y2; y2=swp;
					}
					
					int[] wx = new int[4];
					int[] wy = new int[4];
					int[] wz = new int[4];
					
					wx[0]=(int)(x1*CS-y1*SN);
					wx[1]=(int)(x2*CS-y2*SN);
					wx[2]=wx[0];
					wx[3]=wx[1];
					
					wy[0]=(int)(y1*CS+x1*SN);
					wy[1]=(int)(y2*CS+x2*SN);
					wy[2]=wy[0];
					wy[3]=wy[1];
					
					if(f==0)
						ssector.d+=dist(0,0,(wx[0]+wx[1])/2,(wy[0]+wy[1])/2);
					
					wz[0]=(int)(sz-player.z+((player.l*wy[0])/32.0));
					wz[1]=(int)(sz-player.z+((player.l*wy[1])/32.0));
					wz[2]=wz[0]+(int)sz2;
					wz[3]=wz[1]+(int)sz2;
					if (wy[0]<1&&wy[1]<1)continue;
					if (wy[0]<=0) {
						int[] arr = clipBehindPlayer(wx[0], wy[0], wz[0], wx[1], wy[1], wz[1]);
						wx[0]=arr[0];
						wy[0]=arr[1];
						wz[0]=arr[2];
						int[] arr2 = clipBehindPlayer(wx[2], wy[2], wz[2], wx[3], wy[3], wz[3]);
						wx[2]=arr2[0];
						wy[2]=arr2[1];
						wz[2]=arr2[2];
					}
					if (wy[1]<=1) {
						int[] arr = clipBehindPlayer(wx[1], wy[1], wz[1], wx[0], wy[0], wz[0]);
						wx[1]=arr[0];
						wy[1]=arr[1];
						wz[1]=arr[2];
						int[] arr2 = clipBehindPlayer(wx[3], wy[3], wz[3], wx[2], wy[2], wz[2]);
						wx[3]=arr2[0];
						wy[3]=arr2[1];
						wz[3]=arr2[2];
					}
					try {
					
					int res=2;
					wx[0]=wx[0]*200/wy[0]+frameBufferWidth/(res); wy[0]=wz[0]*200/wy[0]+frameBufferHeight/(res);
					wx[1]=wx[1]*200/wy[1]+frameBufferWidth/(res); wy[1]=wz[1]*200/wy[1]+frameBufferHeight/(res);
					wx[2]=wx[2]*200/wy[2]+frameBufferWidth/(res); wy[2]=wz[2]*200/wy[2]+frameBufferHeight/(res);
					wx[3]=wx[3]*200/wy[3]+frameBufferWidth/(res); wy[3]=wz[3]*200/wy[3]+frameBufferHeight/(res);
					drawWall(wx[0], wx[1], wy[0], wy[1], wy[2], wy[3], s, l, f);
					}catch(Exception e) {e.printStackTrace();try {
						Thread.sleep(5);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}}
					
				}
				if(f==0)
				ssector.d/=(ssector.segcount);				
			}
			}
			
		}
	}
	public void update() {
		if (screenPixels == null) return;
		lastTime = curTime;
		curTime = System.currentTimeMillis();
		
		totalTime += curTime - lastTime;
		if (totalTime > 1000) {
			totalTime -= 1000;
			fps = frames;
			frames = 0;
		}
		frames++;
		for (int i = 0; i < screenPixels.length; i++) {
			screenPixels[i] = clearColor;
		}
		draw3D();
	}
	public void pixel(int x, int y, int c) {
		if (x >= 0 && x < frameBufferWidth && y >= 0 && y < frameBufferHeight)
			screenPixels[x + y * frameBufferWidth] = c;
	}
	public void onKey(KeyEvent e) {
		key[e.getKeyCode()] = 1;
	}
	public void onKeyUp(KeyEvent e) {
		if (key[e.getKeyCode()] == 1)
			key[e.getKeyCode()] = 0;
	}
}

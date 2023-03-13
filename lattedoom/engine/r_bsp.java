package lattedoom.engine;

import lattedoom.doom.d_main;
import lattedoom.doom.line_t;
import lattedoom.doom.sector_t;
import lattedoom.doom.seg_t;
import lattedoom.doom.side_t;
import lattedoom.doom.ssector_t;
import lattedoom.doom.vertex;
import mutech0.Mutech0;

public class r_bsp {
	public side_t[] sidedef;
	public line_t[] linedef;
	public sector_t[] sector;
	public vertex[] vertexes;
	public ssector_t[] ssector;
	public seg_t[] seg;
	public d_main main;
	public r_bsp(d_main main) {
		this.main = main;
	}
	public void render(d_main main, Mutech0 engine) {
		/*for (int i = 0; i < sector.length; i++) {
			sector_t sec = sector[i];
			for (int f = 0; f < 2; f++) {
				texture tex = main.tex(sec.fltexture);
				engine.bindTexture(tex.tex, tex.width, tex.height);
				engine.begin();
				for (int j = 0; j < linedef.length; j++) {
					line_t line = linedef[j];
					if (sidedef[line.rightface].snum != i) continue;
					short height = (short)-sec.flheight;
					if (f == 1) height = (short)-sec.clheight;
					engine.vertex(vertexes[line.sv].x, vertexes[line.sv].y, height);
				}
				engine.end(4);
			}
			for (int f = 0; f < 3; f++) {
				for (int j = 0; j < linedef.length; j++) {
					line_t line = linedef[j];
					if (sidedef[line.rightface].snum != i) continue;
					short start = (short)0;
					short height = (short)-sec.flheight;
					if (f == 1) {
						start = (short)-sec.flheight;
						height = (short)-sec.clheight;
					}
					else if (f == 2) {
						start = (short)-sec.clheight;
						height = (short)-100;
					}
					if (f == 0) {
						if (sidedef[line.rightface].lower == "-") continue;
						texture tex = main.tex(sidedef[line.rightface].lower);
						engine.bindTexture(tex.tex, tex.width, tex.height);
					}
					else if (f == 1) {
						if (sidedef[line.rightface].middle == "-") continue;
						texture tex = main.tex(sidedef[line.rightface].middle);
						engine.bindTexture(tex.tex, tex.width, tex.height);
					}
					else if (f == 2) {
						if (sidedef[line.rightface].upper == "-") continue;
						texture tex = main.tex(sidedef[line.rightface].upper);
						engine.bindTexture(tex.tex, tex.width, tex.height);
					}
					engine.begin();
					engine.vertex(vertexes[line.sv].x, vertexes[line.sv].y, start);
					engine.vertex(vertexes[line.ev].x, vertexes[line.ev].y, start);
					engine.vertex(vertexes[line.ev].x, vertexes[line.ev].y, height);
					engine.vertex(vertexes[line.sv].x, vertexes[line.sv].y, height);
					engine.end(4);
				}
			}
		}*/
	}

}

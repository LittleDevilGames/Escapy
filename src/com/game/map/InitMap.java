package com.game.map;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import com.game.map.objects.AnimatedObject;
import com.game.map.objects.BackGround;
import com.game.map.objects.InGameObject;
import com.game.map.objects.NonAnimatedObject;
import com.game.utils.primitives.walls.Walls;

import cern.colt.matrix.ObjectMatrix3D;
import cern.colt.matrix.impl.SparseObjectMatrix3D;

public class InitMap {

	private InGameObject[][] inGameObject;
	private BackGround bckgr;
	private ObjectMatrix3D areaMap;
	private int[] size;
	private int[] adrtab = new int[] { 4, 2, 1, 0, 3 };

	private Walls walls;
	private long actualPointerPos;

	
	
	
	public InitMap(String Location, int frameW, int frameH, double scaleRatio) {
		double[] mapsize = readMapSize(Location);

		this.areaMap = new SparseObjectMatrix3D((int) mapsize[0], (int) mapsize[1], 4);
		this.size = getObjectsNumb(Location);
		this.inGameObject = loadObjects(size);
		this.inGameObject = createObjectsFF(size, inGameObject, Location);
		this.bckgr = loadBackGround(Location, frameW, frameH, scaleRatio);

		ArrayList<int[]> wallPointList = new ArrayList<>();
		fillMapFF(new int[] { size[5], size[6] }, Location, wallPointList); // wallPointList
																			// &
																			// areaMap

		this.walls = new Walls(wallPointList);

		System.gc();
	}

	
	
	
	private double[] readMapSize(String loc) {
		double[] arrd = new double[2];
		try {
			RandomAccessFile raf = new RandomAccessFile(new File(loc + "_.gmd"), "r");
			raf.seek(0);
			arrd[0] = (raf.readShort());
			arrd[1] = (raf.readShort());
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arrd;
	}

	public ObjectMatrix3D map() {
		return areaMap;
	}

	public InGameObject[][] gameObjects() {
		return inGameObject;
	}

	public BackGround backGround() {
		return bckgr;
	}

	public int[] indexTab() {
		return adrtab;
	}

	public int[] objectSize() {
		return size;
	}

	
	
	
	private InGameObject[][] createObjectsFF(int[] tabsize, InGameObject[][] inGameObject, String locUrl) {
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(locUrl + "_.gmd", "r");
			raf.seek(actualPointerPos);

			int[] adtab = new int[] { 0, 1, 4, 2, 3 };

			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < tabsize[adtab[i]]; j++) {
					int x = raf.readShort();
					int y = raf.readShort();
					int id = raf.readShort();
					int spt = raf.readShort();
					double zoom = raf.readDouble();
					int[] animtime = new int[10];
					if (spt == 1) {
						for (int anm = 0; anm < 10; anm++) {
							animtime[anm] = raf.readShort();
						}
					}
					if (spt == 1) {
						inGameObject[adtab[i]][j] = new AnimatedObject(x, y, id, locUrl + "" + id + ".png", animtime,
								zoom, adtab[i]);
					} else if (spt == 0) {
						inGameObject[adtab[i]][j] = new NonAnimatedObject(x, y, id, locUrl + "" + id + ".png", zoom,
								adtab[i]);
					}
					actualPointerPos = raf.getFilePointer();
				}
			}
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inGameObject;
	}

	
	
	
	
	private int[] getObjectsNumb(String locUrl) {
		int[] numbtab = new int[7];
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(locUrl + "_.gmd", "r");
			raf.seek(4);
			for (int i = 0; i < 7; i++) {
				numbtab[i] = raf.readShort();
			}
			actualPointerPos = raf.getFilePointer();
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return numbtab;
	}

	
	
	
	private InGameObject[][] loadObjects(int[] tab) {
		int[] adtab3 = new int[] { 0, 1, 4, 2, 3 };
		InGameObject[][] intab = new InGameObject[5][];
		for (int i = 0; i < 3; i++) {
			intab[adtab3[i]] = new AnimatedObject[tab[adtab3[i]]];
		}
		for (int i = 3; i < 5; i++) {
			intab[adtab3[i]] = new NonAnimatedObject[tab[adtab3[i]]];
		}
		return intab;
	}

	
	
	
	
	private BackGround loadBackGround(String location, int frameW, int frameH, double scaleRatio) {
		return new BackGround(location + "bckgr.png", frameW, frameH, scaleRatio);
	}

	
	
	
	
	private void fillMapFF(int[] tab, String locUrl, ArrayList<int[]> wallPointList) {
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(locUrl + "_.gmd", "r");
			raf.seek(actualPointerPos);

			for (int i = 0; i < tab[0]; i++) {
				int Figuretype = raf.readShort();
				int StX = raf.readShort();
				int StY = raf.readShort();
				int EdX = raf.readShort();
				int EdY = raf.readShort();
				int ID = raf.readShort();
				int ActionType = raf.readShort();
				crFigure(StX, StY, EdX, EdY, new Object[] { (byte) 2, (short) ID, (byte) ActionType }, Figuretype);
			}
			for (int i = 0; i < tab[1]; i++) // 1 == square 2 == triangle
			{
				int Figuretype = raf.readShort();
				int StX = raf.readShort();
				int StY = raf.readShort();
				int EdX = raf.readShort();
				int EdY = raf.readShort();
				crFigure(StX, StY, EdX, EdY, new Object[] { (byte) 1, (short) Short.MAX_VALUE, (byte) 0xFF },
						Figuretype);

				wallPointList.add(new int[] { StX, StY, EdX, EdY, Figuretype });
			}
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	
	
	
	private void crFigure(int sx, int sy, int ex, int ey, Object[] vals, int type) {
		switch (type) {
		case 1:
			figureSquare(sx, sy, ex, ey, vals);
			break;
		case 2:
			figureTriagnle(sx, sy, ex, ey, vals);
			break;
		}
	}
	
	
	
	
	

	private void figureSquare(int sx, int sy, int ex, int ey, Object[] vals) {
		if (vals == null)
			vals = new Object[] { (byte) 0, (short) 0, (byte) 0 };
		if (ex < 0)
			ex = 0;
		if (ey < 0)
			ey = 0;
		if (sx > ex) {
			int temp = sx;
			sx = ex;
			ex = temp;
		}
		if (sy > ey) {
			int temp = sy;
			sy = ey;
			ey = temp;
		}
		for (int i = sx; i < ex; i++) {
			for (int j = sy; j < ey; j++) {
				try {
					areaMap.set(i, j, 0, (byte) vals[0]);
					areaMap.set(i, j, 1, (short) vals[1]);
					areaMap.set(i, j, 2, (byte) vals[2]);
				} catch (IndexOutOfBoundsException exdc) {
				}
			}
		}
	}

	
	
	
	
	
	private void figureTriagnle(int stx, int sty, int edx, int edy, Object[] vals) {
		double sx = stx;
		double sy = sty;
		double ex = edx;
		double ey = edy;

		if (sx < ex) {
			for (double i = sx; i < ex; i += 1) {
				double tg = ((ey - sy) / (ex - sx));
				double ki = ex - i;
				if (sy > ey) {
					for (double j = sy - ey; j > -ki * (tg); j -= 1) {
						try {
							areaMap.set((int) i, (int) (j + ey), 0, (byte) vals[0]);
							areaMap.set((int) i, (int) (j + ey), 1, (short) vals[1]);
							areaMap.set((int) i, (int) (j + ey), 2, (byte) vals[2]);
						} catch (IndexOutOfBoundsException exdc) {
						}
					}
				} else if (sy < ey) {
					for (double j = sy - ey; j < -ki * (tg); j += 1) {
						try {
							areaMap.set((int) i, (int) (j + ey), 0, (byte) vals[0]);
							areaMap.set((int) i, (int) (j + ey), 1, (short) vals[1]);
							areaMap.set((int) i, (int) (j + ey), 2, (byte) vals[2]);
						} catch (IndexOutOfBoundsException exdc) {
						}
					}
				}
			}
		} else if (sx > ex) {
			for (double i = sx; i > ex; i -= 1) {
				double tg = (((ey - sy) / (ex - sx)));
				double ki = sx - i;
				if (sy > ey) {
					for (double j = 0; j > -ki * (tg); j -= 1) {
						try {
							areaMap.set((int) i, (int) (j + sy), 0, (byte) vals[0]);
							areaMap.set((int) i, (int) (j + sy), 1, (short) vals[1]);
							areaMap.set((int) i, (int) (j + sy), 2, (byte) vals[2]);
						} catch (IndexOutOfBoundsException exdc) {
						}

					}
				} else if (sy < ey) {
					for (double j = 0; j < -ki * (tg); j += 1) {
						try {
							areaMap.set((int) i, (int) (j + sy), 0, (byte) vals[0]);
							areaMap.set((int) i, (int) (j + sy), 1, (short) vals[1]);
							areaMap.set((int) i, (int) (j + sy), 2, (byte) vals[2]);
						} catch (IndexOutOfBoundsException exdc) {
						}
					}
				}
			}
		}

	}
	
	
	

	public Walls getWalls() {
		return walls;
	}

}
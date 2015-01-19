package de.dakror.gravityrun.game.tile;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import de.dakror.gravityrun.util.Palette;

/**
 * Represents a 16x16 Region of tiles
 * 
 * @author Maximilian Stark | Dakror
 */
public class Block {
	public static final int TILE_SIZE = 16;
	public static final int TILE_COUNT = 16;
	
	float x, y;
	
	/**
	 * Stores not the color of each tile, but the index of that color
	 * 
	 * @see Palette
	 */
	short[] tiles;
	
	/**
	 * Lazy initialization for OPTIMAL EFFICIENCY
	 */
	public void init() {
		tiles = new short[TILE_COUNT * TILE_COUNT];
	}
	
	/**
	 * @return true if this block has been initialized, false otherwise.
	 */
	public boolean isInitialized() {
		return tiles != null;
	}
	
	/**
	 * Sets the color of a tile in this block.
	 * 
	 * @param x in local space (0 - TILE_COUNT)
	 * @param y in local space (0 - TILE_COUNT)
	 * @param colorIndex the index of a color from the {@link Palette}.
	 */
	public void set(int x, int y, int colorIndex) {
		checkInBounds(x, y);
		
		tiles[x * TILE_COUNT + y] = (short) colorIndex;
	}
	
	/**
	 * Sets the color of a tile in this block.
	 * 
	 * @param x in local space (0 - TILE_COUNT)
	 * @param y in local space (0 - TILE_COUNT)
	 * @param color a color from the {@link Palette}.
	 */
	public void set(int x, int y, Color color) {
		set(x, y, Palette.instance.indexOf(color));
	}
	
	/**
	 * Sets the color of a region of tiles in this block.
	 * 
	 * @param x in local space (0 - TILE_COUNT)
	 * @param y in local space (0 - TILE_COUNT)
	 * @param width
	 * @param height
	 * @param colorIndex the index of a color from the {@link Palette}.
	 */
	public void setRegion(int x, int y, int width, int height, int colorIndex) {
		checkInBounds(x, y);
		checkInBounds(x + width, y + height);
		
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				set(x + i, y + j, colorIndex);
	}
	
	/**
	 * Sets the color of a region of tiles in this block.
	 * 
	 * @param x in local space (0 - TILE_COUNT)
	 * @param y in local space (0 - TILE_COUNT)
	 * @param width
	 * @param height
	 * @param color a color from the {@link Palette}.
	 */
	public void setRegion(int x, int y, int width, int height, Color color) {
		setRegion(x, y, width, height, Palette.instance.indexOf(color));
	}
	
	/**
	 * Gets the color index of a tile in this block.
	 * 
	 * @param x in local space (0 - TILE_COUNT)
	 * @param y in local space (0 - TILE_COUNT)
	 */
	public int get(int x, int y) {
		return tiles[x * TILE_COUNT + y];
	}
	
	/**
	 * Gets the color of a tile in this block.
	 * 
	 * @param x in local space (0 - TILE_COUNT)
	 * @param y in local space (0 - TILE_COUNT)
	 */
	public Color getColor(int x, int y) {
		return new Color(get(x, y));
	}
	
	/**
	 * @return the full array of color indices
	 */
	public short[] getAll() {
		return tiles;
	}
	
	/**
	 * Compresses the color indices and encodes the array in base64.
	 * 
	 * @return a compressed b64 representation of the tile data.
	 */
	public String serialize() {
		ByteBuffer bb = ByteBuffer.allocate(2 * TILE_COUNT * TILE_COUNT);
		for (int i = 0; i < TILE_COUNT * TILE_COUNT; i++) {
			bb.putShort(tiles[i]);
		}
		
		byte[] data = new byte[bb.capacity()];
		
		Deflater deflater = new Deflater(9);
		deflater.setInput(bb.array());
		deflater.finish();
		int len = deflater.deflate(data);
		deflater.end();
		
		byte[] compressedData = new byte[len];
		System.arraycopy(data, 0, compressedData, 0, len);
		
		return new String(Base64.getEncoder().encode(compressedData));
	}
	
	/**
	 * Takes a String, produced in {@link #serialize()} and reverses the process.
	 * The tiles get overwritten with the serialized data values.
	 * 
	 * @param b64Data
	 */
	public void deserialize(String b64Data) {
		byte[] compressedData = Base64.getDecoder().decode(b64Data);
		byte[] data = new byte[2 * TILE_COUNT * TILE_COUNT];
		Inflater inflater = new Inflater();
		inflater.setInput(compressedData);
		try {
			inflater.inflate(data);
		} catch (DataFormatException e) {
			e.printStackTrace();
		}
		inflater.end();
		
		ByteBuffer bb = ByteBuffer.wrap(data);
		
		if (tiles == null) tiles = new short[TILE_COUNT * TILE_COUNT];
		for (int i = 0; i < TILE_COUNT * TILE_COUNT; i++)
			tiles[i] = bb.getShort();
	}
	
	void checkInBounds(int x, int y) {
		boolean inBounds = x >= 0 && y >= 0 && x < TILE_COUNT && y < TILE_COUNT;
		if (!inBounds) throw new IllegalArgumentException("Coordinates out of bounds!");
	}
}

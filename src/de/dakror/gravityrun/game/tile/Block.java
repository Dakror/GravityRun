/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package de.dakror.gravityrun.game.tile;

import java.awt.Color;
import java.awt.image.BufferedImage;
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
	
	BufferedImage batch;
	
	/**
	 * Lazy initialization for OPTIMAL EFFICIENCY
	 */
	public void init() {
		if (isInitialized()) return;
		
		tiles = new short[TILE_COUNT * TILE_COUNT];
		batch = new BufferedImage(TILE_COUNT * TILE_SIZE, TILE_COUNT * TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * @return true if this block has been initialized, false otherwise.
	 */
	public boolean isInitialized() {
		return tiles != null && batch != null;
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
		
		int old = get(x, y);
		tiles[x * TILE_COUNT + y] = (short) colorIndex;
		
		if (old != get(x, y)) {
			int rgb = Palette.instance.getWithAlpha(colorIndex).getRGB();
			for (int i = 0; i < TILE_SIZE; i++) {
				for (int j = 0; j < TILE_SIZE; j++) {
					batch.setRGB(x + i, y + j, rgb);
				}
			}
		}
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
	 * @return unsigned int (0 - 65535)
	 */
	public int get(int x, int y) {
		return Short.toUnsignedInt(getAsShort(x, y));
	}
	
	/**
	 * Gets the color index of a tile in this block.
	 * 
	 * @param x in local space (0 - TILE_COUNT)
	 * @param y in local space (0 - TILE_COUNT)
	 */
	public short getAsShort(int x, int y) {
		return tiles[x * TILE_COUNT + y];
	}
	
	/**
	 * Gets the color of a tile in this block.
	 * 
	 * @param x in local space (0 - TILE_COUNT)
	 * @param y in local space (0 - TILE_COUNT)
	 */
	public Color getColor(int x, int y) {
		return Palette.instance.get(get(x, y));
	}
	
	/**
	 * Gets the color of a tile in this block.
	 * 
	 * @param x in local space (0 - TILE_COUNT)
	 * @param y in local space (0 - TILE_COUNT)
	 */
	public Color getColorWithAlpha(int x, int y) {
		return Palette.instance.getWithAlpha(get(x, y));
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
		
		init();
		
		for (int i = 0; i < TILE_COUNT; i++)
			for (int j = 0; j < TILE_COUNT; j++)
				set(i, j, bb.getShort());
	}
	
	/**
	 * Loads the block pixel by pixel from the image provided.
	 * 
	 * @param image
	 */
	public void load(BufferedImage image) {
		if (image.getWidth() != TILE_COUNT || image.getHeight() != TILE_COUNT) throw new IllegalArgumentException("Invalid image size!");
		
		init();
		
		for (int i = 0; i < image.getWidth(); i++)
			for (int j = 0; j < image.getHeight(); j++)
				set(i, j, Palette.instance.indexOf(image.getRGB(i, j)));
	}
	
	void checkInBounds(int x, int y) {
		boolean inBounds = x >= 0 && y >= 0 && x < TILE_COUNT && y < TILE_COUNT;
		if (!inBounds) throw new IllegalArgumentException("Coordinates out of bounds!");
	}
	
	/**
	 * @return the prerendered batch of this block.
	 */
	public BufferedImage getBatch() {
		return batch;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
}

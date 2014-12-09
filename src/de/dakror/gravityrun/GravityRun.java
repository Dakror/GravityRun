package de.dakror.gravityrun;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import de.dakror.gravityrun.render.Drawable;

/**
 * @author Dakror
 */
public class GravityRun extends JFrame implements Drawable {
	private static final long serialVersionUID = 1L;
	public static GravityRun instance;
	
	static HashMap<String, BufferedImage> imageCache = new HashMap<>();
	
	public GravityRun() {
		super("GravityRun");
		instance = this;
		
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		createBufferStrategy(2);
		
		mainLoop();
	}
	
	public void mainLoop() {
		Graphics2D g = null;
		long last = System.currentTimeMillis();
		
		while (true) {
			BufferStrategy bs = getBufferStrategy();
			g = (Graphics2D) bs.getDrawGraphics();
			g.translate(getInsets().left, getInsets().top);
			
			
			update((System.currentTimeMillis() - last) / 1_000f);
			
			g.clearRect(0, 0, innerWidth(), innerHeight());
			draw(g);
			
			g.dispose();
			
			if (!bs.contentsLost()) bs.show();
		}
	}
	
	@Override
	public void draw(Graphics2D g) {}
	
	@Override
	public void update(float deltaTime) {}
	
	public int innerWidth() {
		return getWidth() - (getInsets().left + getInsets().right);
	}
	
	public int innerHeight() {
		return getHeight() - (getInsets().top + getInsets().bottom);
	}
	
	public BufferedImage loadImage(String p) {
		try {
			BufferedImage i = ImageIO.read(GravityRun.class.getResource((p.startsWith("/") ? "" : "/img/") + p));
			
			return i;
		} catch (Exception e) {
			return null;
		}
	}
	
	// -- statics -- //
	
	public static int width() {
		return instance.innerWidth();
	}
	
	public static int height() {
		return instance.innerHeight();
	}
	
	public static BufferedImage getImage(String p) {
		if (imageCache.containsKey(p)) return imageCache.get(p);
		BufferedImage img = instance.loadImage(p);
		imageCache.put(p, img);
		return img;
	}
	
	public static void main(String[] args) {
		new GravityRun();
	}
}

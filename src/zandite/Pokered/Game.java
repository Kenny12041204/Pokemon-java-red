package zandite.Pokered;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import zandite.Pokered.GFX.Colours;
import zandite.Pokered.GFX.Font;
import zandite.Pokered.GFX.Screen;
import zandite.Pokered.GFX.SpriteSheet;
import zandite.Pokered.Level.Level;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 160;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 3;
	public static final String NAME = "Pokemon JRed";

	private JFrame frame;

	public boolean running = false;
	public int tickCount = 0;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
			BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer())
			.getData();
	private int[] colours = new int[6 * 6 * 6];

	private Screen screen;
	public InputHandler input;
	public Level level;

	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

		frame = new JFrame(NAME);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void init() {
		int index = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);

					colours[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}

		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet(
				"/Red_SpriteSheet.png"));
		input = new InputHandler(this);
		level = new Level(64, 64);
	}

	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}

	public synchronized void stop() {
		running = false;
	}

	public void run() {
		long lastTime = System.nanoTime();
		long lastTimer = System.currentTimeMillis();
		double nsPerTick = 1000000000D / 60D;
		double delta = 0;
		int ticks = 0;
		int frames = 0;

		init();

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;

			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				System.out.println(ticks + " ticks , " + frames
						+ " frames per second");
				frames = 0;
				ticks = 0;
			}
		}
	}

	private int x = 0, y = 0;

	public void tick() {
		tickCount++;

		if (input.up.isPressed()) {
			y--;
		}
		if (input.down.isPressed()) {
			y++;
		}
		if (input.left.isPressed()) {
			x--;
		}
		if (input.right.isPressed()) {
			x++;
		}
		level.tick();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		int xOffset = x - (screen.width / 2);
		int yOffset = y - (screen.height / 2);

		level.renderTiles(screen, xOffset, yOffset);
		
		for (int x = 0; x < level.width; x++) {
            int colour = Colours.get(-1, -1, -1, 000);
            if (x % 10 == 0 && x != 0) {
                    colour = Colours.get(-1, -1, -1, 500);
            }
            Font.render((x % 10) + "", screen, 0 + (x * 8), 0, colour);
    }

		// Moved to Level.jar
		// for (int y = 0; y < 32; y++) {
		// for (int x = 0; x < 32; x++) {
		// screen.render(x << 3, y << 3, 2,
		// Colours.get(000, 111, 222, 555), false, false);
		// int x2 = x + 1;
		// screen.render(x2 << 3, y << 3, 3,
		// Colours.get(000, 111, 222, 555), false, false);
		// int y2 = y + 1;
		// screen.render(x << 3, y2 << 3, 34,
		// Colours.get(000, 111, 222, 555), false, false);
		// screen.render(x2 << 3, y2 << 3, 35,
		// Colours.get(000, 111, 222, 555), false, false);
		// }
		// }

		// String msg = "Hello World";
		// Font.render(msg, screen,
		// screen.xOffset + screen.width / 2 - (msg.length() * 8 / 2),
		// screen.yOffset + screen.height / 2,
		// Colours.get(-1, -1, -1, 505));

		for (int y = 0; y < screen.height; y++) {
			for (int x = 0; x < screen.width; x++) {
				int ColourCode = screen.pixels[x + y * screen.width];
				if (ColourCode < 255) {
					pixels[x + y * WIDTH] = colours[ColourCode];

				}
			}
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		new Game().start();
	}

}
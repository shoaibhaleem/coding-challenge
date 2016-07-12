import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Box
{
	JLabel label;
	boolean[] paths;
	Line[] lines;
	Node data;

	public Box(int x, int y, int l, int m)
	{
		data = null;
		label = new JLabel(l + "" + m);
		label.setFont(new Font("Tahoma", Font.PLAIN, 11));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		int w = 104, h = 53;
		label.setBounds(x, y, w, h);
		label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(30, 144, 255)));
		paths = new boolean[16];
		lines = new Line[] { new Line(x + (w / 2), y, x + (w / 2), y + 12), new Line(x + w, y, x + w - 9, y + 12),
				new Line(x + w, y + (h / 2), x + w - 9, y + (h / 2)), new Line(x + w, y + h, x + w - 9, y + h - 12),
				new Line(x + (w / 2), y + h, x + (w / 2), y + h - 12), new Line(x, y + h, x + 9, y + h - 12),
				new Line(x, y + (h / 2), x + 9, y + (h / 2)), new Line(x, y, x + 9, y + 12) };
	}

}

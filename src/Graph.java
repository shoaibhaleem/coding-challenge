import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class Graph
{

	static public Graph window;

	private JFrame frame;

	private CardLayout card;

	public JDesktopPane startPane;
	public JDesktopPane graphPane;

	private JLabel lblheading;
	private JTextPane fileNameTitle;
	private JTextField fileName;

	private JButton browseBtn;
	private JButton startBtn;

	ArrayList<ArrayList<Box>> grid;
	ArrayList<Line> lines;
	HashMap<Integer, ArrayList<Object>> pathMap;
	HashMap<String, Integer> directionMap;

	int[] placers = { 5, 5 };

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					window = new Graph();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Graph()
	{
		initialize();

		drawGraph();

		directionMap = new HashMap<String, Integer>();
		directionMap.put("0-", 0);
		directionMap.put("+-", 1);
		directionMap.put("+0", 2);
		directionMap.put("++", 3);
		directionMap.put("0+", 4);
		directionMap.put("-+", 5);
		directionMap.put("-0", 6);
		directionMap.put("--", 7);

		pathMap = new HashMap<Integer, ArrayList<Object>>();
		pathMap.put(0, new ArrayList<Object>(Arrays.asList(4, 1, 7)));
		pathMap.put(1, new ArrayList<Object>(Arrays.asList(8)));
		pathMap.put(2, new ArrayList<Object>(Arrays.asList(6, 1, 3)));
		pathMap.put(3, new ArrayList<Object>(Arrays.asList(9)));
		pathMap.put(4, new ArrayList<Object>(Arrays.asList(0, 3, 5)));
		pathMap.put(5, new ArrayList<Object>(Arrays.asList(10)));
		pathMap.put(6, new ArrayList<Object>(Arrays.asList(2, 5, 7)));
		pathMap.put(7, new ArrayList<Object>(Arrays.asList(11)));
		pathMap.put(8, new ArrayList<Object>(Arrays.asList(5, 14, 15)));
		pathMap.put(9, new ArrayList<Object>(Arrays.asList(7, 12, 15)));
		pathMap.put(10, new ArrayList<Object>(Arrays.asList(1, 12, 13)));
		pathMap.put(11, new ArrayList<Object>(Arrays.asList(3, 13, 14)));
		pathMap.put(12, new ArrayList<Object>(Arrays.asList("9>8&9", "10>11&10")));
		pathMap.put(13, new ArrayList<Object>(Arrays.asList("10>9&10", "11>8&11")));
		pathMap.put(14, new ArrayList<Object>(Arrays.asList("8>9&8", "11>10&11")));
		pathMap.put(15, new ArrayList<Object>(Arrays.asList("9>10&9", "8>11&8")));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		card = new CardLayout(0, 0);

		final int length = 1920;
		final int height = 1045;

		frame = new JFrame();
		frame.setBounds(0, 0, length, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(card);
		frame.setTitle("Venmo Grapher");
		frame.setFocusable(true);

		startPane = new JDesktopPane();
		startPane.setBackground(Color.WHITE);
		frame.getContentPane().add(startPane, "startPane");

		lines = new ArrayList<>();

		graphPane = new JDesktopPane()
		{
			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);

				for (Line line : lines)
				{
					g.drawLine(line.x1, line.y1, line.x2, line.y2);
				}
			}
		};
		graphPane.setBackground(Color.WHITE);
		frame.getContentPane().add(graphPane, "graphPane");

		card.show(frame.getContentPane(), "startPane");

		String title = "Venmo Grapher";
		int titleLength = title.length() * 83;
		lblheading = new JLabel(title);
		lblheading.setForeground(new Color(30, 144, 255));
		lblheading.setFont(new Font("Trajan Pro", Font.BOLD, 86));
		lblheading.setHorizontalAlignment(SwingConstants.CENTER);
		lblheading.setBounds((length - titleLength) / 2, 29, titleLength, 79);
		startPane.add(lblheading);

		fileNameTitle = new JTextPane();
		fileNameTitle.setFont(new Font("Tahoma", Font.PLAIN, 19));
		fileNameTitle.setEditable(false);
		fileNameTitle.setText("Input file name");
		fileNameTitle.setBounds(((length - 319) / 2) - 35, ((height - 35) / 2) - 35, 219, 35);
		startPane.add(fileNameTitle);

		fileName = new JTextField();
		fileName.setHorizontalAlignment(SwingConstants.CENTER);
		fileName.setBounds(((length - 319) / 2) - 131, ((height - 35) / 2), 319, 35);
		fileName.setFont(new Font("Trajan", Font.PLAIN, 16));
		startPane.add(fileName);

		startBtn = new JButton("Load");
		final Graph tr = this;
		startBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				tr.card.show(frame.getContentPane(), "graphPane");
				Node n1 = new Node("Rezwan");
				Node n2 = new Node("Azfar", n1);
				Node n3 = new Node("Haleem", n1);
				Node n4 = new Node("Haleem", n3);
				Node n5 = new Node("Haleem", n3);
				Node n6 = new Node("Haleem", n3);
				Node n7 = new Node("Haleem", n3);
				Node n8 = new Node("Haleem", n3);
				Node n9 = new Node("Haleem", n3);
				Node n10 = new Node("Haleem", n3);
			}
		});
		startBtn.setForeground(new Color(30, 144, 255));
		startBtn.setFont(new Font("Vani", Font.BOLD, 17));
		startBtn.setBounds(((length - 99) / 2) + 190, ((height - 57) / 2), 99, 57);
		startPane.add(startBtn);

		browseBtn = new JButton("Browse");
		browseBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(tr.frame);
				if (result == JFileChooser.APPROVE_OPTION)
				{
					File selectedFile = fileChooser.getSelectedFile();
					System.out.println("Selected file: " + selectedFile.getAbsolutePath());
					tr.fileName.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		browseBtn.setForeground(new Color(30, 144, 255));
		browseBtn.setFont(new Font("Vani", Font.BOLD, 11));
		browseBtn.setBounds(((length - 99) / 2) + 80, ((height - 57) / 2) + 10, 80, 35);
		startPane.add(browseBtn);

	}

	private void drawGraph()
	{
		grid = new ArrayList<ArrayList<Box>>();
		for (int i = 0; i < 12; i++)
		{
			grid.add(new ArrayList<Box>());
			for (int j = 0; j < 30; j++)
			{
				Box temp = new Box(j * 103, i * 52, i, j);
				grid.get(i).add(temp);
				graphPane.add(temp.label);
			}
		}
	}
}

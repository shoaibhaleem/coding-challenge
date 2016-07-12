import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;

public class Node
{
	String name;
	Point location;
	ArrayList<Node> connections;

	public Node(String name)
	{
		this.name = name;
		location = new Point(Graph.window.placers[0] += 3, Graph.window.placers[1] += 3);
		connections = new ArrayList<>();
		paint(null);
	}

	public Node(String name, Node parent)
	{
		Point info = findEmptyLocation(parent.location);
		if (info != null)
		{
			this.name = name;
			location = info;
			connections = new ArrayList<>();
			paint(parent.location);
		}
		else
		{
			System.out.println("Could find any places");
		}
	}

	public void paint(Point parent)
	{
		Box place = Graph.window.grid.get((int) location.getY()).get((int) location.getX());
		place.data = this;
		place.label.setText(name);

		if (parent != null)
		{
			for (int row = 0; row < Graph.window.grid.size(); row++)
			{
				for (int col = 0; col < Graph.window.grid.get(row).size(); col++)
				{
					Box box = Graph.window.grid.get(row).get(col);
					for (int i = 0; i < 8; i++)
					{
						if (box.paths[i])
						{
							Graph.window.lines.add(box.lines[i]);
						}
					}

					int[] border = new int[4];
					for (int i = 12; i < 16; i++)
					{
						if (box.paths[i])
							border[i - 12] = 1;
						else
							border[i - 12] = 0;
					}

					box.label.setBorder(BorderFactory.createMatteBorder(border[0], border[1], border[2], border[3],
							new Color(30, 144, 255)));
				}
			}
		}

		Graph.window.graphPane.repaint();
	}

	public static Point findEmptyLocation(Point parent)
	{
		Point location = new Point();

		int u = (int) parent.getX();
		int v = (int) parent.getY();

		boolean allChecked = false;

		for (int l = 1; !allChecked; l++)
		{
			allChecked = true;
			ArrayList<Point> currentlvl = new ArrayList<>();

			for (int x = u; x <= u + l; x++)
			{
				int y = v - l;

				if ((x >= 0 && x <= 29) && (y >= 0 && y <= 11))
				{
					allChecked = false;

					currentlvl.add(new Point(x, y));
				}
			}

			for (int y = v - l + 1; y <= v + l; y++)
			{
				int x = u + l;

				if ((x >= 0 && x <= 29) && (y >= 0 && y <= 11))
				{
					allChecked = false;

					currentlvl.add(new Point(x, y));
				}
			}

			for (int x = u + l - 1; x >= u - l; x--)
			{
				int y = v + l;

				if ((x >= 0 && x <= 29) && (y >= 0 && y <= 11))
				{
					allChecked = false;

					currentlvl.add(new Point(x, y));
				}
			}

			for (int y = v + l - 1; y >= v - l; y--)
			{
				int x = u - l;

				if ((x >= 0 && x <= 29) && (y >= 0 && y <= 11))
				{
					allChecked = false;

					currentlvl.add(new Point(x, y));
				}
			}

			for (int x = u - l + 1; x < u; x++)
			{
				int y = v - l;

				if ((x >= 0 && x <= 29) && (y >= 0 && y <= 11))
				{
					allChecked = false;

					currentlvl.add(new Point(x, y));
				}
			}

			int n = 2 * l;
			int j = 0;

			for (int i = l + 1; i > 0; i--)
			{
				if (i == l + 1)
					j = 0;
				else
					j = i;

				for (; j < currentlvl.size(); j += n)
				{
					int x = (int) currentlvl.get(j).getX();
					int y = (int) currentlvl.get(j).getY();
					if (Graph.window.grid.get(y).get(x).data == null)
					{
						Object[] isLinkable = isLinkable(u, v, x, y);
						if ((boolean) isLinkable[0])
						{
							location = new Point(x, y);
							return location;
						}
					}
				}

				if (i == l)
					n--;
			}

		}

		return null;
	}

	public static Object[] isLinkable(int u, int v, int x, int y)
	{
		Object[] info = new Object[2];

		int[] bearing = { x - u, y - v };
		String direction = "";

		for (int sign : bearing)
			if (sign > 0)
				direction += "+";
			else if (sign < 0)
				direction += "-";
			else
				direction += "0";

		int[] currentBox = { u, v };
		boolean entered = false;
		Stack path = new Stack(), keyChain = new Stack();
		int key = Graph.window.directionMap.get(direction);
		int prevKey = key;
		int tempKey = 0;
		ArrayList<Object> currentOptions = Graph.window.pathMap.get(key);
		while (true)
		{
			keyChain.push(new ArrayList<>(Arrays.asList(key)));

			path.push(currentOptions);

			if (key < 8)
			{
				if (!entered)
				{
					entered = true;
					Graph.window.grid.get(currentBox[1]).get(currentBox[0]).paths[key] = true;
					int[] dir = findDirection(key);
					currentBox[0] += dir[0];
					currentBox[1] += dir[1];
					prevKey = key;
					key = (int) currentOptions.get(0);
					currentOptions = Graph.window.pathMap.get(key);
				}
				else
				{
					if (currentOptions.isEmpty())
					{
						info[0] = false;
						info[1] = null;
						return info;
					}

					if ((currentBox[0] == x && currentBox[1] == y)
							&& !Graph.window.grid.get(currentBox[1]).get(currentBox[0]).paths[key])
					{
						Graph.window.grid.get(currentBox[1]).get(currentBox[0]).paths[key] = true;
						info[0] = true;
						info[1] = keyChain;
						return info;
					}
					else
					{
						path.pop();
						keyChain.pop();
						key = (int) keyChain.pop().get(0);
						if (!keyChain.isEmpty())
						{
							prevKey = (int) keyChain.pop().get(0);
							keyChain.push(new ArrayList<Object>(Arrays.asList(prevKey)));
						}
						if (key >= 8 && key <= 11)
							openPoint(currentBox, prevKey, key);
						currentOptions = path.pop();
						currentOptions.remove(0);
						continue;
					}
				}
			}
			else if (key < 12)
			{
				int[] dir = findDirection(key);
				dir[0] += currentBox[0];
				dir[1] += currentBox[1];

				if (isPointTaken(currentBox, prevKey, key) || currentOptions.isEmpty())
				{
					path.pop();
					keyChain.pop();
					key = (int) keyChain.pop().get(0);
					prevKey = (int) keyChain.pop().get(0);
					keyChain.push(new ArrayList<Object>(Arrays.asList(prevKey)));
					currentOptions = path.pop();
					currentOptions.remove(0);
					if (key < 8)
						Graph.window.grid.get(currentBox[1]).get(currentBox[0]).paths[key] = false;

					int temp = key + 2;
					if (temp > 11)
						temp = 8;
					currentBox = findDirection(temp);
					continue;
				}
				else
				{
					currentBox = dir;
					closePoint(currentBox, prevKey, key);
					prevKey = key;
					key = (int) currentOptions.get(0);
					currentOptions = Graph.window.pathMap.get(key);
				}
			}
			else if (key < 16)
			{
				if (currentOptions.isEmpty())
				{
					path.pop();
					keyChain.pop();
					key = (int) keyChain.pop().get(0);
					prevKey = (int) keyChain.pop().get(0);
					if (key >= 8 && key <= 11)
						openPoint(currentBox, prevKey, key);
					keyChain.push(new ArrayList<Object>(Arrays.asList(prevKey)));
					currentOptions = path.pop();
					currentOptions.remove(0);
				}

				for (int j = 0; j > currentOptions.size(); j++)
				{
					String[] split = ((String) currentOptions.get(j)).split(">");
					if (split[0].equals(Integer.toString(prevKey)))
					{
						keyChain.push(new ArrayList<>(Arrays.asList(currentOptions.get(j))));
						split = split[1].split("&");
						currentOptions = new ArrayList<>(
								Arrays.asList(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
						Graph.window.grid.get(currentBox[1]).get(currentBox[0]).paths[key] = true;
						tempKey = prevKey;
						prevKey = key;
						key = 16;
						break;
					}
				}
			}
			else
			{
				if (currentOptions.isEmpty())
				{
					path.pop();
					keyChain.pop();
					key = (int) keyChain.pop().get(0);
					prevKey = (int) keyChain.pop().get(0);
					keyChain.push(new ArrayList<Object>(Arrays.asList(prevKey)));
					Graph.window.grid.get(currentBox[1]).get(currentBox[0]).paths[key] = false;
					currentOptions = path.pop();
					currentOptions.remove(0);
				}
				else
				{
					if (tempKey == Graph.window.directionMap.get(direction))
					{
						key = tempKey;
					}
					else
					{
						prevKey = key;
						key = (Integer) currentOptions.get(0);
					}
					currentOptions = Graph.window.pathMap.get(key);
				}
			}
		}
	}

	public static boolean isPointTaken(int[] currentBox, int prevKey, int key)
	{
		boolean output = true;
		int[][] bools = new int[][] { findDirection(prevKey - 1), findDirection(prevKey),
				findDirection(prevKey == 7 ? 0 : prevKey + 1), currentBox };
		for (int i = 0; i < 3; i++)
		{
			bools[i][0] += currentBox[0];
			bools[i][1] += currentBox[1];
		}

		int[] keys = new int[4];
		keys[3] = key;
		for (int i = 0; i < 3; i++)
		{
			if (key + i + 1 > 11)
			{
				key = 8;
			}
			else
				key += (i + 1);

			keys[i] = key;
		}

		for (int i = 0; i < 4; i++)
		{
			output = output && Graph.window.grid.get(bools[i][1]).get(bools[i][0]).paths[keys[i]];
		}

		return output;
	}

	public static void openPoint(int[] currentBox, int prevKey, int key)
	{
		modPoint(currentBox, prevKey, key, false);
	}

	public static void closePoint(int[] currentBox, int prevKey, int key)
	{
		modPoint(currentBox, prevKey, key, true);
	}

	public static void modPoint(int[] currentBox, int prevKey, int key, boolean change)
	{
		int[][] bools = new int[][] { findDirection(prevKey - 1), findDirection(prevKey),
				findDirection(prevKey == 7 ? 0 : prevKey + 1), currentBox };
		for (int i = 0; i < 3; i++)
		{
			bools[i][0] += currentBox[0];
			bools[i][1] += currentBox[1];
		}

		int[] keys = new int[4];
		keys[3] = key;
		for (int i = 0; i < 3; i++)
		{
			if (key + i + 1 > 11)
			{
				key = 8;
			}
			else
				key += (i + 1);

			keys[i] = key;
		}

		for (int i = 0; i < 4; i++)
		{
			Graph.window.grid.get(bools[i][1]).get(bools[i][0]).paths[keys[i]] = change;
		}
	}

	public static int[] findDirection(int dir)
	{
		int[] output = new int[2];

		switch (dir)
		{
			case 0:
				output[0] = 0;
				output[1] = -1;
				break;

			case 1:
				output[0] = 1;
				output[1] = -1;
				break;

			case 2:
				output[0] = 1;
				output[1] = 0;
				break;

			case 3:
				output[0] = 1;
				output[1] = 1;
				break;

			case 4:
				output[0] = 0;
				output[1] = 1;
				break;

			case 5:
				output[0] = -1;
				output[1] = 1;
				break;

			case 6:
				output[0] = -1;
				output[1] = 0;
				break;

			case 7:
				output[0] = -1;
				output[1] = -1;
				break;
		}

		return output;
	}

	public static int[] findDirection(String dir)
	{
		int[] output = new int[2];

		switch (dir)
		{
			case "0-":
				output[0] = 0;
				output[1] = -1;
				break;

			case "+-":
				output[0] = 1;
				output[1] = -1;
				break;

			case "+0":
				output[0] = 1;
				output[1] = 0;
				break;

			case "++":
				output[0] = 1;
				output[1] = 1;
				break;

			case "0+":
				output[0] = 0;
				output[1] = 1;
				break;

			case "-+":
				output[0] = -1;
				output[1] = 1;
				break;

			case "-0":
				output[0] = -1;
				output[1] = 0;
				break;

			case "--":
				output[0] = -1;
				output[1] = -1;
				break;
		}

		return output;
	}
}


import golite.node.Node;
import golite.node.Token;
import golite.node.Start;
import golite.analysis.DepthFirstAdapter;

public class ASTSerializer extends DepthFirstAdapter {
	private StringBuilder builder;

	public ASTSerializer() {
		builder = new StringBuilder();
	}

	public String getSerialized() {
		int i = builder.length();
		while (i > 0) {
			if (builder.charAt(i-1) == ',') {
				i--;
			} else {
				break;
			}
		}
		builder.delete(i, builder.length());
		return builder.toString();
	}

	public void inStart(Start node) {
		// do nothing
	}

	public void outStart(Start node) {
		// do nothing
	}

	public void defaultIn(Node node) {
		builder.append(node.getClass().getSimpleName().substring(1));
		builder.append('(');
	}

    public void defaultCase(Node node)
    {
		builder.append(((Token) node).getText());
		builder.append(',');
    }

	public void defaultOut(Node node) {
		if (builder.charAt(builder.length()-1) == ',') {
			builder.insert(builder.length()-1, ')');
		} else {
			builder.append("),");
		}
	}
}
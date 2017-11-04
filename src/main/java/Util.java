import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class Util {
    static void setAllAnchorsTo0(Node node) {
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
    }

    static <T extends Comparable<T>> T clamp(T value, T min, T max) {
        if (value.compareTo(max) > 0) return max;
        if (value.compareTo(min) < 0) return min;
        else return value;
    }
}

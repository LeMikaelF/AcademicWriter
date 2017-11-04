import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class RacerImpl extends Racer {

    final private Timeline timeline;
    private final int objective;
    private final IntegerProperty numberOfWords;
    //This value is the accuracy of the racer. For example, if the value is 30, it will be at full right if the word count is 30 over objective,
    //and at full left if word count is 30 under objective.
    private final double accuracy = 30;
    private long startTime;
    private Rectangle green;
    private Rectangle red;
    private AnchorPane anchorPaneGreen;
    private AnchorPane anchorPaneRed;

    RacerImpl(int objective, IntegerProperty numberOfWords) {
        this.objective = objective;
        this.numberOfWords = numberOfWords;
        timeline = buildTimeline();
        numberOfWords.addListener((observable, oldValue, newValue) -> update(newValue.intValue()));
        createShapes();
    }

    private Timeline buildTimeline() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            update(numberOfWords.get());
        }));
        return timeline;
    }

    private void createShapes() {
        //Setup panes that will hold the two rectangles.
        anchorPaneGreen = new AnchorPane();
        AnchorPane.setTopAnchor(anchorPaneGreen, 0d);
        AnchorPane.setBottomAnchor(anchorPaneGreen, 0d);
        AnchorPane.setRightAnchor(anchorPaneGreen, 0d);

        anchorPaneRed = new AnchorPane();
        AnchorPane.setTopAnchor(anchorPaneRed, 0d);
        AnchorPane.setBottomAnchor(anchorPaneRed, 0d);
        AnchorPane.setLeftAnchor(anchorPaneRed, 0d);

        widthProperty().addListener((observable, oldValue, newValue) -> {
            AnchorPane.setLeftAnchor(anchorPaneGreen, newValue.doubleValue() / 2d);
            AnchorPane.setRightAnchor(anchorPaneRed, newValue.doubleValue() / 2d);
        });

        //Create the two rectangles: green for positive (over objective), red for negative.
        //They are each contained in their own AnchorPane.

        green = new Rectangle(0d, getHeight());
        green.setId("green");
        AnchorPane.setLeftAnchor(green, 0d);
        green.heightProperty().bind(anchorPaneGreen.heightProperty());

        red = new Rectangle(0d, getHeight());
        red.setId("red");
        AnchorPane.setRightAnchor(red, 0d);
        red.heightProperty().bind(anchorPaneRed.heightProperty());


        //Build node hierarchy
        anchorPaneGreen.getChildren().add(green);
        anchorPaneRed.getChildren().add(red);

        getChildren().addAll(anchorPaneGreen, anchorPaneRed);
    }

    private void update(int numberOfWords) {
        long timer = System.currentTimeMillis() - startTime;
        double hoursDelta = Duration.millis(timer).toHours();
        double desired = objective * hoursDelta;
        double delta = numberOfWords - desired;
        System.out.println(delta);
        adjustRectangles(delta);

    }

    private void adjustRectangles(double delta) {
        green.setWidth(Util.clamp(delta, 0d, accuracy) * anchorPaneGreen.getWidth() / accuracy);
        red.setWidth(Math.abs(Util.clamp(delta, accuracy * -1, 0d)) * anchorPaneRed.getWidth() / accuracy);
    }

    @Override
    public void start() {
        //Setup timeline
        timeline.playFromStart();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void stop() {
        //Stop timeline
        timeline.stop();
    }

}

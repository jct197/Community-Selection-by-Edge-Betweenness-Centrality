package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import graph.CapGraph;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.OutputType;
import org.graphstream.stream.file.FileSinkImages.Resolutions;
import java.io.BufferedReader;
import java.io.FileReader;


public class TextProController {

	
	private final static double DEFAULT_SPACING = 55;
	private final static double CONTROL_HEIGHT = 132;
	private final static double SPACE_DIV = 8.5;
	private final static double BUTTON_WIDTH = 160.0;
	private final static double RBOX_THRESHOLD = 520;	 // threshold to change spacing of right VBox
	
	
	// used when showing new stage/scene
	private MainApp mainApp;
	
	// used for getting new objects
	private LaunchClass launch;
	
	// UI Controls
	private TextArea textBox;
	
	@FXML
	private VBox leftPane;
	
	@FXML
	private VBox rightBox;
	
	@FXML
	private HBox container;
	
	@FXML
	private TextField commField;

	@FXML
	private TextField dField;

	@FXML
	private TextField eField;
	
	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * 
     * Initialize and add text area to application
     */
	@FXML
	private void initialize() {
		// make field displaying flesch score read-only
		commField.setEditable(true);
		dField.setEditable(false);
		eField.setEditable(false);
		
		
		launch = new LaunchClass();
		
		// instantiate and add custom text area
		textBox = new TextArea();
		textBox.setPrefSize(570, 492);
		textBox.setStyle("-fx-font-size: 14px");
		
		
		textBox.setWrapText(true);
		try {
			String localDir = System.getProperty("user.dir");
			BufferedReader br = new BufferedReader(new FileReader(localDir+"/src/application/temp/test_6.txt"));
			StringBuilder sb = new StringBuilder();
			try {
				String line = br.readLine();
				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}
				textBox.appendText(sb.toString());
			} catch (IOException e){
				System.out.print("Init File Load error");
			}
		} catch (FileNotFoundException e) {
			System.out.print("Init File Not Found");
			}
		// add text area as first child of left VBox
		ObservableList<Node> nodeList = leftPane.getChildren();
		Node firstChild = nodeList.get(0);
		nodeList.set(0, textBox);
		nodeList.add(firstChild);
		
		VBox.setVgrow(textBox, Priority.ALWAYS);
		
		
		
		// ADD LISTENERS FOR ADJUSTING ON RESIZE
		
		container.widthProperty().addListener(li -> {
			
			if((container.getWidth() - leftPane.getPrefWidth()) < BUTTON_WIDTH) {
				rightBox.setVisible(false);
			}
			else {
				rightBox.setVisible(true);
			}
		});
		
		// function for setting spacing of rightBox
		Consumer<VBox> adjustSpacing = box ->  {
			if(container.getHeight() < RBOX_THRESHOLD) {
				rightBox.setSpacing((container.getHeight() - CONTROL_HEIGHT)/SPACE_DIV);
			}
			else {
				rightBox.setSpacing(DEFAULT_SPACING);
			}
		};
		
		container.heightProperty().addListener(li -> {
			adjustSpacing.accept(rightBox);
		});
		
		rightBox.visibleProperty().addListener( li -> {
			if(rightBox.isVisible()) {
				 container.getChildren().add(rightBox);
				 adjustSpacing.accept(rightBox);
			 }
			 else {
				 container.getChildren().remove(rightBox);
			 }	 
		});
	}

	/**
     * Is called by the main application to give a reference back to itself.
     *
     * 
     * @param mainApp
     */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
	}

    @FXML
    private void handleLoadText() {
        mainApp.showLoadFileDialog(textBox);
	}

	@FXML
	private void handleGetdepth() {
		String text = textBox.getText();
		graph.graph doc = launch.getGraph(text);
		HashMap<Integer, List<Integer>> community = ((CapGraph) doc).getCommunity();
		for (int j : community.keySet()){
			System.out.print(j + ": "+community.get(j)+"  ");
			System.out.println();
		}
		System.out.println();
		int dvar = 0;
		for (int i : community.keySet()){
			if (community.get(i).size()>dvar){
				dvar = community.get(i).size();
			}
		}
		// create Document representation of  current text
		dField.setText(Integer.toString(dvar));
		eField.clear();
		commField.clear();
	}

	private void graphwithnoComm() throws IOException{
		String text = textBox.getText();
		eField.clear();
		graph.graph doc = launch.getGraph(text);
		HashMap<Integer, HashSet<Integer>> test = ((CapGraph) doc).exportGraph();
		HashMap<Integer, List<Integer>> community = ((CapGraph) doc).getCommunity();
		Graph Out = new SingleGraph("test_8");
		HashSet<Integer> temp = new HashSet<>();
		HashMap<List<Integer>,Integer> cEdge = new HashMap<>();
		for (int i : test.keySet()){
			if (!temp.contains(i)) {
				temp.add(i);
				Out.addNode(String.valueOf(i));
			}
			for (int j : test.get(i)) {
				List<Integer> aEdge = new ArrayList<>();
				if (!temp.contains(j)) {
					temp.add(j);
					Out.addNode(String.valueOf(j));
					aEdge.add(i);
					aEdge.add(j);
					if (!cEdge.containsKey(aEdge)){
						cEdge.put(aEdge,1);
						Out.addEdge((String.valueOf(i) + "_" + String.valueOf(j)), String.valueOf(i), String.valueOf(j), true);
					}
				}
				else {
					aEdge.add(i);
					aEdge.add(j);
					if (!cEdge.containsKey(aEdge)) {
						cEdge.put(aEdge,1);
						Out.addEdge((String.valueOf(i) + "_" + String.valueOf(j)), String.valueOf(i), String.valueOf(j), true);
					}
				}
			}
		}
		int dmax = 0;
		FileSinkImages pic = new FileSinkImages(OutputType.PNG, Resolutions.VGA);
		pic.setOutputPolicy(FileSinkImages.OutputPolicy.BY_STEP);
		pic.setLayoutPolicy(FileSinkImages.LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);
		pic.writeAll(Out, "src/application/temp/Test" + ".png");
		mainApp.showImage();
	}

	private void graphwithComm() throws IOException{
		String text = textBox.getText();
		int depth = Integer.parseInt(commField.getText());
		depth--;
		graph.graph doc = launch.getGraph(text);
		HashMap<Integer, HashSet<Integer>> test = ((CapGraph) doc).exportGraph();
		HashMap<Integer, List<Integer>> community = ((CapGraph) doc).getCommunity();
		Graph Out = new SingleGraph("test_8");
		HashSet<Integer> temp = new HashSet<>();
		HashMap<List<Integer>,Integer> cEdge = new HashMap<>();
		for (int i : test.keySet()){
			if (!temp.contains(i)) {
				temp.add(i);
				Out.addNode(String.valueOf(i));
			}
			for (int j : test.get(i)) {
				List<Integer> aEdge = new ArrayList<>();
				if (!temp.contains(j)) {
					temp.add(j);
					Out.addNode(String.valueOf(j));
					aEdge.add(i);
					aEdge.add(j);
					if (!cEdge.containsKey(aEdge)){
						cEdge.put(aEdge,1);
						Out.addEdge((String.valueOf(i) + "_" + String.valueOf(j)), String.valueOf(i), String.valueOf(j), true);
					}
				}
				else {
					aEdge.add(i);
					aEdge.add(j);
					if (!cEdge.containsKey(aEdge)) {
						cEdge.put(aEdge,1);
						Out.addEdge((String.valueOf(i) + "_" + String.valueOf(j)), String.valueOf(i), String.valueOf(j), true);
					}
				}
			}
		}
		int dmax = 0;
		int length = 0;
		for (int i : community.keySet()) {
			if (community.get(i).get(depth) > dmax) {
				dmax = community.get(i).get(depth);
			}
			if (community.get(i).size()>length){
				length = community.get(i).size();
			}
		}
		for (int i = 0; i < Out.getNodeCount(); i++) {
			org.graphstream.graph.Node node = Out.getNode(i);
			int num = Integer.parseInt(node.toString());
			double gcol = ((double) community.get(num).get(depth)) / ((double) dmax);
			if (!Double.isNaN(gcol)) {
				node.addAttribute("ui.style", "shape:circle;fill-mode:dyn-plain;fill-color:red,green,blue;size:15px;");
				node.setAttribute("ui.color", gcol);
			}
		}
		dmax = dmax+1;
		eField.setText(Integer.toString(dmax));
		FileSinkImages pic = new FileSinkImages(OutputType.PNG, Resolutions.VGA);
		pic.setOutputPolicy(FileSinkImages.OutputPolicy.BY_STEP);
		pic.setLayoutPolicy(FileSinkImages.LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);
		pic.writeAll(Out, "src/application/temp/Test" + ".png");
		mainApp.showImage();
	}
	@FXML
	private void handleCreateGraphic() throws IOException {
		if (commField.getText().equals((""))){
			graphwithnoComm();
		}
		else{
			try{
				int out = Integer.parseInt(commField.getText());
			}
			catch (NumberFormatException e) {
				mainApp.showInputErrorDialog("integers only");
				commField.clear();
				return;
			}
			if (Integer.parseInt(commField.getText())>Integer.parseInt(dField.getText())
					||Integer.parseInt(commField.getText())<=0) {
				mainApp.showInputErrorDialog("out of bounds exception");
				commField.clear();
				return;
			}
			else {
				graphwithComm();
			}
		}
	}
	@FXML
    private void handleClear() {
        textBox.clear();
        dField.clear();
        commField.clear();
        eField.clear();
    }
}

package cubevisual;

import cubesolver.CubeFileFinder;
import cubesolver.cube.Cube3x3x3;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Scanner;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CubeApp extends Application {

    private final GridPane grid = new GridPane();
    private final GridPane artGP = new GridPane();
    private final double lPer = 0.65;
    private final double rPer = 0.35;
    private final VBox vboxC = new VBox();
    private final ImageView logo = new ImageView();
    private final ImageView settingsIcon = new ImageView();
    private final ImageView settingsRegion = new ImageView();
    private final VBox vboxT = new VBox();
    private final TextArea tp = new TextArea(); //terminal prompt, response, and output
    private final TextField tr = new TextField();
    private final TextFlow to = new TextFlow();//new TextArea();
    private final Group root = new Group();
    private final PerspectiveCamera camera = new PerspectiveCamera();
    private final double theta = 15;
    private final float cs = 100;
    private double xm = 0; //x movement
    private double xr = 15; //x rotate
    private double xrt = xr; //x rotate total
    private double ym = 0;
    private double yr = -15;
    private double yrt = yr;
    private double xrsu = 0; //x rotate since update
    private double yrsu = 0;
    private static final String cubeStateFile = CubeFileFinder.getPath("storage\\cubeState.txt");
    private static final String settingsFile = CubeFileFinder.getPath("storage\\settings.txt");
    private final Cube3x3x3 cube = loadCube();
    private final Cube3D cube3d = new Cube3D(cube, 100, this, null);
    private final SubScene sub = new SubScene(root, 0, 0, true, SceneAntialiasing.BALANCED);
    private double logoW;
    private double logoH;
    private double width;
    private double height;
    private double subW;
    private double subH;
    private static String[] settings = loadSettings();
    private boolean settingsOpen = false;
    private Stage settingsStage;
    private boolean colorPickerOpen = false; //TODO: can remove static!!
    private Stage colorPickerStage;
    private double stageWidth = 0;
    private ScrollPane sp = new ScrollPane(to);

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(grid);
        //scene.getStylesheets().add(this.getClass().getResource("\\CubeStyles.css").toExternalForm());
        width = grid.getWidth();
        height = grid.getHeight();
        subW = width * lPer;
        subH = height * .83;

        sub.setFill(Color.WHITE);


        sub.setCamera(camera);

        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        try {
            logo.setImage(new Image(new FileInputStream(new File(CubeFileFinder.getPath("art\\logo.png")))));
            settingsIcon.setImage(new Image(new FileInputStream(new File(CubeFileFinder.getPath("art\\settingsIcon.png")))));
            settingsRegion.setImage(new Image(new FileInputStream(new File(CubeFileFinder.getPath("art\\settingsRegion.png")))));
        } catch (FileNotFoundException e) {
        }
        logo.setPreserveRatio(true);
        settingsIcon.setPreserveRatio(true);
        settingsRegion.setPreserveRatio(true);
        settingsRegion.setOpacity(0);

        logoW = logo.getImage().getWidth();
        logoH = logo.getImage().getHeight();

        AmbientLight al = new AmbientLight(Color.WHITE);
        root.getChildren().addAll(al);

        vboxC.setSpacing(5);
        vboxC.setAlignment(Pos.CENTER);

        artGP.add(settingsIcon, 0, 0);
        artGP.add(settingsRegion, 0, 0);
        artGP.add(logo, 1, 0);

        ColumnConstraints cc0 = new ColumnConstraints();
        ColumnConstraints cc1 = new ColumnConstraints();
        ColumnConstraints cc2 = new ColumnConstraints();
        cc0.setPercentWidth(30);
        cc1.setPercentWidth(40);
        cc2.setPercentWidth(30);
        artGP.getColumnConstraints().addAll(cc0, cc1, cc2);

        GridPane.setHalignment(settingsIcon, HPos.LEFT);
        GridPane.setHalignment(settingsRegion, HPos.LEFT);
        GridPane.setHalignment(logo, HPos.CENTER);

        vboxC.getChildren().addAll(artGP, sub);

        tp.setEditable(false);
        //to.setEditable(false);
        tp.setWrapText(true);
        //to.setWrapText(true);
        to.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        vboxT.setSpacing(5);
        vboxT.setAlignment(Pos.CENTER);
        //ScrollPane sp = new ScrollPane(to);
        sp.setFitToWidth(true);
        sp.setPadding(new Insets(5));
        sp.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        vboxT.getChildren().addAll(tp, tr, sp);


        grid.add(vboxC, 0, 0);
        grid.add(vboxT, 1, 0);

        tr.requestFocus();


        root.getChildren().addAll(cube3d.getMeshViews());

        CubeTerminal ct = new CubeTerminal(cube, tp, tr, to, cube3d, this);
        cube3d.setCT(ct);
        tr.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    ct.exec(tr.getText());
                    tr.clear();
                }
            }
        });

        settingsRegion.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (settingsOpen) {
                    settingsStage.toFront();
                } else {
                    settingsOpen = true;
                    settingsStage = new Stage();
                    CubeAppSettings cas = new CubeAppSettings();
                    cas.start(settingsStage);
                }
            }
        });

        sub.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xm = event.getSceneX();
                ym = event.getSceneY();
            }
        });

        sub.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xr += event.getSceneX() - xm;
                yr += event.getSceneY() - ym;
                xr /= 2.5;
                yr /= 2.5;
                xrt += xr;
                yrt += yr;
                xrt %= 360;
                yrt %= 360;
                xrsu += xr;
                yrsu += yr;
                xm = event.getSceneX();
                ym = event.getSceneY();
                if (!cube3d.isAnimating()) {
                    int xUpThreshold = 140 - (int) xrt % 90;
                    int xDownThreshold = -140 - (int) xrt % 90;
                    int yRightThreshold = 140 - (int) yrt % 90;
                    int yLeftThreshold = -140 - (int) yrt % 90;
                    if (xrsu > xUpThreshold || xrsu < xDownThreshold) {
                        cube.move(xrsu > xUpThreshold ? "y'" : "y");
                        xrt -= xrsu;
                        xrsu = 0;
                        updateCube3d();
                    }
                    if (yrsu > yRightThreshold || yrsu < yLeftThreshold) {
                        cube.move(yrsu > yRightThreshold ? "x" : "x'");
                        yrt -= yrsu;
                        yrsu = 0;
                        updateCube3d();
                    }
                }
                camera.getTransforms().clear();
                camera.getTransforms().add(new Rotate(xrt, Rotate.Y_AXIS));
                camera.getTransforms().add(new Rotate(yrt, Rotate.X_AXIS));
                camera.getTransforms().add(new Translate(subW / -2, subH / -2));
            }

        });

        stage.setTitle("Cube Solver");
        try {
            stage.getIcons().add(new Image(new FileInputStream(new File(CubeFileFinder.getPath("art\\icon.png")))));
        } catch (FileNotFoundException e) {
        }

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            stageWidth = stage.getWidth();
            setNodesProportionally(stage.getWidth(), stage.getHeight());
        };

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        stage.setOnCloseRequest(e -> Platform.exit());
    }

    private void setNodesProportionally(double w, double h) {
        w -= 30;
        h -= 75;
        subW = w * lPer;
        subH = h * .85;
        sub.setWidth(subW);
        sub.setHeight(subH);
        if (subW * .4 < logo.getFitWidth() && vboxC.getChildren().contains(artGP)) {
            vboxC.getChildren().remove(artGP);
            vboxC.getChildren().add(0, logo);
        } else if (subW * .4 > logo.getFitWidth() && !vboxC.getChildren().contains(artGP)){
            vboxC.getChildren().remove(logo);
            artGP.add(logo, 1, 0);
            vboxC.getChildren().add(0, artGP);
        }
        float cs = (float) h / 8;
        if (cs * 3 > w * (lPer - .3)) {
            cs = (float) (w * (lPer - .3) / 3);
        }
        cube3d.setCS(cs);
        cube3d.cubeToCube3d();
        changeMeshViews();
        camera.getTransforms().clear();
        camera.getTransforms().add(new Rotate(yrt, Rotate.X_AXIS));
        camera.getTransforms().add(new Rotate(xrt, Rotate.Y_AXIS));
        camera.getTransforms().add(new Translate(subW / -2, subH / -2));
        if (h * .15 / logoH * logoW > subW) {
            logo.setFitWidth(subW);
            logo.setFitHeight(subW / logoW * logoH);
            settingsIcon.setFitWidth(subW / 2);
            settingsIcon.setFitHeight(subW / 2);
            settingsRegion.setFitWidth(subW / 2);
            settingsRegion.setFitHeight(subW / 2);
        } else {
            logo.setFitHeight(h * .15);
            logo.setFitWidth(h * .15 / logoH * logoW);
            settingsIcon.setFitWidth(h * .15 / 2);
            settingsIcon.setFitHeight(h * .15 / 2);
            settingsRegion.setFitWidth(h * .15 / 2);
            settingsRegion.setFitHeight(h * .15 / 2);
        }
        tp.setPrefWidth(w * rPer);
        tr.setPrefWidth(w * rPer);
        to.setPrefWidth(w * rPer);
        h -= tr.getHeight() + 5;
        tp.setPrefHeight(h * .15);
        to.setPrefHeight(h * .85);
        Font font = Font.font("monospace", getFontSize());
        tp.setFont(font);
        tr.setFont(font);
        /*for (Node text : to.getChildren()) {
            if (text.getStyleClass().size() == 0)
                text.getStyleClass().add("highlight-text");
                text.applyCss();
                ((Text) text).setFont(font);
        }*/
        sp.setVvalue(1);
        //to.setFont(font);
    }

    public int getFontSize() {
        return stageWidth * rPer / 40 > 10 ? (int) (stageWidth * rPer / 40) : 10;
    }

    public void scrollToBottom() {
        sp.setVvalue(1);
    }

    private void updateCube3d() {
        cube3d.setCube(cube);
        cube3d.cubeToCube3d();
        changeMeshViews();
    }

    public void changeMeshViews() {
        Iterator<Node> iter = root.getChildren().iterator();
        while (iter.hasNext()) {
            Node n = iter.next();
            if (n instanceof MeshView) {
                iter.remove();
            }
        }
        root.getChildren().addAll(cube3d.getMeshViews());
    }

    private static Cube3x3x3 loadCube() {
        Scanner fileInput = null;
        try {
            fileInput = new Scanner(new File(cubeStateFile));
        } catch (FileNotFoundException e) {
            return new Cube3x3x3();
        }
        String[] cubeState = new String[Cube3x3x3.NUM_PIECES];
        for (int i = 0; i < Cube3x3x3.NUM_PIECES; i++) {
            cubeState[i] = fileInput.nextLine().substring(4);
        }
        return new Cube3x3x3(cubeState);
    }

    private static String[] loadSettings() {
        String[] settings = new String[7];
        try {
            Scanner fileInput = null;
            fileInput = new Scanner(new File(settingsFile));
            for (int i = 0; i < settings.length; i++) {
                settings[i] = fileInput.nextLine();
            }
        } catch (Exception e) {
            settings[0] = "Terminal";
            settings[1] = "Light";
            settings[2] = "3x3x3";
            settings[3] = "Fridrich";
            settings[4] = "Automatic";
            settings[5] = "1.0x";
            settings[6] = "Yes";
        }
        return settings;
    }

    private static void writeSettings() {
        try {
            Formatter output = new Formatter(settingsFile);
            for (int i = 0; i < settings.length; i++) {
                output.format(settings[i] + "%n");
            }
            if (output != null) {
                output.close();
            }
        } catch (FileNotFoundException e) {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void launchColorPicker() {
        if (colorPickerOpen) {
            colorPickerStage.toFront();
        } else {
            colorPickerOpen = true;
            colorPickerStage = new Stage();
            ColorPicker cp = new ColorPicker();
            cp.start(colorPickerStage);
        }
    }

    private class CubeAppSettings extends Application {

        @Override
        public void start(Stage stage) {
            GridPane pane = new GridPane();
            ObservableList<String> interactionModes = FXCollections.observableArrayList("Terminal", "Buttons");
            ObservableList<String> themes = FXCollections.observableArrayList("Light", "Dark");
            ObservableList<String> cubes = FXCollections.observableArrayList("2x2x2", "3x3x3", "4x4x4", "5x5x5", "3x3x3 Mixup");
            ObservableList<String> solveModes = FXCollections.observableArrayList("Fridrich", "Optimal");
            ObservableList<String> pressToMove = FXCollections.observableArrayList("Automatic", "Enter to move");
            ObservableList<String> turnSpeeds = FXCollections.observableArrayList("0.5x", "1.0x", "2.0x");
            ObservableList<String> saveOnExit = FXCollections.observableArrayList("Yes", "No");
            ComboBox imcb = new ComboBox(interactionModes);
            ComboBox tcb = new ComboBox(themes);
            ComboBox ccb = new ComboBox(cubes);
            ComboBox smcb = new ComboBox(solveModes);
            ComboBox ptmcb = new ComboBox(pressToMove);
            ComboBox tscb = new ComboBox(turnSpeeds);
            ComboBox soecb = new ComboBox(saveOnExit);
            ArrayList<ComboBox> cbs = new ArrayList<>();
            cbs.addAll(Arrays.asList(imcb, tcb, ccb, smcb, ptmcb, tscb, soecb));
            for (int i = 0; i < cbs.size(); i++) {
                cbs.get(i).setValue(settings[i]);
                cbs.get(i).setPrefWidth(150);
                GridPane.setHalignment(cbs.get(i), HPos.CENTER);
            }
            imcb.setOnAction((event) -> {
                settings[0] = (String) imcb.getSelectionModel().getSelectedItem();
                writeSettings();
            });
            tcb.setOnAction((event) -> {
                settings[1] = (String) tcb.getSelectionModel().getSelectedItem();
                writeSettings();
            });
            ccb.setOnAction((event) -> {
                settings[2] = (String) ccb.getSelectionModel().getSelectedItem();
                writeSettings();
            });
            smcb.setOnAction((event) -> {
                settings[3] = (String) smcb.getSelectionModel().getSelectedItem();
                writeSettings();
            });
            ptmcb.setOnAction((event) -> {
                settings[4] = (String) ptmcb.getSelectionModel().getSelectedItem();
                writeSettings();
            });
            tscb.setOnAction((event) -> {
                settings[5] = (String) tscb.getSelectionModel().getSelectedItem();
                writeSettings();
            });
            soecb.setOnAction((event) -> {
                settings[6] = (String) soecb.getSelectionModel().getSelectedItem();
                writeSettings();
            });
            Label iml = new Label("Interaction Mode");
            Label tl = new Label("Theme");
            Label cl = new Label("Cube");
            Label sml = new Label("Solve Mode");
            Label ptml = new Label("Solve Pace");
            Label tsl = new Label("Turn Speed");
            Label soel = new Label("Save on Exit");
            ArrayList<Label> ls = new ArrayList<>();
            ls.addAll(Arrays.asList(iml, tl, cl, sml, ptml, tsl, soel));
            for (int i = 0; i < ls.size(); i++) {
                GridPane.setHalignment(ls.get(i), HPos.CENTER);
            }
            int row = 0;
            for (int i = 0; i < ls.size(); i++) {
                pane.add(ls.get(i), 0, row++);
                pane.add(cbs.get(i), 0, row++);
                row++;
            }
            pane.setPadding(new Insets(30));
            pane.setVgap(5);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100);
            pane.getColumnConstraints().add(cc);
            Scene scene = new Scene(pane, 300, 525);
            stage.setTitle("Settings");
            try {
                stage.getIcons().add(new Image(new FileInputStream(new File(CubeFileFinder.getPath("art\\icon.png")))));
            } catch (FileNotFoundException e) {
            }
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    settingsOpen = false;
                }
            });
        }
    }

    private class ColorPicker extends Application {

        @Override
        public void start(Stage stage) {
            GridPane pane = new GridPane();
            pane.setPadding(new Insets(30));
            Scene scene = new Scene(pane, 800, 600);
            stage.setTitle("Cube Input");
            try {
                stage.getIcons().add(new Image(new FileInputStream(new File(CubeFileFinder.getPath("art\\icon.png")))));
            } catch (FileNotFoundException e) {
            }
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    colorPickerOpen = false;
                }
            });
        }
    }
}

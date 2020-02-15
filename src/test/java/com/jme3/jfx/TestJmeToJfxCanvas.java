package com.jme3.jfx;

import static com.jme3.jfx.injfx.processor.FrameTransferSceneProcessor.TransferMode;
import com.jme3.jfx.injfx.processor.FrameTransferSceneProcessor;
import com.jme3.jfx.injfx.JmeToJfxIntegrator;
import com.jme3.jfx.injfx.JmeToJfxApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * The test to show how to integrate jME to Canvas.
 *
 * @author JavaSaBr
 */
public class TestJmeToJfxCanvas extends Application {

    private JmeToJfxApplication application;
    private FrameTransferSceneProcessor processor;

    public static void main(@NotNull String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull Stage stage) {

        var canvas = new Canvas();
        canvas.setFocusTraversable(true);
        canvas.setOnMouseClicked(event -> canvas.requestFocus());

        var button = new Button("BUTTON");
        var stackPane = new StackPane(canvas, button);
        var scene = new Scene(stackPane, 800, 600);

        canvas.widthProperty()
                .bind(stackPane.widthProperty());
        canvas.heightProperty()
                .bind(stackPane.heightProperty());

        stage.setTitle("Test");
        stage.setScene(scene);
        stage.show();

        // creates jME application
        application = makeJmeApplication();

        // integrate jME application with Canvas
        processor = JmeToJfxIntegrator.startAndBindMainViewPort(application, canvas,
                Thread::new, TransferMode.DOUBLE_BUFFERED);
    }

    @Override
    public void stop() {
        JmeToJfxIntegrator.destroy(application, processor);
        System.exit(0);
    }

    private static @NotNull JmeToJfxApplication makeJmeApplication() {

        var settings = JmeToJfxIntegrator.prepareSettings(new AppSettings(true));
        var application = new JmeToJfxApplication() {

            protected Geometry player;
            Boolean isRunning = true;

            @Override
            public void simpleInitApp() {
                super.simpleInitApp();
                Box b = new Box(1, 1, 1);
                player = new Geometry("Player", b);
                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                mat.setColor("Color", ColorRGBA.Blue);
                player.setMaterial(mat);
                rootNode.attachChild(player);
                initKeys(); // load my custom keybinding
            }

            /** Custom Keybinding: Map named actions to inputs. */
            private void initKeys() {
                /** You can map one or several inputs to one named mapping. */
                inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
                inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
                inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_K));
                inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE), // spacebar!
                        new MouseButtonTrigger(MouseInput.BUTTON_LEFT));        // left click!
                /** Add the named mappings to the action listeners. */
                inputManager.addListener(actionListener, "Pause");
                inputManager.addListener(analogListener, "Left", "Right", "Rotate");
            }

            /** Use this listener for KeyDown/KeyUp events */
            private ActionListener actionListener = (name, keyPressed, tpf) -> {
                if (name.equals("Pause") && !keyPressed) {
                    isRunning = !isRunning;
                }
            };

            /** Use this listener for continuous events */
            private AnalogListener analogListener = new AnalogListener() {
                public void onAnalog(String name, float value, float tpf) {
                    if (isRunning) {
                        if (name.equals("Rotate")) {
                            player.rotate(0, value, 0);
                        }
                        if (name.equals("Right")) {
                            player.move((new Vector3f(value, 0, 0)));
                        }
                        if (name.equals("Left")) {
                            player.move(new Vector3f(-value, 0, 0));
                        }
                    } else {
                        System.out.println("Press P to unpause.");
                    }
                }
            };
        };

        application.setSettings(settings);
        application.setShowSettings(false);

        return application;
    }
}

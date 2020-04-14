package com.jme3.jfx.injfx;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.post.FilterPostProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The base implementation of {@link Application} for using in the JavaFX.
 *
 * @author JavaSaBr.
 */
public class JmeToJfxApplication extends SimpleApplication {

    private static final ApplicationThreadExecutor EXECUTOR = ApplicationThreadExecutor.getInstance();

    /**
     * The post filter processor.
     */
    @Nullable
    protected FilterPostProcessor postProcessor;

    public JmeToJfxApplication() {
    }

    @Override
    public void update() {
        EXECUTOR.execute();
        super.update();
    }

    @Override
    public void simpleInitApp() {
        postProcessor = new FilterPostProcessor(assetManager);
        viewPort.addProcessor(postProcessor);
    }

    @Override
    public void reshape(int w, int h) {
    }

    /**
     * Get the post filter processor.
     *
     * @return the post filter processor.
     */
    protected @NotNull FilterPostProcessor getPostProcessor() {
        return notNull(postProcessor);
    }
}

JME3-JFX
========

JFX Gui bridge for JME with usefull utilities for common usecases

License is the New BSD License (same as JME3) 
http://opensource.org/licenses/BSD-3-Clause

#### How to add the library to your project

#### Gradle

```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/javasabr/maven" 
    }
}

dependencies {
    implementation 'com.jme3:jfx:3.0.7'
}
```
    
#### Maven

```xml
<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>bintray-javasabr-maven</id>
        <name>bintray</name>
        <url>https://dl.bintray.com/javasabr/maven</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.jme3</groupId>
    <artifactId>jfx</artifactId>
    <version>3.0.7</version>
</dependency>
```

#### How to integrate jME application to JavaFX ImageView:

```java

    var imageView = new ImageView();
        
    var settings = JmeToJfxIntegrator.prepareSettings(new AppSettings(true));
    var application = new MySomeApplication();
    
    JmeToJfxIntegrator.startAndBindMainViewPort(application, imageView, Thread::new, TransferMode.DOUBLE_BUFFERED);
```

#### How to integrate javaFX UI to jME application:

```java

    public class MyApplication extends SimpleApplication {
    
        private JmeFxContainer container;
        
        @Override
        public void simpleInitApp() {
            container = JmeFxContainer.install(this, getGuiNode());
    
            var button = new Button("BUTTON");
            var rootNode = new Group(button);
            var scene = new Scene(rootNode, 600, 600);
            scene.setFill(Color.TRANSPARENT);

            container.setScene(scene, rootNode);
            
            getInputManager().setCursorVisible(true);
        }
    
        @Override
        public void simpleUpdate(float tpf) {
            super.simpleUpdate(tpf);
            // we decide here that we need to do transferring the last frame from javaFX to jME
            if (container.isNeedWriteToJme()) {
                container.writeToJme();
            }
        }
    }
```

Also, you can look at some examples in the tests package:

* [jME Application is inside jFX Canvas](https://github.com/JavaSaBr/JME3-JFX/blob/master/src/test/java/com/jme3/jfx/TestJmeToJfxCanvas.java)
* [jME Application is inside jFX ImageView](https://github.com/JavaSaBr/JME3-JFX/blob/master/src/test/java/com/jme3/jfx/TestJmeToJfxImageView.java)
* [JavaFX Scene is inside jME Application](https://github.com/JavaSaBr/JME3-JFX/blob/master/src/test/java/com/jme3/jfx/TestJfxInJme.java)

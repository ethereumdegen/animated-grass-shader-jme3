package grass;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

import java.util.Random;

import jme3tools.optimize.GeometryBatchFactory;

public class CustomGrassTest extends SimpleApplication {

public static void main(String[] args){
CustomGrassTest app = new CustomGrassTest();
app.start();
}

private float elapsedTime = 0;

private Material grassShader;

private Geometry grassGeom;

private Node allGrass = new Node("all grass");

private Vector2f windDirection = new Vector2f();
private float windStrength;

private Random random = new Random();

private Geometry ground;

public void simpleInitApp() {
	
	DirectionalLight light = new DirectionalLight();
	light.setColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.9f));
	light.setDirection(new Vector3f(0.7f,0.8f,0.7f));
	this.getRootNode().addLight(light);
	
	DirectionalLight otherlight = new DirectionalLight();
	otherlight.setColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.9f));
	otherlight.setDirection(new Vector3f(-0.7f,0.8f,-0.7f));
	this.getRootNode().addLight(otherlight);
	
this.getFlyByCamera().setMoveSpeed(15);
ground = new Geometry("ground", new Quad(10, 10) );
ground.setMaterial( new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md") );
ground.getMaterial().setColor("Color", ColorRGBA.Brown);
//Texture t = assetManager.loadTexture("Textures/Terrain/grass.dds");
//t.setWrap(Texture.WrapMode.Repeat);
//ground.getMaterial().setTexture("ColorMap", t );

ground.setLocalTranslation(0, 0, 10);
ground.rotate(-90*FastMath.DEG_TO_RAD, 0, 0);
rootNode.attachChild(ground);

windDirection.x = random.nextFloat();
windDirection.y = random.nextFloat();
windDirection.normalize();

grassGeom = new Geometry("grass", new Quad(2, 2));

grassShader = new Material(assetManager, "MatDefs/Grass/MovingGrass.j3md");
Texture grass = assetManager.loadTexture("Textures/grass.jpg");
grass.setWrap(Texture.WrapAxis.S, Texture.WrapMode.Repeat);
grassShader.setTexture("Texture", grass);
grassShader.setTexture("Noise", assetManager.loadTexture("Textures/normal.jpg"));


// set wind direction
grassShader.setVector2("WindDirection", windDirection);
windStrength = 0.8f;
grassShader.setFloat("WindStrength", windStrength);

grassShader.setTransparent(true);
grassShader.getAdditionalRenderState().setAlphaTest(true);
grassShader.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
grassShader.getAdditionalRenderState().setAlphaFallOff(0.5f);
grassShader.setColor("Color", new ColorRGBA(0.53f, 0.83f, 0.53f, 1f));
grassShader.setFloat("Time", 0);

grassShader.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
grassGeom.setQueueBucket(Bucket.Transparent);
grassGeom.setMaterial(grassShader);
grassGeom.center();

for (int y = 0; y < 10; y++){
for (int x = 0; x < 10; x++){
Geometry grassInstance = grassGeom.clone();

grassInstance.setLocalTranslation(x  + (float)(Math.random()*1f), 0, y + (float)(Math.random()*1f));
grassInstance.scale (0.4f, 0.4f + random.nextFloat()*.2f, 0.4f);
allGrass.attachChild(grassInstance);
}
}

Spatial grassNode = GeometryBatchFactory.optimize(allGrass);
grassNode.setQueueBucket(Bucket.Transparent);
grassNode.setMaterial(grassShader);
grassNode.updateModelBound();

rootNode.attachChild(grassNode);

cam.setLocation(new Vector3f(8.378951f, 5.4324f, 8.795956f));
cam.setRotation(new Quaternion(-0.083419204f, 0.90370524f, -0.20599906f, -0.36595422f));

}

@Override
public void simpleUpdate(float tpf){
elapsedTime += 0.01;
grassShader.setFloat("Time", elapsedTime);
}
}
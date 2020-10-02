package scene;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import light.AmbientLight;
import light.Light;
import light.ParallelLight;
import light.PointLight;
import light.SpotLight;
import surface.Mesh;
import surface.Sphere;
import surface.Surface;

public class Scene {
	private String outputFile;
	private Vec3 backgroundColor;
	private Camera camera;
	private List<Light> lights = new ArrayList<Light>();
	private List<Surface> surfaces = new ArrayList<Surface>();
	private String filePath;
	public Scene(File xmlFile) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();
			NodeList nodeList = document.getElementsByTagName("scene");
			Node rootElement = nodeList.item(0);
			this.filePath = xmlFile.getParent();
			if(rootElement.hasAttributes()) {
				NamedNodeMap attribs = rootElement.getAttributes();
				Node output = attribs.getNamedItem("output_file");
				this.outputFile = this.filePath + "\\" + output.getNodeValue();
			}
			initBackgroundColor(rootElement);
			initCamera(rootElement);
			initLights(rootElement);
			initSurfaces(rootElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initBackgroundColor(Node rootElement) {
		if (rootElement.hasChildNodes()) {
			boolean colorFound = false;
			double x = -1, y = -1, z = -1;
			NodeList nodeList = rootElement.getChildNodes();
			for(int index = 0; (index < nodeList.getLength()) || !colorFound; index++) {
				Node node = nodeList.item(index);
				if(node.getNodeName()=="background_color") {
					colorFound = true;
					x = Double.parseDouble(node.getAttributes().getNamedItem("r").getNodeValue());
					y = Double.parseDouble(node.getAttributes().getNamedItem("g").getNodeValue());
					z = Double.parseDouble(node.getAttributes().getNamedItem("b").getNodeValue());
				}
			}
			this.backgroundColor = new Vec3(x ,y ,z );
		}		
	}
	private void initCamera(Node rootElement){
		try {
			if(rootElement.hasChildNodes()) {
				Vec3 position = new Vec3();
				Vec3 lookAt = new Vec3();
				Vec3 up = new Vec3();
				double horizontalFOV = -1;
				Vec2 vec2 = new Vec2();
				double maxBounces = -1;
				boolean cameraFound = false;
				NodeList nodeList = rootElement.getChildNodes();
				for(int index = 0; (index < nodeList.getLength()) || !cameraFound; index++) {
					Node node = nodeList.item(index);
					if(node.getNodeName()=="camera") {
						cameraFound = true;
						NodeList cameraList = node.getChildNodes();
						for(int i = 0; i < cameraList.getLength(); i++) {
							switch(cameraList.item(i).getNodeName()) {
							case "position": {
								position.setX(Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("x").getNodeValue()));
								position.setY(Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("y").getNodeValue()));
								position.setZ(Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("z").getNodeValue()));
							}
							break;
							case "lookat": {
								lookAt.setX(Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("x").getNodeValue()));
								lookAt.setY(Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("y").getNodeValue()));
								lookAt.setZ(Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("z").getNodeValue()));
							}
							break;
							case "up": {
								up.setX(Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("x").getNodeValue()));
								up.setY(Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("y").getNodeValue()));
								up.setZ(Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("z").getNodeValue()));
							}
							break;
							case "horizontal_fov": {
								horizontalFOV = Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("angle").getNodeValue());
							}
							break;
							case "resolution": {
								vec2.setH(Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("horizontal").getNodeValue()));
								vec2.setV(Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("vertical").getNodeValue()));
							}
							break;
							case "max_bounces": {
								maxBounces = Double.parseDouble(cameraList.item(i).getAttributes().getNamedItem("n").getNodeValue());
							}
							break;
							default: break;
							}
						}
					}
					camera = new Camera(position, lookAt, up, horizontalFOV, vec2, maxBounces);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void initLights(Node rootElement) {
		try {
			if(rootElement.hasChildNodes()) {
				Vec3 color = new Vec3();
				Vec3 position = new Vec3();
				Vec3 direction = new Vec3();
				Vec2 falloff = new Vec2();
				boolean lightsFound = false;
				NodeList nodeList = rootElement.getChildNodes();
				for(int index = 0; (index < nodeList.getLength()) || !lightsFound; index++) {
					Node node = nodeList.item(index);
					if(node.getNodeName()=="lights") {
						lightsFound = true;
						NodeList lightsList = node.getChildNodes();
						for(int i = 0; i < lightsList.getLength(); i++) {
							switch(lightsList.item(i).getNodeName()) {
							case "ambient_light": {
								Element element = (Element) lightsList.item(i);
								color.setX(Double.parseDouble(element.getElementsByTagName("color").item(0).getAttributes().getNamedItem("r").getNodeValue()));
								color.setY(Double.parseDouble(element.getElementsByTagName("color").item(0).getAttributes().getNamedItem("g").getNodeValue()));
								color.setZ(Double.parseDouble(element.getElementsByTagName("color").item(0).getAttributes().getNamedItem("b").getNodeValue()));
								AmbientLight ambientLight = new AmbientLight(color);
								this.lights.add(ambientLight);
							}
							break;
							case "parallel_light": {
								Element element = (Element) lightsList.item(i);
								color.setX(Double.parseDouble(element.getElementsByTagName("color").item(0).getAttributes().getNamedItem("r").getNodeValue()));
								color.setY(Double.parseDouble(element.getElementsByTagName("color").item(0).getAttributes().getNamedItem("g").getNodeValue()));
								color.setZ(Double.parseDouble(element.getElementsByTagName("color").item(0).getAttributes().getNamedItem("b").getNodeValue()));
								direction.setX(Double.parseDouble(element.getElementsByTagName("direction").item(0).getAttributes().getNamedItem("x").getNodeValue()));
								direction.setY(Double.parseDouble(element.getElementsByTagName("direction").item(0).getAttributes().getNamedItem("y").getNodeValue()));
								direction.setZ(Double.parseDouble(element.getElementsByTagName("direction").item(0).getAttributes().getNamedItem("z").getNodeValue()));
								ParallelLight parallelLight = new ParallelLight(color, direction);
								this.lights.add(parallelLight);
							}
							break;
							case "point_light": {
								Element element = (Element) lightsList.item(i);
								color.setX(Double.parseDouble(element.getElementsByTagName("color").item(0).getAttributes().getNamedItem("r").getNodeValue()));
								color.setY(Double.parseDouble(element.getElementsByTagName("color").item(0).getAttributes().getNamedItem("g").getNodeValue()));
								color.setZ(Double.parseDouble(element.getElementsByTagName("color").item(0).getAttributes().getNamedItem("b").getNodeValue()));
								position.setX(Double.parseDouble(element.getElementsByTagName("position").item(0).getAttributes().getNamedItem("x").getNodeValue()));
								position.setY(Double.parseDouble(element.getElementsByTagName("position").item(0).getAttributes().getNamedItem("y").getNodeValue()));
								position.setZ(Double.parseDouble(element.getElementsByTagName("position").item(0).getAttributes().getNamedItem("z").getNodeValue()));
								PointLight pointLight = new PointLight(color, position);
								this.lights.add(pointLight);
							}
							break;
							case "spot_light": {
								Element element = (Element) lightsList.item(i);
								color.setX(Double.parseDouble(element.getElementsByTagName("color").item(0).getAttributes().getNamedItem("r").getNodeValue()));
								color.setY(Double.parseDouble(element.getElementsByTagName("color").item(0).getAttributes().getNamedItem("g").getNodeValue()));
								color.setZ(Double.parseDouble(element.getElementsByTagName("color").item(0).getAttributes().getNamedItem("b").getNodeValue()));
								position.setX(Double.parseDouble(element.getElementsByTagName("position").item(0).getAttributes().getNamedItem("x").getNodeValue()));
								position.setY(Double.parseDouble(element.getElementsByTagName("position").item(0).getAttributes().getNamedItem("y").getNodeValue()));
								position.setZ(Double.parseDouble(element.getElementsByTagName("position").item(0).getAttributes().getNamedItem("z").getNodeValue()));
								direction.setX(Double.parseDouble(element.getElementsByTagName("direction").item(0).getAttributes().getNamedItem("x").getNodeValue()));
								direction.setY(Double.parseDouble(element.getElementsByTagName("direction").item(0).getAttributes().getNamedItem("y").getNodeValue()));
								direction.setZ(Double.parseDouble(element.getElementsByTagName("direction").item(0).getAttributes().getNamedItem("z").getNodeValue()));
								falloff.setH(Double.parseDouble(element.getElementsByTagName("falloff").item(0).getAttributes().getNamedItem("alpha1").getNodeValue()));
								falloff.setV(Double.parseDouble(element.getElementsByTagName("falloff").item(0).getAttributes().getNamedItem("alpha2").getNodeValue()));
								SpotLight spotLight = new SpotLight(color, position, direction, falloff);
								this.lights.add(spotLight);
							}
							break;
							default: break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void initSurfaces(Node rootElement) {
		try {
			if(rootElement.hasChildNodes()) {				
				boolean surfacesFound = false;
				String materialType = "";
				NodeList nodeList = rootElement.getChildNodes();
				for(int index = 0; (index < nodeList.getLength()) || !surfacesFound; index++) {
					Node node = nodeList.item(index);
					if(node.getNodeName()=="surfaces") {
						surfacesFound = true;
						NodeList surfacesList = node.getChildNodes();
						for(int surfaceIndex = 0; surfaceIndex < surfacesList.getLength(); surfaceIndex++) {
							Vec3 position = new Vec3();
							double radius = -1;
							Vec3 materialColor = new Vec3();
							Vec3 phong = new Vec3();
							double phongExponent = 0;
							double reflectance = 0;
							double transmittance = 0;
							double refraction = 0;
							File meshFile = null;
							File textureFile = null;
							boolean transformed = false;
							boolean done =  false;
							switch(surfacesList.item(surfaceIndex).getNodeName()) {
							case "sphere": {
								NodeList sphereList = surfacesList.item(surfaceIndex).getChildNodes();
								radius = Double.parseDouble(surfacesList.item(surfaceIndex).getAttributes().getNamedItem("radius").getNodeValue());
								Sphere sphere = new Sphere();
								done = false;
								for(int sphereIndex = 0; sphereIndex < sphereList.getLength(); sphereIndex++) {
									switch (sphereList.item(sphereIndex).getNodeName()) {
									case "position": {
										Element element = (Element) sphereList.item(sphereIndex);
										position.setX(Double.parseDouble(element.getAttributes().getNamedItem("x").getNodeValue()));
										position.setY(Double.parseDouble(element.getAttributes().getNamedItem("y").getNodeValue()));
										position.setZ(Double.parseDouble(element.getAttributes().getNamedItem("z").getNodeValue()));
									}
									break;
									case "material_solid": {
										materialType = "solid";
										NodeList materialList = sphereList.item(sphereIndex).getChildNodes();
										for(int materialIndex = 0; materialIndex < materialList.getLength(); materialIndex++) {
											switch(materialList.item(materialIndex).getNodeName()) {
											case "color": {
												Element element = (Element) materialList.item(materialIndex);
												materialColor.setX(Double.parseDouble(element.getAttributes().getNamedItem("r").getNodeValue()));
												materialColor.setY(Double.parseDouble(element.getAttributes().getNamedItem("g").getNodeValue()));
												materialColor.setZ(Double.parseDouble(element.getAttributes().getNamedItem("b").getNodeValue()));
											}
											break;
											case "phong": {
												Element element = (Element) materialList.item(materialIndex);
												phong.setX(Double.parseDouble(element.getAttributes().getNamedItem("ka").getNodeValue()));
												phong.setY(Double.parseDouble(element.getAttributes().getNamedItem("kd").getNodeValue()));
												phong.setZ(Double.parseDouble(element.getAttributes().getNamedItem("ks").getNodeValue()));
												phongExponent = Double.parseDouble(element.getAttributes().getNamedItem("exponent").getNodeValue());
											}
											break;
											case "reflectance": {
												Element element = (Element) materialList.item(materialIndex);
												reflectance = Double.parseDouble(element.getAttributes().getNamedItem("r").getNodeValue());
											}
											break;
											case "transmittance": {
												Element element = (Element) materialList.item(materialIndex);
												transmittance = Double.parseDouble(element.getAttributes().getNamedItem("t").getNodeValue());
											}
											break;
											case "refraction": {
												Element element = (Element) materialList.item(materialIndex);
												refraction = Double.parseDouble(element.getAttributes().getNamedItem("iof").getNodeValue());
												done = true;
											}
											break;
											default: break;
											}
										}										
									}
									break;
									case "material_textured": {
										materialType = "textured";
										NodeList materialList = sphereList.item(sphereIndex).getChildNodes();
										for(int materialIndex = 0; materialIndex < materialList.getLength(); materialIndex++) {
											switch(materialList.item(materialIndex).getNodeName()) {
											case "texture": {
												Element element = (Element) materialList.item(materialIndex);
												textureFile = new File(element.getAttributes().getNamedItem("name").getNodeValue());
											}
											break;
											case "phong": {
												Element element = (Element) materialList.item(materialIndex);
												phong.setX(Double.parseDouble(element.getAttributes().getNamedItem("ka").getNodeValue()));
												phong.setY(Double.parseDouble(element.getAttributes().getNamedItem("kd").getNodeValue()));
												phong.setZ(Double.parseDouble(element.getAttributes().getNamedItem("ks").getNodeValue()));
												phongExponent = Double.parseDouble(element.getAttributes().getNamedItem("exponent").getNodeValue());
											}
											break;
											case "reflectance": {
												Element element = (Element) materialList.item(materialIndex);
												reflectance = Double.parseDouble(element.getAttributes().getNamedItem("r").getNodeValue());
											}
											break;
											case "transmittance": {
												Element element = (Element) materialList.item(materialIndex);
												transmittance = Double.parseDouble(element.getAttributes().getNamedItem("t").getNodeValue());
											}
											break;
											case "refraction": {
												Element element = (Element) materialList.item(materialIndex);
												refraction = Double.parseDouble(element.getAttributes().getNamedItem("iof").getNodeValue());
												done = true;
											}
											break;
											default: break;
											}
										}							
									}
									case "transform": {
										transformed = true;
										Vec3 vradius = new Vec3(radius, radius, radius);
										if (materialType.equals("solid")) {
											sphere = new Sphere(position, materialColor, vradius, phong, phongExponent, reflectance, transmittance, refraction);
										} else {
											sphere = new Sphere(position, textureFile, vradius, phong, phongExponent, reflectance, transmittance, refraction);
										}
										NodeList transformList = sphereList.item(sphereIndex).getChildNodes();
										for(int transformIndex = 0; transformIndex < transformList.getLength(); transformIndex++) {
											switch(transformList.item(transformIndex).getNodeName()) {
											case "translate": {
												Element element = (Element) transformList.item(transformIndex);
												double x = Double.parseDouble(element.getAttributes().getNamedItem("x").getNodeValue());
												double y = Double.parseDouble(element.getAttributes().getNamedItem("y").getNodeValue());
												double z = Double.parseDouble(element.getAttributes().getNamedItem("z").getNodeValue());
												sphere.translate(new Vec3(x, y, z));
											}
											break;
											case "scale": {
												Element element = (Element) transformList.item(transformIndex);
												double x = Double.parseDouble(element.getAttributes().getNamedItem("x").getNodeValue());
												double y = Double.parseDouble(element.getAttributes().getNamedItem("y").getNodeValue());
												double z = Double.parseDouble(element.getAttributes().getNamedItem("z").getNodeValue());
												sphere.scale(new Vec3(x, y, z));
											}
											break;
											case "rotateX": {
												Element element = (Element) transformList.item(transformIndex);
												double angle = Double.parseDouble(element.getAttributes().getNamedItem("theta").getNodeValue());
												sphere.rotateX(angle);
											}
											break;
											case "rotateY": {
												Element element = (Element) transformList.item(transformIndex);
												double angle = Double.parseDouble(element.getAttributes().getNamedItem("theta").getNodeValue());
												sphere.rotateY(angle);
											}
											break;
											case "rotateZ": {
												Element element = (Element) transformList.item(transformIndex);
												double angle = Double.parseDouble(element.getAttributes().getNamedItem("theta").getNodeValue());
												sphere.rotateZ(angle);
											}
											break;
											default:break;
											}
										}
									}
									default: break;
									}	
								}
								if (transformed) {
									if (!sphere.isNull()) surfaces.add(sphere);
								} else if (done){
									Vec3 vradius = new Vec3(radius, radius, radius);
									if (materialType.equals("solid")) {
										surfaces.add(new Sphere(position, materialColor, vradius, phong, phongExponent, reflectance, transmittance, refraction));
									} else {
										surfaces.add(new Sphere(position, textureFile, vradius, phong, phongExponent, reflectance, transmittance, refraction));
									}
								}
							}
							break;
							case "mesh": {
								NodeList meshList = surfacesList.item(surfaceIndex).getChildNodes();
								meshFile = new File(filePath + "/" + surfacesList.item(surfaceIndex).getAttributes().getNamedItem("name").getNodeValue());
								Mesh mesh = new Mesh();
								done = false;
								for(int meshIndex = 0; meshIndex < meshList.getLength(); meshIndex++) {
									switch (meshList.item(meshIndex).getNodeName()) {
									case "material_solid": {
										materialType = "solid";
										NodeList materialList = meshList.item(meshIndex).getChildNodes();
										for(int materialIndex = 0; materialIndex < materialList.getLength(); materialIndex++) {
											switch(materialList.item(materialIndex).getNodeName()) {
											case "color": {
												Element element = (Element) materialList.item(materialIndex);
												materialColor.setX(Double.parseDouble(element.getAttributes().getNamedItem("r").getNodeValue()));
												materialColor.setY(Double.parseDouble(element.getAttributes().getNamedItem("g").getNodeValue()));
												materialColor.setZ(Double.parseDouble(element.getAttributes().getNamedItem("b").getNodeValue()));
											}
											break;
											case "phong": {
												Element element = (Element) materialList.item(materialIndex);
												phong.setX(Double.parseDouble(element.getAttributes().getNamedItem("ka").getNodeValue()));
												phong.setY(Double.parseDouble(element.getAttributes().getNamedItem("kd").getNodeValue()));
												phong.setZ(Double.parseDouble(element.getAttributes().getNamedItem("ks").getNodeValue()));
												phongExponent = Double.parseDouble(element.getAttributes().getNamedItem("exponent").getNodeValue());
											}
											break;
											case "reflectance": {
												Element element = (Element) materialList.item(materialIndex);
												reflectance = Double.parseDouble(element.getAttributes().getNamedItem("r").getNodeValue());
											}
											break;
											case "transmittance": {
												Element element = (Element) materialList.item(materialIndex);
												transmittance = Double.parseDouble(element.getAttributes().getNamedItem("t").getNodeValue());
											}
											break;
											case "refraction": {
												Element element = (Element) materialList.item(materialIndex);
												refraction = Double.parseDouble(element.getAttributes().getNamedItem("iof").getNodeValue());
												done = true;
											}
											break;
											default: break;
											}
										}										
									}
									break;
									case "material_textured": {
										materialType = "textured";
										NodeList materialList = meshList.item(meshIndex).getChildNodes();
										for(int materialIndex = 0; materialIndex < materialList.getLength(); materialIndex++) {
											switch(materialList.item(materialIndex).getNodeName()) {
											case "texture": {
												Element element = (Element) materialList.item(materialIndex);
												textureFile = new File(filePath + "/" + element.getAttributes().getNamedItem("name").getNodeValue());
											}
											break;
											case "phong": {
												Element element = (Element) materialList.item(materialIndex);
												phong.setX(Double.parseDouble(element.getAttributes().getNamedItem("ka").getNodeValue()));
												phong.setY(Double.parseDouble(element.getAttributes().getNamedItem("kd").getNodeValue()));
												phong.setZ(Double.parseDouble(element.getAttributes().getNamedItem("ks").getNodeValue()));
												phongExponent = Double.parseDouble(element.getAttributes().getNamedItem("exponent").getNodeValue());
											}
											break;
											case "reflectance": {
												Element element = (Element) materialList.item(materialIndex);
												reflectance = Double.parseDouble(element.getAttributes().getNamedItem("r").getNodeValue());
											}
											break;
											case "transmittance": {
												Element element = (Element) materialList.item(materialIndex);
												transmittance = Double.parseDouble(element.getAttributes().getNamedItem("t").getNodeValue());
											}
											break;
											case "refraction": {
												Element element = (Element) materialList.item(materialIndex);
												refraction = Double.parseDouble(element.getAttributes().getNamedItem("iof").getNodeValue());
												done = true;
											}
											break;
											default: break;
											}
										}							
									}
									case "transform": {
										transformed = true;
										if (materialType.equals("solid")) {
											mesh = new Mesh(meshFile, materialColor, phong, phongExponent, reflectance, transmittance, refraction);
										} else {
											mesh = new Mesh(meshFile, textureFile, phong, phongExponent, reflectance, transmittance, refraction);
										}
										NodeList transformList = meshList.item(meshIndex).getChildNodes();
										for(int transformIndex = 0; transformIndex < transformList.getLength(); transformIndex++) {
											switch(transformList.item(transformIndex).getNodeName()) {
											case "translate": {
												Element element = (Element) transformList.item(transformIndex);
												double x = Double.parseDouble(element.getAttributes().getNamedItem("x").getNodeValue());
												double y = Double.parseDouble(element.getAttributes().getNamedItem("y").getNodeValue());
												double z = Double.parseDouble(element.getAttributes().getNamedItem("z").getNodeValue());
												mesh.translate(new Vec3(x, y, z));
											}
											break;
											case "scale": {
												Element element = (Element) transformList.item(transformIndex);
												double x = Double.parseDouble(element.getAttributes().getNamedItem("x").getNodeValue());
												double y = Double.parseDouble(element.getAttributes().getNamedItem("y").getNodeValue());
												double z = Double.parseDouble(element.getAttributes().getNamedItem("z").getNodeValue());
												mesh.scale(new Vec3(x, y, z));
											}
											break;
											case "rotateX": {
												Element element = (Element) transformList.item(transformIndex);
												double angle = Double.parseDouble(element.getAttributes().getNamedItem("theta").getNodeValue());
												mesh.rotateX(angle);
											}
											break;
											case "rotateY": {
												Element element = (Element) transformList.item(transformIndex);
												double angle = Double.parseDouble(element.getAttributes().getNamedItem("theta").getNodeValue());
												mesh.rotateY(angle);
											}
											break;
											case "rotateZ": {
												Element element = (Element) transformList.item(transformIndex);
												double angle = Double.parseDouble(element.getAttributes().getNamedItem("theta").getNodeValue());
												mesh.rotateZ(angle);
											}
											break;
											default:break;
											}
										}
									}
									default: break;
									}
								}
								if (transformed) {
									if (!mesh.isNull()) surfaces.add(mesh);
								} else if (done) {
									if (materialType.equals("solid")) {
										surfaces.add(new Mesh(meshFile, materialColor, phong, phongExponent, reflectance, transmittance, refraction));
									} else {
										surfaces.add(new Mesh(meshFile, textureFile, phong, phongExponent, reflectance, transmittance, refraction));
									}
								}
							}
							break;
							default: break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public String getOutputFile() {
		return this.outputFile;
	}
	public Vec3 getBackgroundColor() {
		return this.backgroundColor;
	}
	public Camera getCamera() {
		return this.camera;
	}
	public List<Light> getLights() {
		return this.lights;
	}
	public List<Surface> getSurfaces() {
		return this.surfaces;
	}
	@Override
	public String toString() {
		return "Scene [outputFile=" + outputFile + ", backgroundColor=" + backgroundColor + ", camera=" + camera
				+ ", lights=" + lights + ", surfaces=" + surfaces + "]";
	}
	
}

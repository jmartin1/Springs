package Springs;

import java.awt.Color;
import java.util.ArrayList;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.frames.DisplayFrame;

public class Springs extends AbstractSimulation {
	ArrayList<ArrayList<Particle>> springs = new ArrayList<ArrayList<Particle>>();
	DisplayFrame frame = new DisplayFrame("X" , "Y", "Spring");
	//ArrayList<Particle> particles = new ArrayList<Particle>();
	double t = 0;
	double miny;
	double maxy;
	//double amplitude;
	double netforcex;
	double netforcey;
	double windx = 0;
	double windy = 0;
	double betax = 0;
	double betay = 0;


	protected void doStep() {
		t += control.getDouble("timestep");
		double restlength = 1/(control.getDouble("particles per meter"));
		for (int n = 0; n < springs.size(); n++) {
			//System.out.println("whats up");
			ArrayList<Particle> particles = springs.get(n);

			particles.get(0).setYpos(control.getDouble("amplitude") * Math.sin(2*Math.PI*particles.get(0).getFrequency()*t));
			particles.get(0).setXpos(0);
			particles.get(particles.size()-1).setYpos(control.getDouble("amplitude") * Math.sin(2*Math.PI*10.2*t));
			particles.get(particles.size()-1).setXpos(200);
			for (int m = 1; m < particles.size()-1; m++) {
				if(m <= particles.size()-2) {
					Particle p = particles.get(m);
					Particle q = particles.get(m-1);
					Particle r = particles.get(m+1);
					netforcey = p.getK()*(q.getYpos() - p.getYpos() - restlength) + -p.getK()*(p.getYpos() - r.getYpos() - restlength);
					netforcex = p.getK()*(q.getXpos() - p.getXpos() - restlength) + -p.getK()*(p.getXpos() - r.getXpos() - restlength);
					p.step(control.getDouble("timestep"), netforcey, netforcex, betax, betay, windx, windy);
				}
			}
			for (int j = 1; j <= particles.size()-2; j++) {
				particles.get(j).move(control.getDouble("timestep"));
			}
		}
	}

	public void initialize() {
		this.delayTime = 1;
		for (int j = 0; j < control.getDouble("number of springs"); j++) {
			ArrayList<Particle> particles = new ArrayList<Particle>();
			Particle object = new Particle();
			object.setXpos(0);
			object.setYpos(0 + j*2000);
			object.setXY(object.getXpos(),object.getYpos());
			object.setK(control.getDouble("k"));
			object.setMass(control.getDouble("object mass"));
			object.setFrequency(control.getDouble("frequency") + j);
			System.out.println(object.getFrequency());
			particles.add(object);
			for (int i = 0; i < (control.getDouble("particles per meter")*control.getDouble("spring length")); i++) {
				Particle p = new Particle();
				particles.add(p);
				p.color = Color.magenta;
				p.setXpos(i+1);
				p.setYpos(0 + 2000*j);
				p.setMass(control.getDouble("total mass")/((control.getDouble("particles per meter")*control.getDouble("spring length"))));
				p.setK(control.getDouble("k"));
				p.setXY(p.getXpos(),p.getYpos());
				frame.addDrawable(p);
			}
			springs.add(particles);

		}




		DrawableShape wall = DrawableShape.createRectangle((control.getDouble("particles per meter")*control.getDouble("spring length"))+1, 0, 2, 100); //creates goalpost rectangle
		wall.setMarkerColor(Color.cyan, Color.cyan);
		frame.addDrawable(wall);
		frame.setVisible(true);
		frame.setPreferredMinMax(-300, 300, -300, 300);

	}

	public void reset() {
		control.setValue("x position", 0);
		control.setValue("y position", 0);
		control.setValue("frequency", 2.5);
		control.setValue("total mass", 50);
		control.setValue("particles per meter", 10);
		control.setValue("spring length", 20);
		control.setValue("object mass", 50);
		control.setValue("k", 10000);
		control.setValue("timestep", 0.004);
		control.setValue("amplitude", 3);
		control.setValue("number of springs", 1);
	}
	public static void main(String[] args) { //main
		SimulationControl.createApp(new Springs()); //Creates a SIP animation control and establishes communication between the control and the model
	}
}

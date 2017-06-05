package Springs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.frames.DisplayFrame;

import Springs.Particle;


public class Bungee extends AbstractSimulation {
	ArrayList<ArrayList<Particle>> springs = new ArrayList<ArrayList<Particle>>();
	ArrayList<Double> goodparticles = new ArrayList<Double>();
	ArrayList<Double> badparticles = new ArrayList<Double>();
	HashSet<Double> goodp = new HashSet<Double>();
	HashSet<Double> badp = new HashSet<Double>();

	DisplayFrame frame = new DisplayFrame("X" , "Y", "Bungee");
	boolean arrayfull = false;
	boolean running = true;
	int numberofkinarray = 0;




	protected void doStep() {
		//calculate force on each:
		double netforcey;
		double netforcex;
		double restlength = (control.getDouble("bungee length"))/(control.getDouble("particles per meter")*control.getDouble("bungee length")); //here is the issue


		//find force on each: force of gravity, of particle above and below

		for (int m = 0; m < springs.size(); m++) {
			ArrayList<Particle> particles = springs.get(m);
			double sizeofarray = particles.size()-1;
			for (int i = 0; i < particles.size(); i++) {
				if(i > 0 && i < particles.size()-1) {
					Particle p = particles.get(i);
					Particle q = particles.get(i-1);
					Particle r = particles.get(i+1);
					netforcey = p.getMass()*(-9.8);
					netforcey += p.getK()*(q.getYpos() - p.getYpos() - restlength);
					netforcey += -p.getK()*(p.getYpos() - r.getYpos() - restlength);
					netforcex = p.getK()*(q.getXpos() - p.getXpos()) -p.getK()*(p.getXpos() - r.getXpos());
					p.step(control.getDouble("timestep"), netforcey, netforcex, control.getDouble("beta x"), control.getDouble("beta y"), control.getDouble("wind x velocity"), control.getDouble("wind y velocity"));
				}
				else if (i == particles.size()-1) {
					Particle p = particles.get(i);
					Particle q = particles.get(i-1);
					netforcey = p.getMass()*(-9.8) + p.getK()*(q.getYpos() - p.getYpos() - restlength);
					netforcex = p.getK()*(q.getXpos() - p.getXpos());
					p.step(control.getDouble("timestep"), netforcey, netforcex, control.getDouble("beta x"), control.getDouble("beta y"), control.getDouble("wind x velocity"), control.getDouble("wind y velocity"));


					if (p.getVelocityy() > 0 && p.getY() > -100) {
						for (int j = 0; j < springs.size(); j++) {
							
							//check if p.getX is in correct spring
							for (int c = 0; c < springs.get(j).size(); c++) {
								if (springs.get(j).get(c).getX() == p.getX()) {
									//System.out.println(j + "  " + c);
									double xpos = 10*j;
									double minx = -control.getDouble("width of river")/2 + xpos;
									double maxx = control.getDouble("width of river")/2 + xpos;
									if (p.getX() < minx || p.getX() > maxx) {
								
										if (!badp.contains(p.getK())) {
											badp.add(p.getK());
											for (int h = 0; h < springs.get(m).size(); h++) {
												particles.get(h).color = Color.red;
											}
										}
									}
								}
							}
						}						
						if (!badp.contains(p.getK())) {
							goodp.add(p.getK());
							for (int h = 0; h < springs.get(m).size(); h++) {
								particles.get(h).color = Color.green;
							}
						}
					}
					if (p.getVelocityy() > 0 && p.getY() < -100) {
						badp.add(p.getK());
						for (int h = 0; h < springs.get(m).size(); h++) {
							particles.get(h).color = Color.red;
						}
					}
					if (badp.size() + goodp.size() == 10) {
						arrayfull = true;
						running = false;
					}
				}
			}
			for (int j = 0; j <= sizeofarray; j++) {
				particles.get(j).move(control.getDouble("timestep"));
			}
		}


		if (arrayfull == true) {
			if (goodp.size() > 0) {
				Object[] karray = goodp.toArray();

				for (int i = 0; i < karray.length; i++) {
					System.out.println("The k is " + karray[i]);


				}
				System.out.println();

			}

			else {
				System.out.println("No k satisfies the solution.");
			}
			arrayfull = false;
			goodp.clear();
		}

	}

	public void initialize() {
		this.delayTime = 1;
		for (int j = 0; j < control.getDouble("number of springs"); j++) {
			ArrayList<Particle> particles = new ArrayList<Particle>();
			for(double i = 0; i > -(control.getDouble("particles per meter")*control.getDouble("bungee length")); i--) {
				ArrayList<Double> initx = new ArrayList<Double>();
				Particle p = new Particle();
				particles.add(p);
				p.setXpos(0 + j*10);
				initx.add(p.getX());
				p.setYpos(i*.1);
				p.setMass(control.getDouble("total mass")/((control.getDouble("particles per meter")*control.getDouble("bungee length"))));
				p.setK(control.getDouble("k") + j*25);
				p.setXY(p.getXpos(),p.getYpos());
				p.color = Color.magenta;
				frame.addDrawable(p);
			}

			Particle girl = new Particle();
			girl.setXpos(0 + j*10);
			girl.setYpos(-((control.getDouble("particles per meter")*control.getDouble("bungee length"))*.1));
			girl.setMass(control.getDouble("girl mass"));
			girl.setK(control.getDouble("k") + j*25);
			girl.setXY(girl.getXpos(),girl.getYpos());
			girl.color = Color.magenta;
			particles.add(girl);
			frame.addDrawable(girl);

			springs.add(particles);
		}
		frame.setVisible(true);
		frame.setPreferredMinMax(-100, 100, -100, 100);

	}
	public void reset() {
		control.setValue("x position", 0);
		control.setValue("y position", 0);
		control.setValue("total mass", 50);
		control.setValue("particles per meter", 10);
		control.setValue("bungee length", 20);
		control.setValue("girl mass", 60);
		control.setValue("k", 4000);
		control.setValue("timestep", 0.004);
		control.setValue("distance between cliff and river", 100);
		control.setValue("width of river", 50);
		control.setValue("number of springs", 10);
		control.setValue("wind x velocity", 0);
		control.setValue("wind y velocity", 0);
		control.setValue("beta x", 0);
		control.setValue("beta y", 0);

	}
	public static void main(String[] args) { //main
		SimulationControl.createApp(new Bungee()); //Creates a SIP animation control and establishes communication between the control and the model
	}
}

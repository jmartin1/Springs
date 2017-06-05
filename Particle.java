package Springs;

import java.util.ArrayList;

import org.opensourcephysics.display.Circle;


public class Particle extends Circle {
	double mass;
	double k;
	double initialvelocity;
	double accelerationx;
	double accelerationy;
	double velocityx;
	double velocityy;
	double frequency;




	public double getMass() {
		return mass;
	}


	public void setMass(double mass) {
		this.mass = mass;
	}


	public double getK() {
		return k;
	}


	public void setK(double k) {
		this.k = k;
	}


	public double getXpos() {
		return x;
	}


	public void setXpos(double xpos) {
		this.x = xpos;
	}


	public double getYpos() {
		return y;
	}


	public void setYpos(double ypos) {
		this.y = ypos;
	}


	public double getInitialvelocity() {
		return initialvelocity;
	}


	public void setInitialvelocity(double initialvelocity) {
		this.initialvelocity = initialvelocity;
	}


	public double getAccelerationx() {
		return accelerationx;
	}


	public void setAccelerationx(double accelerationx) {
		this.accelerationx = accelerationx;
	}


	public double getAccelerationy() {
		return accelerationy;
	}


	public void setAccelerationy(double accelerationy) {
		this.accelerationy = accelerationy;
	}


	public double getVelocityx() {
		return velocityx;
	}


	public void setVelocityx(double velocityx) {
		this.velocityx = velocityx;
	}


	public double getVelocityy() {
		return velocityy;
	}


	public void setVelocityy(double velocityy) {
		this.velocityy = velocityy;
	}
	
	public double getFrequency() {
		return frequency;
	}


	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}



	public void step (double timestep, double netforcey, double netforcex, double betax, double betay, double windx, double windy) {
		setAccelerationx(netforcex/getMass() - betax*(getVelocityx()-windx));
		setAccelerationy(netforcey/getMass() - betay*(getVelocityy()-windy));
		setVelocityx(getVelocityx() + getAccelerationx()*timestep);
		setVelocityy(getVelocityy() + getAccelerationy()*timestep);
	}
	
	public void move (double timestep) {
		setXpos(getXpos() + getVelocityx()*timestep);
		setYpos(getYpos() + getVelocityy()*timestep);
		setXY(getXpos(), getYpos());
	}
}

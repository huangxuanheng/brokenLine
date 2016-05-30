package com.example.gv;

import java.util.Date;

public class Point {

	private Date x;
	private float y;
	
	public Point(Date x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	public Date getX() {
		return x;
	}
	public void setX(Date x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
}

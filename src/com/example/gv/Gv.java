package com.example.gv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class Gv extends View{

	private int left;
	private int top;
	private int right;
	private int down;
	
	private int maxY=160;
	private float maxX=160;   //x轴最大显示数字
	private int minY=40;
	
	private final int lengthY=200;
	private final int lengthX=230;
	
	private float pointMaxY;   //Y轴最大点的真实坐标
	private float iy;   //Y轴最大点的到原点的真是距离
	private float ix;   //X轴最大点的到原点的真是距离
	private int mCalibrateX;   //x刻度数量
	private Date minDate;   //x最小日期时间
	
	private List<Point>points;
	
	private Canvas canvas;
	private Paint paint;     //设置绘制的折线画笔
	private Paint paintXY=new Paint();     //设置绘制的坐标轴
	
	private String showDateFormat="MM-dd";
	
	private final float calibrate=5;    //刻度长度
	private final float radiu=2;    //直线小点半径
	
	public Gv(Context context) {
		super(context, null);
	}

	public Gv(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		this.canvas=canvas;
		drawY(canvas);
		drawX(canvas);
		drawCalibrateY(canvas);
		drawCalibrateX(canvas);
		List<Point>points=new ArrayList<Point>();
		points.add(new Point(getDateByString("2016-5-26 15:30:25","yyyy-MM-dd hh:mm:ss"),50));
		points.add(new Point(getDateByString("2016-5-27 15:30:25","yyyy-MM-dd hh:mm:ss"),80));
		setPoints(points, Color.BLUE,null);
		drawPoint();
		super.onDraw(canvas);
	}
	
	/**
	 * 设置数据点集
	 * @param points
	 */
	public void setPoints(List<Point>points,int color,String format){
		if(points==null){   //如果数据集合是空的，就抛出异常
			throw new RuntimeException();
		}
		this.points=points;
		if(paint==null){
			paint=new Paint();
		}
		paint.setColor(color);
		if(!TextUtils.isEmpty(format)){
			this.showDateFormat=format;
		}
		
	}
	
	public void setPaintXY(int color){
		if(paintXY==null){
			paintXY=new Paint();
		}
		paintXY.setColor(color);
	}
	
	@SuppressLint("SimpleDateFormat")
	private Date getDateByString(String dateText,String format){
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
		try {
			return simpleDateFormat.parse(dateText);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void setMaxY(int maxY){
		this.maxY=maxY;
	}
	public void setMixY(int minY){
		this.minY=minY;
	}
	
	public void setMargin(int left,int top,int right,int down){
		this.left=left;
		this.top=top;
		this.right=right;
		this.down=down;
	}
	private void drawY(Canvas canvas){
		int height = getHeight();
		int stopY=top+lengthY;
		if(stopY>height||stopY+down>height){
			stopY=height-down;
		}
		canvas.drawLine(left, top, left, stopY, paintXY);
	}
	
	private void drawX(Canvas canvas){
		//获取控件宽度
		int width = getWidth();
		int stopX=left+lengthX;
		if(stopX>width||stopX+right>width){
			stopX=width-right;    
		}
		canvas.drawLine(left, top+lengthY, stopX, top+lengthY, paintXY);
	}
	
	
	//绘制Y轴刻度
	private void drawCalibrateY(Canvas canvas){
		paintXY.setTextSize(10);
		final int calibrate=4;   //刻度数量
		int mod=(maxY-minY)/(calibrate-1);
		int dy=Math.abs((lengthY-lengthY/calibrate)/calibrate);//取整
		this.pointMaxY=top+lengthY-calibrate*dy;
		iy=calibrate*dy;
		for(int i=calibrate;i>0;i--){
			int dx=top+lengthY-(i*dy);
			//绘制刻度
			canvas.drawLine(left-calibrate, dx, left, dx, paintXY);
			
			String calibrateY=mod*i+"";
			canvas.drawText(calibrateY, left-calibrate-calibrateY.length()*8, dx+5, paintXY);
		}
	}
	
	//绘制X轴刻度
	private void drawCalibrateX(Canvas canvas){
		mCalibrateX = 7;
		int dx=Math.abs(lengthX/mCalibrateX);//取整
		Date date = new Date();    //当前时间
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    
		long millis =calendar.getTimeInMillis();
		calendar.add(Calendar.DAY_OF_MONTH,-mCalibrateX);
		
		long timeInMillis =calendar.getTimeInMillis();
		
		minDate=calendar.getTime();
		maxX=getDif(millis,timeInMillis);
		
		
		ix=dx*mCalibrateX;
		
		for(int i=1;i<=mCalibrateX;i++){
			//设置为当前时间
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH,i-mCalibrateX );
			String dateFormat = getDateFormat(calendar.getTime(),showDateFormat);
			canvas.drawLine(left+dx*i, top+lengthY,left+dx*i , top+lengthY+calibrate, paintXY);
			canvas.drawText(dateFormat, left+dx*i-dateFormat.length()*3, top+lengthY+calibrate*3, paintXY);
		}
	}
	
	//获取两个时间差：单位（天）
	private float getDif(float day1,float day2){
		return (day1-day2)/(24*60*60*1000);
	}
	@SuppressLint("SimpleDateFormat")
	private String getDateFormat(Date date,String format){
		SimpleDateFormat dateFormat=new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
	/**
	 * 根据传入的各个点集绘制各个点
	 * @param canvas
	 * @param value
	 */
	private void drawPoint(){
		if(points==null){
			return ;
		}
		Calendar calendar=Calendar.getInstance();
		float startX=0;
		float startY=0;
		float pointX = 0;
		float pointY = 0;
		for(int x=0;x<points.size();x++){
			Point point = points.get(x);
			if(x!=0){   //不只有一个点
				startX=pointX;
				startY=pointY;
			}
			Date date = point.getX();
			calendar.setTime(date);
			
			long millis =calendar.getTimeInMillis();
			calendar.setTime(minDate);
			long timeInMillis =calendar.getTimeInMillis();
			float dx=getDif(millis,timeInMillis);   //获取两个时间差：单位（天）
			
			
			pointX=left+ix*dx/maxX;    //x轴坐标点
			pointY=pointMaxY+iy-iy*point.getY()/maxY;
			canvas.drawCircle(pointX, pointY, radiu, paint);
			
			if(x!=0){
				canvas.drawLine(startX, startY,pointX , pointY, paint);
			}
			
		}
		

	}
}

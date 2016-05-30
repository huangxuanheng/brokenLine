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
	private float maxX=160;   //x锟斤拷锟斤拷锟斤拷锟绞撅拷锟斤拷锟斤拷值
	private int minY=40;
	
	private final int lengthY=200;
	private final int lengthX=230;
	
	private float pointMaxY;   //Y锟斤拷锟斤拷锟斤拷锟斤拷锟绞碉拷锟斤拷锟�
	private float iy;   //Y锟斤拷潭鹊锟斤拷锟襟长度ｏ拷锟斤拷0-锟斤拷锟街碉拷锟斤拷锟绞碉拷锟斤拷锟�
	private float ix;   //X锟斤拷潭鹊锟斤拷锟襟长度ｏ拷锟斤拷0-锟斤拷锟街碉拷锟斤拷锟绞碉拷锟斤拷锟�
	private int mCalibrateX;   //x锟斤拷潭锟斤拷锟�
	private Date minDate;   //x锟斤拷锟斤拷小锟斤拷锟斤拷锟节ｏ拷锟斤拷锟斤拷锟叫的第硷拷锟斤拷
	
	private List<Point>points;
	
	private Canvas canvas;
	private Paint paint;     //设置绘制的折线画笔
	
	private String showDateFormat="MM-dd";
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
	
	
	@SuppressLint("SimpleDateFormat")
	private Date getDateByString(String dateText,String format){
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
		try {
			return simpleDateFormat.parse(dateText);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
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
		Paint paint=new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawLine(left, top, left, top+lengthY, paint);
	}
	
	private void drawX(Canvas canvas){
		Paint paint=new Paint();
		paint.setColor(Color.BLACK);
		//锟斤拷x锟斤拷
		canvas.drawLine(left-20, top+lengthY, left+lengthX, top+lengthY, paint);
	}
	
	
	//锟斤拷Y锟教讹拷
	private void drawCalibrateY(Canvas canvas){
		Paint paint=new Paint();
		paint.setColor(Color.BLACK);
		final int calibrate=4;   //锟教度碉拷锟斤拷锟斤拷
		int mod=(maxY-minY)/(calibrate-1);
		int dy=Math.abs((lengthY-lengthY/calibrate)/calibrate);//锟斤拷锟�
		this.pointMaxY=top+lengthY-calibrate*dy;
		iy=calibrate*dy;
		for(int i=calibrate;i>0;i--){
			int dx=top+lengthY-(i*dy);
			//锟斤拷Y锟教讹拷
			canvas.drawLine(left-20, dx, left, dx, paint);
			canvas.drawText(mod*i+"", 5, dx+5, paint);
		}
	}
	
	//锟斤拷锟教讹拷
	private void drawCalibrateX(Canvas canvas){
		Paint paint=new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(10);
		mCalibrateX = 7;
		int dx=Math.abs(lengthX/mCalibrateX);//锟斤拷锟�
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
			//锟斤拷X锟教讹拷
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH,i-mCalibrateX );
			String dateFormat = getDateFormat(calendar.getTime(),showDateFormat);
			canvas.drawLine(left+dx*i, top+lengthY,left+dx*i , top+lengthY+20, paint);
			canvas.drawText(dateFormat, left+dx*i-dateFormat.length()*3, top+lengthY+30, paint);
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
			float dx=getDif(millis,timeInMillis);   //锟斤拷锟斤拷锟斤拷锟斤拷跃锟斤拷锟�
			
			
			pointX=left+ix*dx/maxX;    //x轴坐标点
			pointY=pointMaxY+iy-iy*point.getY()/maxY;
			canvas.drawCircle(pointX, pointY, 3, paint);
			
			if(x!=0){
				canvas.drawLine(startX, startY,pointX , pointY, paint);
			}
			
		}
		

	}
}

package br.ufrj.dismo.elave.marker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import br.ufrj.dismo.elave.component.Component;

public class MarkerView extends View implements Callback{
	
	private Paint p;
	
	private List<Component> components;

	public MarkerView(Context context) {
		super(context);
		
		p = new Paint();
		p.setAntiAlias(false);
		p.setStyle(Paint.Style.STROKE);
		
		components = new ArrayList<Component>();
		
	}

	@Override
	protected void onDraw(Canvas canvas) {

		//Dont draw Background
		//canvas.drawColor(Color.CYAN);
		
		//Drawing a simple Red rectangle
		
		p.setColor(Color.RED);
		p.setStrokeWidth(3.5f);
		
		// opacity
		//p.setAlpha(0x80); //
		//drawRect (float left, float top, float right, float bottom, Paint paint)
		
		for(Component component: components){
			
			canvas.drawRect(component.getX(), component.getY(), component.getX()+30, component.getY()+30, p);
		}
		
		this.invalidate();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}
	
}

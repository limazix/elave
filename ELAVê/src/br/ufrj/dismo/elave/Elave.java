package br.ufrj.dismo.elave;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import br.ufrj.dismo.elave.component.Component;
import br.ufrj.dismo.elave.marker.MarkerView;


public class Elave extends Activity {

	private static final String TAG = "Elave";
	
	private CameraPreview preview;
	
	private MarkerView mark;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		
		List<Component> components = new ArrayList<Component>();
		
		RelativeLayout layout = new RelativeLayout(this);

		mark = new MarkerView(this);
		mark.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
		mark.setComponents(components);
		
		preview = new CameraPreview(this);
		preview.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
		preview.setComponents(components);
		
		layout.addView(preview);
		layout.addView(mark);

		setContentView(layout);
		
		Log.d(TAG, "onCreate");
	}
		
}

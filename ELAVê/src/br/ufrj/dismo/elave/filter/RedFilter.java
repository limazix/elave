package br.ufrj.dismo.elave.filter;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import br.ufrj.dismo.elave.component.Component;

public class RedFilter extends Filter{

	public RedFilter(){
		super();
	}	

	public List<Component> filter(int[][] rgb){

		int w = rgb.length;
		int h = rgb[0].length;

		//Log.d("RedFilter","rgb has w = "+w);

		List<Component> components = new ArrayList<Component>();

		for(int j=0;j<h;j++){

			for(int i=0;i<w;i++){
				
				if((getRed(rgb[i][j])>0x85)&&(getGreen(rgb[i][j])<0x45)&&(getBlue(rgb[i][j])<0x45)){
					components.add(new Component(i, j));
					
					Log.d("RedFilter", "FoundComponent at "+i+" "+j);
					
					break;
				}
				
			}
			
			if(!components.isEmpty()){
				break;
			}

		}

		return components;

	}

}

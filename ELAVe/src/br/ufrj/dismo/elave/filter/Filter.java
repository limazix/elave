package br.ufrj.dismo.elave.filter;

import java.util.List;

import br.ufrj.dismo.elave.component.Component;

public abstract class Filter {

	protected int step = 1;
	
	public abstract List<Component> filter(int[][] rgb);
	
	public int getRed(int rgb){
		
		return (rgb >> 16) & 0xFF;
		
	}
	
	public int getGreen(int rgb){
		
		return (rgb >> 8 ) & 0xFF;
		
	}
	
	public int getBlue(int rgb){
		
		return (rgb >> 0 ) & 0xFF;
		
	}
	
}

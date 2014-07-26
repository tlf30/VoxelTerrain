package VoxelSystem.DensityVolumes.Shapes;

import VoxelSystem.VoxelNoise.SimplexNoise;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

public class NoiseShape extends VolumeShape{
	int dimX = 40;
	int dimY = 20;
	int dimZ = 40;
	float d;
	public NoiseShape(float res){
		this.d = res;
	}
	
	
	@Override
	public int getType(float x, float y, float z){
		if(!isOutside(x, y, z)){
			if( x > 5){
				return 2;
			}else{
				return 1;
			}
		}else{
			return -1;
		}
	}
	
	@Override
	public float getDensity(float x, float y, float z) {
		if(Math.abs(x)>dimX || Math.abs(y) > dimY || Math.abs(z)>dimZ){
			return -1;
		}
//		float r  =  10f;
//		float rInnter = r/2.0f;
//		float len = (float) Math.sqrt(x*x + y*y );
		if(y < -5){
			return 1;
		}
//		if(len > r){
//			return -1;
//		}else if(len < rInnter){
//			return -1;
//		}else{
//			return 0;
//		}
		
		int octaves = 5;
		float d = 0;
		float base = .5f;
		float multiplier = 2f;
		float baseFreq = 10f;
		float FreqMultiplier = 2f;
		
		//"Mountain" octave: Low Frequency high octave
		for(int i = 0; i< octaves;i++){
			float localFreq = baseFreq + baseFreq*FreqMultiplier*i;
			d += SimplexNoise.noise(x/localFreq,z/localFreq,y/localFreq) * (base+multiplier*i);
		}
		
		//"Cave Octave"
		
//		octaves = 4;
//		base = 1.5f;
//		multiplier = .5f;
//		baseFreq = 3f;
//		FreqMultiplier = 2f;
//		
//		for(int i = 0; i< octaves;i++){
//			float localFreq = baseFreq + baseFreq*FreqMultiplier*i;
//			d -= SimplexNoise.noise(x/localFreq,z/localFreq,y/localFreq) * (base+multiplier*i);
//		}

		//"Localized roughness":
//		octaves = 4;
//		base = .5f;
//		multiplier = .5f;
//		baseFreq = 1f;
//		FreqMultiplier = .25f;
//		
//		for(int i = 0; i< octaves;i++){
//			float localFreq = baseFreq + baseFreq*FreqMultiplier*i;
//			d += SimplexNoise.noise(x/localFreq,z/localFreq,y/localFreq) * (base+multiplier*i);
//		}
		
		
//		if(d < 0){
//			return -1;
//		}else 
		if(d > y){
			return d-y;	
		}else{
			return d-y;
		}
//		float yIso = (float) (5*Math.sin(x/5) + 5*Math.sin(z/5));
//		
//		return (float) (yIso-y);
	}

	@Override
	public Vector3f getSurfaceNormal(float x, float y, float z) {
		//Bilinear:
		double nx = getDensity(x + d, y, z) - getDensity(x - d, y, z);
	    double ny = getDensity(x, y + d, z) - getDensity(x, y - d, z);
	    double nz = getDensity(x, y, z + d) - getDensity(x, y, z - d);
	 
	    Vector3f normal = new Vector3f((float)-nx, (float)-ny, (float)-nz);
	    return normal.normalizeLocal();
	}

	@Override
	public BoundingBox getEffectiveVolume() {
		// TODO Auto-generated method stub
		return new BoundingBox(new Vector3f(-dimX,-dimY,-dimZ),new Vector3f(dimX,dimY,dimZ));
	}

	@Override
	public boolean isOutside(float x, float y, float z) {
		return getDensity(x,y,z)<0;
	}

}

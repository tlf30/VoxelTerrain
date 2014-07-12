package VoxelSystem.Operators;

import java.util.ArrayList;
import java.util.List;

import VoxelSystem.VoxelSystemTables;
import VoxelSystem.Hermite.HermiteCube;
import VoxelSystem.Hermite.HermiteEdge;
import VoxelSystem.Hermite.HermiteExtractor;
import VoxelSystem.Hermite.HermitePoint;

import com.jme3.math.Vector3f;

/***
 * 
 */
public abstract class HermiteBase implements HermiteExtractor{
	public final HermiteCube getCube(Vector3f [] cubeCorners){
		HermitePoint p[] = new HermitePoint[8];
		for(int i=0; i<8; i++){
			p[i] = this.getPoint(cubeCorners[i]);
		}
		
		int [] materials = new int[]{
				p[0].material,p[1].material,
				p[2].material,p[3].material,
				p[4].material,p[5].material,
				p[6].material,p[7].material
		};
		
		int edgeInfo = VoxelSystemTables.getEdgeFromMaterials(materials);
		
		HermiteCube hc = new HermiteCube();
		hc.edgeInfo = edgeInfo;
		hc.materials = materials;
		if(edgeInfo == 0){
			return hc;
		}else{
			int numIntersections = Integer.bitCount(edgeInfo);
//			Vector3f [] intersections = new Vector3f[numIntersections];
//			Vector3f [] normals = new Vector3f[numIntersections];
			List<Vector3f> intersection = new ArrayList<Vector3f>(8);
			List<Vector3f> normals = new ArrayList<Vector3f>(8);
			int count = 0;
			for (int i = 0; i < 12; i++) {
				if ((edgeInfo & (1 << i)) == 0) {
					continue;
				}

				int v1 = VoxelSystemTables.iTable[i * 2];
				int v2 = VoxelSystemTables.iTable[i * 2 + 1];
				// The first corner is set according to the intersection table.
				Vector3f corner1 = cubeCorners[v1];

				// Same with the second corner
				Vector3f corner2 = cubeCorners[v2];

				HermiteEdge he = getEdge(corner1, corner2);

//				if(he!=null){
					intersection.add(he.intersection);
					normals.add(he.normal);
					count++;
//				}

			}
			
				
			hc.intersections = intersection.toArray(new Vector3f[0]);
			hc.normals = normals.toArray(new Vector3f[0]);
			return hc;
		}	
	}
	
}
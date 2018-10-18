import java.io.*;
import java.util.ArrayList;
// import com.syjay.egl.MathFP;

public class Obj2Mds
{
	
	public static void main(String[] args) throws Exception
	{
		if(args==null || args.length<2 || args[0]==null || args[1]==null 
		|| args[0].equals("") || args[1].equals("") ||!new File(args[0]).exists())
		{
			usage();
		}
		float scaleFactor=1;
		if(args.length>2 && args[2]!=null)
		{
			try
			{
				scaleFactor = Float.parseFloat(args[2]);
			}catch(Exception x)
			{
				x.printStackTrace();
				usage();
			}
		}
		
		glRotatef((-90),(1),0,0);
		
		BufferedReader in=new BufferedReader(new FileReader(args[0]));
		DataOutputStream out=new DataOutputStream(new FileOutputStream(args[1]));
		
		ArrayList v=new ArrayList();
		ArrayList vt=new ArrayList();
		ArrayList f=new ArrayList();
		
		String line=in.readLine();
		
		while(line!=null)
		{
			if(line.startsWith("v "))
			{
				String[] val=line.split(" ");
				float[] va=new float[3];
				va[0]=Float.parseFloat(val[1]);
				va[1]=Float.parseFloat(val[2]);
				va[2]=Float.parseFloat(val[3]);
				if(args[2]!=null)
				{
					va[0]*=scaleFactor;
					va[1]*=scaleFactor;
					va[2]*=scaleFactor;
				}
				// matrixMulCoord2(va,matrix,0);
				v.add(va);
			}else
			if(line.startsWith("vt "))
			{
				String[] val=line.split(" ");
				float[] vta=new float[2];
				vta[0]=Float.parseFloat(val[1]);
				vta[1]=Float.parseFloat(val[2]);
				vt.add(vta);
			}else
			if(line.startsWith("f "))
			{
				String[] val=line.split(" ");
				int[] fa=new int[3];
				fa[0]=Integer.parseInt(val[1].split("/")[0]);
				fa[1]=Integer.parseInt(val[2].split("/")[0]);
				fa[2]=Integer.parseInt(val[3].split("/")[0]);
				f.add(fa);
			}
			
			line=in.readLine();
		}
		in.close();
		
		byte[] mdsHeader=new byte[]{0x6D, 0x64, 0x73, 0x00};
		out.write(mdsHeader);
		
		out.writeInt(v.size()*3);
		for(int lo=0;lo<v.size();lo++)
		{
			float[] va=(float[])v.get(lo);
			for(int loo=0;loo<3;loo++)
			{
				// va[loo]*=1000000;
				// out.writeInt((int)va[loo]);
				double temp=va[loo];
				temp=temp*2147483647/(2147483647>>16);
				
				out.writeInt((int)temp);
			}
		}
		
		out.writeInt(vt.size()*2);
		for(int lo=0;lo<vt.size();lo++)
		{
			float[] vta=(float[])vt.get(lo);
			for(int loo=0;loo<2;loo++)
			{
				double temp=vta[loo];
				temp=temp*2147483647/(2147483647>>16);
				
				out.writeInt((int)temp);
				
				//System.out.println("temp = "+vta[loo]+" "+temp);
			}
		}
		
		out.writeInt(f.size()*3);
		out.writeByte(32);
		for(int lo=0;lo<f.size();lo++)
		{
			int[] fa=(int[])f.get(lo);
			for(int loo=0;loo<3;loo++)
			{
				out.writeInt(fa[loo]-1);
			}
		}
		out.close();
	}
	
	public static void usage()
	{
		System.out.println("Usage: java Obj2Mds a.obj a.mds scaleFactor");
		System.exit(1);
	}
	
	static float [] matrix =new float[]
	{
		1.0f,0.0f,0.0f,0.0f,
		0.0f,1.0f,0.0f,0.0f,
		0.0f,0.0f,1.0f,0.0f,
		0.0f,0.0f,0.0f,1.0f
	};
	static float [] tempMatrix=new float[16];
	public static void glMultMatrix(/*int [] m1, */float [] m2)
	{
/* 		if(m1.length!=16 || m2.length!=16)
		{
			throw new IllegalArgumentException("Invald matrix!");
		} */
		
		//int []m=new int[16];
		
		float [] m1=matrix;
		
		tempMatrix[0]=  floatMul(m1[0],m2[0])+
                floatMul(m1[1],m2[4])+
                floatMul(m1[2],m2[8])+
                floatMul(m1[3],m2[12]);
		
		tempMatrix[1]=  floatMul(m1[0],m2[1])+
                floatMul(m1[1],m2[5])+
                floatMul(m1[2],m2[9])+
                floatMul(m1[3],m2[13]);
		
		tempMatrix[2]=  floatMul(m1[0],m2[2])+
                floatMul(m1[1],m2[6])+
                floatMul(m1[2],m2[10])+
                floatMul(m1[3],m2[14]);
		
		tempMatrix[3]=  floatMul(m1[0],m2[3])+
                floatMul(m1[1],m2[7])+
                floatMul(m1[2],m2[11])+
                floatMul(m1[3],m2[15]);
		
		tempMatrix[4]=  floatMul(m1[4],m2[0])+
                floatMul(m1[5],m2[4])+
                floatMul(m1[6],m2[8])+
                floatMul(m1[7],m2[12]);
		
		tempMatrix[5]=  floatMul(m1[4],m2[1])+
                floatMul(m1[5],m2[5])+
                floatMul(m1[6],m2[9])+
                floatMul(m1[7],m2[13]);
		
		tempMatrix[6]=  floatMul(m1[4],m2[2])+
                floatMul(m1[5],m2[6])+
                floatMul(m1[6],m2[10])+
                floatMul(m1[7],m2[14]);
		
		tempMatrix[7]=  floatMul(m1[4],m2[3])+
                floatMul(m1[5],m2[7])+
                floatMul(m1[6],m2[11])+
                floatMul(m1[7],m2[15]);
		
		tempMatrix[8]=  floatMul(m1[8],m2[0])+
                floatMul(m1[9],m2[4])+
                floatMul(m1[10],m2[8])+
                floatMul(m1[11],m2[12]);
		
		tempMatrix[9]=  floatMul(m1[8],m2[1])+
                floatMul(m1[9],m2[5])+
                floatMul(m1[10],m2[9])+
                floatMul(m1[11],m2[13]);
		
		tempMatrix[10]= floatMul(m1[8],m2[2])+
                floatMul(m1[9],m2[6])+
                floatMul(m1[10],m2[10])+
                floatMul(m1[11],m2[14]);
		
		tempMatrix[11]= floatMul(m1[8],m2[3])+
                floatMul(m1[9],m2[7])+
                floatMul(m1[10],m2[11])+
                floatMul(m1[11],m2[15]);       
		
		tempMatrix[12]= floatMul(m1[12],m2[0])+
                floatMul(m1[13],m2[4])+
                floatMul(m1[14],m2[8])+
                floatMul(m1[15],m2[12]);
		
		tempMatrix[13]= floatMul(m1[12],m2[1])+
                floatMul(m1[13],m2[5])+
                floatMul(m1[14],m2[9])+
                floatMul(m1[15],m2[13]);
		
		tempMatrix[14]= floatMul(m1[12],m2[2])+
                floatMul(m1[13],m2[6])+
                floatMul(m1[14],m2[10])+
                floatMul(m1[15],m2[14]);
		
		tempMatrix[15]= floatMul(m1[12],m2[3])+
                floatMul(m1[13],m2[7])+
                floatMul(m1[14],m2[11])+
                floatMul(m1[15],m2[15]);    
		
		System.arraycopy(tempMatrix,0,m1,0,16);
	}
	
	public static void glRotatef(float angle,float x,float y,float z)
	{
		

		float mag, s, c;
		float xx, yy, zz, xy, yz, zx, xs, ys, zs, one_c;
		
		//final int PId180=floatDiv(FP_PI,getFloat(180));
		
		//TODO sin and cos can be further optimize
		s = (float)Math.sin (floatMul(angle , floatDiv((float)Math.PI,getFloat(180))));
		c = (float)Math.cos (floatMul(angle , floatDiv((float)Math.PI,getFloat(180))));
		// System.out.println("c="+GETSTR(c));
		mag =  floatSqrt(floatMul(x , x) + floatMul(y , y) +floatMul( z, z));
		// System.out.println("mag="+GETSTR(mag));
		
		if (mag == 0.0f) { return ; }
		
		x = floatDiv(x,mag); 
		y = floatDiv(y,mag);
		z = floatDiv(z,mag);
		// System.out.println("x="+GETSTR(x));
		xx = floatMul(x , x); yy = floatMul(y , y); zz = floatMul(z , z);
		xy = floatMul(x , y); yz = floatMul(y , z); zx = floatMul(z , x);
		xs = floatMul(x , s); ys = floatMul(y , s); zs = floatMul(z , s);
		one_c = 1.0f - c;
		// System.out.println("xx="+GETSTR(xx));
		tempMatrix [ 0] = floatMul(one_c , xx) + c;
		tempMatrix [ 4] = floatMul(one_c , xy) + zs;
		tempMatrix [ 8] = floatMul(one_c , zx) - ys;
		tempMatrix [12] = 0.0f;
		tempMatrix [ 1] = floatMul(one_c , xy) - zs;
		tempMatrix [ 5] = floatMul(one_c , yy) + c;
		tempMatrix [ 9] = floatMul(one_c , yz) + xs;
		tempMatrix [13] = 0.0f;
		tempMatrix [ 2] = floatMul(one_c , zx) + ys;
		// System.out.println("a [ 2]="+GETSTR(a [ 2]));
		tempMatrix [ 6] = floatMul(one_c , yz) - xs;
		tempMatrix [10] = floatMul(one_c , zz) + c;
		tempMatrix [14] = 0.0f;
		tempMatrix [ 3] = 0.0f;
		tempMatrix [ 7] = 0.0f;
		tempMatrix [11] = 0.0f;
		tempMatrix [15] = 1.0f;

		glMultMatrix(tempMatrix);

	}
	
	public static float floatMul(float x, float y)
	{
		return x*y;
	}
	
	public static float getFloat(int x)
	{
		return x;
	}
	
	public static float floatDiv(float x, float y)
	{
		return x/y;
	}
	
	public static float floatSqrt(float x)
	{
		return (float)Math.sqrt(x);
	}
	
	private  static void matrixMulCoord2(float [] m1, float [] m2, int begin)
	{

		float f0=  floatMul(m1[begin],m2[0])+
                floatMul(m1[begin+1],m2[1])+
                floatMul(m1[begin+2],m2[2])+
                0;
		
		float f1=  floatMul(m1[begin+0],m2[4])+
                floatMul(m1[begin+1],m2[5])+
                floatMul(m1[begin+2],m2[6])+
                0;
		
		float f2=  floatMul(m1[begin+0],m2[8])+
                floatMul(m1[begin+1],m2[9])+
                floatMul(m1[begin+2],m2[10])+
                0;
		
		/* float f3=  floatMul(m1[begin+0],m2[12])+
                floatMul(m1[begin+1],m2[13])+
                floatMul(m1[begin+2],m2[14])+
                floatMul(m1[begin+3],m2[15]); */
		
		m1[begin+0]=f0;
		m1[begin+1]=f1;
		m1[begin+2]=f2;
		//m1[begin+3]=f3;
	}
}
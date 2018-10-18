import java.io.*;
import java.util.ArrayList;
// import com.syjay.egl.MathFP;

public class H3T2Mds
{
	
	public static void main(String[] args) throws Exception
	{
		if(args==null || args.length!=2 || args[0]==null || args[1]==null 
		|| args[0].equals("") || args[1].equals("") ||!new File(args[0]).exists())
		{
			usage();
		}
		
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
				fa[0]=Integer.parseInt(val[1].split("/")[0])+1;
				fa[1]=Integer.parseInt(val[2].split("/")[0])+1;
				fa[2]=Integer.parseInt(val[3].split("/")[0])+1;
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
		System.out.println("Usage: java H3T2Mds a.obj a.mds");
	}
	
	
}
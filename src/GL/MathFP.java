/*

MathFP.java 2007 Jan 11.

EGL is a pure java 3D Graphics API which was designed for J2ME mobile devices
and based on CLDC 1.0 only, which provided OpenGL-like interface and supports
basic 3D pipeline and texture.

Copyright (C) 2007 Jiayi Wu (wujiyish@msn.com).

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the Free Software Foundation, Inc., 51 Franklin
Street, Fifth Floor, Boston, MA 02110-1301 USA.

*/

/* MathFP - Decompiled by JODE
* Visit http://jode.sourceforge.net/
*/

#define EGL_PRECISION 16					// number of fractional bits
#define EGL_ONE		  (1 << EGL_PRECISION)	// representation of 1
#define EGL_ZERO	  0						// representation of 0
#define EGL_HALF	  0x08000				// S15.16 0.5 
#define EGL_PINF	  0x7fffffff			// +inf 
#define EGL_MINF	  0x80000000			// -inf 

#define EGL_2PI			411774//EGL_FixedFromFloat(6.28318530717958647692f)
#define EGL_R2PI		10430//EGL_FixedFromFloat(1.0f/6.28318530717958647692f)

package com.syjay.egl;
/**
Fixed point class which use 16bit as precision.
*/
public  class MathFP
{
/* 	private static int _fbits = 12; // ï¿½ï¿½ï¿½ï¿½Î»ï¿½ï¿½
	private static int _digits = 4;//the number of visual digits behind the decimal point.FP_1ï¿½ï¿½ï¿½Ô³ï¿½ï¿½Ô¼ï¿½ï¿½ï¿½10,ï¿½ï¿½ï¿½ï¿½ï¿?4096(12bitï¿½ï¿½ï¿½ï¿½)ï¿½ï¿½4ï¿½ï¿½
	private static int _one = 4096;//ï¿½ï¿½Ê¾FP_1
	private static int _fmask = 4095;//ï¿½ï¿½Ê¾FP_1-1
	private static int _dmul = 10000;//FP_1ï¿½ï¿½Î»ï¿½ï¿½,4096ï¿½ï¿½ï¿½ï¿½10000, 409 ï¿½ï¿½1000
	private static int _flt = 0;// 12-ï¿½ï¿½ï¿½ï¿½Î»ï¿½ï¿½
	private static int _pi = 12868;
	private static int[] e = { _one, 11134, 30266, 82270, 223636 }; */
	/**
	PI
	*/
	public static int PI = (411774>>1);
	// public static int E = e[1];
	/**
	MAX_VALUE
	*/
	public static final int MAX_VALUE = 2147483647;
	/**
	MIN_VALUE
	*/
	public static final int MIN_VALUE = -2147483647;
/* 	
	public static int setPrecision (int i)
	{
		if (i > 12 || i < 0)
			return _digits;
		_fbits = i;
		_one = 1 << i;
		_flt = 12 - i;
		_digits = 0;
		_dmul = 1;
		_fmask = _one - 1;
		PI = _pi >> _flt;
		E = e[1] >> _flt;
		int i_0_ = _one;
		while (i_0_ != 0)
		{
			i_0_ /= 10;
			_digits++;
			_dmul *= 10;
		}
		return _digits;
	}
	
	public static int getPrecision ()
	{
		return _fbits;
	}
	 */
	 /**
	 Convert FP to int
	 @param i FP to be converted
	 */
	public static int toInt (int i)
	{
		// if (i < 0)
			// return - (round (- i, 0) >> _fbits);
		// return round (i, 0) >> _fbits;
		return i >> EGL_PRECISION;
	}
	/**
	 Convert int to FP
	 @param i int to be converted
	 */
	public static int toFP (int i)
	{
		return i << EGL_PRECISION;
	}
/* 	
	public static int convert (int i, int i_1_)
	{
		int i_2_ = i < 0 ? -1 : 1;
		if (abs (i_1_) < 13)
		{
			if (_fbits < i_1_)
				i = i + i_2_ * (1 << (i_1_ - _fbits >> 1)) >> i_1_ - _fbits;
			else
				i <<= _fbits - i_1_;
		}
		return i;
	}
	
	public static int toFP (String string)
	{
		int i = 0;
		if (string.charAt (0) == '-')
			i = 1;
		String string_3_ = "-1";
		int i_4_ = string.indexOf ('.');
		if (i_4_ >= 0)
		{
			for (string_3_ = string.substring (i_4_ + 1, string.length ());
			string_3_.length () < _digits; string_3_ += "0")
			{
				
			}
			if (string_3_.length () > _digits)
				string_3_ = string_3_.substring (0, _digits);
		}
		else
			i_4_ = string.length ();
		int i_5_ = 0;
		if (i != i_4_)
			i_5_ = Integer.parseInt (string.substring (i, i_4_));
		int i_6_ = Integer.parseInt (string_3_) + 1;
		int i_7_ = (i_5_ << _fbits) + (i_6_ << _fbits) / _dmul;
		if (i == 1)
			i_7_ = - i_7_;
		return i_7_;
	}
	 */
	 
	 /**
	 Get the String of the FP
	 @param i FP 
	 */
	public static String toString (int i)
	{
		return toInt(i)+"";//TODO
		/* boolean bool = false;
		if (i < 0)
		{
			bool = true;
			i = - i;
		}   
                int zhengshu = i >> 16;
                int tmp = ( i % (1<<16) );
		System.out.println(tmp);
		float xiaoshu = ( ( float)tmp)/65536.0f;
		
		return "·ûºÅ:"+(bool?"-":"")+" ÕûÊý:"+zhengshu+" Ð¡Êý:"+xiaoshu; */
	}
	
/* 	public static String toString (int i, int i_10_)
	{
		if (i_10_ > _digits)
			i_10_ = _digits;
		String string = toString (round (i, i_10_));
		return string.substring (0, string.length () - _digits + i_10_);
	}
	
	public static int max (int i, int i_11_)
	{
		return i < i_11_ ? i_11_ : i;
	}
	
	public static int min (int i, int i_12_)
	{
		return i_12_ < i ? i_12_ : i;
	}
	
	public static int round (int i, int i_13_)
	{
		int i_14_ = 10;
		for (int i_15_ = 0; i_15_ < i_13_; i_15_++)
			i_14_ *= 10;
		i_14_ = div (toFP (5), toFP (i_14_));
		if (i < 0)
			i_14_ = - i_14_;
		return i + i_14_;
	} */
	/**
	Return a*b
	*/
	public static int mul (long a, long b)
	{
		// if((((long) a * (long) b)  >> EGL_PRECISION)>MAX_VALUE)System.out.println("error mul");
		return (int) (( a *  b)  >> EGL_PRECISION);
	}
	/**
	Return a/b
	*/
	public static int div (int a, int b)
	{
		if ( ((b >> 24)!=0) && ((b >> 24) + 1)!=0 ) {
			return mul(a >> 8, inverse(b >> 8));
		} else {
			return mul(a, inverse(b));
		}
	}
	
	// --------------------------------------------------------------------------
	// lookup table for calculation of inverse
	// --------------------------------------------------------------------------
	private static final int __gl_rcp_tab[] = { /* domain 0.5 .. 1.0-1/16 */
		0x8000, 0x71c7, 0x6666, 0x5d17, 0x5555, 0x4ec4, 0x4924, 0x4444
	};
	
	// --------------------------------------------------------------------------
	// Calculate inverse of fixed point number
	//
	// Parameters:
	//	a		-	the number whose inverse should be calculated
	// --------------------------------------------------------------------------
	
	 static  int inverse(int a) {
		int exp;
		int x;
		
		if (a == EGL_ZERO) 
			return 0x7fffffff;
		
		boolean sign = false;
		
		if (a < 0) {
			sign = true;
			a = -a;
		}
		
		exp = CountLeadingZeros(a);
		x = ((int)__gl_rcp_tab[(a>>(28-exp))&0x7]) << 2;
		exp -= 16;
		
		if (exp <= 0)
			x >>= -exp;
		else
			x <<= exp;
		
		/* two iterations of newton-raphson  x = x(2-ax) */
		x = mul(x,(EGL_ONE*2 - mul(a,x)));
		x = mul(x,(EGL_ONE*2 - mul(a,x)));
		
		if (sign)
			return -x;
		else
			return x;
	}
	
	private static int CountLeadingZeros(int x) {
		int exp = 31;

		if ((x & 0xffff0000)!=0) { 
			exp -= 16; 
			x >>= 16; 
		}

		if ((x & 0xff00) !=0) { 
			exp -= 8; 
			x >>= 8; 
		}
		
		if ((x & 0xf0)!=0)  { 
			exp -= 4; 
			x >>= 4; 
		}

		if ((x & 0xc) !=0) { 
			exp -= 2; 
			x >>= 2; 
		}
		
		if ((x & 0x2) !=0) { 
			exp -= 1; 
		}

		return exp;
	}
/* 	
	public static int add (int i, int i_24_)
	{
		return i + i_24_;
	}
	
	public static int sub (int i, int i_25_)
	{
		return i - i_25_;
	}
	
	public static int abs (int i)
	{
		if (i < 0)
			return - i;
		return i;
	}
	
	public static int sqrt (int i, int i_26_)
	{
		if (i < 0)
			throw new ArithmeticException ("Bad Input");
		if (i == 0)
			return 0;
		int i_27_ = i + _one >> 1;
		for (int i_28_ = 0; i_28_ < i_26_; i_28_++)
			i_27_ = i_27_ + div (i, i_27_) >> 1;
		if (i_27_ < 0)
			throw new ArithmeticException ("Overflow");
		return i_27_;
	}
	 */
	 /**
	Return sqrt(a)
	*/
	public static int sqrt (int a)
	{
		// return sqrt (i, 16);
		int s;
		int i;
		s = (a + EGL_ONE) >> 1;
		/* 6 iterations to converge */
		for (i = 0; i < 6; i++)
			s = (s + div(a, s)) >> 1;
		return s;
	}
	
	private static final int __gl_sin_tab[] = 
	{
		#include "gl_sin.h"
	};
	/**
	Return sin(a)
	*/
	public static int sin (int a)
	{

		int v;
		
		/* reduce to [0,1) */
		while (a < EGL_ZERO) a += EGL_2PI;
		a *= EGL_R2PI;
		a >>= EGL_PRECISION;
		
		/* now in the range [0, 0xffff], reduce to [0, 0xfff] */
		a >>= 4;
		
		v = ((a & 0x400)!=0) ? __gl_sin_tab[0x3ff - (a & 0x3ff)] : __gl_sin_tab[a & 0x3ff];
		v = mul(v,EGL_ONE);
		return ((a & 0x800)!=0)  ? -v : v;
	}
	/**
	Return cos(a)
	*/
	public static int cos(int a) 
	{
		int v;
		/* reduce to [0,1) */
		while (a < EGL_ZERO) a += EGL_2PI;
		a *= EGL_R2PI;
		a >>= EGL_PRECISION;
		a += 0x4000;
		
		/* now in the range [0, 0xffff], reduce to [0, 0xfff] */
		a >>= 4;
		
		v = ((a & 0x400)!=0) ? __gl_sin_tab[0x3ff - (a & 0x3ff)] : __gl_sin_tab[a & 0x3ff];
		v = mul(v,EGL_ONE);
		return ((a & 0x800)!=0)  ? -v : v;
	}
	
	
/* 
	public static int asin (int i)
	{
		if (abs (i) > _one)
			throw new ArithmeticException ("Bad Input");
		boolean bool = i < 0;
		if (i < 0)
			i = - i;
		int i_34_
		= mul (mul (mul (mul (35 >> _flt, i) - (146 >> _flt), i) + (347 >> _flt),
		i) - (877 >> _flt),
		i) + (6434 >> _flt);
		int i_35_ = PI / 2 - mul (sqrt (_one - i), i_34_);
		return bool ? - i_35_ : i_35_;
	} 
	*/
	
	// public static int cos (int i)
	// {
		// return sin (PI / 2 - i);
	// }
/* 	
	public static int acos (int i)
	{
		return PI / 2 - asin (i);
	}
	 */
	 /**
	Return tan(i)
	*/
	public static int tan (int i)
	{
		return div (sin (i), cos (i));
	}
/* 	
	public static int cot (int i)
	{
		return div (cos (i), sin (i));
	}
	
	public static int atan (int i)
	{
		return asin (div (i, sqrt (_one + mul (i, i))));
	}
	
	public static int exp (int i)
	{
		if (i == 0)
			return _one;
		boolean bool = i < 0;
		i = abs (i);
		int i_36_ = i >> _fbits;
		int i_37_ = _one;
		for (int i_38_ = 0; i_38_ < i_36_ / 4; i_38_++)
			i_37_ = mul (i_37_, e[4] >> _flt);
		if (i_36_ % 4 > 0)
			i_37_ = mul (i_37_, e[i_36_ % 4] >> _flt);
		i &= _fmask;
		if (i > 0)
		{
			int i_39_ = _one;
			int i_40_ = 0;
			int i_41_ = 1;
			for (int i_42_ = 0; i_42_ < 16; i_42_++)
			{
				i_40_ += i_39_ / i_41_;
				i_39_ = mul (i_39_, i);
				i_41_ *= i_42_ + 1;
				if (i_41_ > i_39_ || i_39_ <= 0 || i_41_ <= 0)
					break;
			}
			i_37_ = mul (i_37_, i_40_);
		}
		if (bool)
			i_37_ = div (_one, i_37_);
		return i_37_;
	}
	
	public static int log (int i)
	{
		if (i <= 0)
			throw new ArithmeticException ("Bad Input");
		int i_43_ = 0;
		boolean bool = false;
		int i_44_ = 0;
		while (i >= _one << 1)
		{
			i >>= 1;
			i_44_++;
		}
		int i_45_ = i_44_ * (2839 >> _flt);
		int i_46_ = 0;
		if (i < _one)
			return - log (div (_one, i));
		i -= _one;
		for (int i_47_ = 1; i_47_ < 20; i_47_++)
		{
			int i_48_;
			if (i_43_ == 0)
				i_48_ = i;
			else
				i_48_ = mul (i_43_, i);
			if (i_48_ == 0)
				break;
			i_46_ = i_46_ + (i_47_ % 2 == 0 ? -1 : 1) * i_48_ / i_47_;
			i_43_ = i_48_;
		}
		return i_45_ + i_46_;
	}
	
	public static int pow (int i, int i_49_)
	{
		boolean bool = i_49_ < 0;
		int i_50_ = _one;
		i_49_ = abs (i_49_);
		int i_51_ = i_49_ >> _fbits;
		while (i_51_-- > 0)
			i_50_ = mul (i_50_, i);
		if (i_50_ < 0)
			throw new ArithmeticException ("Overflow");
		if (i != 0)
			i_50_ = mul (i_50_, exp (mul (log (i), i_49_ & _fmask)));
		else
			i_50_ = 0;
		if (bool)
			return div (_one, i_50_);
		return i_50_;
	}
	
	public static int atan2 (int i, int i_52_)
	{
		boolean bool = false;
		int i_53_;
		if (i_52_ > 0)
			i_53_ = atan (div (i, i_52_));
		else if (i_52_ < 0)
			i_53_ = (i_52_ < 0 ? - PI : PI) - atan (abs (div (i, i_52_)));
		else
		{
			if (i_52_ == 0 && i == 0)
				throw new ArithmeticException ("Bad Input");
			i_53_ = (i_52_ < 0 ? - PI : PI) / 2;
		}
		return i_53_;
	} */
}
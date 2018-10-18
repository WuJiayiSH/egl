package com.syjay.egl;

#include "Setting.java"

/**
Programmer must implements a Renderer to draw the buffer of the 3D pipeline.
*/
public interface Renderer
{
	/**
	Draw the buffer from (x,y) coorrdinates, with the width of w and the height of h.
	Buffer stores color in the format of 0x00RRGGBB.
	@param buffer the color buffer of the 3D pipeline.
	@param x the start coorrdinate x.
	@param y the start coorrdinate y.
	@param w the width of the buffer.
	@param h the height of the buffer.
	*/
	public void render(COLOR_TYPE[] buffer, int x, int y, int w, int h);
}
package br.ufrj.dismo.elave;

import java.nio.IntBuffer;

import android.graphics.Bitmap;

public class CameraDecoder {

	//Method from Ketai project
	protected int[][] decodeYUV420SP(byte[] yuv420sp, int width, int height) {

		int[][] rgb = new int[width][height];
		
		final int frameSize = width * height;

		for (int j = 0, yp = 0; j < height; j++) {
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
			for (int i = 0; i < width; i++, yp++) {
				int y = (0xff & ((int) yuv420sp[yp])) - 16;
				if (y < 0)
					y = 0;
				if ((i & 1) == 0) {
					v = (0xff & yuv420sp[uvp++]) - 128;
					u = (0xff & yuv420sp[uvp++]) - 128;
				}

				int y1192 = 1192 * y;
				int r = (y1192 + 1634 * v);
				int g = (y1192 - 833 * v - 400 * u);
				int b = (y1192 + 2066 * u);

				if (r < 0) 		           r = 0; 		        else if (r > 262143)
					r = 262143;
				if (g < 0) 		           g = 0; 		        else if (g > 262143)
					g = 262143;
				if (b < 0) 		           b = 0; 		        else if (b > 262143)
					b = 262143;

				rgb[i][j] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
			}
		}

		return rgb;
	}

	//From Neil Townsend at http://stackoverflow.com/questions/9325861/converting-yuv-rgbimage-processing-yuv-during-onpreviewframe-in-android
	protected Bitmap encode(byte[] data, int w, int h){
		// the bitmap we want to fill with the image
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		int numPixels = w*h;

		// the buffer we fill up which we then fill the bitmap with
		//IntBuffer intBuffer = IntBuffer.allocate(previewBoxWidth*h);
		IntBuffer intBuffer = IntBuffer.allocate(numPixels);
		// If you're reusing a buffer, next line imperative to refill from the start,
		// if not good practice
		intBuffer.position(0);

		// Get each pixel, one at a time
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				// Get the Y value, stored in the first block of data
				// The logical "AND 0xff" is needed to deal with the signed issue
				int Y = data[y*w + x] & 0xff;

				// Get U and V values, stored after Y values, one per 2x2 block
				// of pixels, interleaved. Prepare them as floats with correct range
				// ready for calculation later.
				int xby2 = x/2;
				int yby2 = y/2;
				float U = (float)(data[numPixels + 2*xby2 + yby2*w] & 0xff) - 128.0f;
				float V = (float)(data[numPixels + 2*xby2 + 1 + yby2*w] & 0xff) - 128.0f;
				// Do the YUV -> RGB conversion
				float Yf = 1.164f*((float)Y) - 16.0f;
				int R = (int)(Yf + 1.596f*V);
				int G = (int)(Yf - 0.813f*V - 0.391f*U);
				int B = (int)(Yf            + 2.018f*U);
				int alpha = 1; //unless transparent

				// Clip rgb values to 0-255
				R = R < 0 ? 0 : R > 255 ? 255 : R;
				G = G < 0 ? 0 : G > 255 ? 255 : G;
				B = B < 0 ? 0 : B > 255 ? 255 : B;

				// Put that pixel in the buffer
				intBuffer.put(alpha*16777216 + R*65536 + G*256 + B);
			}
		}

		// Get buffer ready to be read
		intBuffer.flip();

		// Push the pixel information from the buffer onto the bitmap.
		bitmap.copyPixelsFromBuffer(intBuffer);

		return bitmap;
	}

}

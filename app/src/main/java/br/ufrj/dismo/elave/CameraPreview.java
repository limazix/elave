package br.ufrj.dismo.elave;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import br.ufrj.dismo.elave.component.Component;
import br.ufrj.dismo.elave.filter.Filter;
import br.ufrj.dismo.elave.filter.RedFilter;


public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, PreviewCallback {
    private static final String TAG = "Preview";

    private SurfaceHolder mHolder;
    protected Camera camera;
    protected Filter filter;

    private Component rootComponent;
    private List<Component> components;

    private int width;
    private int height;

    private boolean needEncode = false;
    private int[][] pixels;

    public CameraPreview(Context context) {
        super(context);

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        filter = new RedFilter();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //Open Camera
        camera = Camera.open();
        Parameters parameters = camera.getParameters();

        try {
            camera.setPreviewDisplay(mHolder);
            camera.setPreviewCallback(this);

            Size previewSize = parameters.getPreviewSize();
            width = previewSize.width;
            height = previewSize.height;

            pixels = new int[width][height];
            rootComponent = new Component(0, 0, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Verify supported formats

        if (parameters != null) {

            List<Integer> formats = parameters.getSupportedPreviewFormats();

            if (formats != null) {

                if (!parameters.getSupportedPreviewFormats().contains(ImageFormat.RGB_565)) {
                    Toast.makeText(getContext(), R.string.warning_no_supported_format, Toast.LENGTH_LONG).show();
                    Log.d(TAG, getContext().getString(R.string.warning_no_supported_format));
                    needEncode = true;
                } else {
                    parameters.setPreviewFormat(ImageFormat.RGB_565);
                    camera.setParameters(parameters);
                }
            }

            camera.startPreview();

        } else {
            Log.e(TAG, "This device do not have a camera.");
        }
    }

    public void onPreviewFrame(byte[] data, Camera arg1) {
        if (needEncode) {
            CameraDecoder.decodeYUV420SP(data, width, height, pixels);
            process(pixels);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

    }

    private void process(int[][] rgb) {
        components.clear();
        components.addAll(filter.filter(rgb));
        Log.d(TAG, "Component has " + components.size() + " components.");
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }


}
package de.mirkosertic.opencvtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import org.opencv.android.*;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.imgproc.Imgproc;

public class OpenCVActivity extends Activity
        implements CvCameraViewListener {

    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat matGrayscale;
    private FeatureDetector featureDetector;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mOpenCvCameraView = (CameraBridgeViewBase) new JavaCameraView(this, -1);
        setContentView(mOpenCvCameraView);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        matGrayscale = new Mat(height, width, CvType.CV_8UC4);
        featureDetector = FeatureDetector.create(FeatureDetector.SIFT);
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {

        // Create a grayscale image
        Imgproc.cvtColor(inputFrame, matGrayscale, Imgproc.COLOR_RGBA2GRAY);

        // Blur the image
        Imgproc.blur(matGrayscale, matGrayscale, new Size(3, 3));

        // We use a canny depector
        double threhold = 20;
        double factor = 4;

        Imgproc.Canny(matGrayscale, matGrayscale, threhold, threhold * factor);

        return matGrayscale;
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
    }
}

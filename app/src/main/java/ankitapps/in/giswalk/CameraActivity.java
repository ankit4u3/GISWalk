package ankitapps.in.giswalk;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    GPSTracker gps;
    String address;
    DBHelper db;
    Camera camera;
    SurfaceView surfaceView;
    ///////////////////////
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    ///////////////////////////////////////
    TextView desc_txt;
    TextView lat_txt;
    TextView long_txt;
    TextView accuracy_txt;
    int imageCount=0;
    String path1=null;
    String path2=null;
    String path3=null;
    String newFile;
    private Uri fileUri; // file url to store image/video
    private Button btnCapturePicture, btnRecordVideo;

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        db=new DBHelper(CameraActivity.this);

        desc_txt = (TextView) findViewById(R.id.textView5);
        lat_txt = (TextView) findViewById(R.id.textView6);
        long_txt = (TextView) findViewById(R.id.textView7);
        accuracy_txt = (TextView) findViewById(R.id.textView8);
        // Changing action bar background color
        // These two lines are not needed
        //getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.action_bar))));

        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);

        /**
         * Capture image button click event
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        /**
         * Record video button click event
         */
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                //   recordVideo();
                try {
                    if(imageCount==0)
                    {
                        lat_txt.setText("0/2 IMAGE ");
                        btnRecordVideo.setText(" 1/2 IMAGE ");

                        captureImage(v);

                    }
                    if(imageCount==1)
                    {
                        lat_txt.setText("1/2 IMAGE ");
                        btnRecordVideo.setText(" 2/2 IMAGE");
                        captureImage(v);

                    }
                    if(imageCount==2)
                    {
                        lat_txt.setText("2/2 IMAGE : GET GPS NOW");
                        btnRecordVideo.setText("GET GPS");

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
        Intent i = getIntent();

        if(i.getStringExtra("address")!=null) {
            // image or video path that is captured in previous activity
            address = i.getStringExtra("address");
            Toast.makeText(getApplicationContext(), "Your Default Location ID is  " + address, Toast.LENGTH_LONG).show();
            desc_txt.setText("Parent ID #" + address);
        }
        else
        {

        }
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);


        surfaceHolder = surfaceView.getHolder();


        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    newFile = String.format("/sdcard/%d.jpg", System.currentTimeMillis());
                    outStream = new FileOutputStream(newFile);
                    outStream.write(data);
                    outStream.close();
                    //setExif(newFile);

                    if(imageCount==0)
                    {
                        lat_txt.setText("0/2 IMAGE ");
                        path1=newFile.trim();
                        db.addRecordinglog(Integer.valueOf(address),newFile,"one",1);

                        Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_SHORT).show();
                        refreshCamera();
                    }
                    if(imageCount==1)
                    {
                        lat_txt.setText("1/2 IMAGE ");
                        path2=newFile.trim();
                        db.addRecordinglog(Integer.valueOf(address),newFile,"two",2);

                        Toast.makeText(getApplicationContext(), "Picture Saved Stopping Camera", Toast.LENGTH_SHORT).show();
                        refreshCamera();
                        stopCamera();
                        launchUpload();

                    }
                    if(imageCount==2)
                    {
                        lat_txt.setText("2/2 IMAGE : GET GPS NOW");

                        launchUpload();

                    }
                    if(imageCount>=2)
                    {

                        stopCamera();
                        launchUpload();
                    }
                    imageCount+=1;
                  //  launchUpload(newFile);
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        };
        refreshCamera();
    }

//    public void launchUpload(final String ufilepath) {
//        Intent i = new Intent(CameraActivity.this, UploadActivity.class);
//        i.putExtra("filePath", ufilepath);
//        i.putExtra("isImage", 1);
//        i.putExtra("address", address);
//        i.putExtra("path1", path1);
//        i.putExtra("path2", path2);
//        i.putExtra("path3", path3);
//        //  db.addRecordinglog(Integer.valueOf(address),fileUri.getPath(),address,1000);
//        startActivity(i);
//    }

    public void launchUpload() {
        Intent i = new Intent(CameraActivity.this, UploadActivity.class);
        i.putExtra("filePath", path1);
        i.putExtra("isImage", 1);
        i.putExtra("address", address );
        i.putExtra("path1", path1);
        i.putExtra("path2", path2);
        i.putExtra("path3", path3);
        //  db.addRecordinglog(Integer.valueOf(address),fileUri.getPath(),address,1000);
        startActivity(i);
        this.finish();
    }

    public void setExif(final String filePath) {
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 90);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void captureImage(View v) throws IOException {
        //take the picture
        camera.takePicture(null, null, jpegCallback);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {

            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();

        } catch (Exception e) {

        }
    }

    public void stopCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }


    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        refreshCamera();
    }

    protected void onDraw(Canvas canvas) {
//      //  canvas.drawRGB(255, 0, 255);
//    int h =canvas.getHeight();
//        int w =canvas.getWidth();
//        Paint tempTextPaint = new Paint();
//        tempTextPaint.setAntiAlias(true);
//        tempTextPaint.setStyle(Paint.Style.STROKE);
//        tempTextPaint.setColor(Color.BLACK);
//        tempTextPaint.setTextSize(12f);
//
//
//        canvas.drawText("GIS INFO",10,10,tempTextPaint);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // open the camera
            camera = Camera.open();
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        param.set("orientation", "portrait");
        // modify parameter
        param.setPreviewSize(352, 288);
        camera.setParameters(param);
        camera.setDisplayOrientation(90);
//        Canvas canvas = null;
//        try {
//            canvas = holder.lockCanvas();
//            synchronized(holder) {
//                onDraw(canvas);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (canvas != null) {
//                holder.unlockCanvasAndPost(canvas);
//            }
//        }
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);

            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private void getGPS()
    {
        // create class object
        gps = new GPSTracker(CameraActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Launching camera app to record video
     */
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * ------------ Helper Methods ----------------------
     */

    private void launchUploadActivity(boolean isImage){
        Intent i = new Intent(CameraActivity.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        i.putExtra("address",address);
        //  db.addRecordinglog(Integer.valueOf(address),fileUri.getPath(),address,1000);
        startActivity(i);
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
}

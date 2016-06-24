package ankitapps.in.giswalk;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;

public class UploadActivity extends Activity  implements LocationListener {
    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();
    static SecretKey yourKey = null;
    private static String algorithm = "AES";
    DBHelper db;
    long totalSize = 0;
    String address;
    String path1 = null;
    String path2 = null;
    String path3 = null;
    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    private ImageView imgPreview;
    private ImageView imgPreview2;
    private ImageView imgPreview3;
    private VideoView vidPreview;
    private Button btnUpload;
    private TextView latituteField;
    private TextView longitudeField;
    private TextView AccuracyField;
    private LocationManager locationManager;
    private String provider;
    private String encryptedFileName = "Enc_File.txt";

    /**
     * Uploading the file to server
     */
//    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
//        @Override
//        protected void onPreExecute() {
//            // setting progress bar to zero
//            progressBar.setProgress(0);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            // Making progress bar visible
//            progressBar.setVisibility(View.VISIBLE);
//
//            // updating progress bar value
//            progressBar.setProgress(progress[0]);
//
//            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            return uploadFile();
//        }
//
//        @SuppressWarnings("deprecation")
//        private String uploadFile() {
//            String responseString = null;
//
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);
//
//            try {
//                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
//                        new AndroidMultiPartEntity.ProgressListener() {
//
//                            @Override
//                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
//                            }
//                        });
//
//                File sourceFile = new File(filePath);
//
//                // Adding file data to http body
//                entity.addPart("image", new FileBody(sourceFile));
//
//                // Extra parameters if you want to pass to server
//                entity.addPart("website",
//                        new StringBody("www.sos.org.in"));
//                entity.addPart("email", new StringBody("support@sos.org.in"));
//
//                totalSize = entity.getContentLength();
//                httppost.setEntity(entity);
//
//                // Making server call
//                HttpResponse response = httpclient.execute(httppost);
//                HttpEntity r_entity = response.getEntity();
//
//                int statusCode = response.getStatusLine().getStatusCode();
//                if (statusCode == 200) {
//                    // Server response
//                    responseString = EntityUtils.toString(r_entity);
//                } else {
//                    responseString = "Error occurred! Http Status Code: "
//                            + statusCode;
//                }
//
//            } catch (ClientProtocolException e) {
//                responseString = e.toString();
//            } catch (IOException e) {
//                responseString = e.toString();
//            }
//
//            return responseString;
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            Log.e(TAG, "Response from server: " + result);
//
//            // showing the server response in an alert dialog
//            showAlert(result);
//
//            super.onPostExecute(result);
//        }
//
//    }
//    public static byte[] generateKey(String password) throws Exception
//    {
//        byte[] keyStart = password.getBytes("UTF-8");
//
//        KeyGenerator kgen = KeyGenerator.getInstance("AES");
//        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
//        sr.setSeed(keyStart);
//        kgen.init(128, sr);
//        SecretKey skey = kgen.generateKey();
//        return skey.getEncoded();
//    }
//
//    public static byte[] encodeFile(byte[] key, byte[] fileData) throws Exception
//    {
//
//        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
//
//        byte[] encrypted = cipher.doFinal(fileData);
//
//        return encrypted;
//    }
//
//    public static byte[] decodeFile(byte[] key, byte[] fileData) throws Exception
//    {
//        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
//
//        byte[] decrypted = cipher.doFinal(fileData);
//
//        return decrypted;
//    }

//    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "your_folder_on_sd", "file_name");
//    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//    byte[] yourKey = generateKey("password");
//    byte[] filesBytes = encodeFile(yourKey, yourByteArrayContainigDataToEncrypt);
//    bos.write(fileBytes);
//    bos.flush();
//    bos.close();
//
//    byte[] yourKey = generateKey("password");
//    byte[] decodedData = decodeFile(yourKey, bytesOfYourFile);
    public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Number of PBKDF2 hardening rounds to use. Larger values increase
        // computation time. You should select a value that causes computation
        // to take >100ms.
        final int iterations = 1000;

        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        return secretKey;
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        // Generate a 256-bit key
        final int outputKeyLength = 256;
        SecureRandom secureRandom = new SecureRandom();
        // Do *not* seed secureRandom! Automatically seeded from system entropy.
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        yourKey = keyGenerator.generateKey();
        return yourKey;
    }

    public static byte[] encodeFile(SecretKey yourKey, byte[] fileData)
            throws Exception {
        byte[] encrypted = null;
        byte[] data = yourKey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(data, 0, data.length,
                algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(
                new byte[cipher.getBlockSize()]));
        encrypted = cipher.doFinal(fileData);
        return encrypted;
    }

//
//    public void encrypt(final String tempFile)
//    {
//            File file = new File(tempFile);
//        BufferedOutputStream bos = null;
//        try {
//            bos = new BufferedOutputStream(new FileOutputStream(file));
//            byte[] yourKey = generateKey("password");
//            byte fileContent[] = new byte[(int)file.length()];
//
//            byte[] filesBytes = encodeFile(yourKey, fileContent);
//            bos.write(filesBytes);
//            bos.flush();
//            bos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//    public byte[] readFile(final String path) {
//        byte[] contents = null;
//
//        File file = new File(Environment.getExternalStorageDirectory()
//                + File.separator, path);
//        int size = (int) file.length();
//        contents = new byte[size];
//        try {
//            BufferedInputStream buf = new BufferedInputStream(
//                    new FileInputStream(file));
//            try {
//                buf.read(contents);
//                buf.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return contents;
//    }

    public static byte[] decodeFile(SecretKey yourKey, byte[] fileData)
            throws Exception {
        byte[] decrypted = null;
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, yourKey, new IvParameterSpec(
                new byte[cipher.getBlockSize()]));
        decrypted = cipher.doFinal(fileData);
        return decrypted;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        imgPreview2 = (ImageView) findViewById(R.id.imageView2);
        imgPreview3 = (ImageView) findViewById(R.id.imageView3);
        vidPreview = (VideoView) findViewById(R.id.videoPreview);
        db = new DBHelper(UploadActivity.this);
        // Changing action bar background color
        //getActionBar().setBackgroundDrawable(
        //     new ColorDrawable(Color.parseColor(getResources().getString(
        //             R.color.action_bar))));

        // Receiving the data from previous activity
        Intent i = getIntent();

        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("path1");
        path1 = i.getStringExtra("path1");
        path2 = i.getStringExtra("path2");
        //  path3 = i.getStringExtra("path3");
        // boolean flag to identify the media type, image or video
        boolean isImage = i.getBooleanExtra("isImage", true);
        if(i.getStringExtra("address")!=null) {
            // image or video path that is captured in previous activity
            address = i.getStringExtra("address");
            Toast.makeText(getApplicationContext(), " IMAGE PARENT ID is  " + address, Toast.LENGTH_LONG).show();
        }
        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }
        if (path1 != null) {
            // Displaying the image or video on the screen

        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, path 1 Not Found", Toast.LENGTH_LONG).show();
        }


        if (path2 != null) {
            // Displaying the image or video on the screen

        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, path 2 Not Found", Toast.LENGTH_LONG).show();
        }
        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);
        AccuracyField = (TextView) findViewById(R.id.TextView06);


        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (filePath != null) {

                    //  db.addRecordinglog(Integer.valueOf(address),filePath,  "111", Integer.valueOf(AccuracyField.getText().toString()) );
                    //    db.addRecordinglog(Integer.valueOf(address),filePath.trim(),"one",1000);
                }
                // uploading the file to server
                try {
                    uploadMultipart(getApplicationContext(), filePath);

                } catch (Exception ex) {
                    showAlert(ex.getMessage());
                }

            }
        });

        // btnUpload.setEnabled(false);
        checkNetworkStatus();
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
        }
        btnUpload.setText("Communicating");
    }

    public void uploadMultipart(final Context context, String file1) {
        try {
            File f = new File(file1);
            String fname = f.getName();
            String uploadId =
                    new MultipartUploadRequest(context, "http://192.168.1.10/upload.php")
                            .addFileToUpload(file1, "uploaded_file")


                            .addParameter("filename", f.getName())
                            .addParameter("name", fname)
                            .addParameter("file_name", "temp.jpg")
                            .addParameter("email", "ankit@sos.org.in")
                            .addParameter("website", "sos.org.in")
                            .addParameter("lat", latituteField.getText().toString().trim())
                            .addParameter("lon", longitudeField.getText().toString().trim())
                            .addParameter("accuracy", AccuracyField.getText().toString().trim())
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload();
        } catch (Exception exc) {
            Log.e("AndroidUploadService", exc.getMessage(), exc);
        }
    }

    /**
     * Displaying captured image/video on the screen
     * */
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.GONE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(path1, options);


            final Bitmap bitmap1 = BitmapFactory.decodeFile(path2, options);
            //  final Bitmap bitmap2 = BitmapFactory.decodeFile(path2, options);


            //  imgPreview.setImageBitmap(bitmap);
            imgPreview2.setImageBitmap(bitmap1);
            imgPreview2.setRotation(90f);
            imgPreview3.setImageBitmap(bitmap);
            imgPreview3.setRotation(90f);
            //  encrypt(path1);
            //  encrypt(path2);
        } else {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            vidPreview.setVideoPath(filePath);
            // start playing
            vidPreview.start();
        }
    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        String accuracy = String.valueOf(location.getAccuracy());
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
        AccuracyField.setText(accuracy);

        if (Double.valueOf(location.getAccuracy()) < 10.0) {
            btnUpload.setEnabled(true);
            btnUpload.setText("Upload Now");
        } else {
            //  btnUpload.setEnabled(false);
            btnUpload.setText("Waiting for High Accuracy");
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    void saveFile(String stringToSave) {
        try {
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator, encryptedFileName);
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            yourKey = generateKey();
            byte[] filesBytes = encodeFile(yourKey, stringToSave.getBytes());
            bos.write(filesBytes);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void decodeFile(final String encryptedFileName) {

        try {
            byte[] decodedData = decodeFile(yourKey, readFile(encryptedFileName));
            String str = new String(decodedData);
            System.out.println("DECODED FILE CONTENTS : " + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] readFile(final String encryptedFileName) {
        byte[] contents = null;

        File file = new File(encryptedFileName);
        int size = (int) file.length();
        contents = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(
                    new FileInputStream(file));
            try {
                buf.read(contents);
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return contents;
    }

    public void checkNetworkStatus() {

        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable()) {

            Toast.makeText(this, "Wifi", Toast.LENGTH_LONG).show();
        } else if (mobile.isAvailable()) {

            Toast.makeText(this, "Mobile 3G ", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(this, "No Network ", Toast.LENGTH_LONG).show();
        }

    }



}
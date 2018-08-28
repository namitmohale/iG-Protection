package hand.example.com.myapplication;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class UploadPanel extends Activity {
    protected static final String TAG = null;

    String filePath = "";
    String source = "", dest = "";
    TextView tvdisplayFile;
    String userid;
    private ProgressDialog dialog;
    String st;
    EditText txtPassword, txtfilename;
String key="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploadpanel);
        key = getIntent().getStringExtra("key");

        tvdisplayFile = (TextView) findViewById(R.id.tvdisplayFile);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED)) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{

                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }

    }
public  void selectFile(View v){
    Intent intent = new Intent(getBaseContext(), FileChooser.class);
intent.putExtra("dir","");
    startActivityForResult(intent, 1);
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
try{
    if (data == null) {
        return;
    }
    filePath = data.getStringExtra("FILEPATH");
    Log.i("CLOUD", "Selected File Path=" + filePath);
    File f = new File(filePath);

    filePath = f.getPath();

    tvdisplayFile.setText(filePath);
}catch (Exception e){
    e.printStackTrace();
}

    }



    File targetLocation,sourceLocation;
    String strSource;

    public class Encryption extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.i("CLOUD",
                    "sourc" + strSource + " " + targetLocation.toString());
            boolean b = new MainEncryption().DBST(1, strSource, key
                    , targetLocation.toString());// to encrypt
            Log.i("CLOUD", "enc--" + Boolean.toString(b));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            runOnUiThread(new Runnable() {
                public void run() {
                    dialog.setTitle("Saving File");
                    tvdisplayFile.setText("File saved at "+targetLocation.getPath());
                    dialog.dismiss();
                    if(sourceLocation.exists()){
                        sourceLocation.delete();
                    }

                }
            });
        }
    }

    public void upload(View v) {
        try {
            sourceLocation = new File(filePath);

            if (!sourceLocation.exists()) {
                Toast.makeText(getBaseContext(),
                        "Please select a file to upload", Toast.LENGTH_LONG)
                        .show();
            }
            long file_size = sourceLocation.length();
            System.out.println("file size=" + file_size);
            if (sourceLocation.length() < 10485760) {
                // File f1=new File("sdcard/uploads/"+f.getName());

                // your sd card
                String sdCard = Environment.getExternalStorageDirectory()
                        .toString();

                // the file to be moved or copied
                // File sourceLocation = new File (filePath);
                // File sourceLocation = new File (sdCard + "/images.jpg");
                // make sure your target location folder exists!
                strSource = sourceLocation.getAbsolutePath();

                targetLocation = new File(sdCard + "/gesture/" + sourceLocation.getName());
                File ff=new File(sdCard+"/gesture/");
                ff.mkdir();
                dialog = ProgressDialog.show(UploadPanel.this,
                        "Encrypting data...", "Please wait...",true  , true);
                Encryption enc = new Encryption();
                enc.execute();

            } else
                Toast.makeText(getBaseContext(),
                        "File Size is greater than 10 MB.", Toast.LENGTH_LONG)
                        .show();

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }


}
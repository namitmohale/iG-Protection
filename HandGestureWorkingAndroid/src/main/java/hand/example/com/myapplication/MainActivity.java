package hand.example.com.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import edu.washington.cs.touchfreelibrary.sensors.CameraGestureSensor;
import edu.washington.cs.touchfreelibrary.sensors.ClickSensor;
import edu.washington.cs.touchfreelibrary.utilities.LocalOpenCV;
import edu.washington.cs.touchfreelibrary.utilities.PermissionUtility;

public class MainActivity extends AppCompatActivity implements CameraGestureSensor.Listener, ClickSensor.Listener {
    Context cx;
    TextView txtDetails;
    String gesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (PermissionUtility.checkCameraPermission(this)) {
            //The third passing in represents a separate click sensor which is not required if you just want the hand motions
            LocalOpenCV loader = new LocalOpenCV(MainActivity.this, MainActivity.this, MainActivity.this);
        }
        cx = this;
        txtDetails = findViewById(R.id.txtDetails);
        String text = "Move Hand To Record Gesture";
        txtDetails.setText(text);

    }

    public void proceed(View v) {
        if (pattern.length() > 0) {
            Intent intent = new Intent(getBaseContext(), UploadPanel.class);
            intent.putExtra("key", pattern);
            startActivity(intent);
        } else {
            Toast.makeText(cx, "Please draw pattern before proceeding", Toast.LENGTH_SHORT).show();
        }

    }

    File targetLocation, sourceLocation;
    String strSource;
    ProgressDialog dialog;

    public class Encryption extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.i("CLOUD",
                    "sourc" + strSource + " " + targetLocation.toString());
            enc = new MainEncryption().DBST(2, strSource, pattern
                    , targetLocation.toString());// to encrypt
            Log.i("CLOUD", "enc--" + Boolean.toString(enc));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            runOnUiThread(new Runnable() {
                public void run() {
                    dialog.setTitle("Saving File");
                    if (enc) {
                        txtDetails.setText("File saved at " + targetLocation.getPath());
                        dialog.dismiss();
                        if (sourceLocation.exists()) {
                            //  sourceLocation.delete();
                        }
                    } else {
                        txtDetails.setText("Unable to decrypt file");
                    }


                }
            });
        }
    }

    boolean enc = false;

    public void decrypt(View v) {
        if (pattern.length() > 0) {
            if (filePath.length() > 0) {
                try {
                    //pattern="HELLO";
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

                        targetLocation = new File(sdCard + "/gesture_unlock/" + sourceLocation.getName());
                        File ff = new File(sdCard + "/gesture_unlock/");
                        ff.mkdir();
                        dialog = ProgressDialog.show(MainActivity.this,
                                "Decrypting data...", "Please wait...", true, true);
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
            } else {
                Toast.makeText(cx, "Please select file to decrypt", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(cx, "Please draw pattern before decrypting file", Toast.LENGTH_SHORT).show();

        }


    }

    public void viewFiles(View v) {
        Intent intent = new Intent(getBaseContext(), FileChooser.class);
        intent.putExtra("dir", "/gesture");
        startActivityForResult(intent, 1);
    }

    public void clearPattern(View v) {
        pattern = "";
        pattern_time = 0;
        pattern_show = "";
        txtDetails.setText(pattern_show);
        Toast.makeText(cx, "Pattern cleared successfully", Toast.LENGTH_SHORT).show();
    }

    String filePath;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data == null) {
                return;
            }
            filePath = data.getStringExtra("FILEPATH");
            Log.i("CLOUD", "Selected File Path=" + filePath);
            File f = new File(filePath);
            filePath = f.getPath();
            txtDetails.setText("You have selected " + filePath + "\nKindly draw your pattern to decrypt");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtility.checkCameraPermission(this)) {
            //The third passing in represents a separate click sensor which is not required if you just want the hand motions
            LocalOpenCV loader = new LocalOpenCV(MainActivity.this, MainActivity.this, MainActivity.this);
        }
    }

    long last_time = 0;
    long pattern_time = 10000;
    String up_move = "ABC";
    String down_move = "DEF";
    String left_move = "GHI";
    String right_move = "JKL";


    String pattern = "";
    String TAG = "TAG";

    @Override
    public void onGestureUp(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "Up");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(cx, "Up", Toast.LENGTH_SHORT).show();
            }
        });

        recordPattern("UP", up_move);
    }
    @Override
    public void onGestureDown(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "Down");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(cx, "Down", Toast.LENGTH_SHORT).show();
            }
        });

        recordPattern("DOWN", down_move);

    }

    @Override
    public void onGestureLeft(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "Left");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(cx, "Left", Toast.LENGTH_SHORT).show();
            }
        });
        recordPattern("Left", left_move);
    }

    @Override
    public void onGestureRight(CameraGestureSensor caller, long gestureLength) {
        Log.i(TAG, "Right");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(cx, "Right", Toast.LENGTH_SHORT).show();
            }
        });
        recordPattern("Right", right_move);
    }

    @Override
    public void onSensorClick(ClickSensor caller) {
        Log.i(TAG, "Click");

    }
    String pattern_show = "";

    public void recordPattern(String type, String move) {
        pattern_show = pattern_show + "\n" + type;
        if (last_time == 0) {
            last_time = System.currentTimeMillis();


            Log.i(TAG, "onGestureUp: " + pattern);
            pattern = pattern + move;
            Log.i(TAG, "onGestureUp1: " + pattern);


        } else {
            Log.i(TAG, "onGestureUp: " + (System.currentTimeMillis() - last_time));
            if (System.currentTimeMillis() - last_time < pattern_time) {
                if (pattern.length() <= 16) {
                    Log.i(TAG, "onGestureUp: " + pattern);
                    pattern = pattern + move;
                    Log.i(TAG, "onGestureUp1: " + pattern);
                } else {
                    ask();
                }
            } else {
                pattern = move;
                last_time = System.currentTimeMillis();
                pattern_show = "\n" + type;
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // txtDetails.setText("Pattern Recorded: " + pattern_show );
                txtDetails.setText("Pattern Recorded: " + pattern_show + "\nKey=" + pattern);
            }
        });

    }

    public void ask() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Log.i(TAG, "onClick: " + pattern);
                                Intent intent = new Intent(getBaseContext(), UploadPanel.class);
                                intent.putExtra("key", pattern);
                                startActivity(intent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                last_time = 0;
                                pattern = "";
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Pattern Recorded").setPositiveButton("Proceed", dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener).show();
            }
        });

    }



}

package ankitapps.in.giswalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {

    DBHelper db;
    GridView grid;
    String[] web = {
            "Capture",
            "Calibrate",
            "Settings",
            "GPS",
            "Database",
            "List",
            "Wallet",
            "Create",
            "ImageLog"


    } ;
    int[] imageId = {
            R.drawable.splash_img,
            R.drawable.splash_img,
            R.drawable.splash_img,
            R.drawable.splash_img,
            R.drawable.splash_img,
            R.drawable.splash_img,
            R.drawable.splash_img,
            R.drawable.splash_img,
            R.drawable.splash_img


    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new DBHelper(MainActivity.this);
        CustomGrid adapter = new CustomGrid(MainActivity.this, web, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               // Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();
                startNewActivity(web[+ position]);
            }
        });

    }
    public void startNewActivity(String uActivity)
    {
        if(uActivity.equalsIgnoreCase("Calibrate"))
        {

        }
        if(uActivity.equalsIgnoreCase("GPS"))
        {

        }
        if(uActivity.equalsIgnoreCase("Capture")){
            Intent intent = new Intent(MainActivity.this,
                    CameraActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        //ImageLog
        if(uActivity.equalsIgnoreCase("ImageLog")){
            Intent intent = new Intent(MainActivity.this,
                    SavedLog.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        if(uActivity.equalsIgnoreCase("Create")){
            Intent intent = new Intent(MainActivity.this,
                    CreateGIS.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        if(uActivity.equalsIgnoreCase("List")){

            Intent intent = new Intent(MainActivity.this,
                    ListEntry.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

            if(uActivity.equalsIgnoreCase("Wallet")){
                if(db!=null)
                {
                    long countItems2=db.addRecording("XABC","XCCC",200000);
                    Toast.makeText(MainActivity.this, "Total Items " +countItems2, Toast.LENGTH_SHORT).show();
//                    ArrayList<String> array_list=db.getAll();
//                    List<String> list=array_list;
//                    System.out.println("#2 advance for loop");
//
//                    Iterator<String> iterator = list.iterator();
//                    while (iterator.hasNext()) {
//
//                        Toast.makeText(MainActivity.this, "Loop Items " +iterator.next(), Toast.LENGTH_SHORT).show();
//                    }

                }


            }
        }

    }

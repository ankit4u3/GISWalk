package ankitapps.in.giswalk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SavedLog extends AppCompatActivity {
    DBHelper db;
    ///////////////////////

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtFourth;

    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    public static final String THIRD_COLUMN="Third";
    public static final String FOURTH_COLUMN="Fourth";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_log);
        db=new DBHelper(SavedLog.this);
        int nextId=db.getCount();
        setList();
    }
    public void setList()
    {
        ListView listView=(ListView)findViewById(R.id.listView1);

        //list=new ArrayList<HashMap<String,String>>();
        list= db.getAllImageLog();

        ListViewAdapter adapter=new ListViewAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos=position+1;
                Toast.makeText(SavedLog.this, Integer.toString(pos)+" Clicked", Toast.LENGTH_SHORT).show();
                System.out.println(list.get(position).get("file_path"));
                HashMap<String, String> map=list.get(position);
                Toast.makeText(SavedLog.this,map.get(Constants.FIRST_COLUMN), Toast.LENGTH_SHORT).show();
                loadImage(map.get(Constants.FIRST_COLUMN).trim());

            }

        });
    }

    public void startIntentWithId(String id)
    {

    }

    public void loadImage(final String path)
    {
        File imgFile = new File(path);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageView);

            myImage.setImageBitmap(myBitmap);

        }
    }
}
package ankitapps.in.giswalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ListEntry extends AppCompatActivity {
    public static final String FIRST_COLUMN = "First";
    ///////////////////////
    public static final String SECOND_COLUMN = "Second";
    public static final String THIRD_COLUMN = "Third";
    public static final String FOURTH_COLUMN = "Fourth";
    public ArrayList<HashMap<String, String>> list;
    DBHelper db;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtFourth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_entry);
        db=new DBHelper(ListEntry.this);
        int nextId=db.getCount();
        setList();
    }
    public void setList()
    {
        ListView listView=(ListView)findViewById(R.id.listView1);

        //list=new ArrayList<HashMap<String,String>>();
        list= db.getAll();

        ListViewAdapter adapter=new ListViewAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos=position+1;
                Toast.makeText(ListEntry.this, Integer.toString(pos)+" Clicked", Toast.LENGTH_SHORT).show();
                startIntentWithId(String.valueOf(pos));
            }

        });
    }

    public void startIntentWithId(String id)
    {
        Intent i = new Intent(ListEntry.this, CameraActivity.class);
        i.putExtra("address", id);

        startActivity(i);
        this.finish();
    }
}

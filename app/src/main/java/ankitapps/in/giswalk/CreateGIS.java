package ankitapps.in.giswalk;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateGIS extends AppCompatActivity {
    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";
    public static final String THIRD_COLUMN = "Third";
    public static final String FOURTH_COLUMN = "Fourth";
    ////////////////////
    public static ArrayList<String> ArrayofName = new ArrayList<String>();
    public ArrayList<HashMap<String, String>> list;
    ///////////////////////
    EditText localtion;
    EditText description;
    EditText autoId;
    Button bSave;
    DBHelper db;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtFourth;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gis);
        localtion=(EditText)findViewById(R.id.editText);
        description=(EditText)findViewById(R.id.editText2);
        autoId=(EditText)findViewById(R.id.editText3);
        bSave=(Button)findViewById(R.id.button) ;



        db=new DBHelper(CreateGIS.this);
        int nextId=db.getCount();
        autoId.setText(String.valueOf(++nextId));


        gridView = (GridView) findViewById(R.id.gridView);

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loc=localtion.getText().toString().trim();
                String des=description.getText().toString().trim();
                long countItems2=db.addRecording(loc,des,db.getCount()+1);
                Toast.makeText(CreateGIS.this, " Data Saved  "  , Toast.LENGTH_SHORT).show();
                localtion.setText("");
                description.setText("");
                int nextId=db.getCount();
                autoId.setText(String.valueOf(++nextId));
                //setGrid();
                setList();
            }
        });
        setList();

    }

    public void setList()
    {
        ListView listView=(ListView)findViewById(R.id.listView1);

        //list=new ArrayList<HashMap<String,String>>();
        list= db.getAll();
//        HashMap<String,String> temp=new HashMap<String, String>();
//        temp.put(FIRST_COLUMN, "Ankit Karia");
//        temp.put(SECOND_COLUMN, "Male");
//        temp.put(THIRD_COLUMN, "22");
//        temp.put(FOURTH_COLUMN, "Unmarried");
//        list.add(temp);
//
//        HashMap<String,String> temp2=new HashMap<String, String>();
//        temp2.put(FIRST_COLUMN, "Rajat Ghai");
//        temp2.put(SECOND_COLUMN, "Male");
//        temp2.put(THIRD_COLUMN, "25");
//        temp2.put(FOURTH_COLUMN, "Unmarried");
//        list.add(temp2);
//
//        HashMap<String,String> temp3=new HashMap<String, String>();
//        temp3.put(FIRST_COLUMN, "Karina Kaif");
//        temp3.put(SECOND_COLUMN, "Female");
//        temp3.put(THIRD_COLUMN, "31");
//        temp3.put(FOURTH_COLUMN, "Unmarried");
//        list.add(temp3);

        ListViewAdapter adapter=new ListViewAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                int pos=position+1;
                Toast.makeText(CreateGIS.this, Integer.toString(pos)+" Clicked", Toast.LENGTH_SHORT).show();
            }

        });
    }
    public void setGrid()
    {
//        ArrayList<String> array_list=db.getAll();
//        List<String> list=array_list;
//        System.out.println("#2 advance for loop");
//
//        Iterator<String> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            ArrayofName.add(iterator.next());
//
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, ArrayofName);
//
//        gridView.setAdapter(adapter);
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Toast.makeText(getApplicationContext(),
//                        ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}

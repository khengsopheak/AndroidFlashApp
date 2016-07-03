package com.example.sopheak.flashapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LoadImageList extends AppCompatActivity implements AsyncResponse {
ListView lvContainer;
    ArrayList<User_Class> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image_list);
        lvContainer = (ListView)findViewById(R.id.lv_container);
        PostResponseAsyncTask task = new PostResponseAsyncTask(LoadImageList.this, this);
        task.execute("http://192.168.69.1/image.php");
    }

    @Override
    public void processFinish(String s) {
               // Toast.makeText(LoadImageList.this, s, Toast.LENGTH_LONG).show();
                 userList = new JsonConverter<User_Class>().toArrayList(s, User_Class.class);
        BindDictionary<User_Class> dict = new BindDictionary<User_Class>();
        dict.addStringField(R.id.txt_title,
                new StringExtractor<User_Class>() {
                    @Override
                    public String getStringValue(User_Class item, int position) {
                        return item.name;
                    }
                });
        dict.addDynamicImageField(R.id.img,
                new StringExtractor<User_Class>() {

                    @Override
                    public String getStringValue(User_Class item, int position) {
                        return item.img;
                    }
                }, new DynamicImageLoader() {
                    @Override
                    public void loadImage(String url, ImageView view) {
                        Picasso.with(LoadImageList.this)
                                .load(url)
                                .resize(150, 150)
                                .centerCrop()
                                .into(view);
                    }
                });
        FunDapter<User_Class> funDapter = new FunDapter<>(LoadImageList.this, userList, R.layout.activity_custom_list, dict);
        lvContainer.setAdapter(funDapter);
    }
}

package com.example.storbook;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class friends_page extends AppCompatActivity {
    private int[] imageId = new int[]{R.drawable.postcard, R.drawable.time, R.drawable.postcard,
            R.drawable.pwdstatus, R.drawable.gallery, R.drawable.ancestors};//定义并初始化存放图片的数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_page);
        GridView gridView = (GridView) findViewById(R.id.GridView1);//获取GridView组件
        String[] titleId = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};//定义并初始化说明文字的数组
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();//创建一个list集合
        for (int i = 0; i < imageId.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ImageView_id", imageId[i]);
            map.put("TextView_id", titleId[i]);
            listItems.add(map);//将存入map的数据存到List中
        }
        //创建适配器SimpleAdapter
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.image_item,
                new String[]{"photo", "name"},
                new int[]{R.id.photo, R.id.name});//适配items.xml布局
        gridView.setAdapter(adapter);//将适配器与GridView相关联
    }
}
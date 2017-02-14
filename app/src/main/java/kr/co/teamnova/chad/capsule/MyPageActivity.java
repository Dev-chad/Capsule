package kr.co.teamnova.chad.capsule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        ListView listViewOption = (ListView)findViewById(R.id.listView_option);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        adapter.add("프로필 설정");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");
        adapter.add("비밀번호 변경");


        listViewOption.setAdapter(adapter);

    }
}

package kr.co.teamnova.chad.capsule;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Chad on 2017-01-17.
 */

public class MainActivity extends AppCompatActivity {
    private final int STATE_HOME = 0;
    private final int STATE_SEARCH = 1;
    private final int STATE_PEOPLE = 2;
    private final int STATE_ADD = 3;

    private int stateNum = 0;
    private int subState = 0;
    private ImageButton btnHome;
    private ImageButton btnSearch;
    private ImageButton btnPeople;
    private ImageButton btnAdd;

    private FragmentManager fragmentManager;

    private String loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginUser = getIntent().getStringExtra("login_user");
        fragmentManager = getFragmentManager();
        btnHome = (ImageButton) findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateNum = STATE_HOME;
                subState = STATE_HOME;
                onClickMenu();
            }
        });

        btnSearch = (ImageButton) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateNum = STATE_SEARCH;
                subState = STATE_SEARCH;
                onClickMenu();
            }
        });

        btnPeople = (ImageButton) findViewById(R.id.btn_people);
        btnPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateNum = STATE_PEOPLE;
                subState = STATE_HOME;
                onClickMenu();
            }
        });

        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateNum = STATE_ADD;
                onClickMenu();
            }
        });
        onClickMenu();

    }

    private void onClickMenu() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("login_user", loginUser);
        switch (stateNum) {
            case STATE_HOME: {
                btnHome.setImageResource(R.mipmap.image_btn_together);
                btnSearch.setImageResource(R.mipmap.image_btn_home_inactive);
                btnPeople.setImageResource(R.mipmap.image_btn_people_inactive);
                btnAdd.setImageResource(R.mipmap.image_btn_add_inactive);
                HomeFragment fragment = new HomeFragment();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment);
                break;
            }
            case STATE_SEARCH: {
                btnHome.setImageResource(R.mipmap.image_btn_together_inactive);
                btnSearch.setImageResource(R.mipmap.image_btn_home);
                btnPeople.setImageResource(R.mipmap.image_btn_people_inactive);
                btnAdd.setImageResource(R.mipmap.image_btn_add_inactive);
                SearchFragment fragment = new SearchFragment();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment);
                break;
            }
            case STATE_PEOPLE: {
                btnHome.setImageResource(R.mipmap.image_btn_together_inactive);
                btnSearch.setImageResource(R.mipmap.image_btn_home_inactive);
                btnPeople.setImageResource(R.mipmap.image_btn_people);
                btnAdd.setImageResource(R.mipmap.image_btn_add_inactive);
                UserListFragment fragment = new UserListFragment();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment);
                break;
            }
            case STATE_ADD: {
                Intent addIntent = new Intent(this, AddContentActivity.class);
                addIntent.putExtra("login_user", loginUser);
                startActivityForResult(addIntent, 0);
                break;
            }
        }

        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_mypage: {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_logout: {
                SharedPreferences spAppPrefs = getSharedPreferences("app", MODE_PRIVATE);
                SharedPreferences.Editor spAppPrefsEditor = spAppPrefs.edit();
                spAppPrefsEditor.putBoolean("auto_login_use", false);
                spAppPrefsEditor.remove("auto_login_password");
                spAppPrefsEditor.apply();
                Intent intent = new Intent(getApplicationContext(), LoginPageActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (subState == STATE_SEARCH) {
                    btnSearch.callOnClick();
                } else {
                    btnHome.callOnClick();
                }

            }
        }
    }
}

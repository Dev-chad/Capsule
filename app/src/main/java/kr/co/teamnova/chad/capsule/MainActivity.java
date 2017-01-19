package kr.co.teamnova.chad.capsule;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Chad on 2017-01-17.
 */

public class MainActivity extends AppCompatActivity implements AddFragment.OnClickAddListener{
    private final int STATE_HOME = 0;
    private final int STATE_SEARCH = 1;
    private final int STATE_PEOPLE= 2;
    private final int STATE_ADD = 3;

    private int stateNum = 0;

    private ImageButton btnHome;
    private ImageButton btnSearch;
    private ImageButton btnPeople;
    private ImageButton btnAdd;

    private FragmentManager fragmentManager;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();
        fragmentManager = getFragmentManager();

        btnHome = (ImageButton) findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateNum = STATE_HOME;
                onClickMenu();
            }
        });

        btnSearch = (ImageButton) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateNum = STATE_SEARCH;
                onClickMenu();
            }
        });

        btnPeople = (ImageButton) findViewById(R.id.btn_people);
        btnPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateNum = STATE_PEOPLE;
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

    @Override
    protected void onStart() {
        super.onStart();
//        setButtonAlpha();
    }

    private void onClickMenu(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle(1);
        bundle.putString("email", intent.getStringExtra("email"));
        switch (stateNum){
            case STATE_HOME: {
                btnHome.setImageResource(R.mipmap.image_btn_home);
                btnSearch.setImageResource(R.mipmap.image_btn_search_inactive);
                btnPeople.setImageResource(R.mipmap.image_btn_people_inactive);
                btnAdd.setImageResource(R.mipmap.image_btn_add_inactive);
                HomeFragment fragment = new HomeFragment();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment);
                break;
            }
            case STATE_SEARCH: {
                btnHome.setImageResource(R.mipmap.image_btn_home_inactive);
                btnSearch.setImageResource(R.mipmap.image_btn_search);
                btnPeople.setImageResource(R.mipmap.image_btn_people_inactive);
                btnAdd.setImageResource(R.mipmap.image_btn_add_inactive);
                SearchFragment fragment = new SearchFragment();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment);
                break;
            }
            case STATE_PEOPLE: {
                btnHome.setImageResource(R.mipmap.image_btn_home_inactive);
                btnSearch.setImageResource(R.mipmap.image_btn_search_inactive);
                btnPeople.setImageResource(R.mipmap.image_btn_people);
                btnAdd.setImageResource(R.mipmap.image_btn_add_inactive);
                PeopleFragment fragment = new PeopleFragment();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment);
                break;
            }
            case STATE_ADD: {
                btnHome.setImageResource(R.mipmap.image_btn_home_inactive);
                btnSearch.setImageResource(R.mipmap.image_btn_search_inactive);
                btnPeople.setImageResource(R.mipmap.image_btn_people_inactive);
                btnAdd.setImageResource(R.mipmap.image_btn_add);
                AddFragment fragment = new AddFragment();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment);
                break;
            }
        }

        fragmentTransaction.commit();
    }

    public void setHomeFragment(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle(1);
        bundle.putString("email", intent.getStringExtra("email"));
        btnHome.setImageResource(R.mipmap.image_btn_home);
        btnSearch.setImageResource(R.mipmap.image_btn_search_inactive);
        btnPeople.setImageResource(R.mipmap.image_btn_people_inactive);
        btnAdd.setImageResource(R.mipmap.image_btn_add_inactive);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }


        public void AddClickEvent(){
            setHomeFragment();
        }

}
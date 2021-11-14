package com.joey.myim.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.joey.myim.fragments.FriendsFragment;
import com.joey.myim.fragments.RoomsFragment;
import com.joey.myim.service.Connect;
import com.joey.myim.R;
import com.joey.myim.widget.SlideMenu;

/**
 * 登陆成功后显示的主页面
 */
public class MainActivity extends AppCompatActivity {
    static String myUserName;
    TextView mTvMenuUsername;
    Button mBtnAddFriend, mBtnDrawer, mBtnMenuUpdateUsername, mBtnMenuUpdatePassword, mBtnMenuSignOut;
    SlideMenu slideMenu;
    Fragment mFragmentMain;
    BottomNavigationView mNavigation;
    FragmentTransaction transaction;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnAddFriend = findViewById(R.id.btn_add_friend);
        mBtnDrawer = findViewById(R.id.btn_drawer);
        slideMenu = findViewById(R.id.slide_menu);
        mTvMenuUsername = findViewById(R.id.tv_menu_username);
        mBtnMenuUpdateUsername = findViewById(R.id.btn_menu_update_username);
        mBtnMenuUpdatePassword = findViewById(R.id.btn_menu_update_password);
        mBtnMenuSignOut = findViewById(R.id.btn_menu_sign_out);
        mNavigation = findViewById(R.id.navigation_main_bottom);
        myUserName = getIntent().getStringExtra("username");

        //侧滑栏显示用户名
        mTvMenuUsername.setText(myUserName);
        //绑定退出登录单击事件
        mBtnMenuSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("退出")
                        .setMessage("是否退出登录")
                        .setCancelable(true)
                        .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Connect.disConnect();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                builder.show();
            }
        });

        //侧边栏按钮单击事件
        mBtnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideMenu.switchMenu();
            }
        });

        //设置默认fragment
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_main, new FriendsFragment());
        transaction.commit();

        mFragmentMain = new FriendsFragment();
        //主页面下方的导航菜单的【切换】点击事件
        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();

                switch (item.getItemId()) {
                    case R.id.item_friend:
                        transaction.replace(R.id.fragment_main, new FriendsFragment());
                        transaction.commit();
                        return true;
                    case R.id.item_room_chat:
                        transaction.replace(R.id.fragment_main, RoomsFragment.newInstance(myUserName));
                        transaction.commit();
                        return true;
                }
                return false;
            }
        });
    }

    //重写返回方法，强制用户退出登录
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("退出")
                    .setMessage("是否退出登录")
                    .setCancelable(true)
                    .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Connect.disConnect();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            builder.show();
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
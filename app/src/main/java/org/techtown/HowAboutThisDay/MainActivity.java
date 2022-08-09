package org.techtown.HowAboutThisDay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.List;

import org.techtown.HowAboutThisDay.Study_activity;

public class MainActivity extends AppCompatActivity {

    CalendarView cal;
    TextView tv_text;

    SQLiteHelper dbHelper;

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ImageButton btnAdd;

    List<Memo> memoList;

    private DrawerLayout drawerLayout;
    private View drawerView;
    int i=0;
    ImageButton imageButton3=null, imageButton4=null, imageButton5=null;
    ImageButton imageButton2;
    ImageButton imageButton_left;
    ImageButton imageButton_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imageButton2=(ImageButton)findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity1.class);
                startActivity(intent); //액티비티 이동
            }
        }));

        ImageButton button1 = (ImageButton)findViewById(R.id.imageButton_right);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        imageButton5 = (ImageButton) findViewById(R.id.imageButton5);
        imageButton3.setImageResource(R.drawable.study_main);
        imageButton4.setImageResource(R.drawable.meal_main);
        imageButton5.setImageResource(R.drawable.exercise_main);

        imageButton3.setVisibility(View.VISIBLE);
        imageButton4.setVisibility(View.INVISIBLE);
        imageButton5.setVisibility(View.INVISIBLE);


        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                i = 1 - i;

                if (i == 0) {
                    imageButton3.setVisibility(View.VISIBLE);
                    imageButton4.setVisibility(View.INVISIBLE);
                    imageButton5.setVisibility(View.INVISIBLE);
                }
                else if(i==1) {
                    imageButton3.setVisibility(View.INVISIBLE);
                    imageButton4.setVisibility(View.VISIBLE);
                    imageButton5.setVisibility(View.INVISIBLE);
                    i=i+2;
                }
                else {
                    imageButton3.setVisibility(View.INVISIBLE);
                    imageButton4.setVisibility(View.INVISIBLE);
                    imageButton5.setVisibility(View.VISIBLE);
                    i=1;
                }
            }
        });


        ImageButton button2 = (ImageButton)findViewById(R.id.imageButton_left);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        imageButton5 = (ImageButton) findViewById(R.id.imageButton5);
        imageButton3.setImageResource(R.drawable.study_main);
        imageButton4.setImageResource(R.drawable.meal_main);
        imageButton5.setImageResource(R.drawable.exercise_main);

        imageButton3.setVisibility(View.VISIBLE);
        imageButton4.setVisibility(View.INVISIBLE);
        imageButton5.setVisibility(View.INVISIBLE);


        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                i = 1 - i;

                if (i == 0) {
                    imageButton3.setVisibility(View.VISIBLE);
                    imageButton4.setVisibility(View.INVISIBLE);
                    imageButton5.setVisibility(View.INVISIBLE);
                }
                else if(i==1) {
                    imageButton3.setVisibility(View.INVISIBLE);
                    imageButton4.setVisibility(View.INVISIBLE);
                    imageButton5.setVisibility(View.VISIBLE);
                    i=i+2;
                }
                else {
                    imageButton3.setVisibility(View.INVISIBLE);
                    imageButton4.setVisibility(View.VISIBLE);
                    imageButton5.setVisibility(View.INVISIBLE);
                    i=1;
                }
            }
        });


        ImageButton imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imageButton3.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Study_activity.class);
                startActivity(intent); //액티비티 이동
            }
        }));


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);

        ImageButton imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        }));

        Button btn_close = (Button) findViewById((R.id.btn_close));
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });


        Button btn_user = (Button) findViewById(R.id.btn_user);
        btn_user.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, User_activity.class);
                startActivity(intent); //액티비티 이동
            }
        }));

        Button btn_move1 = (Button) findViewById(R.id.btn_move1);
        btn_move1.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Study_activity.class);
                startActivity(intent); //액티비티 이동
            }
        }));



        dbHelper = new SQLiteHelper(MainActivity.this);
        memoList = dbHelper.selectAll();


        recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerAdapter = new RecyclerAdapter(memoList);
        recyclerView.setAdapter(recyclerAdapter);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener((view) -> {
            //새로운 메모 작성
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivityForResult(intent, 0);
        });

        cal = findViewById(R.id.cal);
        tv_text = findViewById(R.id.tv_text);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                tv_text.setText(year + "년 " + (month + 1) + "월 " + day + "일");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            String strMain = data.getStringExtra("main");
            String strSub = data.getStringExtra("sub");

            Memo memo = new Memo(strMain, strSub, 0);
            recyclerAdapter.addItem(memo);
            recyclerAdapter.notifyDataSetChanged();

            dbHelper.insertMemo(memo);
        }
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

        private List<Memo> listdata;

        public RecyclerAdapter(List<Memo> listdata) {
            this.listdata = listdata;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
            Memo memo = listdata.get(i);

            itemViewHolder.maintext.setTag(memo.getSeq());

            itemViewHolder.maintext.setText(memo.getMaintext());
            itemViewHolder.subtext.setText(memo.getSubtext());

            if (memo.getIsdone() == 0) {
                itemViewHolder.img.setBackgroundColor(Color.LTGRAY);
            } else {
                itemViewHolder.img.setBackgroundColor(Color.GREEN);
            }
        }

        void addItem(Memo memo) {
            listdata.add(memo);
        }

        void removeItem(int position) {
            listdata.remove(position);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            private TextView maintext;
            private TextView subtext;
            private ImageView img;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);

                maintext = itemView.findViewById(R.id.item_maintext);
                subtext = itemView.findViewById(R.id.item_subtext);
                img = itemView.findViewById(R.id.item_image);

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        int position = getAbsoluteAdapterPosition();
                        int seq = (int) maintext.getTag();

                        if (position != RecyclerView.NO_POSITION) {
                            dbHelper.deleteMemo(seq);
                            removeItem(position);
                            notifyDataSetChanged();
                        }
                        return false;
                    }
                });


                drawerLayout.setDrawerListener(listener);
                drawerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
            }

            DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(@NonNull View drawerView) {

                }

                @Override
                public void onDrawerClosed(@NonNull View drawerView) {

                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            };


        }
    }
}



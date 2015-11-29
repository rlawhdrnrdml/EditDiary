package hw.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Size;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    TextView texttime;
    Button btn1;
    EditText edit1;
    DatePicker datePicker;
    View dteView;
    Calendar cal;
    String fileName, date, strPath;


    Calendar now = Calendar.getInstance();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Diary APP");

        String Path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(Path+"/mydiary");
        if(!myDir.exists()) {
            myDir.mkdir();
            Toast.makeText(getApplicationContext(), "mydiary폴더가 생성", Toast.LENGTH_SHORT).show();
        }


        strPath = myDir.getAbsolutePath();



        texttime = (TextView) findViewById(R.id.txttime);
        btn1 = (Button) findViewById(R.id.btn1);
        edit1 = (EditText) findViewById(R.id.edit1);


        cal = Calendar.getInstance();
        final int cYear = cal.get(Calendar.YEAR);
        final int cMonth = cal.get(Calendar.MONTH);
        final int cDay = cal.get(Calendar.DAY_OF_MONTH);
        DiaryApp(cYear, cMonth, cDay);


        texttime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dteView = (View) View.inflate(MainActivity.this, R.layout.datepicker, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("날짜");
                dlg.setView(dteView);

                datePicker = (DatePicker) dteView.findViewById(R.id.dtep);
                datePicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        cal.set(year, monthOfYear, dayOfMonth);


                    }
                });
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int sYear = cal.get(Calendar.YEAR);
                        int sMonth = cal.get(Calendar.MONTH);
                        int sDay = cal.get(Calendar.DAY_OF_MONTH);
                        DiaryApp(sYear, sMonth, sDay);
                    }

                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }

        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = new File(strPath, fileName);
                    FileOutputStream outFs = new FileOutputStream(file);
                    String str = edit1.getText().toString();
                    outFs.write(str.getBytes());
                    outFs.close();
                    Toast.makeText(getApplicationContext(), fileName + " 이 저장됨", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();

        mInflater.inflate(R.menu.menu1, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.come:
                cal = Calendar.getInstance();
                final int cYear = cal.get(Calendar.YEAR);
                final int cMonth = cal.get(Calendar.MONTH);
                final int cDay = cal.get(Calendar.DAY_OF_MONTH);
                DiaryApp(cYear, cMonth, cDay);
                return true;
            case R.id.delete:
                AlertDialog.Builder alertBox = new AlertDialog.Builder(this);

                alertBox.setMessage(date+" 일기를 삭제하시겠습니까?");

                alertBox.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg2) {


                    }
                });
                alertBox.setPositiveButton("확인", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteFile(fileName);
                        edit1.setText("");
                        btn1.setHint("일기 없음");
                        btn1.setText("새로 저장");


                    }
                });
                alertBox.show();
                return true;

            case R.id.big:
                edit1.setTextSize(50);
                return true;
            case R.id.middle:
                edit1.setTextSize(30);
                return true;
            case R.id.small:
                edit1.setTextSize(15);
                return true;



        }

        return false;

    }



    public void DiaryApp(int year, int month, int day) {
        date = Integer.toString(year)+"년 "+Integer.toString(month + 1)+"월 "+ Integer.toString(day)+"일";
        fileName = Integer.toString(year) + "_" + Integer.toString(month + 1) + "_" + Integer.toString(day) + ".txt";
        String str = readDiary(fileName);
        texttime.setText(date);
        edit1.setText(str);


        btn1.setEnabled(true);
    }

    String readDiary(String Name) {
        String diary = null;
        FileInputStream inFs;

        try {
            File file = new File(strPath, Name);
            inFs = new FileInputStream(file);

            byte [] txt = new byte[300];
            inFs.read(txt);
            inFs.close();
            diary = (new String(txt)).trim();
            btn1.setText("수정하기");
        } catch (IOException e) {
            btn1.setHint("일기 없음");
            btn1.setText("새로 저장");
        }
        return diary;
    }

}
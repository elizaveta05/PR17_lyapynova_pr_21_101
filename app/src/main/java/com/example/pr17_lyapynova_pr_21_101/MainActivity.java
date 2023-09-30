package com.example.pr17_lyapynova_pr_21_101;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

Button bt1, bt2, bt3;
EditText et1, et2, et3, et4;
DBHelper dbHelper;
final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = (Button) findViewById(R.id.bt1);
        bt1.setOnClickListener(this);

        bt2 = (Button) findViewById(R.id.bt2);
        bt2.setOnClickListener(this);

        bt3 = (Button) findViewById(R.id.bt3);
        bt3.setOnClickListener(this);

        et1 = (EditText) findViewById(R.id.et1);

        et2 = (EditText) findViewById(R.id.et2);

        et3 = (EditText) findViewById(R.id.et3);

        et4 = (EditText) findViewById(R.id.et4);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View view) {
        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        String name = et1.getText().toString();
        String capital = et2.getText().toString();
        String square = et3.getText().toString();
        String size = et4.getText().toString();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        switch (view.getId()) {
            case R.id.bt1://add
                Log.d(LOG_TAG, "--- Insert in countries: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение

                cv.put("name", name);
                cv.put("capital", capital);
                cv.put("square", square);
                cv.put("size", size);

                // вставляем запись и получаем ее ID
                long rowID = db.insert("countries", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                break;


            case R.id.bt2://read
                Log.d(LOG_TAG, "--- Rows in countries: ---");
                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                Cursor c = db.query("countries", null, null, null, null, null, null);

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int capitalColIndex = c.getColumnIndex("capital");
                    int squareColIndex = c.getColumnIndex("square");
                    int sizeColIndex = c.getColumnIndex("size");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", name = " + c.getString(nameColIndex) +
                                        ", capital = " + c.getString(capitalColIndex)+
                                        ", square = " + c.getString(squareColIndex) +
                                        ", size = " + c.getString(sizeColIndex));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();
                break;


            case R.id.bt3://clear
                Log.d(LOG_TAG, "--- Clear countries: ---");
                // удаляем все записи
                int clearCount = db.delete("countries", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                break;
        }
        // закрываем подключение к БД
        dbHelper.close();
    }

    }
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструкторсуперкласса
            super(context, "myDB", null, 1);
        }

        final String LOG_TAG = "myLogs";
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаемтаблицусполями
            db.execSQL("create table countries ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "capital text,"
                    + "square text,"
                    + "size text" +");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }



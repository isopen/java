package com.isopen.vanilla;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TG;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Maxim Tyulenev on 11.05.15.
 */
public class TBase {

    public static Gson gson = new Gson();
    public static boolean systemBackButton = false;
    public static String dbName = "vanilla.db";
    public static SQLiteDatabase sqBase;

    public static String create_info = "CREATE TABLE IF NOT EXISTS info_user(id INTEGER PRIMARY KEY, firstName CHAR(30), lastName CHAR(30), phone CHAR(11), code CHAR(5), online INTEGER);";
    public static String drop_info = "DROP TABLE info_user;";

    public static String sel_info = "SELECT * FROM info_user;";
    public static String ins_info = "INSERT INTO info_user (id,firstName,lastName,phone,code,online) VALUES (0,'','','','',0)";
    public static String upd_info_online = "UPDATE info_user SET online=";
    public static String count_info = "SELECT COUNT(*) as count FROM info_user;";

    public int userId = 0;
    public String phone = "79130953187";
    public String code = "84272";
    public static String firstName = "";
    public static String lastName = "";
    public static String title = "";
    public static Integer photoSmall = 0;

    public static int fl_auth = 2;
    public int begListChatPoint = 0;
    public int endListChatPoint = 20;
    public static JSONArray jsonArray;
    public static Object typeChat;

    public static ArrayList<String> id_chats; //массив чатов
    public static HashMap<Integer,Integer> upload_avatar; //массив хэшев аватарок чатов
    public static ArrayList<HashMap<String,Object>> id_users; //массив хэшев id пользователей
    public static int[] color = {R.drawable.avatar_chats_blue,R.drawable.avatar_chats_green,R.drawable.avatar_chats_orange};
    public static Random random;

    public static int id_item;
    public static HashMap<String,Object> hashMap;

    public static JSONObject jsonObject;
    public static String chatId = "";

}

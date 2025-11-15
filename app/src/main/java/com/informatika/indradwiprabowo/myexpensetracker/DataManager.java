package com.informatika.indradwiprabowo.myexpensetracker;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String FILE_NAME = "transactions.json";

    public static void saveTransactions(Context context, List<Transaksi> transactions) {
        Gson gson = new Gson();
        String json = gson.toJson(transactions);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Transaksi> loadTransactions(Context context) {
        FileInputStream fileInputStream = null;
        List<Transaksi> transactions = new ArrayList<>();

        try {
            fileInputStream = context.openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            String json = stringBuilder.toString();
            Gson gson = new Gson();
            // Menentukan tipe data target untuk deserialisasi (List dari Transaksi)
            Type type = new TypeToken<ArrayList<Transaksi>>() {}.getType();
            transactions = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        return transactions;
    }
}

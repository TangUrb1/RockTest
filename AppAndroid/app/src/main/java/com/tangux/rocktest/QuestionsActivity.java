package com.tangux.rocktest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    private List<Music> musicList;
    private List<JSONObject> easyJSON, mediumJSON, hardJSON;
    private JSONArray easy, medium, hard;
    private JSONObject musicJSON;

    private QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        musicList = new ArrayList<>();
        easyJSON = new ArrayList<>();
        mediumJSON = new ArrayList<>();
        hardJSON = new ArrayList<>();

        //Create a list with all of the song on the json file
        try {
            musicJSON = new JSONObject(loadJSONFromAssets(QuestionsActivity.this));
            easy = musicJSON.getJSONArray("easy");
            medium = musicJSON.getJSONArray("medium");
            hard = musicJSON.getJSONArray("hard");

            for (int i = 0; i < easy.length(); i++) {
                JSONObject musicObject = easy.getJSONObject(i);
                easyJSON.add(musicObject);
            }

            for (JSONObject musicObject : easyJSON) {
                Music easyMusic = new Music();
                easyMusic.title = musicObject.getString("title");
                easyMusic.singer = musicObject.getString("singer");
                musicList.add(easyMusic);
                Log.i("music", "titre" + easyMusic.title);
            }


            for (int i = 0; i < medium.length(); i++) {
                JSONObject musicObject = medium.getJSONObject(i);
                mediumJSON.add(musicObject);
            }

            for (JSONObject musicObject : mediumJSON) {
                Music mediumMusic = new Music();
                mediumMusic.title = musicObject.getString("title");
                mediumMusic.singer = musicObject.getString("singer");
                musicList.add(mediumMusic);
                Log.i("music", "titre" + mediumMusic.title);
            }


            for (int i = 0; i < hard.length(); i++) {
                JSONObject musicObject = hard.getJSONObject(i);
                hardJSON.add(musicObject);
            }

            for (JSONObject musicObject : hardJSON) {
                Music hardMusic = new Music();
                hardMusic.title = musicObject.getString("title");
                hardMusic.singer = musicObject.getString("singer");
                musicList.add(hardMusic);
                Log.i("music", "titre" + hardMusic.title);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Display the list of music on the recycler view
        adapter = new QuestionAdapter(musicList);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    //Get the json file from the directory assets
    public String loadJSONFromAssets(Context context) {

        String json = null;

        try {
            InputStream is = context.getAssets().open("bd.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}

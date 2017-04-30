package com.sheatouk.selmy.componentsdonationasu.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sheatouk.selmy.componentsdonationasu.R;
import com.sheatouk.selmy.componentsdonationasu.Util.SheamusDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        // permit strictMode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //startService(new Intent(this , ScrapCompService.class));
        SheamusDialog dialog = new SheamusDialog(this);
        dialog.setMessage("wait..");
        dialog.setCancelable(false);
        //dialog.show();
        /*
        Window window = dialog.getWindow();
        window.setLayout(Utils.dbTopixel(300,this),Utils.dbTopixel(200,this));
        */
        /*
        MyTask myTask = new MyTask();
        myTask.execute();
        */
    }
    public class MyTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {

            scrapeImg("chemical+sensors");
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
        private void scrapeImg(String searchTerm){
            String searchUrl = "https://www.google.com/search?site=imghp&tbm=isch&source=hp&biw=1920&bih=955&q=" + searchTerm.replace(" ", "+") + "&gws_rd=cr";
            try {
                Document doc = Jsoup.connect(searchUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36")
                        .referrer("https://www.google.com/").get();

                JSONObject obj;

                for (Element result : doc.select("div.rg_meta")) {

                    // div.rg_meta contains a JSON object, which also holds the image url
                    obj = new JSONObject(result.text());

                    String imageUrl = (String) obj.get("ou");

                    // just printing out the url to demonstate the approach
                    System.out.println("imageUrl: " + imageUrl);
                    Log.d("SearchScrape","imageUrl: " + imageUrl);
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

package com.sheatouk.selmy.componentsdonationasu.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.sheatouk.selmy.componentsdonationasu.POJO.Component;
import com.sheatouk.selmy.componentsdonationasu.POJO.SubMenuHtml;
import com.sheatouk.selmy.componentsdonationasu.POJO.SubMenuRowHtml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScrapCompService extends Service {

    public ScrapCompService() {
    }

    @Override
    public void onCreate() {
        MyTask task = new MyTask();
        task.execute();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        //Log.d("Comp >","Start");
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyTask extends AsyncTask<Void,Void,Void> {
        private DatabaseReference databaseReferenceMenu,databaseReferenceComp;
        private String FUTURE_SITE = "https://store.fut-electronics.com";

        public MyTask(){
            databaseReferenceMenu = FirebaseDatabase.getInstance().getReference().child("Menu");
            databaseReferenceComp = FirebaseDatabase.getInstance().getReference().child("Comp");
        }
        @Override
        protected Void doInBackground(Void... params) {
            getCompList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
        private void scrapMenu(String url){
            try {
                //Log.d("Comp >","Start");

                Document mainPage = Jsoup.connect(url).timeout(10*1000).get();
                Elements mainMenu = mainPage.select("ul#main-menu");
                Elements mainMenuRows =mainMenu.select("li");
                List<SubMenuHtml> mainMenuJ = new ArrayList<>();
                for (Element mainMenuRow : mainMenuRows){
                    SubMenuHtml mainRow = new SubMenuHtml();
                    String mainMenuRowName = mainMenuRow.select("a.accordion-button").text();
                    mainRow.setName(mainMenuRowName);
                    //Log.d("Comp >","Main :" + mainMenuRowName);
                    Elements subMenuDiv = mainMenuRow.select("div.accordion-content");
                    Elements subMenuRows = subMenuDiv.select("a");
                    List<SubMenuRowHtml> rows = new ArrayList<>();
                    for (Element subMenuRow : subMenuRows){
                        SubMenuRowHtml row = new SubMenuRowHtml();
                        String subRowName = subMenuRow.text();
                        String subRowRelativeUrl = subMenuRow.attr("href");
                        //Log.d("Comp >","Sub :" + subRowName);
                        row.setName(subRowName);
                        row.setRelativeUrl(subRowRelativeUrl);
                        rows.add(row);
                    }
                    mainRow.setRows(rows);
                    mainMenuJ.add(mainRow);
                }
                databaseReferenceMenu.setValue(mainMenuJ);
            } catch (IOException e) {
                e.printStackTrace();
                //TODO : ERROR MSG
                Log.d("Comp >","Failed");
                return;
            }
        }
        private void scrapComp(String url, String parent, String grandParent){
            try {
                Document page = Jsoup.connect(url).timeout(10000).get();
                Elements gridDiv = page.select("section.product-grid");
                for (Element product : gridDiv.select("div.four")){
                    String imageUrl = product.select("img").attr("src");
                    String name = product.select("h3").text();
                    String price = product.select("h4").text();
                    String relativeUrl = product.select("a").attr("href");
                    Component component = new Component(imageUrl,name,price,grandParent,parent,relativeUrl);
                    DatabaseReference newComp = databaseReferenceComp.push();
                    newComp.setValue(component);
                    Log.d("Comp >","name = "+name);
                }
            } catch (IOException e) {
                e.printStackTrace();
                //TODO : ERROR MSG
                Log.d("Comp >","Failed");
                return;
            }
        }
        private void getCompList(){
            databaseReferenceMenu.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<List<SubMenuHtml>> type = new GenericTypeIndicator<List<SubMenuHtml>>() {};
                    List<SubMenuHtml> mainList = dataSnapshot.getValue(type);
                    if (mainList != null)
                        scrapComps(mainList);
                    else
                        Log.d("Comp >","Failed List Null");
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        private void scrapComps(List<SubMenuHtml> mainList) {
            for (SubMenuHtml subMenu : mainList){
                String grandParent = subMenu.getName();
                if (!grandParent.isEmpty()) {
                    for (SubMenuRowHtml subMenuRow : subMenu.getRows()) {
                        String parent = subMenuRow.getName();
                        String relativeUrl = subMenuRow.getRelativeUrl();
                        String absoluteUrl = FUTURE_SITE + relativeUrl;
                        scrapComp(absoluteUrl, parent, grandParent);
                    }
                }
            }
        }
    }
}

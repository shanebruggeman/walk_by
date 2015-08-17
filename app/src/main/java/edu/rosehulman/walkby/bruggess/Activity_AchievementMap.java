package edu.rosehulman.walkby.bruggess;

import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class Activity_AchievementMap extends FragmentActivity {
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LoginActivity.DEBUG_KEY, "Creating Map");

        setContentView(R.layout.achievements_map);

        Log.d(LoginActivity.DEBUG_KEY, "Created Map");

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        Log.d(LoginActivity.DEBUG_KEY, "Status is " + status + ". Connection success is " + ConnectionResult.SUCCESS);
        if(status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
            Log.d(LoginActivity.DEBUG_KEY, "Getting map fragment. FM: " + (getFragmentManager() == null));

            // Getting reference to the SupportMapFragment of activity_main.xml
            MapFragment fm = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

            Log.d(LoginActivity.DEBUG_KEY, "Getting google map. Map Fragment is null: " + (fm == null));
            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            Log.d(LoginActivity.DEBUG_KEY, "Setting my location");
            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);

            Log.d(LoginActivity.DEBUG_KEY, "Map null: " + (googleMap == null));
            Log.d(LoginActivity.DEBUG_KEY, "ENV: " + Environment.getExternalStorageDirectory().getPath().toString());

            Log.d(LoginActivity.DEBUG_KEY, "Path is:" + Environment.getExternalStorageDirectory().getPath() + ",,,FILE.kml");
            new LoadPath().execute();
        }
    }

    private class LoadPath extends AsyncTask<String, Void, Void> {

        Vector<PolylineOptions> path;
        Vector<Vector<LatLng>> path_fragment;
        Vector<Polyline> lines;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            path_fragment = new Vector<Vector<LatLng>>();
            path = new Vector <PolylineOptions>();
            lines = new Vector<Polyline>();
        }

        @Override
        protected Void doInBackground(String... params) {
            if(true) {
                return null;
            }
            try {

                InputStream inputStream = getResources().openRawResource(R.raw.gtm_analytics);

                DocumentBuilder docBuilder =  DocumentBuilderFactory.newInstance().newDocumentBuilder();

                Document document = docBuilder.parse(inputStream);

                NodeList listCoordinateTag = null;

                if (document == null) {
                    return null;
                }

                listCoordinateTag = document.getElementsByTagName("coordinates");

                for (int i = 0; i < listCoordinateTag.getLength(); i++) {

                    String coordText = listCoordinateTag.item(i).getFirstChild().getNodeValue().trim();
                    String[] vett = coordText.split("\\ ");
                    Vector<LatLng> temp = new Vector<LatLng>();
                    for(int j=0; j < vett.length; j++){
                        temp.add(new LatLng(Double.parseDouble(vett[j].split("\\,")[0]),Double.parseDouble(vett[j].split("\\,")[1])));
                    }
                    path_fragment.add(temp);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(true) {
                return;
            }

            Log.d(LoginActivity.DEBUG_KEY, "We're on post execution");

            googleMap.clear();
            for(int i=0; i < path_fragment.size(); i++){

                // Poliline options
                PolylineOptions temp = new PolylineOptions();

                for(int j=0; j< path_fragment.get(i).size(); j++)
                    temp.add(path_fragment.get(i).get(j));

                path.add(temp);
            }

            for(int i = 0; i < path.size(); i++)
                lines.add(googleMap.addPolyline(path.get(i)));

            for(int i = 0; i < lines.size(); i++){
                lines.get(i).setWidth(2);
                lines.get(i).setColor(Color.RED);
                lines.get(i).setGeodesic(true);
                lines.get(i).setVisible(true);
            }

            Log.d(LoginActivity.DEBUG_KEY, "Done with post execution");
        }
    }
}

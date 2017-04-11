package edu.uah.uahnavigation;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class InteriorNavigationActivity extends AppCompatActivity {

    private Graph graph;
    private Dijkstra dijkstra;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_navigation);

        AssetManager assetManager = getAssets();
        InputStream inStream = null;
        try{
            inStream = assetManager.open("InteriorNavigationResources/ENG/Floor1.PNG");
        }catch (IOException e){
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inStream);

        ImageView imageView = (ImageView)findViewById(R.id.imageViewFloorPlan);
        imageView.setImageBitmap(bitmap);

        graph = new Graph();
        readInputFile();


    }

    private void readInputFile() {
        int START_LINE = 2;
        AssetManager mngr = getAssets();
        try{
            InputStream in = mngr.open("InteriorNavigationResources/ENG/Graph.txt");   //Need to generalize this path
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line = "";
            String SourceNode = "";
            int TotalSources = 0;
            String DestinationNode = "";
            String WeightStr = "";
            int Weight = 0;
            String FloorPlan = "";
            int NumEdges = 0;
            int TotalEdges = 0;
            for(int j = 0; j < START_LINE; j++) {                   //Skips First Two Lines in .txt
                reader.readLine();
            }

            String[] token;
            //Log.d("aMessage", "Token length: " + token.length);

            int i = 3;
            int k = 0;
            //Log.d("aMessage", "Test");

            while ((line = reader.readLine()) != null) {
                    token = line.split(" ");
                //Log.d("aMessage", "Test: " + line.substring(0,1).compareTo("@"));
                //Log.d("aMessage", "First: " + line.substring(0,1));
                if(line.substring(0,1).compareTo("#") == 0){
                    token[0] = line.substring(1) + ".PNG";          //Append .PNG to grab Floor Plan
                    FloorPlan = token[0];
                    Log.d("aMessage", "Case1 " + "Line " + i +":"+ "FloorPlan: " + FloorPlan);          //Case 1: Floor. Need to open floorplan
                }
                else if(line.substring(0,1).compareTo("@") == 0){
                    token[0] = line.substring(1);
                    NumEdges = Integer.parseInt(line.substring(1));
                    TotalEdges = TotalEdges + NumEdges;
                    TotalSources = TotalSources + 1;
                    Log.d("aMessage","Case2 " + "Line " + i +":"+ "NumEdges: " + NumEdges);          //Case 2: Edge. Defines EdgeNum for next vertex
                }
                else {
                    SourceNode = token[0];
                    Log.d("aMessage","Case3 " + "Line " + i + ":" + "SourceNode: " + SourceNode);
                    DestinationNode = token[1];
                    Log.d("aMessage", "Case3 " + "Line " + i + ":" + "DestinationNode: " + DestinationNode);
                    WeightStr = token[2];
                    Weight = Integer.parseInt(WeightStr);
                    Log.d("aMessage", "Case3 " + "Line " + i + ":" + "Weight: " + Weight);

                    graph.addVertex(new Vertex(SourceNode), false);
                    graph.addVertex(new Vertex(DestinationNode), false);

                    Edge e = new Edge(graph.getVertex(SourceNode), graph.getVertex(DestinationNode), Weight);
                    graph.addEdge(e.getOne(), e.getTwo(), e.getWeight());
                }
                i++;

            } //End while

            Log.d("aMessage","Total Edges: " + TotalEdges);
            Log.d("aMessage","Total Source Nodes: " + TotalSources);

        } catch(IOException e) {
            Log.d("aMessage", "Exception");
            e.printStackTrace();
        }

        Dijkstra dijkstra = new Dijkstra(graph, graph.getVertex("ENG102").getLabel());
        Log.d("graphMessage", "Distance to 2: " + dijkstra.getDistanceTo(graph.getVertex("ENG107").getLabel()));
        Log.d("graphMessage", "Path to 2: " + dijkstra.getPathTo(graph.getVertex("ENG107").getLabel()));

    }
}
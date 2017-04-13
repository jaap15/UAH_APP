package edu.uah.uahnavigation;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;


public class InteriorNavigationActivity extends AppCompatActivity {

    private Graph graph;
    private Dijkstra dijkstra;
    private ImageView imageView, up, down;

    int current = 0;
    String[] images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_navigation);

        final AssetManager assetManager = getAssets();

        imageView = (ImageView)findViewById(R.id.imageViewFloorPlan);
        up = (ImageView)findViewById(R.id.imageViewUp);
        down = (ImageView)findViewById(R.id.imageViewDown);

        graph = new Graph();
        readInputFile();
        drawPath();
        try{
            images =assetManager.list("InteriorNavigationResources/ENG/FloorPlans");
            Log.d("iMessage", "List " + images.length);
        }catch (Exception e)
        {
            Log.d("iMessage", "Inside list of images catch");
            e.printStackTrace();
        }

//        try{
//
//            InputStream inStream = assetManager.open("InteriorNavigationResources/ENG/FloorPlans/" + images[current]);
//            Bitmap bitmap = BitmapFactory.decodeStream(inStream);
//            imageView.setImageBitmap(bitmap);
//        }catch (IOException e){
//            e.printStackTrace();
//        }

        for(int i = 0; i < images.length; i++)
        {
            Log.d("iMessage", "Name: " + images[i]);
        }

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if(current<images.length-1){
                    Log.d("iMessage", "If of up: " + current);
                    current++;
                    Log.d("iMessage", "After ++ of up: " + current);
                    if(current==images.length-1)
                    {
                        up.setVisibility(View.GONE);
                        InputStream inStream = null;
                        try{
                            inStream = assetManager.open("InteriorNavigationResources/ENG/FloorPlans/" + images[current]);
                            Log.d("iMessage", "Up Loading image: " + images[current]);
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                        imageView.setImageBitmap(bitmap);
                    }
                    else
                    {
                        down.setVisibility(View.VISIBLE);
                        InputStream inStream = null;
                        try{
                            inStream = assetManager.open("InteriorNavigationResources/ENG/FloorPlans/" + images[current]);
                            Log.d("iMessage", "Up Loading image: " + images[current]);
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                        imageView.setImageBitmap(bitmap);
                    }
                }
                else
                {
                    Log.d("iMessage", "Else of up: " + current);
                }

            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(current > 0)
                {
                    current--;
                    if(current == 0)
                    {
                        down.setVisibility(View.INVISIBLE);
                        InputStream inStream = null;
                        try{
                            inStream = assetManager.open("InteriorNavigationResources/ENG/FloorPlans/" + images[current]);
                            Log.d("iMessage", "Down Loading image: " + images[current]);

                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                        imageView.setImageBitmap(bitmap);
                    }
                    else
                    {
                        up.setVisibility(View.VISIBLE);
                        InputStream inStream = null;
                        try{
                            inStream = assetManager.open("InteriorNavigationResources/ENG/FloorPlans/" + images[current]);
                            Log.d("iMessage", "Down Loading image: " + images[current]);

                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                        imageView.setImageBitmap(bitmap);
                    }

                }

            }
        });


    }

    private void drawPath(){
        AssetManager assetManager = getAssets();
        InputStream inStream = null;
        try{
            inStream = assetManager.open("InteriorNavigationResources/ENG/FloorPlans/Floor1.png");
        }catch (IOException e){
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inStream);

        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

//        imageView = (ImageView)findViewById(R.id.imageViewFloorPlan);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(15);

        Dijkstra dijkstra = new Dijkstra(graph, graph.getVertex("E102").getLabel());
        Log.d("graphMessage", "Distance to 2: " + dijkstra.getDistanceTo(graph.getVertex("ENG107").getLabel()));
        Log.d("graphMessage", "Path to 2: " + dijkstra.getPathTo(graph.getVertex("ENG107").getLabel()));
        Log.d("graphMessage", "X: " + graph.getVertex("ENG256").getCordinateX() + " Y: " + graph.getVertex("ENG256").getCordinateY());
        LinkedList<Vertex> path = (LinkedList<Vertex>) dijkstra.getPathTo("ENG112");

        for(int i = 0; i < path.size()-1;i++)
        {
            Log.d("graphMessage", "Vertex " + i + ": " + path.get(i));
            canvas.drawLine(path.get(i).getCordinateX(),path.get(i).getCordinateY(),path.get(i+1).getCordinateX(),path.get(i+1).getCordinateY(),paint);

        }
        paint.setARGB(200,70,185,99);
        canvas.drawCircle(path.get(path.size()-1).getCordinateX(),path.get(path.size()-1).getCordinateY(), 20, paint);
        imageView.setImageBitmap(mutableBitmap);
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
            int count = 3;
            while ((line = reader.readLine()) != null) {
                    token = line.split(" ");
                //Log.d("aMessage", "Test: " + line.substring(0,1).compareTo("@"));
                //Log.d("aMessage", "First: " + line.substring(0,1));
                Log.d("sMessage", count + ". Value: " + line.substring(0,1));
                if(line.substring(0,1).compareTo("#") == 0){
                    token[0] = line.substring(1) + ".PNG";          //Append .PNG to grab Floor Plan
                    FloorPlan = token[0];
                    Log.d("aMessage", "Case1 " + "Line " + i +":"+ "FloorPlan: " + FloorPlan);          //Case 1: Floor. Need to open floorplan
                    count++;
                }
                else if(line.substring(0,1).compareTo("@") == 0){
                    token[0] = line.substring(1);
                    NumEdges = Integer.parseInt(line.substring(1));
                    TotalEdges = TotalEdges + NumEdges;
                    TotalSources = TotalSources + 1;
                    Log.d("aMessage","Case2 " + "Line " + i +":"+ "NumEdges: " + NumEdges);          //Case 2: Edge. Defines EdgeNum for next vertex

                    for(int j = 0; j < NumEdges; j++)
                    {
                        line = reader.readLine();

                        token = line.split(" ");
                        Log.d("aMessage","Line read in for @: " + line);
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
                        count++;
                        i++;
                    }
                }
                else if(line.substring(0,1).compareTo("!") == 0)
                {
                    while ((line = reader.readLine()) != null) {
                        Log.d("xyMessage",i + ". Line read for node xy: " + line);
                        token = line.split(" ");
                        try{
                            graph.getVertex(token[0]).setCordinateX(Integer.valueOf(token[1]));
                            graph.getVertex(token[0]).setCordinateY(Integer.valueOf(token[2]));
                        }catch (Exception e)
                        {
                            Log.d("xyMessage",i + "Issue Converting to integar");
                            e.printStackTrace();
                        }


                        count++;
                        i++;
                    }
                    break;
                }
                i++;

            } //End while

            Log.d("aMessage","Total Edges: " + TotalEdges);
            Log.d("aMessage","Total Source Nodes: " + TotalSources);

        } catch(IOException e) {
            Log.d("aMessage", "Exception");
            e.printStackTrace();
        }
    }
}
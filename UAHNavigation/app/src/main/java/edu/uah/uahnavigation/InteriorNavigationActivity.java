package edu.uah.uahnavigation;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;


public class InteriorNavigationActivity extends AppCompatActivity {

    private Graph graph;
    private Dijkstra dijkstra;
    private LinkedList<Vertex> path;
    private ImageView imageView, up, down;
    private String tmpFolderPath;
    private String startingFloor;
    private Button back;

    int current = 0;
    String[] images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_navigation);

        Intent intent = getIntent();
        final String sourceName = intent.getStringExtra("source");
        Log.d("iMessage", "source " + sourceName);
        final String destinationName = intent.getStringExtra("destination");
        Log.d("iMessage", "destination " + destinationName);

        tmpFolderPath = getFilesDir() + "/" + "navigation";
        final AssetManager assetManager = getAssets();

        imageView = (ImageView)findViewById(R.id.imageViewFloorPlan);
        up = (ImageView)findViewById(R.id.imageViewUp);
        down = (ImageView)findViewById(R.id.imageViewDown);
        back = (Button)findViewById(R.id.buttonBack);

        File direct = new File(tmpFolderPath);

        if(!direct.exists()) {
            if(direct.mkdir()); //directory is created;
        }

        graph = new Graph();
        readInputFile();
        dijkstra = new Dijkstra(graph, graph.getVertex(sourceName).getLabel());
        Log.d("graphMessage", "Distance to 2: " + dijkstra.getDistanceTo(graph.getVertex(destinationName).getLabel()));
        Log.d("graphMessage", "Path to 2: " + dijkstra.getPathTo(graph.getVertex(destinationName).getLabel()));
        Log.d("graphMessage", "X: " + graph.getVertex(destinationName).getCordinateX() + " Y: " + graph.getVertex(destinationName).getCordinateY());
        path = (LinkedList<Vertex>) dijkstra.getPathTo(destinationName);


        try{
            images =assetManager.list("InteriorNavigationResources/ENG/FloorPlans");
            Log.d("iMessage", "List " + images.length);
        }catch (Exception e)
        {
            Log.d("iMessage", "Inside list of images catch");
            e.printStackTrace();
        }
        Log.d("dMessage", "d1: ");
        for(int i = 0; i < images.length; i++)
        {
            if(path.get(0).getFloor() ==  images[i])
            {
                current =  i;
            }
        }
        Log.d("dMessage", "d2: ");
        for(int i = 0; i < images.length; i++)
        {
            Log.d("iMessage", "Name: " + images[i]);
            boolean isStairs;
            if( path.get(0).getLabel().startsWith("S"))
            {
                isStairs = true;
            }
            else
            {
                isStairs = false;
            }
            drawPath2(images[i],isStairs);
        }
        Log.d("dMessage", "d3: ");
        try{
            Log.d("dMessage", "Image: " + tmpFolderPath +"/" + images[current]);
            Bitmap bitmap = BitmapFactory.decodeFile(tmpFolderPath +"/" + images[current]);
            imageView.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(current == 0)
        {
            down.setVisibility(View.INVISIBLE);
        }
        else if(current == images.length -1)
        {
            up.setVisibility(View.GONE);
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
                        down.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        down.setVisibility(View.VISIBLE);
                    }

                    Bitmap bitmap = BitmapFactory.decodeFile(tmpFolderPath +"/" + images[current]);
                    Log.d("iMessage", "Up Loading image: " + images[current]);
                    imageView.setImageBitmap(bitmap);

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
                        up.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        up.setVisibility(View.VISIBLE);
                    }
                    Bitmap bitmap = BitmapFactory.decodeFile(tmpFolderPath +"/" + images[current]);
                    Log.d("iMessage", "Else Down Loading image: " + images[current]);
                    imageView.setImageBitmap(bitmap);

                }

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                goBack(v);
            }
        });

//        direct.delete();


    }

    private void drawPath2(String imageName,boolean startStairs){
        AssetManager assetManager = getAssets();
        InputStream inStream = null;
        try{
            inStream = assetManager.open("InteriorNavigationResources/ENG/FloorPlans/"+ imageName);
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

        int sizePath = path.size()-1;
        while( !path.isEmpty())
        {
            int i = 0;
            Log.d("graphMessage", "Vertex " + i + ": " + path.get(i));


            if((!startStairs && path.get(i).getLabel().startsWith("S")) || path.size() == 1)
            {
                paint.setARGB(200,70,185,99);
                canvas.drawCircle(path.get(i).getCordinateX(),path.get(i).getCordinateY(), 20, paint);
                path.remove(i);
                sizePath--;
                break;
            }
            else
            {
                canvas.drawLine(path.get(i).getCordinateX(),path.get(i).getCordinateY(),path.get(i+1).getCordinateX(),path.get(i+1).getCordinateY(),paint);
                path.remove(i);
                sizePath--;
            }

        }


        try {
            OutputStream out = new FileOutputStream(this.tmpFolderPath +"/" + imageName);
            mutableBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
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

        int sizePath = path.size()-1;
        while( !path.isEmpty())
        {
            int i = 0;
            Log.d("graphMessage", "Vertex " + i + ": " + path.get(i));


            if(path.get(i).getLabel().startsWith("S") || path.size() == 1)
            {
                paint.setARGB(200,70,185,99);
                canvas.drawCircle(path.get(i).getCordinateX(),path.get(i).getCordinateY(), 20, paint);
                break;
            }
            else
            {
                canvas.drawLine(path.get(i).getCordinateX(),path.get(i).getCordinateY(),path.get(i+1).getCordinateX(),path.get(i+1).getCordinateY(),paint);
                path.remove(i);
                sizePath--;
            }

        }


        imageView.setImageBitmap(mutableBitmap);

        try {
            OutputStream out = new FileOutputStream(this.tmpFolderPath +"/testing.png");
            mutableBitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
            out.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
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

                        graph.getVertex(SourceNode).setFloor(FloorPlan + ".png");
                        graph.getVertex(DestinationNode).setFloor(FloorPlan + ".png");
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

    public void goBack(View v)
    {
        Intent i = new Intent(getApplicationContext(), testInteriorActivity.class);
        startActivity(i);
        finish();
    }
}
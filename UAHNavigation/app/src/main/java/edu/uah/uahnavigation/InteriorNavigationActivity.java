package edu.uah.uahnavigation;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.TestMethod;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
    private TextView floorName;
    private String sourceName ,destinationName ,buildingName, assetFloorPath,assetImageBasePath;
    private String assetBasePath;
    private boolean graphExist = false;

    int current = 0;
    String[] images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_navigation);

        Intent intent = getIntent();
        try {
            sourceName = intent.getStringExtra("source").toUpperCase();
        } catch (NullPointerException e) {
            new DialogException(this, "Oops! Something went wrong!", "Unable to grab entrance data", new String[]{"Cancel"});
            return;
        }
        Log.d("iMessage", "source " + sourceName);
        try {
            destinationName = intent.getStringExtra("destination").toUpperCase();
        } catch (NullPointerException e) {
            new DialogException(this, "Oops! Something went wrong!", "Unable to grab entrance data", new String[]{"Cancel"});
            return;
        }
        Log.d("iMessage", "destination " + destinationName);
        try {
            buildingName = intent.getStringExtra("building").toUpperCase();
        } catch (NullPointerException e) {
            new DialogException(this, "Oops! Something went wrong!", "Unable to grab entrance data", new String[]{"Cancel"});
            return;
        }
        Log.d("iMessage", "building " + buildingName);

        assetImageBasePath = "InteriorNavigationResources/Images/";
        assetBasePath = "InteriorNavigationResources/Buildings/" + buildingName + "/";
        assetFloorPath = assetBasePath + "FloorPlans";

        tmpFolderPath = getFilesDir() + "/" + "navigation";
        final AssetManager assetManager = getAssets();

        imageView = (ImageView)findViewById(R.id.imageViewFloorPlan);
        up = (ImageView)findViewById(R.id.imageViewUp);
        down = (ImageView)findViewById(R.id.imageViewDown);
        back = (Button)findViewById(R.id.buttonBack);
        floorName = (TextView)findViewById(R.id.textViewFloorName);

        File direct = new File(tmpFolderPath);

        if(!direct.exists()) {
            if(direct.mkdir()); //directory is created;
        }

        graph = new Graph();
        graphExist = readInputFile();

        Log.d("iMessage", "graphExist: " + graphExist);
        if(graphExist)
        {
            try {
                dijkstra = new Dijkstra(graph, graph.getVertex(sourceName).getLabel());
                Log.d("graphMessage", "Distance to 2: " + dijkstra.getDistanceTo(graph.getVertex(destinationName).getLabel()));
                Log.d("graphMessage", "Path to 2: " + dijkstra.getPathTo(graph.getVertex(destinationName).getLabel()));
                Log.d("graphMessage", "X: " + graph.getVertex(destinationName).getCordinateX() + " Y: " + graph.getVertex(destinationName).getCordinateY());
                path = (LinkedList<Vertex>) dijkstra.getPathTo(destinationName);
            } catch (NullPointerException e) {
                new DialogException(this, "Oops! Something went wrong!", "Unable to build floor plan", new String[]{"Cancel"});
                return;
            }
        }

        try{
            images =assetManager.list(assetFloorPath);
            Log.d("iMessage", "List " + images.length);
        }catch (Exception e)
        {
            Log.d("iMessage", "Inside list of images catch");
            e.printStackTrace();
        }
        Log.d("dMessage", "d1: ");
        for(int i = 0; i < images.length; i++)
        {
            Log.d("dMessage", "images[i] " + images[i]);
            if(graphExist)
            {
                if (path.get(0).getFloor().toString().equalsIgnoreCase(images[i])) {
                    current = i;
                    Log.d("dMessage", "Setting current to " + i);
                }
            }
            else
            {
                String flrName = images[i].substring(0, images[i].lastIndexOf('.'));
                String floorNum = flrName.substring(flrName.length() - 1);
                if(floorNum.equalsIgnoreCase("0"))
                {
                    current = 1;
                }
            }

        }
        Log.d("dMessage", "d2: ");
        for(int i = 0; i < images.length; i++)
        {
            Log.d("iMessage", "Name: " + images[i]);
            boolean isStairs = false;
            if(graphExist)
            {
                if (!path.isEmpty()) {
                    if (path.get(0).getLabel().startsWith("S")) {
                        isStairs = true;
                    } else {
                        isStairs = false;
                    }
                }
            }
            Log.d("iMessage", "draw calls: " + i);
            drawPath2(images[i],isStairs);
        }
        Log.d("dMessage", "d3: ");
        Log.d("dMessage", "current: " + current);
        try{
            setFloorName(images[current]);
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
                    setFloorName(images[current]);
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
                    setFloorName(images[current]);
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

    private void setFloorName(String imgName)
    {
        imgName = imgName.substring(0, imgName.lastIndexOf('.'));
        String floorNum = imgName.substring(imgName.length() - 1);
        imgName = imgName.substring(0, imgName.length() - 1);
        imgName = imgName + " "  + floorNum;
        if(floorNum.equalsIgnoreCase("0"))
        {
            floorName.setText(buildingName + ": Basement");
        }
        else
        {
            floorName.setText(buildingName + ": " + imgName);
        }
    }

    private void drawPath2(String imageName,boolean startStairs){
        Log.d("iMessage", "drawPath2 d1: " + imageName + startStairs);
        AssetManager assetManager = getAssets();
        InputStream inStream = null;
        try{
            Log.d("iMessage", "drawPath2 d2");
            inStream = assetManager.open(assetFloorPath + "/"+ imageName);
            Log.d("iMessage", "drawPath2 d3");
        }catch (IOException e){
            Log.d("iMessage", "drawPath2 d2 catch");
            e.printStackTrace();
        }
        Log.d("iMessage", "drawPath2 d4");
        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
        Log.d("iMessage", "drawPath2 d5");
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Log.d("iMessage", "drawPath2 d6");
        Bitmap result = Bitmap.createBitmap(mutableBitmap.getWidth(),mutableBitmap.getHeight(),mutableBitmap.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(mutableBitmap,0f,0f,null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(15);
        paint.setARGB(210, 51, 102, 255);

        if(graphExist)
        {
            if (!path.isEmpty()) {
                if (path.get(0).getFloor().toString().equalsIgnoreCase(imageName)) {
                    int sizePath = path.size() - 1;
                    Log.d("iMessage", "drawPath2 d7 size: " + sizePath);
                    float xCoor = path.get(0).getCordinateX();
                    float yCoor = path.get(0).getCordinateY();

                    Bitmap destBitmap = null;
                    Bitmap sourceBitmap = null;
                    Bitmap stairsBitmap = null;

                    inStream = null;
                    try{
                        Log.d("destMessage", "drawPath2 d2");
                        inStream = assetManager.open(assetImageBasePath + "destinationIcon.png");
                        destBitmap = BitmapFactory.decodeStream(inStream);
                        inStream = null;
                        inStream = assetManager.open(assetImageBasePath + "sourceIcon.png");
                        sourceBitmap = BitmapFactory.decodeStream(inStream);
                        inStream = null;
                        inStream = assetManager.open(assetImageBasePath + "stairsIcon.png");
                        stairsBitmap = BitmapFactory.decodeStream(inStream);
                        Log.d("destMessage", "drawPath2 d3");
                    }catch (IOException e){
                        Log.d("destMessage", "drawPath2 d2 catch");
                        e.printStackTrace();
                    }
                    Log.d("destMessage", "drawPath2 d4");

                    //Draw Legends
                    Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paintText.setColor(Color.BLACK);
                    paintText.setTextSize(15);
                    paintText.setStyle(Paint.Style.FILL);
                    paintText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                    Paint tablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    tablePaint.setColor(Color.BLACK);
                    tablePaint.setStrokeWidth(8);

                    float legendYpadding = 40;
                    float legendXpadding = 50;
                    String legendTitle = "Legends";

                    Rect legendTitleBounds = new Rect();
                    Rect legendDescriptionBounds = new Rect();
                    paintText.getTextBounds("Starting Position", 0 , 17, legendDescriptionBounds);
                    paintText.setTextSize(25);
                    paintText.getTextBounds(legendTitle, 0 , legendTitle.length(), legendTitleBounds);

                    canvas.drawText(legendTitle, (legendDescriptionBounds.width()+sourceBitmap.getWidth())/2, legendYpadding, paintText);
                    canvas.drawBitmap(sourceBitmap, legendXpadding - (sourceBitmap.getWidth()/2), legendYpadding + 10, null);
                    canvas.drawBitmap(stairsBitmap, legendXpadding - (destBitmap.getWidth()/2), sourceBitmap.getHeight() + legendYpadding + 20, null);
                    canvas.drawBitmap(destBitmap, legendXpadding - (destBitmap.getWidth()/2),stairsBitmap.getHeight() + sourceBitmap.getHeight() + legendYpadding + 30, null);

                    paintText.setTextSize(15);
                    canvas.drawText("Starting Position", legendXpadding + 75 - (sourceBitmap.getWidth()/2), (sourceBitmap.getHeight()/2) + legendYpadding + 10, paintText);
                    canvas.drawText("Go Up/Down Stairs", legendXpadding + 75 - (sourceBitmap.getWidth()/2), sourceBitmap.getHeight() + (stairsBitmap.getHeight()/2) + legendYpadding + 20, paintText);
                    canvas.drawText("Destination", legendXpadding + 75 - (sourceBitmap.getWidth()/2), sourceBitmap.getHeight() + stairsBitmap.getHeight() + (destBitmap.getHeight()/2) + legendYpadding + 30, paintText);

                    //Drawing table border
                    float yHeight1 = 0;
                    float xWidth1 = 0;
                    float yHeight2 = 0;
                    float xWidth2 = 0;
                    //Top Border
                    xWidth1 = legendXpadding -5 - (sourceBitmap.getWidth()/2);
                    yHeight1 = legendYpadding - legendTitleBounds.height();
                    xWidth2 = legendXpadding + legendDescriptionBounds.width()+sourceBitmap.getWidth();
                    canvas.drawLine(xWidth1, yHeight1, xWidth2, yHeight1, tablePaint);
                    //Bottom Border
                    xWidth1 = legendXpadding -5 - (sourceBitmap.getWidth()/2);
                    yHeight1 =  legendYpadding + 30 + sourceBitmap.getHeight() + stairsBitmap.getHeight() + destBitmap.getHeight();
                    xWidth2 = legendXpadding + legendDescriptionBounds.width()+sourceBitmap.getWidth();
                    canvas.drawLine(xWidth1, yHeight1, xWidth2, yHeight1, tablePaint);

                    //Left Vertical Border
                    xWidth1 = legendXpadding - 5 - (sourceBitmap.getWidth()/2);
                    yHeight1 = legendYpadding - legendTitleBounds.height() - 4;
                    xWidth2 = legendXpadding -5 - (sourceBitmap.getWidth()/2);
                    yHeight2 = legendYpadding + 30 + sourceBitmap.getHeight() + stairsBitmap.getHeight() + destBitmap.getHeight() + 4;
                    canvas.drawLine(xWidth1, yHeight1,xWidth2 , yHeight2, tablePaint);

                    //Right Vertical Border
                    xWidth1 = legendXpadding + legendDescriptionBounds.width()+sourceBitmap.getWidth();
                    yHeight1 = legendYpadding - legendTitleBounds.height() - 4;
                    yHeight2 = legendYpadding + 30 + sourceBitmap.getHeight() + stairsBitmap.getHeight() + destBitmap.getHeight() + 4;
                    canvas.drawLine(xWidth1, yHeight1,xWidth1 , yHeight2, tablePaint);

                    while (!path.isEmpty()) {
                        int i = 0;
                        Log.d("graphMessage", "Vertex " + i + ": " + path.get(i));


                        if ((!startStairs && path.get(i).getLabel().startsWith("S")) || path.size() == 1) {
                            Log.d("drawMessage", "Ending draw");
                            if(path.size() == 1)
                            {
                                canvas.drawBitmap(destBitmap, path.get(i).getCordinateX() - (destBitmap.getWidth()/2), path.get(i).getCordinateY() - destBitmap.getHeight(), null);
                            }
                            else
                            {
                                canvas.drawBitmap(stairsBitmap, path.get(i).getCordinateX() - (destBitmap.getWidth()/2), path.get(i).getCordinateY() - destBitmap.getHeight(), null);
                            }
                            path.remove(i);
                            sizePath--;
                            break;
                        } else {
                            Log.d("drawMessage", "Drawing Line");
                            canvas.drawLine(path.get(i).getCordinateX(), path.get(i).getCordinateY(), path.get(i + 1).getCordinateX(), path.get(i + 1).getCordinateY(), paint);
                            Log.d("drawMessage", "Vertex1 " + path.get(i).getLabel() + " x: " + path.get(i).getCordinateX() + " y: " + path.get(i).getCordinateY());
                            Log.d("drawMessage", "Vertex2 " + path.get(i + 1).getLabel() + " x: " + path.get(i + 1).getCordinateX() + " y: " + path.get(i + 1).getCordinateY());
                            path.remove(i);
                            sizePath--;
                        }

                    }

                    canvas.drawBitmap(sourceBitmap, xCoor - (sourceBitmap.getWidth()/2), yCoor - sourceBitmap.getHeight(), null);
                }
            } else {
                Log.d("iMessage", "drawPath2 path is empty");
            }

        }
        try {
            Log.d("iMessage", "drawPath2 d8");
            OutputStream out = new FileOutputStream(this.tmpFolderPath +"/" + imageName);
            Log.d("iMessage", "drawPath2 d9");
            result.compress(Bitmap.CompressFormat.PNG, 100, out);
            Log.d("iMessage", "drawPath2 d10");
            out.close();
        }catch (IOException e)
        {
            Log.d("iMessage", "drawPath2 d8 catch");
            e.printStackTrace();
        }
    }

    private boolean readInputFile() {
        int START_LINE = 2;
        AssetManager mngr = getAssets();
        try{
            InputStream in = mngr.open(assetBasePath + "Graph.txt");   //Need to generalize this path
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

                        graph.getVertex(SourceNode).setFloor(FloorPlan);
                        graph.getVertex(DestinationNode).setFloor(FloorPlan);
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

        }
        catch (FileNotFoundException e){
            Log.d("aMessage", "Graph.txt doesn't exist!!");
            return false;
        }
        catch(IOException e) {
            Log.d("aMessage", "Exception");
            e.printStackTrace();
        }

        return true;
    }

    public void goBack(View v)
    {
        Intent i = new Intent(getApplicationContext(), testInteriorActivity.class);
        startActivity(i);
        finish();
    }
}
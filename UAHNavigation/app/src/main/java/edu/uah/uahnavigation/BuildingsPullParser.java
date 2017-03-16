package edu.uah.uahnavigation;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.assist.AssistStructure;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import edu.uah.model.Buildings;
import android.content.res.AssetManager;

import java.util.Properties;

public class BuildingsPullParser {

	private static final String LOGTAG = "QWER";
	
	private static final String BUILDING_ID = "buildingId";
	private static final String BUILDING_NAME = "buildingName";
	private static final String BUILDING_DESC = "description";
	private static final String BUILDING_ADDRESS = "address";
	private static final String BUILDING_IMAGE = "image";
	
	private Buildings currentBuilding  = null;
	private String currentTag = null;
	List<Buildings> building = new ArrayList<Buildings>();

	public List<Buildings> parseXML(Context context) {

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();

			AssetManager mngr = context.getAssets();
			try {
				InputStream stream = mngr.open("buildings.xml");
				xpp.setInput(stream, null);
			} catch (final IOException e) {
				e.printStackTrace();
			}


			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					handleStartTag(xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					currentTag = null;
				} else if (eventType == XmlPullParser.TEXT) {
					handleText(xpp.getText(), context);
				}
				eventType = xpp.next();
			}

		} catch (NotFoundException e) {
			Log.d(LOGTAG, e.getMessage());
		} catch (XmlPullParserException e) {
			Log.d(LOGTAG, e.getMessage());
		} catch (IOException e) {
			Log.d(LOGTAG, e.getMessage());
		}

		return building;
	}

	private void handleText(String text, Context context) throws IOException {
		String xmlText = text;
		if (currentBuilding != null && currentTag != null) {
			if (currentTag.equals(BUILDING_ID)) {
				Integer id = Integer.parseInt(xmlText);
				currentBuilding.setId(id);
			} 
			else if (currentTag.equals(BUILDING_NAME)) {
				currentBuilding.setName(xmlText);
			}
			else if (currentTag.equals(BUILDING_DESC)) {
				currentBuilding.setDescription(xmlText);
			}
			else if (currentTag.equals(BUILDING_ADDRESS)) {
				currentBuilding.setAddress(xmlText);
			}
		}
	}

	private void handleStartTag(String name) {
		if (name.equals("building")) {
			currentBuilding = new Buildings();
			building.add(currentBuilding);
		}
		else {
			currentTag = name;
		}
	}
}

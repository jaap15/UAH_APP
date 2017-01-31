package edu.uah.uahnavigation;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.uah.model.Buildings;
import edu.uah.model.Rooms;
import edu.uah.uahnavigation.DatabaseSource;

public class RoomsPullParser {

	private static final String LOGTAG = "QWER";

	private static final String BUILDING_ID = "buildingId";
	private static final String ROOM_NUM = "roomNum";

	private Rooms currentRoom  = null;
	private String currentTag = null;
	List<Rooms> room = new ArrayList<Rooms>();

	List<Buildings> buildings;
	DatabaseSource dbSource;

	public List<Rooms> parseXML(Context context) {

		dbSource = new DatabaseSource(context);
		dbSource.open();

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			
			InputStream stream = context.getResources().openRawResource(R.raw.rooms);
			xpp.setInput(stream, null);

			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					handleStartTag(xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					currentTag = null;
				} else if (eventType == XmlPullParser.TEXT) {
					handleText(xpp.getText());
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

		return room;
	}

	private void handleText(String text) {
		String xmlText = text;
		if (currentTag != null) {
			if (currentTag.equals(BUILDING_ID)) {
				String[] selectArgs = new String[] {xmlText};
				buildings = dbSource.GetFromBuildings("description=?", selectArgs, null);
			}
			else if (currentTag.equals(ROOM_NUM)) {
				currentRoom = new Rooms();
				room.add(currentRoom);
				currentRoom.setRoom(xmlText);
				currentRoom.setBuilding(buildings.get(0).getId());
			}
		}
	}

	private void handleStartTag(String name) {
		if (name.equals("building")) {

		}
		else {
			currentTag = name;
		}
	}
}

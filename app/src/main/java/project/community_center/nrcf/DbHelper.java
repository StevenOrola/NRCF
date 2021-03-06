package project.community_center.nrcf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * contains seeded centre data
 * has helper search functions such as
 * searching all centres, searching for centres
 * that offer a program based on user input
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Helper.sqlite";
    private static final int DB_VERSION = 2;
    private Context context;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        updateMyDatabase(sqLiteDatabase, i, i1);
    }


    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL(getCenterTableSql());
            db.execSQL(getActivityTableSql());

            for (Center c : list) {
                populateDB(db, c);
            }
        }
    }

    /**
     * creates a Centre table using SQL
     */
    private String getCenterTableSql() {
        String sql = "";
        sql += "CREATE TABLE Centre (";
        sql += "_centreId INTEGER PRIMARY KEY, ";
        sql += "NAME TEXT, ";
        sql += "ADDRESS TEXT, ";
        sql += "PHONE TEXT, ";
        sql += "WEBSITE TEXT, ";
        sql += "ACTIVITIES TEXT, ";
        sql += "HOURS TEXT, ";
        sql += "PICTURE INTEGER);";

        return sql;
    }

    /**
     * creates an Activity table in SQL
     * A Centre can have multiple Activities
     */
    private String getActivityTableSql() {
        String sql = "";
        sql += "CREATE TABLE Activity (";
        sql += "_activityId INTEGER PRIMARY KEY, ";
        sql += "NAME TEXT, ";
        sql += "_centreId INTEGER, ";
        sql += "FOREIGN KEY (_centreId) REFERENCES Centre (_centreId));";
        return sql;
    }

    /**
     * Populate the database with seeded data.
     */
    public void populateDB (SQLiteDatabase db, Center center) {
        ContentValues centerValues = new ContentValues();
        centerValues.put("Name", center.getName());
        centerValues.put("Address", center.getAddress());
        centerValues.put("Phone", center.getPhone());
        centerValues.put("Website", center.getWebsite());
        centerValues.put("Activities", center.getActivities());
        centerValues.put("Hours", center.getHours());
        centerValues.put("Picture", center.getPicture());

        int id = (int) db.insert("Centre", null, centerValues);

        ContentValues actValues = null;
        if (center.getName().equalsIgnoreCase("New Westminster Youth Centre")) {
            for (String string: newWestAct) {
                actValues = new ContentValues();
                actValues.put("Name", string);
                actValues.put("_centreId", id);
                db.insert("Activity", null, actValues);
            }
        } else if (center.getName().equalsIgnoreCase("Queensborough Community Centre")) {
            for (String string: queensCommAct) {
                actValues = new ContentValues();
                actValues.put("Name", string);
                actValues.put("_centreId", id);
                db.insert("Activity", null, actValues);
            }
        } else if (center.getName().equalsIgnoreCase("Queens Park Arena")) {
            for (String string: queensParkAct) {
                actValues = new ContentValues();
                actValues.put("Name", string);
                actValues.put("_centreId", id);
                db.insert("Activity", null, actValues);
            }
        } else if (center.getName().equalsIgnoreCase("Moody Park Arena")) {
            for (String string: moodyAct) {
                actValues = new ContentValues();
                actValues.put("Name", string);
                actValues.put("_centreId", id);
                db.insert("Activity", null, actValues);
            }
        } else if (center.getName().equalsIgnoreCase("Century House")) {
            for (String string: centuryAct) {
                actValues = new ContentValues();
                actValues.put("Name", string);
                actValues.put("_centreId", id);
                db.insert("Activity", null, actValues);
            }
        } else if (center.getName().equalsIgnoreCase("Canada Games Pool")) {
            for (String string: canadaAct) {
                actValues = new ContentValues();
                actValues.put("Name", string);
                actValues.put("_centreId", id);
                db.insert("Activity", null, actValues);
            }
        } else if (center.getName().equalsIgnoreCase("Centennial Community Centre")) {
            for (String string: centennialAct) {
                actValues = new ContentValues();
                actValues.put("Name", string);
                actValues.put("_centreId", id);
                db.insert("Activity", null, actValues);
            }
        }

    }

    /**
     * get a single Centre based on the name parameter
     */
    public Center getCenter(String name) {
        Center center = null;
        String selectQuery = "SELECT * FROM Centre WHERE Name = '" + name + "'";
        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                Center c = new Center(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7)
                );
                center = c;
            } while (cursor.moveToNext());
        }
        return center;
    }

    /**
     * get and return all centres
     */
    public ArrayList<Center> getCenters() {
        ArrayList<Center> centers = new ArrayList<Center>();
        String selectQuery = "SELECT * FROM Centre";
        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Center c = new Center(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7)
                );
                centers.add(c);
            } while (cursor.moveToNext());
        }

        return centers;
    }

    /**
     * return centres that offer that user-inputted program
     * eg. user inputs fitness, return all centres that offer fitness
     */
    public ArrayList<Center> getSearch(String name) {
        ArrayList<Center> searchList = new ArrayList<Center>();
        String selectQuery = "SELECT * FROM Centre INNER JOIN Activity ON" +
                " Centre._centreId = Activity._centreId WHERE Activity.Name LIKE '%" +
                name + "%'";
        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Center c = new Center(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getInt(7)
                );
                searchList.add(c);
            } while (cursor.moveToNext());
        }
        return searchList;
    }

    /**
     * seeded data of centres
     */
    public static final Center[] list = {
           new Center("New Westminster Youth Centre", "620 Eighth Street V3M 3S2",
                    "604-515-3801", "http://www.newwestpcr.ca/recreation/youth_centre.php",
                    "Drop-in Basketball, Cooking Program, Computer lab, Fitness Facility, Pool Table, Multi-Purpose Room",
                    "Monday-Thursday: \n 3:15 pm - 9:00 pm, Friday-Saturday, 3:15pm-11:00pm",
                    R.drawable.youthcentre),
            new Center("Queensborough Community Centre", "920 Ewen Avenue V3M 5C8",
                    "604-527-7388",   "https://www.newwestcity.ca/parks-and-recreation/facilities/queensborough-community-centre",
                    "Library, Fitness Classes, Weight Room, Kolumbia Inn Daycare",
                    "Monday-Friday, 7:00 am - 9:30 pm, Saturday, 8:30 am - 5:30 pm, Sunday, 8:30 am - 8:30 pm",
                    R.drawable.queensborough),
            new Center("Queens Park Arena", "3rd Ave",
                    "604-777-5111", "https://www.newwestcity.ca/parks-and-recreation/facilities/queens-park-arena",
                    "Drop-In Hockey, Lacrosse, Ice Rental",
                    "Weekdays, 9:00 am - 4:00 pm",
                    R.drawable.queenpark),
            new Center("Moody Park Arena", "701 Eighth Avenue V3M 2R2",
                    "604-525-5301", "https://www.newwestcity.ca/parks-and-recreation/facilities/moody-park-arena",
                    " Ice Skating, Lacrosse, Sales, Skating",
                    "Monday 8:30 am - 8:30 pm, Tuesday - Friday 8:30 am - 4:30 pm, Sunday 11:30 am - 3:30 pm",
                    R.drawable.moodypark),
            new Center("Century House", "620 Eighth Avenue V3M 2S2",
                    "604-519-1066", "https://www.newwestcity.ca/parks-and-recreation/facilities/century-house",
                    "Senior Activities, Exercise Room, Food Services, Peer Counselling ",
                    "Weekdays 9:00 am - 9:00 pm, Saturday 9:00 am - 4:00 pm, Sunday 12:00 pm - 4:00 pm",
                    R.drawable.centuryhouse),
            new Center("Centennial Community Centre", "65 East Sixth V3l 4G6",
                    "604-777-5100", "https://www.newwestcity.ca/parks-and-recreation/facilities/centennial-community-centre",
                    "Arts & Crafts, Cooking, Dance, Day Camps, Judo, Sports, Yoga, Fitness, " +
                            "Indoor Cycling, Childminding, Music",
                    "Monday-Thursday 9:00 am - 8:30 pm, Friday 9:00 am - 8:00 pm, Weekends 8:30 am - 1:00 pm",
                    R.drawable.centennial),
            new Center("Canada Games Pool", "65 East Sixth V3l 4G6",
                    "604-526-4281", "https://www.newwestcity.ca/parks-and-recreation/facilities/canada-games-pool",
                    "Swimming, Diving, Fitness, Aquafit, Green Thunder Waterslide",
                    "Weekdays 5:30 am - 9:30 pm, Saturday 8:00 am - 8:00 pm, Sunday 8:00 am - 9:30 pm",
                    R.drawable.canadagames)
    };

    /**
     * all activities of each centre
     */
    private static final String[] newWestAct = {
            "basketball", "cooking", "computer", "fitness", "pool table", "multi-purpose room"
    };

    private static final String[] queensCommAct = {
            "library", "fitness", "weight room", "daycare"
    };

    private static final String[] queensParkAct = {
            "hockey", "lacrosse", "ice rentals"
    };

    private static final String[] moodyAct = {
            "ice skating", "lacrosse", "sales", "skating"
    };

    private static final String[] centuryAct = {
            "senior activities", "exercise room", "food services", "peer counselling"
    };

    private static final String[] centennialAct = {
            "arts & crafts", "cooking", "dance", "day camp", "judo",
            "yoga", "fitness", "indoor cycling", "childminding", "music"
    };

    private static final String[] canadaAct = {
            "swimming", "diving", "fitness", "aquafit", "waterslide"
    };

}

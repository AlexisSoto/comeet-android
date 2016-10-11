package today.comeet.android.comeet.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import today.comeet.android.comeet.helper.DBHelper;
import today.comeet.android.comeet.activity.MainActivity;

/**
 * Created by Mika on 10/11/16.
 */

public class EventContentProvider extends ContentProvider {
//:::::::::::::::::::::::::://
//:: URI d'exposition
//:::::::::::::::::::::::::://
    private static String test  = MainActivity.PACKAGE_NAME;

    public static final Uri CONTENT_URL = Uri.parse("content://"+ MainActivity.PACKAGE_NAME);

// Constantes pour identifier les requetes
    private static final int ALLROWS​ = 1;
    private static final int SINGLE_ROW​ = 2;
// Uri matcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher((UriMatcher.NO_MATCH));
        uriMatcher.addURI(MainActivity.PACKAGE_NAME,"elements",ALLROWS​);
        uriMatcher.addURI(MainActivity.PACKAGE_NAME,"elements/#",SINGLE_ROW​);
    }

    private DBHelper myDBHelper;

    @Override
    public boolean onCreate() {
        myDBHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // SQLiteDatabase db = myDBHelper.
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

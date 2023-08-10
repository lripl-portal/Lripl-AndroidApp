package com.lripl.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import android.content.Context;
import android.util.Log;

import com.lripl.daos.ItemTypeDao;
import com.lripl.daos.ItemsDao;
import com.lripl.daos.OrderDao;
import com.lripl.daos.OrderItemDao;
import com.lripl.daos.ProductsDao;
import com.lripl.daos.StatesDao;
import com.lripl.daos.UserDao;
import com.lripl.daos.ZonesDao;
import com.lripl.entities.ItemType;
import com.lripl.entities.Items;
import com.lripl.entities.OrderItem;
import com.lripl.entities.Orders;
import com.lripl.entities.Products;
import com.lripl.entities.States;
import com.lripl.entities.Users;
import com.lripl.entities.Zones;
import com.lripl.utils.BrandConverter;
import com.lripl.utils.DateTypeConverter;

@Database(entities = {Users.class, Items.class, ItemType.class,States.class, Zones.class, Products.class, Orders.class, OrderItem.class},version = 4, exportSchema = false)
@TypeConverters({DateTypeConverter.class, BrandConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "LriplDB";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME).addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract UserDao userDao();
    public abstract ZonesDao zonesDao();
    public abstract StatesDao statesDao();
    public abstract ItemTypeDao itemTypeDao();
    public abstract ItemsDao itemsDao();
    public abstract ProductsDao productsDao();
    public abstract OrderDao ordersDao();
    public abstract OrderItemDao orderItemDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE item_types "
                    + " ADD COLUMN imageurl VARCHAR");
            database.execSQL("ALTER TABLE items "
                    + " ADD COLUMN imageurl VARCHAR");

        }
    };
    static final Migration MIGRATION_2_3 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE item_types "
                    + " ADD COLUMN imageurl VARCHAR");
            database.execSQL("ALTER TABLE items "
                    + " ADD COLUMN imageurl VARCHAR");

        }
    };

}

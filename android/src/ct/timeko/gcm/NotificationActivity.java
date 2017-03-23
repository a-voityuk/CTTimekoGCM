package ct.timeko.gcm;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;

import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.KrollDict;

import java.util.HashMap;

public class NotificationActivity extends Activity {

    private static final String TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            Intent intent = getIntent();
            HashMap<String, Object> data = (HashMap)intent.getSerializableExtra(CttimekogcmModule.NTF_KEY_DATA);
            CttimekogcmModule module = CttimekogcmModule.getInstance();

            if(module != null) {
                module.fireMessage(data, false);
            } else {
                KrollDict kdata = new KrollDict(data);

                TiApplication.getInstance().getAppProperties().setString(CttimekogcmModule.PROPERTY_PENDING_DATA, kdata.toString());
                Log.d(TAG, "Saving data in props: " + kdata.toString());
            }
        } catch (Exception e) {
            Log.d(TAG, "Couldn't send fireMessage from NotificationActivity");
        }
        try {
            if (TiApplication.getAppCurrentActivity() == null) {
                TiApplication tiApp = TiApplication.getInstance();
                String tiPackageName = tiApp.getPackageName();
                String mainClassName = tiApp.getPackageManager().getLaunchIntentForPackage(tiPackageName).getComponent().getClassName();

                Intent mainActivityIntent = new Intent();
                mainActivityIntent.setClassName(tiPackageName, mainClassName);
                mainActivityIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                mainActivityIntent.setAction(Intent.ACTION_MAIN);
                mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(mainActivityIntent);
        	}
        } catch (Exception e) {
            Log.d(TAG, "Couldn't start intent from NotificationActivity");
        }

        finish();
    }
}

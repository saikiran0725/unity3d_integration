package timothypaetz.com.recyclersectionheader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Main activity. Initial state of app.
 */
class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG: String = "UDemo"
        const val EXTRAS_LEVEL_NAME: String = "LEVEL_NAME"
    }

    /**
     * Starts Unity player with default state.
     */
    fun openUnity() {
        if (UnityPlayerOverrideActivity.instance != null) {
            showToast("Unity already started!")
            Log.w(TAG, "Unity player already launched")
            return;
        }
        // Here create Intent with Unity Player Activity
        val intent = Intent(this, UnityPlayerOverrideActivity::class.java).apply {
            // Here add messages to Unity Player Activity
            // putExtra(MESSAGE_NAME, message_value_with_various_types)
        }
        // And start activity with specified intent
        startActivity(intent)
    }

    /**
     * Starts Unity player (if not started yet) and calls method to load specified scene.
     * @param sceneName Scene name to load in Unity3D.
     */
    fun openScene(sceneName: String) {
        // Here create Intent with Unity Player Activity
        val intent = Intent(this, UnityPlayerOverrideActivity::class.java).apply {
            // Here add messages to Unity Player Activity
            putExtra(EXTRAS_LEVEL_NAME, sceneName)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        // And start activity with specified intent
        startActivity(intent)

        // Use fragments only for this activity, do not use Unity viewport and its layout here
        //val newFragment = UFragment()
        //supportFragmentManager.beginTransaction().add(R.id.container, newFragment).commit()
    }

    /**
     * Unloads Unity player (if loaded).
     */
    fun closeUnity() {
        if (UnityPlayerOverrideActivity.instance != null) {
            UnityPlayerOverrideActivity.instance!!.finish()
        } else {
            showToast("Start Unity scene first!")
            Log.w(TAG, "Start Unity scene first!")
        }
    }

    /**
     * Just shows a toasts with specified text for debug.
     * @param message Text to show.
     */
    fun showToast(message: String) {
        val text: CharSequence = message
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // If an toolbar added to layout don't forget to configure it properly
        //val toolbar: Toolbar = findViewById(R.id.toolbar)
        //setSupportActionBar(toolbar)

        // Keep unity player in separate activity with own lifecycle,
        // Use this launcher class just to stat player, pass parameters, call methods, receive messages
        val btn = findViewById<TextView>(R.id.tv)
        btn.setOnClickListener {
            openScene("Magnetic_Field_Solenoid");
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Parse intent and get bundle with extra data here
        Log.i(TAG, "onNewIntent Main activity")
    }
}
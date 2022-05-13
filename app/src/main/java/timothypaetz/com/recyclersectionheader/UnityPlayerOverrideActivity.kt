package timothypaetz.com.recyclersectionheader

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.Button
import android.widget.FrameLayout
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity

/**
 * Override Unity Player Activity class.
 */
class UnityPlayerOverrideActivity : UnityPlayerActivity() {
    companion object {
        @JvmStatic var instance: UnityPlayerOverrideActivity? = null
        const val TAG = "UDemo"
    }

    fun getUnityFrameLayout(): FrameLayout? {
        return mUnityPlayer
    }

    fun showMainActivity(setToColor: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        intent.apply {
            putExtra("SOME_EXTRA_FIELD", "VALUE")
        }
        startActivity(intent)
    }

    fun modifyLayout() {
        val frameLayout = getUnityFrameLayout() ?: return

        val btnShowMain = Button(this)
        btnShowMain.text = "Show Main"
        btnShowMain.x = 10f
        btnShowMain.y = 500f
        btnShowMain.setOnClickListener { showMainActivity("") }
        frameLayout.addView(btnShowMain, 300, 200)

        val btnSendMsg = Button(this)
        btnSendMsg.text = "Send Msg"
        btnSendMsg.x = 320f
        btnSendMsg.y = 500f
        btnSendMsg.setOnClickListener { sendMessage("Cube", "ChangeColor", "yellow") }
        frameLayout.addView(btnSendMsg, 300, 200)

        val btnBack = Button(this)
        btnBack.text = "Unload"
        btnBack.x = 630f
        btnBack.y = 500f
        btnBack.setOnClickListener { finish() }
        frameLayout.addView(btnBack, 300, 200)
    }

    /**
     * Sends message to unity custom method to load specified scene.
     * @param sceneName Name of the scene to load.
     */
    fun loadScene(sceneName: String?) {
        if (sceneName == null) return
        sendMessage(
            "AndroidNativeBridge",
            "SetAddressableAssetName",
            sceneName
        )
    }

    /**
     * Sends message to unity player to specified object.
     * @param gameObj Name of game object in current player state.
     * @param method Method name in game object scripts
     * @param arg Arguments to method. Change type for your needs.
     */
    fun sendMessage(gameObj: String, method: String, arg: String) {
        UnityPlayer.UnitySendMessage(gameObj, method, arg)
    }

    // region Unity player lifecycle

    override fun updateUnityCommandLineArguments(cmdLine: String?): String? {
        return super.updateUnityCommandLineArguments(cmdLine)
    }

    // Setup activity layout
    override fun onCreate(savedInstanceState: Bundle?) {
        // First wait to create Unity player
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate Player Activity")
        instance = this

        // Parse extra data
        val bundle = intent.extras
        val levelName = bundle?.getString("LEVEL_NAME")

        // Update view
        modifyLayout()

        // Then send messages to player

        //SetLinkToCDN
        sendMessage(
            "AndroidNativeBridge",
            "SetLinkToCDN",
            "https://storage.googleapis.com/example_bucket_unity/ServerData"
        )

        if (levelName != null) {
            sendMessage(
                "AndroidNativeBridge",
                "SetAddressableAssetName",
                levelName//"Magnetic_Field_Solenoid"
            )
        }
    }

    // When Unity player unloaded move task to background
    override fun onUnityPlayerUnloaded() {
        super.onUnityPlayerUnloaded()
    }

    // When Unity player quited kill process
    override fun onUnityPlayerQuitted() {
        super.onUnityPlayerQuitted()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Parse intent with extra data from Main Activity here
        Log.i(TAG, "onNewIntent Player Activity")
    }

    // Quit Unity
    override fun onDestroy() {
        instance = null
        super.onDestroy()
        Log.i(TAG, "onDestroy Player Activity")
    }

    // Pause Unity
    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause Player Activity")
    }

    // Resume Unity
    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume Player Activity")
    }

    // Low Memory Unity
    override fun onLowMemory() {
        super.onLowMemory()
    }

    // Trim Memory Unity
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    // This ensures the layout will be correct.
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    // Notify Unity of the focus change.
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return super.dispatchKeyEvent(event)
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyUp(keyCode, event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        return super.onGenericMotionEvent(event)
    }

    // endregion
}
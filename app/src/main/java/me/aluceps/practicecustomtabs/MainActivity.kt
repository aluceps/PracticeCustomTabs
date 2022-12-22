package me.aluceps.practicecustomtabs

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import me.aluceps.practicecustomtabs.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {
        binding.action.setOnClickListener {
            val url = binding.textUrl.text?.toString()
            if (url.isNullOrEmpty()) return@setOnClickListener

            try {
                getCustomTabsPackages().forEach {
                    launchCustomTabs(it, Uri.parse(url))
                }
            } catch (e: Exception) {
                Log.e(MainActivity::class.simpleName, "message: ${e.message}")
            }
        }
    }

    /**
     * see: https://developer.chrome.com/docs/android/custom-tabs/integration-guide/#how-can-i-check-whether-the-android-device-has-a-browser-that-supports-custom-tab
     */
    private fun Context.getCustomTabsPackages(): List<ResolveInfo> {
        val activityIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = Uri.fromParts("http", "", null)
        }
        return packageManager.queryIntentActivities(activityIntent, 0)
            .mapNotNull {
                val serviceIntent = Intent().apply {
                    action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
                    `package` = it.activityInfo.packageName
                }
                return@mapNotNull if (packageManager.resolveService(serviceIntent, 0) != null) {
                    it
                } else {
                    null
                }
            }.toList()
    }

    private fun Context.launchCustomTabs(resolveInfo: ResolveInfo, url: Uri) {
        CustomTabsIntent.Builder()
            .build()
            .apply {
                intent.setPackage(resolveInfo.activityInfo.packageName)
            }
            .launchUrl(this, url)
    }
}

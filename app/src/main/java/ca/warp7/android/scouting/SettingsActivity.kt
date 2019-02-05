package ca.warp7.android.scouting

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

/**
 * @since v0.4.2
 */

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.apply {
            title = "Scouting App Options"
            setDisplayHomeAsUpEnabled(true)
        }
        supportFragmentManager.beginTransaction().add(
            R.id.settings_frame,
            SettingsFragment()
        ).commit()
        setTheme(R.style.AppTheme)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

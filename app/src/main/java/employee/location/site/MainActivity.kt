package employee.location.site

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import employee.location.site.databinding.ActivityMainBinding
import employee.location.site.utils.Constants
import employee.location.site.utils.PreferenceUtils
import java.util.*

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var preferenceUtils: PreferenceUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        preferenceUtils = PreferenceUtils(this.applicationContext)
        setUpLanguage()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setUpToolBar()
    }

    private fun setUpLanguage() {
        val languageItem = preferenceUtils.get(Constants.LANGUAGE, Constants.LANGUAGE_ENGLISH) as Int
        when(languageItem){
            Constants.LANGUAGE_ENGLISH -> {
                val language = "en"
                setLocale(language)
            }
            Constants.LANGUAGE_ARABIC -> {
                val language = "ar"
                setLocale(language)
            }
            Constants.LANGUAGE_URDU -> {
                val language = "ur"
                setLocale(language)
            }
        }
    }

    private fun setUpToolBar() {
        this.setSupportActionBar(binding.toolBar)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setLocale(language: String) {

        val config = resources.configuration
        val locale = Locale(language)
        Locale.setDefault(locale)
        config.setLocale(locale)
        applicationContext.createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

    }

}
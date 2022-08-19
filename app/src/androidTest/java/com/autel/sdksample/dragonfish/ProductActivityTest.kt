package com.autel.sdksample.dragonfish

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import com.autel.sdksample.ProductActivity
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductActivityTest {

    val scenario: ActivityScenario<ProductActivity> = launchActivity<ProductActivity>()

    @After
    fun cleanup() {
        scenario.close()
    }


    @Test
    fun testLaunchWithIntent(bundle: Bundle) {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), ProductActivity::class.java)
        intent.putExtras(bundle)
        // We can pass something to our activity in intent for testing if required
        scenario.use {

        }
    }

    @Test
    fun moveToState(state: Lifecycle.State) {
        scenario.use { scenario ->
            scenario.moveToState(state)
        }
    }


    @Test
    fun getCurrentState(): Lifecycle.State {
        return scenario.use { scenario ->
            scenario.state
        }
    }

    @Test
    fun recreateActivity() {
        scenario.use { scenario ->
            scenario.recreate()
        }
    }

    @Test
    fun testOnClickEvent(id: Int) {
        scenario.use {
            onView(withId(id)).perform(click())
        }
    }


}
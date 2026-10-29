package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.NextGenRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("NextGen Zambia", appName)
  }

  @Test
  fun `verify Next Gen Developers Zambia charter contents`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = NextGenRepository(context)

    // Verify 7 objectives
    assertEquals(7, repository.objectives.size)
    assertEquals("Solve Real-World Problems", repository.objectives[0].title)

    // Verify 6 core values
    assertEquals(6, repository.coreValues.size)
    assertTrue(repository.coreValues.any { it.title == "Innovation" })
    assertTrue(repository.coreValues.any { it.title == "Problem-Solving" })

    // Verify commitment contains the motto
    assertTrue(repository.commitmentText.contains("We think, we create, and we solve"))
  }
}

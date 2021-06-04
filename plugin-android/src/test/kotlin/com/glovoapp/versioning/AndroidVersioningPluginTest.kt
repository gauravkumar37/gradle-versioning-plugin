package com.glovoapp.versioning

import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.File

class AndroidVersioningPluginTest {

    @JvmField
    @RegisterExtension
    val gradle = GradleBuildExtension {
        buildFile {
            """
            plugins {
                id("com.android.application")
                id("com.glovoapp.android-versioning")
            }
            
            android {
                compileSdkVersion(28)
            }
            """.trimIndent()
        }

        File(root, "src/main/AndroidManifest.xml").apply {
            parentFile.mkdirs()
            writeText("<manifest package=\"test.app\"/>")
        }
    }

    @Test
    fun incrementVersionCode() = with(gradle) {
        versionFile("versionCode" to "14")

        val result = runner.withArguments("-s", "incrementVersionCode").build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":incrementVersionCode")?.outcome)
        assertTrue("versionCode=15" in versionFile.readLines())
    }

    @Test
    fun incrementVersionName() = with(gradle) {
        versionFile("versionName" to "0.1.2")

        val result = runner.withArguments("-s", "incrementVersionName").build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":incrementVersionName")?.outcome)
        assertTrue("versionName=0.1.3" in versionFile.readLines())
    }

}

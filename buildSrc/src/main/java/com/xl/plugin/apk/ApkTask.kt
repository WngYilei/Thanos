package com.xl.plugin.apk

import com.android.build.api.artifact.Artifacts
import com.android.build.api.artifact.SingleArtifact
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import com.android.build.api.variant.BuiltArtifactsLoader
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import java.io.File
abstract class ApkTask : DefaultTask() {

    @get:InputDirectory
    abstract val inputApk: DirectoryProperty

    @get:OutputDirectories
    abstract val outputApk: DirectoryProperty


    @TaskAction
    fun taskAction() {
        println("---------执行apk-task-----------")
        val apks = File(inputApk.get().toString())
        apks.listFiles()?.forEach {
            if (it.name.endsWith(".apk")) {
                val file = File(outputApk.get().toString() + "/out.apk")
                if (!file.exists()) file.createNewFile()
                file.writeBytes(it.readBytes())
            }
        }
    }

}
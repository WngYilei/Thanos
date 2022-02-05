package com.xl.plugin.lib

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class LibTask : DefaultTask() {

    @TaskAction
    fun run() {
        val path = "${project.rootDir}/app/build/intermediates/merged_jni_libs/debug/out"
        println("path:${path}")
        val file = File(path)
        println("文件是否存在:${file.exists()}")
        file.listFiles()?.forEach {
            println(it.name)
            if (it.name.equals("armeabi-v7a")) it.delete()
        }
    }
}
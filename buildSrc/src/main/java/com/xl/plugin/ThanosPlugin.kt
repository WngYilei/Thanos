package com.xl.plugin


import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import com.xl.plugin.lib.LibTask

class ThanosPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        val libTask   = project.tasks.register("libTask",LibTask::class.java)

        var taskRelease: TaskProvider<ThanosTask>? = null
        var taskDebug: TaskProvider<ThanosTask>? = null
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants {

            if (it.name == "debug") {
                taskDebug = project.tasks.register(
                    "AThanosTaskDebug", ThanosTask::class.java
                ) {
                    group = "thanos"
                    variantName = it.name
                }
            } else if (it.name == "release") {
                taskRelease = project.tasks.register(
                    "AThanosTaskRelease", ThanosTask::class.java
                ) {
                    group = "thanos"
                    variantName = it.name
                }
            }

        }
        project.afterEvaluate {

            taskDebug?.get()?.dependsOn("processDebugResources")
            project.tasks.getByName("mergeDebugJavaResource").dependsOn(taskDebug)


            taskRelease?.get()?.dependsOn("processReleaseResources")
            project.tasks.getByName("mergeReleaseJavaResource").dependsOn(taskRelease)

            libTask.get().dependsOn("mergeDebugJniLibFolders")
            project.tasks.getByName("mergeDebugNativeDebugMetadata").dependsOn(libTask)
        }

    }
}

fun String.toFirstUp(): String {
    if (this.isEmpty()) return ""
    val stringBuilder = StringBuffer()
    stringBuilder.append(this[0].toUpperCase())
    for (i in 1 until this.length)
        stringBuilder.append(this[i])
    return stringBuilder.toString()
}
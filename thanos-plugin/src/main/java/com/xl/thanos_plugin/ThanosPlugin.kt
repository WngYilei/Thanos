package com.xl.thanos_plugin

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider


class ThanosPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        var taskDebug: TaskProvider<ThanosTask>? = null
        var taskRelease: TaskProvider<ThanosTask>? = null
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
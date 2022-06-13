package com.xl.plugin


import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

class ThanosPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants {

            TaskFactory.createThanosTask(project, it.name.toFirstUp(), "Thanos")
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
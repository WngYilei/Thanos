package com.xl.plugin.apk

import com.android.build.api.artifact.SingleArtifact

import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class ApkPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants() { variant ->

            val apkProducer = project.tasks.register("apk${variant.name}Task", ApkTask::class.java)
            variant.artifacts.use(apkProducer).wiredWithDirectories(
                ApkTask::inputApk,
                ApkTask::outputApk
            ).toTransform(SingleArtifact.APK)

        }
    }
}

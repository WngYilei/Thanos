package com.xl.plugin
import org.gradle.api.Project
object TaskFactory {

    fun  createThanosTask(
        project: Project,
        variant: String,
        taskName: String = "Thanos",
        taskGroup: String = "Thanos"
    ) {
        val task = project.tasks.register("${taskName}${variant}", ThanosTask::class.java) {
            group = taskGroup
            variantName = variant
        }

        project.afterEvaluate {
            task.get().dependsOn("process${variant}Resources")
            project.tasks.getByName("merge${variant}JavaResource").dependsOn(task)
        }
    }
}


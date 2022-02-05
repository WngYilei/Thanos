// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        maven {
            setUrl("https://maven.aliyun.com/repository/central/") }
        google()
        mavenCentral()

        flatDir {
            dirs("./repo/com/xl/thanos-plugin/1.0.1")
        }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.xl.thanos-plugin:thanos-plugin:1.0.1")
    }
}
allprojects {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/central/") }
        google()
        flatDir {
            dirs("./repo/")
        }
    }
}

//// 耗时统计kt化
//class TimingsListener : TaskExecutionListener, BuildListener {
//    private var startTime: Long = 0L
//    private var timings = linkedMapOf<String, Long>()
//
//
//    override fun beforeExecute(task: Task) {
//        startTime = System.nanoTime()
//    }
//
//    override fun afterExecute(task: Task, state: TaskState) {
//
//        val path = "${project.rootDir}/app/build/intermediates/merged_jni_libs/release/out/lib"
//        println("path:${path}")
//        val file = File(path)
//        println("文件是否存在:${file.exists()}")
//        file.listFiles()?.forEach {
//            println(it.name)
//        }
//
//        val ms = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)
//        task.path
//        timings[task.path] = ms
//        project.logger.warn("${task.path} took ${ms}ms")
//    }
//
//    override fun buildFinished(result: BuildResult) {
//        project.logger.warn("Task timings:")
//        timings.forEach {
//            if (it.value >= 50) {
//                project.logger.warn("${it.key} cos  ms  ${it.value}\n")
//            }
//        }
//    }
//
//
//    override fun settingsEvaluated(settings: Settings) {
//    }
//
//    override fun projectsLoaded(gradle: Gradle) {
//
//    }
//
//    override fun projectsEvaluated(gradle: Gradle) {
//
//    }
//
//}
//
//gradle.addListener(TimingsListener())
//

package com.xl.plugin

import org.gradle.api.Project
import java.io.File

import javax.imageio.ImageIO
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.GradleException
import org.gradle.util.GFileUtils.copyFile
import java.io.FileOutputStream
import java.io.InputStream


object OptimizerUtils {
    private const val DRAWABLE = "drawable"
    private const val MIPMAP = "mipmap"
    private const val PNG9 = ".9.png"
    private const val PNG = ".png"
    private const val JPG = ".jpg"
    private const val JPEG = ".jpeg"


    fun isImgFolder(file: File) = file.name.startsWith(DRAWABLE) or file.name.startsWith(MIPMAP)


    fun isPreOptimizePng(file: File) =
        ((file.name.endsWith(PNG) || file.name.endsWith(PNG.toUpperCase()))) && !file.name.endsWith(
            PNG9
        )

    fun isPreOptimizeJpg(file: File) =
        file.name.endsWith(JPG) || file.name.endsWith(JPEG) || file.name.endsWith(JPG.toUpperCase()) || file.name.endsWith(
            JPEG.toUpperCase()
        )

    fun isTransparent(file: File): Boolean {
        val img = ImageIO.read(file)
        return img.colorModel.hasAlpha()
    }

    fun getTool(project: Project, name: String): String {
        val toolName = when {
            name == "apktool" -> {
                "apktool"
            }
            Os.isFamily(Os.FAMILY_WINDOWS) -> {
                "${name}_win.exe"
            }
            Os.isFamily(Os.FAMILY_MAC) -> {
                "${name}_darwin"
            }
            else -> {
                "${name}_linux"
            }
        }
        val path = "${project.buildDir.absolutePath}/tools/$name/$toolName"
        val file = File(path)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            val fileOutputStream = FileOutputStream(file)
            val inputStream: InputStream = this.javaClass.getResourceAsStream("/$name/${toolName}")
            fileOutputStream.write(inputStream.readBytes())
        }

        if (file.exists() && file.setExecutable(true)) {
            return file.absolutePath
        }
        throw GradleException("$toolName 工具不存在或者无法执行")
    }



}
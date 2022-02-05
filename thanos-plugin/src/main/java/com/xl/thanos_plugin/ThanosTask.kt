package com.xl.thanos_plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.util.GFileUtils.copyFile
import java.io.*

abstract class ThanosTask : DefaultTask() {

    companion object {
        const val WEBP_TOOL = "cwebp"
        const val PNG_TOOL = "pngcrush"
        const val JPG_TOOL = "guetzli"
        const val APK_TOOL = "apktool"
    }

    @Input
    lateinit var variantName: String

    @Input
    val webpTool = OptimizerUtils.getTool(project, WEBP_TOOL)

    @Input
    val jpgTool = OptimizerUtils.getTool(project, JPG_TOOL)

    @Input
    val pngTool = OptimizerUtils.getTool(project, PNG_TOOL)

    @Input
    val apkTool = OptimizerUtils.getTool(project, APK_TOOL)

    @TaskAction
    fun taskAction() {
        val jpgs = mutableListOf<File>()
        val pngs = mutableListOf<File>()
        val apPath =
            project.buildDir.path + "//intermediates/processed_res/${variantName.toLowerCase()}/out/resources-${variantName.toLowerCase()}.ap_"
        val apFile = File(apPath)
        val apOut = File(apFile.parent + "/ap")
        if (apOut.exists()) apOut.delete()

        decoderAp(apFile, apOut)
        val resFile = File(apOut.path + "/res")
        resFile.listFiles()?.forEach {
            if (OptimizerUtils.isImgFolder(it)) {
                it.listFiles()?.forEach { file ->
                    if (OptimizerUtils.isPreOptimizeJpg(file)) jpgs.add(file)
                    if (OptimizerUtils.isPreOptimizePng(file)) pngs.add(file)
                }
            }
        }


        val jpegsNotConvert = mutableListOf<File>()
        val pngNotConvert = mutableListOf<File>()

        pngs.forEach {
            convertWebp(webpTool, it, pngNotConvert)
        }

        jpgs.forEach {
            convertWebp(webpTool, it, jpegsNotConvert)
        }


        // 转换无效的再压缩
        compressImg(pngTool, true, pngNotConvert)
        compressImg(jpgTool, false, jpegsNotConvert)


        resFile.listFiles()?.forEach {
            if (OptimizerUtils.isImgFolder(it)) {
                it.listFiles()?.forEach { file ->
                    if (OptimizerUtils.isPreOptimizeJpg(file)) {
                        file.name.print()
                    }
                    if (OptimizerUtils.isPreOptimizePng(file)) {
                        file.name.print()
                    }
                }
            }
        }


        val outAp = File(apFile.parent + "/ap/resources-${variantName.toLowerCase()}.ap_")
        if (!outAp.exists()) outAp.createNewFile()
        buildAp(apOut, outAp)

        copyFile(outAp, apFile)
    }


    private fun decoderAp(targetFile: File, outFile: File) {
        project.exec {
            this.commandLine(
                "java",
                "-jar",
                apkTool,
                "d",
                "-f",
                targetFile,
                "-o",
                outFile
            )
        }

    }

    private fun buildAp(targetFile: File, outFile: File) {
        project.exec {
            this.commandLine(
                "java",
                "-jar",
                apkTool,
                "b",
                targetFile,
                "-o",
                outFile
            )
        }
    }

    private fun convertWebp(tool: String, file: File, noValidConvert: MutableList<File>) {
        //转换wenp
        var name = file.name
        name = name.substring(0, name.lastIndexOf("."))
        val output = File(file.parent, "${name}.webp")
        //google 建议75的质量
        val result = "$tool -q 50 ${file.absolutePath} -o ${output.absolutePath}".execute()
        result.waitFor()
        if (result.exitValue() == 0) {
            val rawlen = file.length()
            val outlen = output.length()
            if (rawlen > outlen) {
                file.delete()
            } else {
                //如果转换后的webp文件比源文件大 ， 后面还可以尝试 压缩
                noValidConvert.add(file)
                output.delete()
                project.logger.error("   convert ${name} bigger than raw")
            }
        } else {
            noValidConvert.add(file)
            project.logger.error("convert ${file.absolutePath} to webp error")
        }
    }

    //pngcrush  -brute -rem alla -reduce -q in.png out.png
    //guetzli --quality quality  in.jpg out.jpg
    private fun compressImg(tool: String, isPng: Boolean, files: MutableList<File>) {
        files.forEach { file ->
            val output = File(file.parent, "temp-preOptimizer-${file.name}")
            val result = if (isPng)
                "$tool -brute -rem alla -reduce -q ${file.absolutePath}  ${output.absolutePath}".execute()
            else
                "$tool --quality 84 ${file.absolutePath}  ${output.absolutePath}".execute()
            result.waitFor()
            //压缩成功
            if (result.exitValue() == 0) {
                val rawlen = file.length()
                val outlen = output.length()
                // 压缩后文件确实减小了
                if (outlen < rawlen) {
                    //删除原图片
                    file.delete()
                    //将压缩后的图片重命名为原图片
                    output.renameTo(file)
                } else {
                    output.delete()
                    project.logger.error("   compress ${file.name} bigger than raw")
                }
            } else {
                project.logger.error("compress ${file.absolutePath} error")
            }
        }
    }


    fun String.print() {
        project.logger.error("----------$this----------")
    }
}


fun String.execute() = Runtime.getRuntime().exec(this)!!

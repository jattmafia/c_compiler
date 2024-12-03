package com.example.myapp

import android.os.Handler
import android.os.Looper
import io.flutter.plugin.common.MethodChannel
import java.io.File
import java.io.PrintWriter
import java.util.concurrent.Executors

class CompilerService {
    fun compileCCode(code: String, result: MethodChannel.Result) {
        Executors.newSingleThreadExecutor().execute {
            try {
                // Save code to a temporary file
                val tmpFile = File.createTempFile("source", ".c")
                PrintWriter(tmpFile).use { it.write(code) }

                // Ensure the file is saved successfully
                if (!tmpFile.exists()) {
                    Handler(Looper.getMainLooper()).post { result.error("FILE_ERROR", "Failed to save the file", null) }
                    return@execute
                }

                // Run GCC/Clang compilation command
                val process = ProcessBuilder("gcc", tmpFile.absolutePath, "-o", tmpFile.parent + "/output").start()
                val errors = process.errorStream.bufferedReader().readText()

                if (errors.isNotEmpty()) {
                    Handler(Looper.getMainLooper()).post { result.success(errors) }
                } else {
                    val runProcess = ProcessBuilder(tmpFile.parent + "/output").start()
                    val output = runProcess.inputStream.bufferedReader().readText()
                    Handler(Looper.getMainLooper()).post { result.success(output) }
                }
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post { result.error("COMPILE_ERROR", e.message, null) }
            }
        }
    }
}

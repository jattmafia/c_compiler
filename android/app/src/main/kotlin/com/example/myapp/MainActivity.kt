package com.example.myapp

import android.os.Bundle  // Ensure correct import
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import androidx.annotation.NonNull  // Import this

class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.compiler"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "compileCCode") {
                val code = call.argument<String>("code") ?: ""
                CompilerService().compileCCode(code, result)
            } else {
                result.notImplemented()
            }
        }
    }
}

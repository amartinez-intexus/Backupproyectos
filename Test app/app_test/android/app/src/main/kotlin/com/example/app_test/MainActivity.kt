package com.example.app_test

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

public class MainActivity: FlutterActivity() {

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val CHANNEL = "Sumatory"
        val channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        channel.setMethodCallHandler { call, result ->
            if (call.method == "sumatoryNumbers") {
                val arguments = call.arguments as List<Int>
                if (arguments.size == 2) {
                    val num1 = arguments[0]
                    val num2 = arguments[1]
                    val sum = num1 + num2
                    result.success(sum)

                } else {
                    result.error("ARGUMENT_ERROR", "Expecting 2 int numbers.", null)
                }
            } else {
                result.notImplemented()
            }
        }
    }
}

    


import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class Compilerscreen extends StatefulWidget {
  const Compilerscreen({super.key});

  @override
  State<Compilerscreen> createState() => _CompilerscreenState();
}

class _CompilerscreenState extends State<Compilerscreen> {
  static const platform = MethodChannel('com.example.compiler');

  Future<String> compile(String code) async {
    try {
      final result =
          await platform.invokeMethod('compileCCode', {'code': code});
      print(result);
      return result;
    } on PlatformException catch (e) {
      print(e);
      return "Error: ${e.message}";
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          TextField(),
          OutlinedButton(
              onPressed: () {
                compile(''' #include <stdio.h>

int main() {
    printf("Hello, World!\n");
    return 0;
} ''');
              },
              child: Text('compile'))
        ],
      ),
    );
  }
}

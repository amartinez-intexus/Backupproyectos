import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

class AntelopChannel{
  static const android = MethodChannel("Antelop");

  void call () async{
    try {
     await android.invokeMethod("call");  
    } catch (e) {
      debugPrint('$e');
    }
  } 

}
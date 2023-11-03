import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

class ComuncationAndroid{
  static const android = MethodChannel("Sumatory");

  Future<int> sumatoryNumbers (int num1, int num2) async{
    try {
      final resultSum = await android.invokeMethod("sumatoryNumbers", [num1, num2]);  
      return(resultSum); 
    } catch (e) {
      debugPrint('$e');
    }
    return 0;    
  } 

}
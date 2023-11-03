import 'package:chat_bot_app/config/theme/app_theme_dark.dart';
import 'package:chat_bot_app/presentaion/providers/chat_provider.dart';
import 'package:chat_bot_app/presentaion/screens/chat/chat_screen.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context){
    return MultiProvider(
      providers: [ChangeNotifierProvider(
        create: (_) => ChatProvider())],
      child: MaterialApp(
        title: 'Yes no App',
        debugShowCheckedModeBanner: false,
        theme:  AppTheme(selectedColor: 2).theme(),
        home: const ChatScreen()
      ),
    );
  }
}
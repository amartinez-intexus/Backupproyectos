import 'package:chat_bot_app/config/theme/helpers/get_answer.dart';
import 'package:chat_bot_app/domain/entities/message.dart';
import 'package:flutter/material.dart';

class ChatProvider extends ChangeNotifier {

  final ScrollController chatScrollcontroller = ScrollController();
  final getAnswer = GetAnswer();

  List<Message> messageList = [
    Message(text: 'Hola gatito', fromWho: FromWho.me),
    Message(text: 'Estas melo caramelo?', fromWho: FromWho.me),
  ];

  Future<void> sendMessage (String text) async {
    if (text.isEmpty) return; 
    final newMessage = Message(text: text, fromWho: FromWho.me);
    messageList.add(newMessage);

    if(text.endsWith('?')){
      herReply();
    }

    notifyListeners();
    moveScrollToBottom();
  }


  Future<void> herReply() async{
    final herMessage = await getAnswer.getAnswer();
    messageList.add(herMessage);
    notifyListeners();
  }


  void moveScrollToBottom(){
    chatScrollcontroller.animateTo(
      chatScrollcontroller.position.maxScrollExtent, 
      duration: const Duration(milliseconds: 100), 
      curve: Curves.easeOut);
  }
}
import 'package:chat_bot_app/domain/entities/message.dart';
import 'package:chat_bot_app/presentaion/providers/chat_provider.dart';
import 'package:chat_bot_app/presentaion/widgets/chat/answer_message.dart';
import 'package:chat_bot_app/presentaion/widgets/chat/message_bubble.dart';
import 'package:chat_bot_app/presentaion/widgets/shared/message_field_box.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';


class ChatScreen  extends StatelessWidget {
  const ChatScreen ({super.key});

  @override 
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: const Padding(
          padding: EdgeInsets.all(4.0),
          child: CircleAvatar(
            backgroundImage: NetworkImage('https://img.freepik.com/vector-gratis/personaje-dibujos-animados-gatito-ojos-dulces_1308-133242.jpg'),
          ),
        ),
        title: const Center(child: Text('Saluda')),
      ),
      body: _ChatView(),
    );
  }
}

class _ChatView extends StatelessWidget{

  @override
  Widget build(BuildContext context){
    final chatProvider = context.watch<ChatProvider>();
    return SafeArea(
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 10),
        child: Column(
          children: [
             Expanded(
              child: ListView.builder(
                controller: chatProvider.chatScrollcontroller,
                itemCount: chatProvider.messageList.length, // Example itemCount
                itemBuilder: (context, index) {
                  final message = chatProvider.messageList[index];
                  
                  return (message.fromWho == FromWho.hers)
                    ? const AnswerMessage()
                    :  MessageBubble (message: message,);
                },
              ),
            ),
            MesssageFieldBox(
              onValue: (value) => chatProvider.sendMessage(value),
            ),
          ],
        ),
      ),
    );
  }
}
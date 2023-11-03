import 'package:flutter/material.dart';
import 'package:app_test/user_service.dart';

class api_Screen extends StatefulWidget {
  const api_Screen({super.key});

  @override
  State<api_Screen> createState() => _ApiScreenState();
}

class _ApiScreenState extends State<api_Screen> {
  final items = List.generate(50, (i) => i);
  late Future<List<User>> futureUsers;
  @override
  void initState() {
    super.initState();
    futureUsers = UserService().getUser();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Consumo de API'),
          centerTitle: true,
        ),
        body: FutureBuilder<List<User>>(
          future: futureUsers,
          builder: ((context, snapshot) {
            if (snapshot.hasData) {
              return ListView.separated(
                itemCount:
                    snapshot.data!.length, 
                itemBuilder: (context, index) {
                  User user = snapshot.data![index];
                  return ListTile(
                    title: Text(user.email),
                    subtitle: Text(user.name.firstName),
                    trailing: const Icon(Icons.person),
                  );
                },
                separatorBuilder: (context, index) {
                  return const Divider();
                },
              );
            } else if (snapshot.hasError) {
              return Text('ERROR: ${snapshot.error}');
            }
            return const CircularProgressIndicator();
          }),
        ));
  }
}

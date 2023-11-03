import 'dart:convert';
import 'dart:developer';
import 'package:http/http.dart' as http ;

class Name{
  final String  firstName;
  final String lastName;

  const Name ({
    required this.firstName,
    required this.lastName
  });

  factory Name.fromJson(Map<String, dynamic> json) {
    return Name(firstName: json['first'], lastName: json['last']);
  } 
}

class User {
  final String email;
  // final String picture;
  final Name name;

  const User({
    required this.email,
    // required this.picture,
    required this.name
  });

  factory User.fromJson(Map<String, dynamic> json){
    return User(
      email: json['email'], 
      name: Name.fromJson(json['name']));
  }
}

class UserService{
  Future<List<User>> getUser() async {
    final response = await http.get(Uri.parse("https://randomuser.me/api?results=50"));
    log('$response');
  if (response.statusCode == 200){
    final data = jsonDecode(response.body);

    final List<User> list = [];

    for(var i=0; i < 50; i++){
      final entry = data['results'][i];
      list.add(User.fromJson(entry));
    }
    return list;
  } else {
    throw Exception('ERROR json');
  }
  }
}
import 'package:flutter_apapedia/screens/home.dart';
import 'package:flutter_apapedia/screens/login.dart';
import 'package:flutter_apapedia/screens/register.dart';
import 'package:flutter_apapedia/screens/profile/profile_page.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'APAPEDIA',
      routes: {
        '/': (context) => const HomeScreen(),
        '/login': (context) => const LoginFormScreen(),
        '/register': (context) => const RegisterPage(),
        '/profile': (context) => const ProfilePage(),
      },
    );
  }
}

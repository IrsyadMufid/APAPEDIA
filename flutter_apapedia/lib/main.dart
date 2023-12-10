import 'package:flutter_apapedia/screens/login.dart';
import 'package:flutter_apapedia/screens/view_all_buku.dart';
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
        '/': (context) => const LoginFormScreen(),
        '/all-buku': (context) => const BukuScreen(),
      },
    );
  }
}

import 'package:flutter/material.dart';
import 'package:flutter_apapedia/screens/login.dart';
import 'package:flutter_apapedia/screens/register.dart';
import 'package:flutter_apapedia/utils/drawer.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

class HomeScreen extends StatefulWidget {
  static const routeName = '/';
  const HomeScreen({super.key});

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _currentIndex = 0;
  final List<Widget> _children = [
    const Text('Home Screen'),
    const LoginFormScreen(),
    const RegisterPage(),
  ];
  Map<String, dynamic>? userData;
  bool isLoading = true;

  @override
  void initState() {
    super.initState();
    _checkTokenAndGetUser();
  }

  void _checkTokenAndGetUser() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString('token');
    if (token != null) {
      await _fetchUserData(token);
    }
    setState(() {
      isLoading = false;
    });
  }

  Future<void> _fetchUserData(String token) async {
    final response = await http.post(
      Uri.parse('http://localhost:8081/api/auth/get-user'),
      headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Headers':
                'Origin, X-Requested-With, Content-Type, Accept'
          },
      body: jsonEncode({'accessToken': token}),
    );

    if (response.statusCode == 200) {
      final data = json.decode(response.body);
      setState(() {
        userData = data;
      });
      final SharedPreferences prefs = await SharedPreferences.getInstance();
      // Save the user ID in SharedPreferences
      await prefs.setString('activeUserId', data['id']);
    } else {
      print('Failed to fetch user data');
    }
  }

  void onTabTapped(int index) {
    setState(() {
      _currentIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return Scaffold(
        appBar: AppBar(title: const Text('Loading...')),
        body: const Center(child: CircularProgressIndicator()),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'APAPEDIA',
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
      ),
      drawer: CustomDrawer(), // Add the custom drawer here
      body: Center(
        child: userData != null
            ? Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Text('Welcome, ${userData!['name']}'),
                  Text('Email: ${userData!['email']}'),
                  // More user details
                ],
              )
            : const Icon(Icons.add_shopping_cart, size: 200.0),
      ),
    );
  }
}

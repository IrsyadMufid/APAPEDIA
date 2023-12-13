import 'package:flutter/material.dart';
import 'package:flutter_apapedia/screens/home.dart';
import 'package:flutter_apapedia/screens/login.dart';
import 'package:flutter_apapedia/screens/register.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'dart:convert';

class CustomDrawer extends StatelessWidget {
  final Future<String?> token = SharedPreferences.getInstance().then((prefs) {
    return prefs.getString('token');
  });

  Future<void> _logout(BuildContext context) async {
    final prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString('token');
    String? activeUserId = prefs.getString('activeUserId');

    if (token != null && activeUserId != null) {
      // Perform the POST request to log out
      final response = await http.post(
        Uri.parse('http://localhost:8081/api/auth/log-out'),
        headers: <String, String>{
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        // Successfully logged out
        await prefs.remove('token');
        await prefs.remove('activeUserId');
        Navigator.pop(context); // Close the drawer
        Navigator.pushReplacement(
            context, MaterialPageRoute(builder: (context) => LoginFormScreen()));
      } else {
        // Handle error here
        print('Logout failed');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<String?>(
      future: token,
      builder: (context, snapshot) {
        List<Widget> drawerOptions = [
          ListTile(
            leading: Icon(Icons.home),
            title: Text('Home'),
            onTap: () {
              Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => HomeScreen()));
            },
          ),
        ];

        if (snapshot.data == null) {
          // User is not logged in
          drawerOptions.addAll([
            ListTile(
              leading: Icon(Icons.login),
              title: Text('Login'),
              onTap: () {
                Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => const LoginFormScreen()));
              },
            ),
            ListTile(
              leading: Icon(Icons.app_registration),
              title: Text('Register'),
              onTap: () {
                Navigator.pushReplacement(context, MaterialPageRoute(builder: (context) => RegisterPage()));
              },
            ),
          ]);
        } else {
          // User is logged in
          drawerOptions.add(
            ListTile(
              leading: Icon(Icons.exit_to_app),
              title: Text('Logout'),
              onTap: () => _logout(context),
            ),
          );
        }

        return Drawer(
          child: ListView(
            padding: EdgeInsets.zero,
            children: drawerOptions,
          ),
        );
      },
    );
  }
}

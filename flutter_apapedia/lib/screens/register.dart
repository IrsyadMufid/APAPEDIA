import 'package:flutter/material.dart';
import 'package:flutter_apapedia/screens/home.dart';
import 'package:flutter_apapedia/utils/drawer.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import 'package:shared_preferences/shared_preferences.dart';

class RegisterPage extends StatefulWidget {
  const RegisterPage({Key? key}) : super(key: key); // Make it const
 
  @override
  _RegisterPageState createState() => _RegisterPageState();
}

class _RegisterPageState extends State<RegisterPage> {
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  String name = '';
  String username = '';
  String password = '';
  String email = '';
  String address = '';
  String role = 'Customer';

  showLoading(BuildContext context) {
    return showDialog(
        context: context,
        barrierDismissible: false,
        builder: (BuildContext context) {
          return const Center(
            child: CircularProgressIndicator(),
          );
        });
  }

  TextEditingController usernameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  @override
  void dispose() {
    usernameController.dispose();
    passwordController.dispose();
    super.dispose();
  }

  Future<void> registerUser() async {
    showLoading(context); // Show loading indicator
    try {
      final response = await http.post(
        Uri.parse('http://localhost:8081/api/auth/sign-up'),
        headers: {
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Headers':
              'Origin, X-Requested-With, Content-Type, Accept'
        },
        body: jsonEncode(<String, String>{
          'name': name,
          'username': username,
          'password': password,
          'email': email,
          'address': address,
          'role': role,
        }),
      );

      Navigator.of(context).pop(); // Dismiss loading indicator

      final responseBody = response.body;

      if (response.statusCode == 200) {
        if (responseBody.contains('Customer created')) {
          // Proceed to login the user or navigate to the home screen
          await login(); // Assuming login() method navigates to the HomeScreen after successful login
          Navigator.of(context).pop();
        } else if (responseBody.contains('User already exists with the username')) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('User already exists with the username.'),
              backgroundColor: Colors.red,
            ),
          );
        } else if (responseBody.contains('User already exists with the email')) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('User already exists with the email.'),
              backgroundColor: Colors.red,
            ),
          );
        } else if (responseBody.contains('User already exists with the password')) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('User already exists with the password.'),
              backgroundColor: Colors.red,
            ),
          );
        } else {
          // Handle any other response
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Unexpected response: $responseBody'),
              backgroundColor: Colors.red,
            ),
          );
        }
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Failed to register user. Please try again.'),
            backgroundColor: Colors.red,
          ),
        );
      }
    }
    catch (error) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Error. Please try again.'),
          backgroundColor: Colors.red,
        ),
      );
    }
    finally {
      Navigator.of(context).pop(); // This will close the loading dialog
    }
  }


  Future<void> login() async {
    if (_formKey.currentState!.validate()) {
      showLoading(context);

      try {
        final response = await http.post(
          Uri.parse('http://localhost:8081/api/auth/log-in'),
          headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Headers':
                'Origin, X-Requested-With, Content-Type, Accept'
          },
          body: jsonEncode({
            'username': usernameController.text,
            'password': passwordController.text,
          }),
        );

        if (response.statusCode == 200) {
          final token = jsonDecode(response.body)['accessToken'];
          final prefs = await SharedPreferences.getInstance();
          prefs.setString('token', token);

          await Navigator.push(context,
              MaterialPageRoute(builder: (context) => HomeScreen()));
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Login failed. Please check your credentials.'),
              backgroundColor: Colors.red,
            ),
          );
        }
      } catch (error) {
        ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Error. Please try again.'),
              backgroundColor: Colors.red,
            ),
          );
      } finally {
        Navigator.of(context).pop();
      }
    }
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Register'),
      ),
      drawer: CustomDrawer(),
      body: Center(
            child: SingleChildScrollView(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const Text("APAPEDIA",
                      style:
                          TextStyle(fontSize: 28, fontWeight: FontWeight.w700)),
                  const SizedBox(
                    height: 10,
                  ),
                  const Icon(
                    Icons.app_registration,
                    size: 100,
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  Container(
                    padding:
                        const EdgeInsets.symmetric(horizontal: 8, vertical: 20),
                    margin: const EdgeInsets.all(16),
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(8),
                      color: Colors.white,
                    ),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        const Text("Register!",
                            style: TextStyle(
                              fontSize: 24,
                            )),
                        Form(
                          key: _formKey,
                          child: Column(
                            children: <Widget>[
                              TextFormField(
                                decoration: InputDecoration(labelText: 'Name'),
                                onSaved: (value) => name = value!,
                              ),
                              TextFormField(
                                controller: usernameController,
                                decoration: InputDecoration(labelText: 'Username'),
                                onSaved: (value) => username = value!,
                              ),
                              TextFormField(
                                controller: passwordController,
                                decoration: InputDecoration(labelText: 'Password'),
                                onSaved: (value) => password = value!,
                              ),
                              TextFormField(
                                decoration: InputDecoration(labelText: 'Email'),
                                onSaved: (value) => email = value!,
                              ),
                              TextFormField(
                                decoration: InputDecoration(labelText: 'Address'),
                                onSaved: (value) => address = value!,
                              ),
                              ElevatedButton(
                                onPressed: () {
                                  if (_formKey.currentState!.validate()) {
                                    _formKey.currentState!.save();
                                    registerUser();
                                  }
                                },
                                child: Text('Register'),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ),
    );
  }
}

import 'package:flutter/material.dart';
import 'package:flutter_apapedia/screens/profile/profile_page.dart';
import 'package:flutter_apapedia/utils/drawer.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

class EditProfilePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white, // Light background for the whole page
      appBar: AppBar(
        backgroundColor: Colors.grey, // Light color for AppBar
        elevation: 1,
        title: const Text('Profile Page', style: TextStyle(color: Colors.white)),
        centerTitle: true,
      ),
      body: SingleChildScrollView( // Allows scrolling when keyboard is open
        child: Padding(
          padding: EdgeInsets.all(16),
          child: Card( // Enclosing all content in a Card
            color: Colors.white.withOpacity(0.9), // Semi-transparent card background
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            elevation: 5,
            child: Padding(
              padding: EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: <Widget>[
                  _buildTextField(hint: 'Name'),
                  _buildTextField(hint: 'Email'),
                  _buildTextField(hint: 'Password', obscureText: true),
                  _buildTextField(hint: 'Address'),
                  SizedBox(height: 24),
                  ElevatedButton(
                    onPressed: () {
                      // Submit action
                    },
                    child: Text('Submit'),
                    style: ElevatedButton.styleFrom(
                      primary: Colors.blue, // Button color
                      onPrimary: Colors.white, // Text color
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                      padding: EdgeInsets.symmetric(vertical: 12),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildTextField({required String hint, bool obscureText = false}) {
    return Padding(
      padding: EdgeInsets.only(top: 16),
      child: TextFormField(
        obscureText: obscureText,
        decoration: InputDecoration(
          hintText: hint,
          hintStyle: TextStyle(color: Colors.grey[600]),
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8),
            borderSide: BorderSide(color: Colors.grey[300]!),
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8),
            borderSide: BorderSide(color: Colors.blue),
          ),
          enabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8),
            borderSide: BorderSide(color: Colors.grey[400]!),
          ),
          contentPadding: EdgeInsets.symmetric(horizontal: 16, vertical: 10),
        ),
        style: TextStyle(color: Colors.black),
      ),
    );
  }
}

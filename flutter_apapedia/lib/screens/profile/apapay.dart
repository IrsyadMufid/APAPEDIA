import 'package:flutter/material.dart';
import 'package:flutter_apapedia/screens/profile/profile_page.dart';
import 'package:flutter_apapedia/utils/drawer.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

class TopUpPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white, // Light theme background color
      appBar: AppBar(
        backgroundColor: Colors.grey, // Light color for AppBar
        elevation: 1,
        title: const Text('Profile Page', style: TextStyle(color: Colors.white)),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Card(
              color: Colors.grey[100], // Light card color
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(16),
              ),
              child: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    const Text(
                      'APAPAY Balance',
                      style: TextStyle(color: Colors.black, fontSize: 18),
                    ),
                    const SizedBox(height: 10),
                    Row(
                      children: [
                        Icon(Icons.account_balance_wallet, color: Colors.blue),
                        const SizedBox(width: 8),
                        const Text('Rp 10.000', style: TextStyle(color: Colors.black, fontSize: 30, fontWeight: FontWeight.bold)),
                      ],
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 24),
            TextFormField(
              decoration: InputDecoration(
                fillColor: Colors.grey[200], // Light color for text field
                filled: true,
                border: OutlineInputBorder(
                  borderSide: BorderSide.none, // No visible border
                  borderRadius: BorderRadius.circular(12), // Rounded corners
                ),
                hintText: 'Input your top up balance...',
                hintStyle: TextStyle(color: Colors.grey[700]),
              ),
              keyboardType: TextInputType.number,
              style: const TextStyle(color: Colors.black),
            ),
            const SizedBox(height: 24),
            Center(
              child: ElevatedButton(
                onPressed: () {
                  // Top-up action
                },
                child: const Text('Submit'),
                style: ElevatedButton.styleFrom(
                  primary: Colors.blue, // Standard button color
                  onPrimary: Colors.white,
                  padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 12),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12), // Rounded corners
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

import 'package:flutter/material.dart';
import 'package:flutter_apapedia/screens/profile/edit_profile.dart';
import 'package:flutter_apapedia/screens/profile/apapay.dart';
import 'package:flutter_apapedia/utils/drawer.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

class ProfilePage extends StatefulWidget {
  static const routeName = '/profile';
  const ProfilePage({Key? key}) : super(key: key);

  @override
  _ProfilePageState createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
  bool isLoading = true;
  String name = '';
  String email = '';
  String address = '';
  String balance = '';

  @override
  void initState() {
    super.initState();
    _loadUserProfile();
  }

  Future<void> _loadUserProfile() async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('token');
    final userId = prefs.getString('activeUserId');

    if (token != null && userId != null) {
      final response = await http.get(
        Uri.parse('http://localhost:8081/api/user/profile/$userId'),
        headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*',
            'Authorization': 'Bearer $token',
            'Access-Control-Allow-Headers':
                'Origin, X-Requested-With, Content-Type, Accept'
          },
      );

      if (response.statusCode == 200) {
        final userProfile = json.decode(response.body);
        setState(() {
          name = userProfile['name'];
          email = userProfile['email'];
          address = userProfile['address'];
          balance = 'Rp ${userProfile['balance'].toString()}';
          isLoading = false;
        });
      } else {
        // Handle the error; maybe log out the user or show an error message
        setState(() {
          isLoading = false;
          print('Failed to load user profile');
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return const Scaffold(
        body: Center(
          child: CircularProgressIndicator(),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text('Profile'),
      ),
      drawer: CustomDrawer(),
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              const SizedBox(height: 24),
              Center(
                child: Column(
                  children: [
                    const SizedBox(height: 16),
                    Text(
                      name,
                      style: const TextStyle(color: Colors.black, fontSize: 24, fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      email,
                      style: TextStyle(color: Colors.black54, fontSize: 16),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      address,
                      style: TextStyle(color: Colors.black54, fontSize: 16),
                    ),
                  ],
                ),
              ),
              const SizedBox(height: 24),
              _buildCard(
                title: 'APAPAY Balance',
                content: balance,
                icon: Icons.account_balance_wallet,
                actionWidget: ElevatedButton(
                  onPressed: () {
                    Navigator.of(context).push(
                      MaterialPageRoute(builder: (context) => TopUpPage()),
                    );
                  },
                  child: const Text('Top-up'),
                  style: ElevatedButton.styleFrom(primary: Colors.blue, shape: const StadiumBorder()),
                ),
              ),
              const SizedBox(height: 24),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  _buildActionButton(
                    text: 'Edit Profile',
                    icon: Icons.edit,
                    color: Colors.blue,
                    onTap: () {
                      Navigator.of(context).push(
                        MaterialPageRoute(builder: (context) => EditProfilePage()),
                      );
                    },
                  ),
                  _buildActionButton(
                    text: 'Sign Out',
                    icon: Icons.logout,
                    color: Colors.grey,
                    onTap: () async {
                      final prefs = await SharedPreferences.getInstance();
                      await prefs.remove('token');
                      await prefs.remove('activeUserId');
                      // Navigate to login or another appropriate screen
                    },
                  ),
                  _buildActionButton(
                    text: 'Delete Account',
                    icon: Icons.delete_forever,
                    color: Colors.redAccent,
                    onTap: () {
                      // Add delete account functionality
                    },
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildCard({required String title, required String content, required IconData icon, required Widget actionWidget}) {
    return Container(
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.grey[200],
        borderRadius: BorderRadius.circular(16),
        boxShadow: [
          BoxShadow(
            color: Colors.grey.withOpacity(0.5),
            spreadRadius: 0,
            blurRadius: 4,
            offset: const Offset(0, 4),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(title, style: TextStyle(color: Colors.black54, fontSize: 18)),
          const SizedBox(height: 10),
          Row(
            children: [
              Icon(icon, color: Colors.blue),
              const SizedBox(width: 8),
              Text(content, style: TextStyle(color: Colors.black, fontSize: 30, fontWeight: FontWeight.bold)),
            ],
          ),
          const SizedBox(height: 16),
          Center(child: actionWidget),
        ],
      ),
    );
  }

  Widget _buildActionButton({required String text, required IconData icon, required Color color, required VoidCallback onTap}) {
    return ElevatedButton.icon(
      onPressed: onTap,
      icon: Icon(icon, size: 24),
      label: Text(text),
      style: ElevatedButton.styleFrom(
        primary: color,
        onPrimary: Colors.white,
        minimumSize: const Size(100, 40),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10),
        ),
      ),
    );
  }
}
